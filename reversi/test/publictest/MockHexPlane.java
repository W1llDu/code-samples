package publictest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;

/**
 * A MockHexPlane is a grid of hexagons (pointy end up) with a specified radius. Coordinates can be
 * negative, and it is always symmetrical across the three cubic axis {@code q, r,} and {@code s}.
 * This logs actions on the inner hexPlane.
 *
 * @param <T> the type held at each point on the plane
 */
public class MockHexPlane<T> implements HexPlane<T> {
  private final HexPlane<T> plane;
  private final StringBuilder log;
  private final List<HexPlaneCoord> getCoords;
  private final List<HexPlaneCoord> setCoords;
  private final List<T> setVals;

  /**
   * Constructor for MockHexPlane.
   *
   * @param plane the inner plane.
   * @param log the log.
   */
  public MockHexPlane(HexPlane<T> plane, StringBuilder log) {
    this.plane = Objects.requireNonNull(plane);
    this.log = Objects.requireNonNull(log);
    this.getCoords = new ArrayList<>();
    this.setCoords = new ArrayList<>();
    this.setVals = new ArrayList<>();
  }

  @Override
  public Map<HexPlaneCoord, Optional<T>> getPlane() {
    return plane.getPlane();
  }

  @Override
  public int getRadius() {
    return plane.getRadius();
  }

  @Override
  public Set<HexPlaneCoord> getKnownCoords() {
    return plane.getKnownCoords();
  }

  @Override
  public void setElementAt(HexPlaneCoord coord, T elem)
          throws NullPointerException, IndexOutOfBoundsException {
    log.append(String.format("MockHexPlane: Setting element at %s to %s.\n", coord, elem));
    setCoords.add(coord);
    setVals.add(elem);
    plane.setElementAt(coord, elem);
  }

  @Override
  public <R> HexPlane<R> keysetMap(Function<HexPlaneCoord, R> func) {
    log.append(String.format("MockHexPlane: Mapping %s.\n", func.toString()));
    return plane.keysetMap(func);
  }

  @Override
  public Optional<T> getAtHex(HexPlaneCoord coordinate) throws IndexOutOfBoundsException {
    log.append(String.format("MockHexPlane: Getting at position %s.\n", coordinate));
    getCoords.add(coordinate);
    return plane.getAtHex(coordinate);
  }

  public List<HexPlaneCoord> getLog() {
    return new ArrayList<>(getCoords);
  }

  public List<HexPlaneCoord> setLogCoords() {
    return new ArrayList<>(setCoords);
  }

  public List<T> setLogVals() {
    return new ArrayList<>(setVals);
  }
}
