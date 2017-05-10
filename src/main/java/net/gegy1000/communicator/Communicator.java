package net.gegy1000.communicator;

import net.gegy1000.communicator.device.DeviceInfo;
import net.gegy1000.communicator.exception.SchoolException;
import net.gegy1000.communicator.impl.Feed;
import net.gegy1000.communicator.model.FeedListModel;
import net.gegy1000.communicator.model.FeedModel;
import net.gegy1000.communicator.model.SettingsModel;
import net.gegy1000.communicator.model.local.ActiveFeedsModel;
import net.gegy1000.communicator.storage.DataStorage;

import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Random;
import java.util.Set;

public class Communicator {
    private DataStorage dataStorage;
    private DeviceInfo device = DeviceInfo.random(new Random());

    private String username;
    private String email;

    private RequestHandler requestHandler;

    private SettingsModel settingsModel;
    private ActiveFeedsModel activeFeeds;

    private Set<Feed> feeds;

    private Communicator(DataStorage dataStorage, DeviceInfo deviceInfo) {
        this.dataStorage = dataStorage;
        if (deviceInfo != null) {
            this.device = deviceInfo;
        }
        this.requestHandler = new RequestHandler(this);
    }

    /**
     * Logs in to D6 with the given username and email address
     *
     * @param username the username to login with
     * @param email the email to login with
     * @throws SchoolException if an exception occurred during login
     */
    public void login(String username, String email) throws SchoolException {
        this.username = username;
        this.email = email;
        try {
            if (!this.dataStorage.has(DataStorage.GLOBAL_SETTINGS)) {
                this.settingsModel = this.requestHandler.downloadSettings();
                this.dataStorage.save(DataStorage.GLOBAL_SETTINGS, this.settingsModel);
            } else {
                this.settingsModel = this.dataStorage.load(DataStorage.GLOBAL_SETTINGS, SettingsModel.class);
            }
        } catch (IOException e) {
            throw new SchoolException(e);
        }
    }

    public DeviceInfo getDevice() {
        return this.device;
    }

    public SettingsModel getSettings() {
        return this.settingsModel;
    }

    public Set<Feed> getFeeds() throws SchoolException {
        if (this.feeds == null) {
            this.feeds = new HashSet<>();
            FeedListModel feedListModel = this.requestHandler.downloadFeedList(this.settingsModel);
            for (FeedModel feedModel : feedListModel.getFeeds()) {
                this.feeds.add(new Feed(this, feedModel));
            }
        }
        return this.feeds;
    }

    /**
     * Suggests feeds with a similar feed name
     *
     * @param query the query to check
     * @return a set of possible feeds
     * @throws SchoolException if an exception occurred when requesting feeds
     */
    public Set<Feed> suggestFeeds(String query) throws SchoolException {
        Set<Feed> suggested = new HashSet<>();
        String[] queryArray = query.toLowerCase(Locale.ROOT).trim().split(" ");
        Set<Feed> feeds = this.getFeeds();
        for (Feed feed : feeds) {
            String[] name = feed.getName().toLowerCase(Locale.ROOT).trim().split(" ");
            boolean matched = true;
            for (String q : queryArray) {
                boolean contains = false;
                for (String n : name) {
                    if (q.equals(n)) {
                        contains = true;
                    }
                }
                if (!contains) {
                    matched = false;
                    break;
                }
            }
            if (matched) {
                suggested.add(feed);
            }
        }
        return suggested;
    }

    /**
     * Gets a feed by it's token
     *
     * @param token the token to search for
     * @return the feed with the given token, or null if none found
     * @throws SchoolException if an exception occurred when requesting feeds
     */
    public Feed getFeed(String token) throws SchoolException {
        Set<Feed> feeds = this.getFeeds();
        for (Feed feed : feeds) {
            if (feed.getToken().equals(token)) {
                return feed;
            }
        }
        return null;
    }

    public ActiveFeedsModel getActiveFeeds() throws SchoolException {
        try {
            if (this.activeFeeds == null) {
                if (this.dataStorage.has(DataStorage.ACTIVE_FEEDS)) {
                    this.activeFeeds = this.dataStorage.load(DataStorage.ACTIVE_FEEDS, ActiveFeedsModel.class);
                } else {
                    this.activeFeeds = new ActiveFeedsModel();
                    this.dataStorage.save(DataStorage.ACTIVE_FEEDS, this.activeFeeds);
                }
            }
        } catch (IOException e) {
            throw new SchoolException(e);
        }
        return this.activeFeeds;
    }

    public DataStorage getStorage() {
        return this.dataStorage;
    }

    public String getUsername() {
        return this.username;
    }

    public String getEmail() {
        return this.email;
    }

    public void updateFeed(Feed feed) throws SchoolException {
        this.requestHandler.updateUserFeed(feed, null);
    }

    public static Builder builder() {
        return new Builder();
    }

    public RequestHandler getRequestHandler() {
        return this.requestHandler;
    }

    public static class Builder {
        private DataStorage dataStorage;
        private DeviceInfo device;

        private Builder() {
        }

        public Builder withStorage(DataStorage dataStorage) {
            this.dataStorage = dataStorage;
            return this;
        }

        public Builder withDevice(DeviceInfo device) {
            this.device = device;
            return this;
        }

        public Communicator build() {
            if (this.dataStorage == null) {
                throw new IllegalArgumentException("Could not build Communicator! dataStorage was not set!");
            }
            return new Communicator(this.dataStorage, this.device);
        }
    }
}
