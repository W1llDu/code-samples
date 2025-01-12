package reversi.hex.summarizer.strategy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import reversi.controller.Player;
import reversi.hex.coordinates.CubicalCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexPlaneSummarizer;
import reversi.hex.summarizer.reversiinfo.PlayerPossibleMovesSummarizer;

/**
 * A Summarizer for use in implementing a smarter AI. It will filter out any moves that are next to
 * corners to avoid the opponent making a move that results in an invulnerable piece.
 */
public class AvoidNextToCornersStrategySummarizer
    implements HexPlaneSummarizer<Player, List<HexPlaneCoord>> {
  private final Player player;

  /**
   * Create a new {@link AvoidNextToCornersStrategySummarizer} given the player this summarizer uses
   * to calculate move points.
   *
   * @param player the player used to calculate the points gained from moves
   */
  public AvoidNextToCornersStrategySummarizer(Player player) {
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public List<HexPlaneCoord> apply(HexPlane<Player> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    List<HexPlaneCoord> moves = new PlayerPossibleMovesSummarizer(player).apply(plane);
    // when moves are added to retList the uppermost-leftmost coordinate is added first.
    List<HexPlaneCoord> retList = new ArrayList<>();
    for (HexPlaneCoord coord : moves) {
      if (nextToCorners(plane.getRadius()).contains(coord)) {
        continue;
      }
      retList.add(coord);
    }
    return retList;
  }

  private List<HexPlaneCoord> nextToCorners(int rad) {
    List<HexPlaneCoord> neighbors = new ArrayList<>();
    for (HexPlaneCoord corner : Arrays.asList(
        new CubicalCoordinate(rad, -rad, 0),
        new CubicalCoordinate(-rad, rad, 0),
        new CubicalCoordinate(rad, 0, -rad),
        new CubicalCoordinate(-rad, 0, rad),
        new CubicalCoordinate(0, rad, -rad),
        new CubicalCoordinate(0, -rad, rad))) {
      for (HexPlaneCoord delta : Arrays.asList(
          new CubicalCoordinate(1, -1, 0),
          new CubicalCoordinate(-1, 1, 0),
          new CubicalCoordinate(1, 0, -1),
          new CubicalCoordinate(-1, 0, 1),
          new CubicalCoordinate(0, 1, -1),
          new CubicalCoordinate(0, -1, 1))) {
        HexPlaneCoord neighbor = new CubicalCoordinate(corner.getQ() + delta.getQ(),
            corner.getR() + delta.getR(),
            corner.getS() + delta.getS());
        if (Math.abs(neighbor.getQ()) <= rad
            && Math.abs(neighbor.getR()) <= rad
            && Math.abs(neighbor.getS()) <= rad) {
          neighbors.add(neighbor);
        }
      }
    }
    return neighbors;
  }
}
