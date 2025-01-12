package reversi.hex.summarizer.reversiinfo;

import reversi.controller.Player;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexPlaneSummarizer;

/**
 * A {@link IsGameOverSummarizer} will summarize whether the game is over or not. A game can be over
 * only if there are no possible moves that can be made by either player.
 */
public class IsGameOverSummarizer implements HexPlaneSummarizer<Player, Boolean> {

  @Override
  public Boolean apply(HexPlane<Player> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    return !(new PlayerHasLegalMovesSummarizer(Player.PLAYER1).apply(plane)
        || new PlayerHasLegalMovesSummarizer(Player.PLAYER2).apply(plane));
  }
}
