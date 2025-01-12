package reversi.view;

import java.awt.Color;
import java.util.Objects;

import javax.swing.JOptionPane;

import reversi.controller.ControllerCallback;
import reversi.controller.Player;
import reversi.hex.summarizer.BoardSizeSummarizer;
import reversi.hex.summarizer.reversiinfo.TotalPointsSummarizer;
import reversi.model.ReadOnlyReversiModel;

/**
 * A {@link ReversiGraphicsView} is a GUI for Reversi. It displays a pointy-side-up hexagonal grid.
 * It uses Swing to display the GUI.
 *
 * <p>If you are using Linux and experience internal errors from Swing, it may be due to your
 * fontconfig. Please ensure you have the required packages installed.
 *
 * @see AbstractReversiView
 */
public class ReversiGraphicsView extends AbstractReversiView {
  private final ReversiPanel panel;
  private final Player myPlayer;

  /**
   * Create a new {@link ReversiGraphicsView} given a model to show.
   *
   * @param model the read-only model to use to display the view
   * @throws NullPointerException if the model is null
   */
  public ReversiGraphicsView(ReadOnlyReversiModel<Player> model, Player player) {
    super(model);
    this.myPlayer = Objects.requireNonNull(player);

    int radius = new BoardSizeSummarizer<Player>().apply(model.getHexPlane());
    // Multiply radius by 3 * log_2(radius) to logarithmically grow size. Or multiply by 4 to make
    // it more reasonable for larger sizes.
    radius = Math.min((int) (radius * 3 * (Math.log(radius) / Math.log(2))), radius * 4);
    this.panel = new ReversiPanel(model, highlightedCoords, radius);
    this.add(panel);
    this.setTitle("Reversi: Playing as player " + player + " (X = P1, O = P2)");
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);

    // weird quirk: on linux, removing this line blanks the display output. On windows,
    // it works perfectly fine. magic!
    this.pack();
    int size = new BoardSizeSummarizer<Player>().apply(model.getHexPlane());
    this.setSize((int) ((2 * size + 1.3) * 2 * (radius + 1) * Math.sqrt(3) / 2),
        (int) ((1.5 * size + 1.7) * 2 * (radius + 1)));
  }

  @Override
  public boolean render() {
    this.setVisible(true);
    boolean focused = this.isFocused();
    if (model.getPlayer() == myPlayer) {
      panel.setBackground(Color.ORANGE);
    } else {
      panel.setBackground(Color.DARK_GRAY);
    }
    repaint();
    return focused;
  }

  @Override
  public void setControllerCallback(ControllerCallback controllerCallback) {
    panel.setControllerCallback(controllerCallback);
  }

  @Override
  public void showErrorMessage(String message) throws NullPointerException {
    panel.showErrorMessage(message);
  }

  @Override
  public void gameEnd() {
    if (!isVisible()) {
      return;
    }
    JOptionPane.showConfirmDialog(null,
        String.format("X: %s points.\nO: %s points.",
            new TotalPointsSummarizer(Player.PLAYER1).apply(model.getHexPlane()),
            new TotalPointsSummarizer(Player.PLAYER2).apply(model.getHexPlane())),
        "Game Over!",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.INFORMATION_MESSAGE);
  }
}
