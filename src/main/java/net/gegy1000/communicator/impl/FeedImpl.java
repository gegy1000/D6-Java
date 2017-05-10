package net.gegy1000.communicator.impl;

import net.gegy1000.communicator.Communicator;
import net.gegy1000.communicator.Feed;
import net.gegy1000.communicator.FeedEntry;
import net.gegy1000.communicator.exception.SchoolException;
import net.gegy1000.communicator.model.FeedModel;
import net.gegy1000.communicator.model.local.ActiveFeedsModel;
import net.gegy1000.communicator.storage.DataStorage;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class FeedImpl implements Feed {
    private Communicator communicator;

    private String name;
    private String token;
    private long id;

    private String logo;

    private String country;
    private String province;

    private boolean isPrivate;

    private boolean active;
    private String uuid;
    private long userId;

    public FeedImpl(Communicator communicator, FeedModel entry) throws SchoolException {
        this.communicator = communicator;
        this.name = entry.getName();
        this.token = entry.getToken();
        this.id = entry.getId();
        this.logo = entry.getLogo();
        this.country = entry.getCountry();
        this.province = entry.getProvice();
        this.isPrivate = entry.isPrivate() == 1;
        ActiveFeedsModel.ActiveFeed activeFeed = communicator.getActiveFeeds().get(this.getId());
        this.active = activeFeed != null;
        if (this.active) {
            this.uuid = activeFeed.getUuid();
            this.userId = activeFeed.getUserId();
        }
    }

    @Override
    public void setActive(boolean active) throws SchoolException {
        if (this.active != active) {
            ActiveFeedsModel activeFeeds = this.communicator.getActiveFeeds();
            activeFeeds.remove(this.id);
            this.active = active;
            if (this.active) {
                this.uuid = UUID.randomUUID().toString();
                activeFeeds.getActiveFeeds().add(new ActiveFeedsModel.ActiveFeed(this.id, this.uuid));
            }
            try {
                this.communicator.getStorage().save(DataStorage.ACTIVE_FEEDS, activeFeeds);
            } catch (IOException e) {
                throw new SchoolException(e);
            }
            this.communicator.updateFeed(this);
        }
    }

    @Override
    public void setUserId(long userId) throws SchoolException {
        ActiveFeedsModel activeFeeds = this.communicator.getActiveFeeds();
        ActiveFeedsModel.ActiveFeed feed = activeFeeds.get(this.id);
        feed.setUserId(userId);
        try {
            this.communicator.getStorage().save(DataStorage.ACTIVE_FEEDS, activeFeeds);
        } catch (IOException e) {
            throw new SchoolException(e);
        }
        this.userId = userId;
    }

    @Override
    public List<FeedEntry> requestContent() throws SchoolException {
        return this.communicator.getRequestHandler().requestFeedContent(this);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getToken() {
        return this.token;
    }

    @Override
    public String getUuid() {
        return this.uuid;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public long getUserId() {
        return this.userId;
    }

    @Override
    public boolean isPrivate() {
        return this.isPrivate;
    }

    @Override
    public String getLogo() {
        return this.logo;
    }

    @Override
    public String getCountry() {
        return this.country;
    }

    @Override
    public String getProvince() {
        return this.province;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public int hashCode() {
        return (int) this.id;
    }

    @Override
    public boolean equals(Object object) {
        return object instanceof FeedImpl && ((FeedImpl) object).id == this.id;
    }
}
