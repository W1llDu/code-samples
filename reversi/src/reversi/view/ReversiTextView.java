package reversi.view;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

import reversi.controller.ControllerCallback;
import reversi.controller.Player;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.reversiinfo.TotalPointsSummarizer;
import reversi.model.ReadOnlyReversiModel;

/**
 * A {@link ReversiTextView} is a text-only output version of a {@link ReversiView}. It writes out
 * to an {@link Appendable}. The appendable must not be closed during any rendering process.
 */
public final class ReversiTextView extends AbstractReversiView {
  private final Appendable out;

  /**
   * Create a new ReversiTextView with the desired destination for output and the Model to render.
   * The appendable must remain open for the duration of this class's lifespan.
   *
   * @param out   the appendable to write to
   * @param model the read-only model to use when rendering
   * @throws NullPointerException if any argument is null
   */
  public ReversiTextView(Appendable out, ReadOnlyReversiModel<Player> model) {
    super(model);
    this.out = Objects.requireNonNull(out);
  }

  @Override
  public boolean render() {
    HexPlane<Player> curPlane = model.getHexPlane();

    int maxIndex = curPlane.getRadius();
    StringBuilder builder = new StringBuilder();
    // this gets from the top of the hex grid to the middle
    for (int r = -maxIndex; r <= maxIndex; r++) {
      // offset is the number of spaces to add before displaying
      int offset = Math.abs(r);
      builder.append(" ".repeat(Math.max(0, offset)));
      // iterate over q, going left to right
      for (int q = Math.max(-maxIndex - r, -maxIndex); q <= Math.min(maxIndex - r, maxIndex); q++) {
        HexPlaneCoord coord = new AxialCoordinate(q, r);
        Optional<Player> val = curPlane.getAtHex(coord);
        // if it's an empty position, use an _ to represent it
        if (val.isEmpty()) {
          appendToBuilderAndHighlight(builder, "_ ", coord);
        } else {
          // otherwise use the value at the position
          appendToBuilderAndHighlight(builder, val.get().toString(), coord);
          builder.append(" ");
        }
      }
      builder.append("\n");
    }
    // reset the highlighted coordinates
    this.highlightedCoords.clear();
    writeOutWithNewline(builder.toString().stripTrailing());
    return false;
  }

  @Override
  public void setControllerCallback(ControllerCallback controllerCallback) {
    this.controllerCallback = controllerCallback; // we are fine with it being null
  }

  @Override
  public void showErrorMessage(String message) throws NullPointerException {
    writeOutWithNewline(makeHighlighted(Objects.requireNonNull(message)));
  }

  @Override
  public void gameEnd() {
    try {
      out.append("Game over!\n");
      out.append(String.format("X: %s points.\nO: %s points.\n",
          new TotalPointsSummarizer(Player.PLAYER1).apply(model.getHexPlane()),
          new TotalPointsSummarizer(Player.PLAYER2).apply(model.getHexPlane())));
    } catch (IOException ex) {
      throw new IllegalStateException("IOException on writing out stream. Something bad happened!");
    }
  }

  private void writeOutWithNewline(String str) {
    try {
      out.append(str).append("\n");
    } catch (IOException ex) {
      throw new IllegalStateException("IOException on writing out stream. Something bad happened!");
    }
  }

  private String makeHighlighted(String str) {
    return "\033[0;31m" + str + "\033[0m";
  }

  private void appendToBuilderAndHighlight(StringBuilder builder, String str, HexPlaneCoord coord) {
    builder.append(makeHighlighted(str));
  }
}
