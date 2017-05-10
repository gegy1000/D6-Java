package net.gegy1000.communicator.impl;

public class FeedEntry {
    private final long serial;
    private final long identifier;
    private final long category;
    private final long time;

    private final String title;
    private final String details;
    private final String url;

    private final Type type;
    private final Transaction transaction;

    public FeedEntry(long serial, long identifier, long category, long time, String title, String details, String url, Type type, Transaction transaction) {
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

    public String getTitle() {
        return this.title;
    }

    public String getDetails() {
        return this.details;
    }

    public String getUrl() {
        return this.url;
    }

    public long getTime() {
        return this.time;
    }

    public boolean hasTime() {
        return this.time != -1;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public Type getType() {
        return this.type;
    }

    public long getIdentifier() {
        return this.identifier;
    }

    public long getSerial() {
        return this.serial;
    }

    public long getCategory() {
        return this.category;
    }

    public enum Type {
        RESOURCE,
        CHANNEL,
        GRADE,
        NEWS,
        CALENDAR,
        ALERT,
        RESOURCE_CATEGORY,
        GALLERY,
        CONTACT,
        CLIENT_SETTINGS;

        public static Type get(String name) {
            switch (name) {
                case "resource":
                    return RESOURCE;
                case "channel":
                    return CHANNEL;
                case "grade":
                    return GRADE;
                case "news":
                    return NEWS;
                case "calendar":
                    return CALENDAR;
                case "client_settings":
                    return CLIENT_SETTINGS;
                case "resourceCategory":
                    return RESOURCE_CATEGORY;
                case "alert":
                    return ALERT;
                case "gallery":
                    return GALLERY;
                case "contact":
                    return CONTACT;
            }
            return null;
        }
    }

    public enum Transaction {
        UPDATE,
        DELETE;

        public static Transaction get(String name) {
            switch (name) {
                case "update":
                    return UPDATE;
                case "delete":
                    return DELETE;
            }
            return null;
        }
    }
}
