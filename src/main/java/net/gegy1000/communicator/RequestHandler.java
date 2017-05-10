package net.gegy1000.communicator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import net.gegy1000.communicator.exception.InvalidRequestException;
import net.gegy1000.communicator.exception.SchoolException;
import net.gegy1000.communicator.impl.FeedEntryImpl;
import net.gegy1000.communicator.model.FeedListModel;
import net.gegy1000.communicator.model.SettingsModel;
import net.gegy1000.communicator.model.UserDetails;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles all requests to the D6 servers
 */
public class RequestHandler {
    private static final Gson GSON = new Gson();

    private static final String SETTINGS_URL = "http://ifeed.d6communicator.com/settings.php";
    private static final String REGISTER_URL = "http://afeed.d6communicator.com/user_update.php";
    private static final String IMAGE_UPLOAD = "http://ifeed.d6communicator.com/upload.php";

    private Communicator communicator;
    private OkHttpClient client;

    public RequestHandler(Communicator communicator) {
        this.communicator = communicator;
        this.client = new OkHttpClient.Builder().addNetworkInterceptor(chain -> {
            Request newRequest = chain.request().newBuilder()
                    .header("User-Agent", this.getUserAgent())
                    .build();
            return chain.proceed(newRequest);
        }).build();
    }

    public SettingsModel downloadSettings() throws SchoolException {
        try {
            JsonObject parameters = new JsonObject();
            parameters.addProperty("serial", -1);
            parameters.addProperty("token", D6Constants.SETTINGS_TOKEN);
            parameters.addProperty("appId", D6Constants.APP_ID);
            RequestBody body = RequestBody.create(MediaType.parse("application/json"), GSON.toJson(parameters).getBytes());
            Request request = new Request.Builder()
                    .url(SETTINGS_URL)
                    .post(body)
                    .build();
            Response response = this.client.newCall(request).execute();
            SettingsModel model = GSON.fromJson(response.body().charStream(), SettingsModel.class);
            response.close();
            return model;
        } catch (IOException e) {
            throw new SchoolException(e);
        }
    }

    public FeedListModel downloadFeedList(SettingsModel settings) throws SchoolException {
        try {
            String url = settings.getUrls().getClients() + "?appId=" + D6Constants.APP_ID + "&subType=0";
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Response response = this.client.newCall(request).execute();
            FeedListModel model = GSON.fromJson(response.body().charStream(), FeedListModel.class);
            response.close();
            return model;
        } catch (IOException e) {
            throw new SchoolException(e);
        }
    }

    public void updateUserFeed(Feed feed, UserDetails userDetails) throws SchoolException {
        try {
            if (feed.isActive()) {
                JsonObject parameters = new JsonObject();
                parameters.addProperty("feedID", feed.getId());
                parameters.addProperty("uuid", feed.getUuid());
                parameters.addProperty("feedToken", feed.getToken());
                parameters.addProperty("userName", this.communicator.getUsername());
                parameters.addProperty("userEmailAddress", this.communicator.getEmail());
                parameters.addProperty("device", "ios");
                parameters.addProperty("appId", D6Constants.APP_ID);
                parameters.addProperty("deviceToken", 0);
                parameters.addProperty("lang", D6Constants.LANGUAGE);
                JsonArray channels = new JsonArray();
                parameters.add("channels", channels);
                JsonArray grades = new JsonArray();
                parameters.add("grades", grades);
                if (userDetails != null) {
                    parameters.add("details", GSON.toJsonTree(userDetails));
                }
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), GSON.toJson(parameters).getBytes());
                Request request = new Request.Builder()
                        .url(REGISTER_URL)
                        .post(body)
                        .build();
                Response response = this.client.newCall(request).execute();
                JsonObject responseObject = new JsonParser().parse(new JsonReader(response.body().charStream())).getAsJsonObject();
                response.close();
                if (responseObject.has("userID")) {
                    long userID = responseObject.get("userID").getAsLong();
                    feed.setUserId(userID);
                } else {
                    JsonObject status = responseObject.getAsJsonObject("status");
                    if (status.get("code").getAsInt() == 1) {
                        throw new InvalidRequestException(status.get("message").getAsString());
                    }
                }
            }
        } catch (IOException e) {
            throw new SchoolException(e);
        }
    }

    public List<FeedEntry> requestFeedContent(Feed feed) throws SchoolException {
        List<FeedEntry> entries = new ArrayList<>();
        try {
            if (feed.isActive()) {
                JsonObject parameters = new JsonObject();
                parameters.addProperty("feedID", feed.getId());
                parameters.addProperty("userID", feed.getUserId());
                parameters.addProperty("uuid", feed.getUuid());
                parameters.addProperty("feedToken", feed.getToken());
                parameters.addProperty("serial", this.communicator.getSettings().getSerial());
                parameters.addProperty("deviceToken", "");
                parameters.addProperty("lang", D6Constants.LANGUAGE);
                JsonArray channels = new JsonArray();
                parameters.add("channels", channels);
                JsonArray grades = new JsonArray();
                parameters.add("grades", grades);
                JsonArray feeds = new JsonArray();
                for (Feed f : this.communicator.getFeeds()) {
                    if (f.isActive()) {
                        feeds.add(f.getId());
                    }
                }
                parameters.add("subscribed_clients", feeds);
                parameters.addProperty("lang", D6Constants.LANGUAGE);
                parameters.addProperty("device", "ios");
                parameters.addProperty("appId", D6Constants.APP_ID);
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), GSON.toJson(parameters).getBytes());
                Request request = new Request.Builder()
                        .url(this.communicator.getSettings().getUrls().getContent())
                        .post(body)
                        .build();
                Response response = this.client.newCall(request).execute();
                JsonArray content = new JsonParser().parse(new JsonReader(response.body().charStream())).getAsJsonArray();
                response.close();
                for (JsonElement element : content) {
                    JsonObject object = element.getAsJsonObject();
                    if (object.has("serial")) {
                        FeedEntry.Transaction transaction = FeedEntry.Transaction.get(object.get("transaction").getAsString());
                        FeedEntry.Type type = FeedEntry.Type.get(object.get("feed").getAsString());
                        long serial = object.get("serial").getAsLong();
                        if (object.has("values")) {
                            JsonObject values = object.getAsJsonObject("values");
                            if (values.has("identifier")) {
                                long identifier = values.get("identifier").getAsLong();
                                long category = -1;
                                if (values.has("category")) {
                                    values.get("category").getAsLong();
                                }
                                long time = -1;
                                if (values.has("date")) {
                                    try {
                                        time = D6Constants.FEED_DATE_FORMAT.parse(values.get("date").getAsString()).getTime();
                                    } catch (ParseException e) {
                                        throw new SchoolException("Received invalid date format", e);
                                    }
                                }
                                String url = null;
                                if (values.has("url")) {
                                    url = values.get("url").getAsString();
                                }
                                String title = null;
                                String details = null;
                                if (values.has("lang")) {
                                    JsonObject lang = values.getAsJsonObject("lang");
                                    if (lang.has(D6Constants.LANGUAGE)) {
                                        JsonObject localized = lang.get(D6Constants.LANGUAGE).getAsJsonObject();
                                        if (localized.has("title")) {
                                            title = localized.get("title").getAsString();
                                        }
                                        if (localized.has("details")) {
                                            details = localized.get("details").getAsString();
                                        }
                                    } else {
                                        for (Map.Entry<String, JsonElement> entry : lang.entrySet()) {
                                            if (entry.getValue().isJsonObject()) {
                                                JsonObject localized = entry.getValue().getAsJsonObject();
                                                if (localized.has("title")) {
                                                    title = localized.get("title").getAsString();
                                                    if (localized.has("details")) {
                                                        details = localized.get("details").getAsString();
                                                    }
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                }
                                FeedEntryImpl entry = new FeedEntryImpl(serial, identifier, category, time, title, details, url, type, transaction);
                                entries.add(entry);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new SchoolException("Failed to request feed contents", e);
        }
        return entries;
    }

    public String getUserAgent() {
        return D6Constants.APP_NAME + "/" + D6Constants.APP_VERSION + " (" + this.communicator.getDevice().toString() + ")";
    }
}
