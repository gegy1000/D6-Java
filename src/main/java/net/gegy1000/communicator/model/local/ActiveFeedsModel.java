package net.gegy1000.communicator.model.local;

import com.google.gson.annotations.SerializedName;

import java.util.HashSet;
import java.util.Set;

public class ActiveFeedsModel {
    @SerializedName("active_feeds")
    private Set<ActiveFeed> activeFeeds = new HashSet<>();

    public void add(ActiveFeed feed) {
        if (!this.activeFeeds.contains(feed)) {
            this.activeFeeds.add(feed);
        }
    }

    public void remove(long id) {
        this.activeFeeds.remove(this.get(id));
    }

    public ActiveFeed get(long id) {
        for (ActiveFeed feed : this.activeFeeds) {
            if (feed.getId() == id) {
                return feed;
            }
        }
        return null;
    }

    public Set<ActiveFeed> getActiveFeeds() {
        return this.activeFeeds;
    }

    public static class ActiveFeed {
        private long id;
        private String uuid;
        private long userId;

        public ActiveFeed(long id, String uuid) {
            this.id = id;
            this.uuid = uuid;
        }

        public void setUserId(long userId) {
            this.userId = userId;
        }

        public long getId() {
            return this.id;
        }

        public long getUserId() {
            return this.userId;
        }

        public String getUuid() {
            return this.uuid;
        }

        @Override
        public int hashCode() {
            return (int) this.id;
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof ActiveFeed && ((ActiveFeed) obj).id == this.id;
        }
    }
}
