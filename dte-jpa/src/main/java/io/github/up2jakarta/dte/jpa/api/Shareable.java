package io.github.up2jakarta.dte.jpa.api;

/**
 * Shareable entity.
 */
public interface Shareable {

    /**
     * @return the related group.
     */
    IGroup getGroup();

    /**
     * Test if the current entity is shared with other groups or via web API.
     *
     * @return shared flag
     */
    boolean isShared();

    /**
     * Should share the current object with other groups or via web API.
     *
     * @param shared shared flag
     */
    void setShared(boolean shared);
}
