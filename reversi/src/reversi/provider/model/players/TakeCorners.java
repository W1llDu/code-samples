package reversi.provider.model.players;

import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.TilePosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A Reversi strategy that aims to take corners and falls back to another strategy if
 * no corner is available.
 */
public class TakeCorners implements ReversiStrategy {

  /**
   * Chooses a move based on taking corners on the Reversi board.
   *
   * @param model gets in a ReversiModel.
   * @param player gets a Player.
   * @return an Optional containing the TilePosition representing the chosen move, or an empty
   *        Optional if no corner is available.
   */
  @Override
  public Optional<TilePosition> chooseMove(ReversiModel model, Player player) {
    List<TilePosition> corners = getCorners(model);
    MoveGenerator mg = new MoveGenerator();
    for (TilePosition tile : mg.generateMoves(model, player)) {
      if (corners.contains(tile)) {
        return Optional.of(tile);
      }
    }
    return Optional.empty();
  }

  private List<TilePosition> getCorners(ReversiModel model) {
    List<TilePosition> corners = new ArrayList<>();
    corners.add(new TilePosition(
            0, 0));
    corners.add(new TilePosition(
            0, model.getBoard().getSize() - 1));
    corners.add(new TilePosition(
            model.getBoard().getSize() - 1, 0));
    corners.add(new TilePosition(
            model.getBoard().getSize() - 1, model.getBoard().getSize() * 2 - 1));
    corners.add(new TilePosition(
            model.getBoard().getSize() * 2 - 1, 0));
    corners.add(new TilePosition(
            model.getBoard().getSize() * 2 - 1, model.getBoard().getSize() - 1));
    return corners;
  }
}
