package squareGame.controller;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Scanner;

import squareGame.model.SquareGameModel;
import squareGame.controller.player.AIPlayer;
import squareGame.controller.player.HumanPlayer;
import squareGame.controller.player.IPlayer;
import squareGame.controller.player.QuitException;
import squareGame.view.SquareGameTextualView;
import squareGame.view.SquareGameView;

public class SquareGameTextualController implements SquareGameController {
  private final Scanner sc;
  private final Appendable ap;

  public SquareGameTextualController(Readable rd, Appendable ap) {
    if ((rd == null) || (ap == null)) {
      throw new IllegalArgumentException();
    }
    this.sc = new Scanner(rd);
    this.ap = ap;
  }

  @Override
  public void playGame(SquareGameModel model, String player1, String player2) {
    if (model == null || player1 == null || player2 == null) {
      throw new IllegalArgumentException();
    }
    SquareGameView view = new SquareGameTextualView(model, this.ap);
    Deque<IPlayer> players = new ArrayDeque<>();
    for (String type : List.of(player1, player2)) {
      switch (type) {
        case "human":
          players.add(new HumanPlayer(this.sc, this.ap));
          break;
        case "computer":
        case "ai":
          players.add(new AIPlayer());
          break;
        default:
        throw new IllegalArgumentException();
      }
    }
    try {
      this.ap.append("------------------------------" + "\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
    int player = 0;
    while (! model.isGameOver()) {
      try {
        view.render(player);
      } catch (IOException e) {
        e.printStackTrace();
      }
      try {
        model.move(players.getFirst().getMove(model));
        players.addLast(players.removeFirst());
        player = (player + 1) % 2;
      } catch (QuitException q) {
        return;
      }
    }
    try {
      this.ap.append("Winner: Player ").append(String.valueOf((2 + player - 1) % 2 + 1)).append("\n");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
