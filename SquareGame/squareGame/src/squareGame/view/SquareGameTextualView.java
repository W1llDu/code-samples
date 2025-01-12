package squareGame.view;

import java.io.IOException;
import java.util.stream.Collectors;

import squareGame.model.SquareGameModel;

public class SquareGameTextualView implements SquareGameView {
  private final SquareGameModel model;
  private final Appendable ap;

  public SquareGameTextualView(SquareGameModel model, Appendable ap) {
    this.model = model;
    this.ap = ap;
  }

  @Override
  public void render(int player) throws IOException {
    for (int row : model.getRows()) {
      this.ap.append("[] ".repeat(row).substring(0, Math.max(0, row * 3 - 1)));
      this.ap.append('\n');
    }
    this.ap.append("------------------------------").append("\n");
    this.ap.append("Player ").append(String.valueOf(player + 1)).append("\n");
  }
}
