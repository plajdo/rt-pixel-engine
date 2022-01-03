package sk.bytecode.bludisko.rt.game.graphics;

/**
 * @see Traceable#hitDistance(Ray)
 */
public interface Traceable {

    /**
     * Checks if the object intersects with a ray.
     * @param ray Ray to check the intersection with.
     * @param <T> Object that can be traced implementing {@link Traceable} interface
     * @return Negative number if there was no intersection or float greater or equal than 0
     * representing the distance of intersection from rays' origin.
     */
    <T extends Traceable> float hitDistance(Ray<T> ray);

}
