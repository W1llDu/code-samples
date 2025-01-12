package reversi.hex.coordinates;


import java.util.Optional;

/**
 * Interfaces implementing this interface are able to use {@link HexPlaneCoord}s as indices.
 *
 * @param <T> the type of the elements at the index
 */
public interface HexIndexable<T> {
  /**
   * Get the element at the hex coordinate.
   *
   * @param coordinate the coordinate to index to
   * @return the element at the coordinate
   * @throws IndexOutOfBoundsException if the coordinate is out of bounds of the structure
   * @throws NullPointerException      if coordinate is null
   */
  Optional<T> getAtHex(HexPlaneCoord coordinate)
      throws IndexOutOfBoundsException, NullPointerException;
}
