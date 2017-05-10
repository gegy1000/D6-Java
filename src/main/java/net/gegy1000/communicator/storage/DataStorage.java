package net.gegy1000.communicator.storage;

import java.io.IOException;

public interface DataStorage {
    String GLOBAL_SETTINGS = "global_settings";
    String ACTIVE_FEEDS = "active_feeds";
    String CHANNELS = "channels";
    String GRADES = "grades";

    boolean has(String identifier);

    <T> T load(String identifier, Class<T> type) throws IOException;

    <T> void save(String identifier, T data) throws IOException;
}
