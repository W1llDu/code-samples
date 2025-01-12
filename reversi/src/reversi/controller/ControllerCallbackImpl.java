package reversi.controller;

import java.util.Objects;
import java.util.Scanner;

import reversi.command.CommandFactory;
import reversi.command.CommandIdentifier;
import reversi.command.UnifiedCommand;
import reversi.command.ViewCommand;
import reversi.exceptions.IllegalCommandException;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.view.ReversiView;

/**
 * A {@link ControllerCallbackImpl} is a Features-like abstract class dispatched to various
 * subcomponents to allow for communications with the {@link ReversiController}. Callbacks that
 * exhibit unique behavior are welcome to extend this class.
 */
public class ControllerCallbackImpl extends ControllerCallback {

  /**
   * Superclass constructor for {@link ControllerCallbackImpl}s. Requires the Controller the
   * callback is made for, the view the callback belongs to, and the player this callback acts on
   * behalf of.
   *
   * @param controller the controller to call back to
   * @param view       the view the callback belongs to
   * @param player     the player the callback acts on behalf of
   * @throws NullPointerException if any argument is null
   */
  public ControllerCallbackImpl(ReversiController controller,
                                ReversiView view,
                                Player player) {
    super(controller, view, player);
  }

  @Override
  public void accept(CommandIdentifier commandIdentifier, HexPlaneCoord coord)
      throws IllegalArgumentException {
    Objects.requireNonNull(commandIdentifier);
    try {
      CommandFactory factory = lookup(commandIdentifier);
      controller.accept(getWithFactory(factory, coord, player), player);
    } catch (IllegalArgumentException | IllegalCommandException e) {
      showErrorWithView(e.getMessage());
    }
  }

  @Override
  public void accept(CommandIdentifier commandIdentifier, Scanner scanner) {
    Objects.requireNonNull(commandIdentifier);
    Objects.requireNonNull(scanner);
    CommandFactory factory = lookup(commandIdentifier);
    try {
      controller.accept(getWithFactory(factory, scanner), player);
    } catch (IllegalCommandException e) {
      showErrorWithView(e.getMessage());
    }
  }

  @Override
  public void accept(ViewCommand command) {
    Objects.requireNonNull(command);
    try {
      controller.accept(new UnifiedCommand(command), player);
    } catch (IllegalCommandException e) {
      // this should be unreachable
      throw new RuntimeException(e);
    }
  }

}
