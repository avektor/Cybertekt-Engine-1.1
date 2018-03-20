package net.cybertekt.app.listener;

/**
 * Application Update Listener - (C) Cybertekt Software
 *
 * Interface for receiving application update events. This interface should be
 * implemented by classes that need to hook directly into the main update loop
 * of an application. Update listeners are called in the order that they were
 * attached to the application.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public interface UpdateListener {

    /**
     * Fixed Time Step Update. This method is called repeatedly at a fixed rate.
     * Should be used sparingly to implement logic that requires deterministic
     * behavior, such as physics. The time required to process all fixed updates
     * must be less than the update frequency or the overall application speed
     * will be slower than expected and may result in a "spiral of death". The
     * frequency at which this method is called is determined by the target UPS
     * as defined in the {@link AppSettings application settings}.
     */
    public abstract void update();

    /**
     * Variable Time Step Update. This method is called repeatedly at a rate of
     * once per frame. The update frequency is determined entirely by the frame
     * rate of the application. Due to potential rounding errors and variable
     * frame rates, this method should only be used to implement logic that does
     * not require precise deterministic behavior. This method is called more
     * often than the fixed update method which makes it ideal for implementing
     * smooth motion.
     *
     * @param tpf the amount of elapsed time, in milliseconds, since the last
     * variable time step update.
     */
    public abstract void update(final double tpf);
}
