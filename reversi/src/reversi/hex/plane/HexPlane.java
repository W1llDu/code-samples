package reversi.hex.plane;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import reversi.hex.coordinates.HexIndexable;
import reversi.hex.coordinates.HexPlaneCoord;

/**
 * A HexPlane is a grid of hexagons (pointy end up) with a specified radius. Coordinates can be
 * negative, and it is always symmetrical across the three cubic axis {@code q, r,} and {@code s}.
 *
 * @param <T> the type held at each point on the plane
 */
public interface HexPlane<T> extends HexIndexable<T> {
  /**
   * Gets a Map representation of the plane. <b><i>Changes made to the map returned will be
   * reflected on the HexPlane.</i></b> Users of HexPlane wishing to expose the HexPlane should make
   * sure to avoid leaking a mutable reference.
   *
   * @return a mutable {@link Map} representation of the plane
   */
  Map<HexPlaneCoord, Optional<T>> getPlane();

  /**
   * Gets the radius of the HexPlane.
   */
  int getRadius();

  /**
   * Gets the {@link Set} of known HexPlaneCoords.
   */
  Set<HexPlaneCoord> getKnownCoords();

  /**
   * Set the element at a coordinate to a given value. Overwrites anything present.
   *
   * @param coord the coordinate to add the element
   * @param elem  the element to add
   * @throws NullPointerException      if any parameter is null
   * @throws IndexOutOfBoundsException if the coordinate is not valid for the radius of this
   *                                   HexPlane
   */
  void setElementAt(HexPlaneCoord coord, T elem)
      throws NullPointerException, IndexOutOfBoundsException;

  /**
   * Map over the keys (i.e. {@link HexPlaneCoord}s) of the HexPlane, returning a new HexPlane with
   * values according to the given function.
   *
   * @param func the function to apply to every key in the map
   * @param <R>  the type of the values of teh HexPlane
   * @return a new HexPlane after mapping over the map's key set
   * @throws NullPointerException if the function is null
   */
  <R> HexPlane<R> keysetMap(Function<HexPlaneCoord, R> func);

  @Override
  Optional<T> getAtHex(HexPlaneCoord coordinate) throws IndexOutOfBoundsException;

  @Override
  boolean equals(Object o);

  @Override
  int hashCode();
}
