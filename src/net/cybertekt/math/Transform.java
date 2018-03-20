package net.cybertekt.math;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

/**
 * Transform - (C) Cybertekt Software.
 *
 * Defines a translation (position), rotation (orientation), and scale (size).
 * Methods are provided for combining and perform mathematical operations on
 * transforms.
 *
 * @version 1.1.0
 * @since 1.1.0
 * @author Andrew Vektor
 */
public final class Transform {

    /**
     * Location (position) component.
     */
    private Vector3f translation = new Vector3f().zero();

    /**
     * Rotation (orientation) component.
     */
    private Quaternionf rotation = new Quaternionf().identity();

    /**
     * Scale (size) component.
     */
    private Vector3f scale = new Vector3f(1f, 1f, 1f);

    /**
     * Constructs a transform with a default translation, rotation, and scale.
     * Default translation is a zero vector, default rotation is an identity
     * quaternion, and default scale is one.
     */
    public Transform() {
    }

    /**
     * Constructs a new transform by providing a translation component and the
     * default rotation and scale components. The default rotation is an
     * identity quaternion and the default scale is one.
     *
     * @param translation the translation (location) component of this
     * transform.
     */
    public Transform(final Vector3f translation) {
        this.translation.set(translation);
    }

    /**
     * Constructs a new transform with the provided translation and rotation
     * components and the default scale component. The default scale is one.
     *
     * @param translation the translation (location) component of this
     * transform.
     * @param rotation the rotation (orientation) component of this transform.
     */
    public Transform(final Vector3f translation, final Quaternionf rotation) {
        this.translation.set(translation);
        this.rotation.set(rotation);
    }

    /**
     * Constructs a new transform with the provided translation, rotation, and
     * scale components.
     *
     * @param location the translation (location) component of this transform.
     * @param rotation the rotation (orientation) component of this transform.
     * @param scale the scale (size) component of this transform.
     */
    public Transform(final Vector3f location, final Quaternionf rotation, final Vector3f scale) {
        this.translation = location;
        this.rotation = rotation;
        this.scale = scale;
    }

    /**
     * Constructs a new transform defined by copying the values from another
     * transform. The parameter transform is not modified in any way. Passing a
     * null argument into this construction will cause a NullPointerException.
     *
     * @param toCopy the transform from which to copy.
     * @throws NullPointerException if the argument passed into this constructor
     * is null.
     */
    public Transform(final Transform toCopy) {
        this.translation.set(toCopy.getTranslation());
        this.rotation.set(toCopy.getRotation());
        this.scale.set(toCopy.getScale());
    }

    /**
     * Sets the translation, rotation, and scale of this transform equal to the
     * translation, rotation, and scale of the parameter transform. This
     * transform is internally modified by this operation. The parameter
     * transform is not modified in any way by this operation.
     *
     * @param toSet
     * @return
     */
    public final Transform set(final Transform toSet) {
        translation.set(toSet.getTranslation());
        rotation.set(toSet.getRotation());
        scale.set(toSet.getScale());
        return this;
    }
    
    /**
     * Sets the translation component of this transform.
     *
     * @param toSet the translation to set.
     */
    public final void setTranslation(final Vector3f toSet) {
        translation.set(toSet);
    }

    /**
     * Sets the translation component of this transform.
     *
     * @param x the x-axis translation.
     * @param y the y-axis translation.
     * @param z the z-axis translation.
     */
    public final void setTranslation(final float x, final float y, final float z) {
        translation.set(x, y, z);
    }

    /**
     * Returns the translation component of this transform.
     *
     * @return the translation component of this transform.
     */
    public final Vector3f getTranslation() {
        return translation;
    }

    /**
     * Sets the rotation component of this transform.
     *
     * @param toSet the rotation component of this transform.
     */
    public final void setRotation(final Quaternionf toSet) {
        rotation.set(toSet);
    }

    /**
     * Returns the rotation component of this transform.
     *
     * @return the rotation rotation component of this transform.
     */
    public final Quaternionf getRotation() {
        return rotation;
    }
    
    /**
     * Sets the vector that defines the scale component of this transform.
     *
     * @param toSet the scale vector to set.
     */
    public final void setScale(final Vector3f toSet) {
        scale.set(toSet);
    }

    /**
     * Sets the vector that defines the scale component of this transform.
     *
     * @param x the x-axis scale to set.
     * @param y the y-axis scale to set.
     * @param z the z-axis scale to set.
     */
    public final void setScale(final float x, final float y, final float z) {
        scale.set(x, y, z);
    }

    /**
     * Sets the scale component of this transform.
     *
     * @param scalar the scale component of this transform.
     */
    public final void setScale(final float scalar) {
        setScale(scalar, scalar, scalar);
    }

    /**
     * Returns the scale component of this transform.
     *
     * @return the scale component of this transform.
     */
    public final Vector3f getScale() {
        return scale;
    }

    /**
     * Generates a transformation matrix from the combined translation,
     * rotation, and scale components of this transform.
     *
     * Mathematically equivalent to:
     * <i>Translation Matrix * Rotation Matrix * Scale Matrix</i>
     *
     * @return the matrix defined by multiplying the translation, rotation, and
     * scale of components of this transform.
     */
    public final Matrix4f getMatrix() {
        return new Matrix4f().translate(translation).rotate(rotation).scale(scale);
    }

}
