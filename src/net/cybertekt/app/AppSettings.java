package net.cybertekt.app;

/**
 * Application Settings - (C) Cybertekt Software
 *
 * Defines basic settings for an application such as its name, version, and
 * update frequency. These settings may be changed at any time while the
 * application is running. Changes made to these settings while the application
 * is running will take effect immediately.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class AppSettings {

    /**
     * Application name.
     */
    private String name;

    /**
     * Application version string.
     */
    private String version;

    /**
     * The frequency, in milliseconds, at which the fixed application
     * {@link Application#update() update method} is called.
     */
    private double msPerUpdate;

    /**
     * The maximum number of times the {@link Application#update() fixed update}
     * method may be called in a single frame. This prevents the "spiral of
     * death" from occurring when the time required to perform a fixed update
     * exceeds the amount processing time required within the fixed update. This
     * value cannot be set directly, instead it is always equal to the target
     * number of updates per second multiplied by two.
     */
    private int maxUpdates;

    /**
     * Simple constructor for defining application settings. Uses a default
     * update frequency of 30 updates per second.
     *
     * @param name the name of the application.
     * @param version the application version string.
     */
    public AppSettings(final String name, final String version) {
        this(name, version, 30);
    }

    /**
     * Full constructor for defining application settings.
     *
     * @param name the name of the application.
     * @param version the application version string.
     * @param ups the frequency, in seconds, at which to update the application.
     */
    public AppSettings(final String name, final String version, final int ups) {
        this.name = name;
        this.version = version;
        this.msPerUpdate = 1000 / ups;
        this.maxUpdates = ups * 2;
    }

    /**
     * Applies the specified application settings to these settings. Any changes
     * made to the provided application settings after this call <b>will not</b>
     * have any effect on these settings unless the provided settings are
     * reapplied to these settings via another call to this method.
     *
     * @param settings the application settings to apply.
     */
    public final void setSettings(final AppSettings settings) {
        this.name = settings.getName();
        this.version = settings.getVersion();
        this.msPerUpdate = settings.getMsPerUpdate();
        this.maxUpdates = settings.getMaxUpdates();
    }

    /**
     * Sets the name of the application.
     *
     * @param name the new name of the application.
     */
    public final void setName(final String name) {
        this.name = name;
    }

    /**
     * Returns the name of the application.
     *
     * @return the name of the application.
     */
    public final String getName() {
        return name;
    }

    /**
     * Sets the version string of the application.
     *
     * @param version the application version string.
     */
    public final void setVersion(final String version) {
        this.version = version;
    }

    /**
     * Returns the version string of the application.
     *
     * @return the version string of the application.
     */
    public final String getVersion() {
        return version;
    }

    /**
     * Sets the frequency, in seconds, at which the fixed application
     * {@link Application#update() update method} is called. This method also
     * changes the maximum number of updates that may be performed in a single
     * frame which is always equal to the UPS multiplied by two.
     *
     * @param ups the frequency, in milliseconds, at which the fixed update
     * method is called.
     */
    public final void setUps(final int ups) {
        msPerUpdate = 1000 / ups;
        maxUpdates = ups * 2;
    }

    /**
     * Returns the frequency, in milliseconds, at which the fixed application
     * {@link Application#update() update method} is called.
     *
     * @return the update frequency, in milliseconds.
     */
    public final double getMsPerUpdate() {
        return msPerUpdate;
    }

    /**
     * Returns the maximum number of times the {@link Application#update()}
     * method may be called in a single frame. This prevents the "spiral of
     * death" from occurring when the time required to perform a fixed update
     * exceeds the amount processing time required within the fixed update.
     *
     * @return the maximum number of time the fixed update method may be called
     * in a single frame.
     */
    public final int getMaxUpdates() {
        return maxUpdates;
    }

}
