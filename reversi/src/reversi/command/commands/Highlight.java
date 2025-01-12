package reversi.command.commands;

import java.util.Objects;

import reversi.command.ViewCommand;
import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.model.ReadOnlyReversiModel;
import reversi.view.ReversiView;


/**
 * {@link Highlight} is a {@link ViewCommand} that will highlight the cell at a point. The
 * Highlighting is agnostic to the backend currently being run. This will only highlight the View
 * this Command is used by; because the Callback will be provided this Command, it will only
 * highlight the respective player's View.
 */
public class Highlight implements ViewCommand {

  private final HexPlaneCoord coord;

  /**
   * Create a new Highlight command given the coordinate to highlight.
   *
   * @param coord the coordinate to highlight on the view
   * @throws NullPointerException if any argument is null
   */
  public Highlight(HexPlaneCoord coord) {
    this.coord = Objects.requireNonNull(coord);
  }

  @Override
  public void performViewCommand(ReversiView view, ReadOnlyReversiModel<Player> model)
      throws NullPointerException {
    view.clearHighlights();
    view.highlightTile(coord);
  }
}
