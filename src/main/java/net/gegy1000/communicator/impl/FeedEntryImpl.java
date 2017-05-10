package net.gegy1000.communicator.impl;

import net.gegy1000.communicator.FeedEntry;

/**
 * A {@link net.gegy1000.communicator.Feed} content entry
 */
public class FeedEntryImpl implements FeedEntry {
    private final long serial;
    private final long identifier;
    private final long category;
    private final long time;

    private final String title;
    private final String details;
    private final String url;

    private final Type type;
    private final Transaction transaction;

    public FeedEntryImpl(long serial, long identifier, long category, long time, String title, String details, String url, Type type, Transaction transaction) {
        this.serial = serial;
        this.identifier = identifier;
        this.category = category;
        this.time = time;
        this.title = title;
        this.details = details;
        this.url = url;
        this.type = type;
        this.transaction = transaction;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public String getDetails() {
        return this.details;
    }

    @Override
    public String getUrl() {
        return this.url;
    }

    @Override
    public long getTime() {
        return this.time;
    }

    @Override
    public boolean hasTime() {
        return this.time != -1;
    }

    @Override
    public Transaction getTransaction() {
        return this.transaction;
    }

    @Override
    public Type getType() {
        return this.type;
    }

    @Override
    public long getIdentifier() {
        return this.identifier;
    }

    @Override
    public long getSerial() {
        return this.serial;
    }

    @Override
    public long getCategory() {
        return this.category;
    }
}
