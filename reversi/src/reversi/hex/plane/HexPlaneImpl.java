package reversi.hex.plane;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;

/**
 * A HexPlaneImpl is a grid of hexagons (pointy end up) with a specified radius. Coordinates can be
 * negative, and it is always symmetrical across the three cubic axis {@code q, r,} and {@code s}.
 *
 * @param <T> the type held at each point on the plane
 */
public final class HexPlaneImpl<T> implements HexPlane<T> {
  private final Map<HexPlaneCoord, Optional<T>> plane;
  private final int radius;

  /**
   * Create a new, empty HexPlane with the given radius.
   *
   * @param radius the radius of the plane from the center out, where a radius of zero is equivalent
   *               to an empty plane
   * @throws IllegalArgumentException if radius is less than 0
   */
  public HexPlaneImpl(int radius) throws IllegalArgumentException {
    if (radius < 1) {
      throw new IllegalArgumentException("radius of a HexPlane may not be less than zero");
    }
    // logically makes more sense this way; radius includes the center
    radius--;
    this.radius = radius;
    plane = new HashMap<>();
    for (int q = -radius; q <= radius; q++) {
      for (int r = Math.max(-radius - q, -radius); r <= Math.min(radius - q, radius); r++) {
        plane.put(new AxialCoordinate(q, r), Optional.empty());
      }
    }
  }

  /**
   * Create a new HexPlane given an existing plane. All properties are copied over, but none of the
   * references are carried over (shallow copy).
   *
   * @param plane the plane to use for the new HexPlane
   * @throws NullPointerException if {@code plane} is null
   */
  public HexPlaneImpl(HexPlane<T> plane) throws NullPointerException {
    Objects.requireNonNull(plane);
    this.radius = plane.getRadius();
    this.plane = new HashMap<>(plane.getPlane());
  }

  private HexPlaneImpl(Map<HexPlaneCoord, Optional<T>> map, int radius) {
    if (radius < 0) {
      throw new IllegalArgumentException("in private constructor for HexPlane: " +
          "radius of a HexPlane may not be less than zero");
    }
    Objects.requireNonNull(map);
    this.radius = radius;
    this.plane = map;
  }

  @Override
  public Map<HexPlaneCoord, Optional<T>> getPlane() {
    return plane;
  }

  /**
   * Gets the radius of the HexPlane.
   */
  @Override
  public int getRadius() {
    return radius;
  }

  @Override
  public Set<HexPlaneCoord> getKnownCoords() {
    return plane.keySet();
  }

  /**
   * Set the element at a coordinate to a given value. Overwrites anything present.
   *
   * @param coord the coordinate to add the element
   * @param elem  the element to add
   * @throws NullPointerException      if any parameter is null
   * @throws IndexOutOfBoundsException if the coordinate is not valid for the radius of this
   *                                   HexPlane
   */
  @Override
  public void setElementAt(HexPlaneCoord coord, T elem)
      throws NullPointerException, IndexOutOfBoundsException {
    checkCoordIsPresent(coord);
    plane.put(coord, Optional.of(elem));
  }

  /**
   * Map over the keys (i.e. {@link HexPlaneCoord}s) of the HexPlane, returning a new HexPlane with
   * values according to the given function.
   *
   * @param func the function to apply to every key in the map
   * @param <R>  the type of the values of teh HexPlane
   * @return a new HexPlane after mapping over the map's key set
   * @throws NullPointerException if the function is null
   */
  @Override
  public <R> HexPlane<R> keysetMap(Function<HexPlaneCoord, R> func) {
    Objects.requireNonNull(func);
    Map<HexPlaneCoord, Optional<R>> retMap = new HashMap<>();
    for (HexPlaneCoord coord : this.getKnownCoords()) {
      Optional<R> val = Optional.of(func.apply(coord));
      retMap.put(coord, val);
    }
    return new HexPlaneImpl<>(retMap, this.radius);
  }

  @Override
  public Optional<T> getAtHex(HexPlaneCoord coordinate) throws IndexOutOfBoundsException {
    checkCoordIsPresent(coordinate);
    return plane.get(coordinate);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof HexPlaneImpl)) {
      return false;
    }
    return this.getPlane().equals(((HexPlane<?>) o).getPlane());
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.getPlane(), this.getRadius());
  }

  private void checkCoordIsPresent(HexPlaneCoord coordinate) {
    if (!plane.containsKey(Objects.requireNonNull(coordinate))) {
      throw new IndexOutOfBoundsException("HexPlane: coordinate must already be " +
          "present in the plane. Coordinate: " + coordinate);
    }
  }
}