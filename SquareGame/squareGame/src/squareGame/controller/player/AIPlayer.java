package squareGame.controller.player;

import java.util.List;

import squareGame.model.Move;
import squareGame.model.SquareGameModel;

public class AIPlayer implements IPlayer {

  @Override
  public Move getMove(SquareGameModel model) {
    int amount = 0;
    List<Integer> rows = model.getRows();
    // bitwise XOR of all rows
    for (int row : rows) amount ^= row;
    if (amount == 0) {
      // losing game, remove 1 from longest row
      int row = 0;
      for (int i = 0; i < rows.size(); i++) {
        if (rows.get(i) > rows.get(row)) {
          row = i;
        }
      }
      return new Move(row, 1);
    } else {
      for (int i = 0; i < rows.size(); i++) {
        // find row s.t. #squares XOR amount < #squares
        if ((rows.get(i) ^ amount) < rows.get(i)) {
          return new Move(i, rows.get(i) - (rows.get(i) ^ amount));
        }
      }
      throw new IllegalStateException("This should never happen.");
    }
  }
}
