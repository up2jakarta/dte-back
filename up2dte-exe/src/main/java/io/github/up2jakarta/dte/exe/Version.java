package io.github.up2jakarta.dte.exe;

import io.github.up2jakarta.dte.exe.loader.LoadingException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

/**
 * Version info singleton, filled at build time.
 */
@SuppressWarnings("WeakerAccess")
public final class Version {

    static final Version INSTANCE = new Version("META-INF/engine.properties");

    private final String specVersion;
    private final String buildJavaVersion;
    private final String buildVersion;
    private final LocalDateTime buildTime;
    private final String buildUser;

    private final String commitId;
    private LocalDateTime commitTime;

    private final String developerName;
    private final String developerEmail;
    private final String developerRole;

    /**
     * Protected constructor for Version.
     *
     * @param properties the properties file
     */
    Version(final String properties) {
        try {
            final Properties prop = new Properties();
            prop.load(Version.class.getClassLoader().getResourceAsStream(properties));
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern((String) prop.get("dte.build.date.format"));
            buildTime = LocalDateTime.parse((String) prop.get("dte.build.time"), formatter);
            buildVersion = (String) prop.get("dte.build.version");
            buildJavaVersion = (String) prop.get("dte.build.java.version");
            buildUser = (String) prop.get("dte.build.user");
            specVersion = (String) prop.get("dte.spec.version");

            commitId = (String) prop.get("dte.commit.abbrev");
            try {
                commitTime = LocalDateTime.parse((String) prop.get("dte.commit.time"), formatter);
            } catch (RuntimeException e) {
                commitTime = buildTime;
            }
            developerName = (String) prop.get("dte.developer.name");
            developerEmail = (String) prop.get("dte.developer.email");
            developerRole = (String) prop.get("dte.developer.role");
        } catch (IOException | RuntimeException ex) {
            throw new LoadingException("file", properties, ex.getMessage());
        }
    }

    /**
     * @return The project spec version.
     */
    public String getSpecVersion() {
        return specVersion;
    }

    /**
     * @return The build Java version.
     */
    public String getBuildJavaVersion() {
        return buildJavaVersion;
    }

    /**
     * @return The project build version.
     */
    public String getBuildVersion() {
        return buildVersion;
    }

    /**
     * @return The project build time.
     */
    public LocalDateTime getBuildTime() {
        return buildTime;
    }

    /**
     * @return The project build user.
     */
    public String getBuildUser() {
        return buildUser;
    }

    /**
     * @return The last commit id.
     */
    public String getCommitId() {
        return commitId;
    }

    /**
     * @return The last commit time.
     */
    public LocalDateTime getCommitTime() {
        return commitTime;
    }

    /**
     * @return The developer full name.
     */
    public String getCreator() {
        return developerName;
    }

    /**
     * @return The developer email.
     */
    public String getContact() {
        return developerEmail;
    }

    /**
     * @return The developer role.
     */
    public String getRole() {
        return developerRole;
    }
}
