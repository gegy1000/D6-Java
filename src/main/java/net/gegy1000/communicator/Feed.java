package net.gegy1000.communicator;

import net.gegy1000.communicator.exception.SchoolException;

import java.util.List;

/**
 * An interface representing a d6 feed
 */
public interface Feed {
    /**
     * Sets this feed as active or inactive
     *
     * @param active true if this feed should be marked active
     * @throws SchoolException if an exception occurred while marking as active
     */
    void setActive(boolean active) throws SchoolException;

    /**
     * Requests all content posted to this feed
     *
     * @return a list of all entries in this feed
     * @throws SchoolException if an exception occurred while requesting content
     */
    List<FeedEntry> requestContent() throws SchoolException;

    void setUserId(long userId) throws SchoolException;

    String getName();

    String getToken();

    String getUuid();

    String getLogo();

    String getCountry();

    String getProvince();

    long getId();

    long getUserId();

    boolean isPrivate();

    boolean isActive();
}
