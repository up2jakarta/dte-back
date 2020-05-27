package io.github.up2jakarta.dte.exe.loader;

/**
 * Exception thrown the loader when a persisted object is not found.
 *
 * @author A.ABBESSI
 */
public class LoadingException extends RuntimeException {

    private static final String FORMAT = "Cannot load %s identified by [%s]";
    private final String resource;
    private final String origin;

    /**
     * Public constructor for LoadingException.
     *
     * @param resourceName the resource name
     * @param resourceId   the resource identifier
     */
    public LoadingException(final String resourceName, final Number resourceId) {
        super(String.format(FORMAT, resourceName, resourceId));
        this.resource = resourceName;
        this.origin = resourceName + "Id";
    }

    /**
     * Public constructor for LoadingException.
     *
     * @param resourceName the resource name
     * @param resourceId   the resource identifier
     * @param message      the message details
     */
    public LoadingException(final String resourceName, final Number resourceId, final String message) {
        super(String.format(FORMAT, resourceName, resourceId) + ": " + message);
        this.resource = resourceName;
        this.origin = resourceName + "Id";
    }

    /**
     * Public constructor for LoadingException.
     *
     * @param resourceName the resource name
     * @param resourceId   the resource identifier
     * @param message      the message details
     */
    public LoadingException(final String resourceName, final String resourceId, final String message) {
        super(String.format(FORMAT, resourceName, resourceId) + ": " + message);
        this.resource = resourceName;
        this.origin = resourceId;
    }

    /**
     * @return the resource name
     */
    public String getResource() {
        return resource;
    }

    /**
     * @return the error origin
     */
    public String getOrigin() {
        return origin;
    }
}
