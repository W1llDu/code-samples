package reversi.hex.summarizer.reversiinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import reversi.controller.Player;
import reversi.hex.coordinates.CubicalCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexPlaneSummarizer;

/**
 * A {@link CoordsToFlipGivenMoveSummarizer} summarizes the coords to invert of a potential move at
 * a specific spot for a given player.
 */
public class CoordsToFlipGivenMoveSummarizer
    implements HexPlaneSummarizer<Player, List<HexPlaneCoord>> {
  private final HexPlaneCoord coordinate;
  private final Player player;

  /**
   * Create a new {@link CoordsToFlipGivenMoveSummarizer}, which will use the supplied coordinates
   * to calculate its summarization.
   *
   * @param coordinate the coordinate to find points for a particular move
   * @param player     the player to use in calculations for points
   * @throws NullPointerException if any argument is null
   */
  public CoordsToFlipGivenMoveSummarizer(HexPlaneCoord coordinate, Player player) {
    this.coordinate = Objects.requireNonNull(coordinate);
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public List<HexPlaneCoord> apply(HexPlane<Player> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    Objects.requireNonNull(plane);
    if (!plane.getPlane().containsKey(coordinate)) {
      throw new IndexOutOfBoundsException("CoordsToInvertAtPointSummarizer: " +
          "coordinate is out of bounds: " + coordinate);
    }
    // cant overwrite piece
    if (plane.getAtHex(coordinate).isPresent()) {
      return new ArrayList<>();
    }
    List<HexPlaneCoord> coords = new ArrayList<>();
    for (HexPlaneCoord delta : Arrays.asList(
        new CubicalCoordinate(1, -1, 0),
        new CubicalCoordinate(-1, 1, 0),
        new CubicalCoordinate(1, 0, -1),
        new CubicalCoordinate(-1, 0, 1),
        new CubicalCoordinate(0, 1, -1),
        new CubicalCoordinate(0, -1, 1))) {
      coords.addAll(getCoordsInDirection(
          iterCount -> new CubicalCoordinate(
              coordinate.getQ() + (iterCount * delta.getQ()),
              coordinate.getR() + (iterCount * delta.getR()),
              coordinate.getS() + (iterCount * delta.getS())),
          plane));
    }
    coords.add(this.coordinate);
    return coords;
  }

  private List<HexPlaneCoord> getCoordsInDirection(
      Function<Integer, CubicalCoordinate> offsetToCubical,
      HexPlane<Player> plane) {
    int iterCount = 1;
    List<HexPlaneCoord> retArr = new ArrayList<>();
    CubicalCoordinate next = offsetToCubical.apply(iterCount);
    // if the coord is out of bounds or the coord is empty, abort
    while (plane.getKnownCoords().contains(next) && plane.getAtHex(next).isPresent()) {

      // if the coord we are checking is the opposite player, add a point
      if (plane.getAtHex(next).get().equals(this.player.getOther())) {
        retArr.add(next);
      } else {
        // the coord we are checking is the current player, so stop
        return retArr;
      }

      iterCount++;
      next = offsetToCubical.apply(iterCount);
    }
    return new ArrayList<>();
  }
}
