package reversi.command.commands;

import java.awt.event.KeyEvent;
import java.util.Objects;

import reversi.command.CommandAggregator;
import reversi.command.CommandFactory;
import reversi.command.CommandIdentifier;
import reversi.command.CommandParser;
import reversi.command.ModelCommand;
import reversi.command.UnifiedCommand;
import reversi.controller.Player;
import reversi.exceptions.IllegalCommandException;
import reversi.exceptions.MoveOutOfBoundsException;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.model.MutableReversiModel;

/**
 * A {@link Move} is a command that makes a move at a given position for a given player. The player
 * is given to the command when the command is executed. It's command name and arguments are
 * {@code move HexPlaneCoord}. The default keyboard command to execute this command is ENTER.
 */
public class Move implements ModelCommand {
  static {
    CommandAggregator.registerSelf(new CommandIdentifier("move", KeyEvent.VK_ENTER),
        new CommandFactory(
            (coord, ignore) -> {
              if (coord == null) {
                return new UnifiedCommand();
              }
              return new UnifiedCommand(new Move(coord));
            },
            scanner -> {
              HexPlaneCoord coord = CommandParser.parseCoord(scanner);
              return new UnifiedCommand(new Move(coord));
            })
    );
  }

  private final HexPlaneCoord coord;

  /**
   * Create a new Move given the coordinate attempting to be played in.
   *
   * @param coord the coordinate a player or AI is attempting to play in.
   * @throws NullPointerException if {@code coord} is null
   */
  public Move(HexPlaneCoord coord) {
    this.coord = Objects.requireNonNull(coord);
  }

  /**
   * Perform the action on the model as a specific player.
   *
   * @param model  the model the action is performed on
   * @param player the player making the move
   * @throws IllegalCommandException if the move cannot be completed for whatever reason
   * @throws NullPointerException    if any parameter is null
   */
  @Override
  public void runCommandAs(MutableReversiModel model, Player player)
      throws IllegalCommandException, NullPointerException {
    Objects.requireNonNull(model);
    Objects.requireNonNull(player);
    try {
      model.makeMoveAsPlayer(coord, player);
    } catch (MoveOutOfBoundsException | IllegalStateException ex) {
      throw new IllegalCommandException("Move command failed.\n" + ex.getMessage(), ex);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Move move = (Move) o;
    return Objects.equals(coord, move.coord);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coord);
  }

  @Override
  public String toString() {
    return "Move{" + coord + "}";
  }
}
