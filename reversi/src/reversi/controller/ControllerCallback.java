package reversi.controller;

import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.Optional;
import java.util.Scanner;

import reversi.command.CommandAggregator;
import reversi.command.CommandFactory;
import reversi.command.CommandIdentifier;
import reversi.command.UnifiedCommand;
import reversi.command.ViewCommand;
import reversi.command.commands.Highlight;
import reversi.exceptions.IllegalCommandException;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.view.ReversiView;

/**
 * A ControllerCallback is a key interface in the communications between the
 * {@link ReversiController} and the {@link ReversiView}. It "knows" about the the {@link Player} it
 * belongs to. Any messages the view wishes to send to the controller are done through this class.
 * It is expected that the Controller will provide instances of {@link ControllerCallback} to the
 * View upon initialization.
 */
public abstract class ControllerCallback {
  protected final ReversiController controller;
  protected final ReversiView view;
  protected final Player player;

  /**
   * Create a new {@link ControllerCallback} given the Controller this instance belongs to, the
   * View to pass messages to, and the Player this Callback acts as. Note that this constructor
   * may not be of much use outside of Controller implementations, since the Controller itself
   * must maintain a reference to this instance; if the Controller does not instantiate it (or has
   * no means of being provided a {@link ControllerCallback} post-instantiation), then the
   * Controller will not be able to pass messages through it.
   *
   * @param controller the controller to pass messages back up to
   * @param view the view the controller should pass messages to
   * @param player the player this Callback belongs to
   * @throws NullPointerException if any argument is null
   */
  public ControllerCallback(ReversiController controller, ReversiView view, Player player) {
    this.controller = Objects.requireNonNull(controller);
    this.view = Objects.requireNonNull(view);
    this.player = Objects.requireNonNull(player);
  }

  /**
   * Performs the respective command or throws an IllegalArgumentException. The command is looked up
   * and instantiated with the respective coordinate as the argument. If the command is found but
   * fails to run, then the error message is forwarded to the view and snuffed out.
   *
   * @param commandIdentifier the command identifier, which is looked up first by name and then by
   *                          its key
   * @param coord             the coordinate to run the command at
   * @throws IllegalArgumentException if the command cannot be found
   * @throws NullPointerException     if the command identifier is null
   */
  public abstract void accept(CommandIdentifier commandIdentifier, HexPlaneCoord coord)
      throws IllegalArgumentException;

  /**
   * Performs the respective command or throws an IllegalArgumentException. The command is looked up
   * and instantiated using the scanner. If the command is found but fails to run, then the error
   * message is forwarded to the View and snuffed out.
   *
   * @param commandIdentifier the command identifier, which is looked up first by name and then by
   *                          its key
   * @param scanner           the scanner to consume input from
   * @throws IllegalArgumentException if the command cannot be found
   * @throws NullPointerException     if any argument is null
   */
  public abstract void accept(CommandIdentifier commandIdentifier, Scanner scanner);

  /**
   * Perform a specific {@link ViewCommand}. Usually these are commands that cannot be looked up
   * directly, and are not meant for users to interact with.
   *
   * @param command the {@link ViewCommand} to run
   * @throws NullPointerException if the command is null
   */
  public abstract void accept(ViewCommand command);

  /**
   * Convenience method to highlight a position on the view. Utilizes
   * {@link #accept(CommandIdentifier, HexPlaneCoord)} to forward a Highlight command to the
   * Controller.
   *
   * @param coord the coordinate to highlight
   * @throws NullPointerException     if the coord is null
   * @throws IllegalArgumentException if the coord is invalid in some way
   */
  public void highlightCell(HexPlaneCoord coord) {
    try {
      controller.accept(new UnifiedCommand(new Highlight(coord)), player);
    } catch (IllegalCommandException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Create the {@link UnifiedCommand} using a {@link CommandFactory}. Uses a coord to create the
   * command.
   *
   * @param factory the factory to create the command
   * @param coord   the coordinate to create the command with
   * @return the command made using the factory
   */
  protected UnifiedCommand getWithFactory(CommandFactory factory,
                                          HexPlaneCoord coord,
                                          Player player) {
    return factory.getDetailedInstance(coord, player);
  }

  /**
   * Create the {@link UnifiedCommand} using a {@link CommandFactory}. Uses a partially consumed
   * Scanner to create the command. See {@link CommandFactory#getScannerInstance(Scanner)} for
   * details on the Scanner behavior.
   *
   * @param factory the factory to create the command
   * @param scanner the scanner to create the command with
   * @return the command made using the factory
   */
  protected UnifiedCommand getWithFactory(CommandFactory factory, Scanner scanner) {
    return factory.getScannerInstance(scanner);
  }

  /**
   * Show an error message to the view.
   *
   * @param message the message to show via the view
   */
  protected void showErrorWithView(String message) {
    view.showErrorMessage(message);
  }

  protected final CommandFactory lookup(CommandIdentifier identifier) {
    Objects.requireNonNull(identifier);
    Optional<CommandFactory> factory = CommandAggregator.getByIdentifier(identifier);
    if (factory.isEmpty()) {
      // it looks weird to throw a message with an empty name, so we hide that
      if (!identifier.name.isBlank()) {
        throw new IllegalArgumentException("Command could not be found." +
            "\nCommand name: " + identifier.name +
            "\nCommand key: " + KeyEvent.getKeyText(identifier.key));
      }
      throw new IllegalArgumentException("Command could not be found." +
          "\nCommand key: " + KeyEvent.getKeyText(identifier.key));
    }
    return factory.get();
  }
}
