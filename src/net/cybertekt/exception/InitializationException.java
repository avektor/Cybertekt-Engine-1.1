package net.cybertekt.exception;

/**
 * Initialization Exception - (C) Cybertekt Software
 *
 * <p>
 * Occurs when the application encounters an error while attempting to
 * initialize a critical resource.
 * </p>
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public class InitializationException extends RuntimeException {

    public InitializationException(final String msg) {
        super(msg);
    }
}
