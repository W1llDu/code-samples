package squareGame.controller.player;

import java.io.IOException;
import java.util.Scanner;

import squareGame.model.Move;
import squareGame.model.SquareGameModel;

public class HumanPlayer implements IPlayer {
  private final Scanner sc;
  private final Appendable ap;

  public HumanPlayer(Scanner sc, Appendable ap) {
    if (sc == null || ap == null) {
      throw new IllegalArgumentException();
    }
    this.sc = sc;
    this.ap = ap;
  }

  @Override
  public Move getMove(SquareGameModel model) throws QuitException {
    Move move = new Move(getNext() - 1, getNext());
    while (!model.isMoveValid(move)) {
      try {
        this.ap.append("Move invalid. Enter new move: ");
      } catch (IOException e) {
        e.printStackTrace();
      }
      move = new Move(getNext() - 1, getNext());
    }
    return move;
  }

  private int getNext() throws QuitException {
    while (!sc.hasNextInt()) {
      if (!sc.hasNext()) {
        throw new IllegalStateException();
      }
      if (sc.next().equals("q")) {
        throw new QuitException();
      }
    }
    return this.sc.nextInt();
  }
}
