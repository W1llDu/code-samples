package reversi.provider.model.players;

import java.util.Optional;

import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.TileColor;
import reversi.provider.model.board.TilePosition;

/**
 * Robot version of the player.
 */
public class ReversiRobotPlayer implements Player {

  private final TileColor color;
  private InfallibleReversiStrategy strategy = null;
  private ReversiStrategy strategyR = null;
  private final ReversiModel model;

  /**
   * Constructs a ReversiPlayer with the specified tile color.
   *
   * @param color the tile color of the player (either TileColor.WHITE or TileColor.BLACK)
   */
  public ReversiRobotPlayer(TileColor color, InfallibleReversiStrategy strategy,
                            ReversiModel model) {
    this.color = color;
    this.strategy = strategy;
    this.model = model;
  }

  /**
   * Constructor for the robot player.
   *
   * @param color     of tile.
   * @param strategyR type of strategy for the robot.
   * @param model     board.
   */
  public ReversiRobotPlayer(TileColor color, ReversiStrategy strategyR, ReversiModel model) {
    this.color = color;
    this.strategyR = strategyR;
    this.model = model;
  }

  @Override
  public TileColor getColor() {
    return color;
  }

  @Override
  public Boolean isHuman() {
    return false;
  }

  @Override
  public TilePosition makeMove() {

    if (strategy != null) {
      TilePosition result = strategy.chooseMove(model, this);
      return result;
    } else {
      Optional<TilePosition> result = strategyR.chooseMove(model, this);
      if (result.isEmpty()) {
        return new TilePosition(-1, -1);
      } else {
        return result.get();
      }
    }
  }
}
