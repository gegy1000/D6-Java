package net.gegy1000.communicator;

public interface FeedEntry {
    String getTitle();

    String getDetails();

    String getUrl();

    long getTime();

    boolean hasTime();

    Transaction getTransaction();

    Type getType();

    long getIdentifier();

    long getSerial();

    long getCategory();

    enum Type {
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

    enum Transaction {
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
