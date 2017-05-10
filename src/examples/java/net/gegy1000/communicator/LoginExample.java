package net.gegy1000.communicator;

import net.gegy1000.communicator.exception.SchoolException;
import net.gegy1000.communicator.storage.JSONDataStorage;

import java.io.File;
import java.util.Date;
import java.util.List;

public class LoginExample {
    public static void main(String[] args) throws SchoolException {
        // Build a Communicator object with JSON data storage in a "data" directory
        Communicator communicator = Communicator.builder()
                .withStorage(new JSONDataStorage(new File("data")))
                .build();
        // Login with the Communicator
        communicator.login("d6-api", "d6-api");
        System.out.println("Connected to " + communicator.getSettings().getAppName() + " and received " + communicator.getFeeds().size() + " feeds");
        for (Feed feed : communicator.getFeeds()) {
            // If the feed is active, request all feed content and print it
            if (feed.isActive()) {
                List<FeedEntry> entries = feed.requestContent();
                for (FeedEntry entry : entries) {
                    // For every UPDATE entry, print the contents
                    if (entry.getTransaction() == FeedEntry.Transaction.UPDATE) {
                        System.out.println(entry.getTitle() + " (" + entry.getType() + ")");
                        if (entry.getUrl() != null) {
                            System.out.println(entry.getUrl());
                        }
                        if (entry.hasTime()) {
                            System.out.println("Created at " + D6Constants.FEED_DATE_FORMAT.format(new Date(entry.getTime())));
                        }
                        System.out.println();
                    }
                }
            }
        }
    }
}
