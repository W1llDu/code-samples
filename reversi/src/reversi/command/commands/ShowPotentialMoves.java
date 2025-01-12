package reversi.command.commands;

import java.awt.event.KeyEvent;
import java.util.Objects;

import reversi.command.CommandAggregator;
import reversi.command.CommandFactory;
import reversi.command.CommandIdentifier;
import reversi.command.CommandParser;
import reversi.command.UnifiedCommand;
import reversi.command.ViewCommand;
import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.summarizer.reversiinfo.PlayerPossibleMovesSummarizer;
import reversi.model.ReadOnlyReversiModel;
import reversi.view.ReversiView;

/**
 * {@link ShowPotentialMoves} is a {@link ViewCommand} to show a particular player's available
 * moves. It highlights the cells that a player can move in. The scanner instance requires an
 * argument representing the player to check, while the keybinding simply shows for the current
 * player.
 */
public class ShowPotentialMoves implements ViewCommand {
  static {
    CommandAggregator.registerSelf(new CommandIdentifier("show-moves", KeyEvent.VK_H),
        new CommandFactory(
            (coord, player) -> new UnifiedCommand(new ShowPotentialMoves(player)),
            scanner -> {
              Player player = CommandParser.parsePlayer(scanner);
              return new UnifiedCommand(new ShowPotentialMoves(player));
            }
        ));
  }


  private final Player player;

  /**
   * Create a new ShowPotential command given the Player. The Player must not be null.
   *
   * @param player the player to show the moves for
   * @throws NullPointerException if any argument is null
   */
  public ShowPotentialMoves(Player player) {
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public void performViewCommand(ReversiView view, ReadOnlyReversiModel<Player> model)
      throws NullPointerException {
    view.clearHighlights();
    for (HexPlaneCoord coord :
        new PlayerPossibleMovesSummarizer(player)
            .apply(model.getHexPlane())) {
      view.highlightTile(coord);
    }
  }
}
