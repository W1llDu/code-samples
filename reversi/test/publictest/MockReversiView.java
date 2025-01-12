package publictest;

import java.util.Objects;

import reversi.command.ViewCommand;
import reversi.controller.ControllerCallback;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.view.ReversiView;

/**
 * A {@link MockReversiView} is a {@link ReversiView} that logs error messages, commands,
 * and game over messages. It is otherwise a stub, doing nothing interesting.
 */
public class MockReversiView implements ReversiView {
  private final StringBuilder log;

  /**
   * Create a new {@link MockReversiView} given the log to output to.
   *
   * @param log the log to append the information about error messages, commands, and game
   *            over messages to
   */
  public MockReversiView(StringBuilder log) {
    this.log = Objects.requireNonNull(log);
  }

  @Override
  public boolean render() {
    return false;
  }

  @Override
  public void setControllerCallback(ControllerCallback controllerCallback) {
    // this class is a stub and implements no functionality other than logging
  }

  @Override
  public void showErrorMessage(String message) throws NullPointerException {
    log.append(message).append('\n');
  }

  @Override
  public void highlightTile(HexPlaneCoord coord)
      throws IndexOutOfBoundsException, NullPointerException {
    // this class is a stub and implements no functionality other than logging
  }

  @Override
  public void clearHighlights() {
    // this class is a stub and implements no functionality other than logging
  }

  @Override
  public void accept(ViewCommand command) throws NullPointerException {
    log.append("viewCommand: ").append(command).append('\n');
  }

  @Override
  public void gameEnd() {
    log.append("gameEnd").append("\n");
  }
}
