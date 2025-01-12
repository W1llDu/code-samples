package reversi.view;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.swing.JFrame;

import reversi.command.ViewCommand;
import reversi.controller.ControllerCallback;
import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.model.ReadOnlyReversiModel;

/**
 * Abstract class for {@link ReversiView}s. Handles the setting of the {@link ControllerCallback},
 * highlighted coords, and {@link ReadOnlyReversiModel}. Inheritors should implement the methods
 * from {@link ReversiView} as required, customizing the View to however it is intended to look. It
 * is not recommended that the methods already implemented be overridden, but they do not rely on
 * any other methods defined in this class.
 */
public abstract class AbstractReversiView extends JFrame implements ReversiView {
  protected final Set<HexPlaneCoord> highlightedCoords;
  protected final ReadOnlyReversiModel<Player> model;
  protected ControllerCallback controllerCallback;

  public AbstractReversiView(ReadOnlyReversiModel<Player> model) {
    this.highlightedCoords = new HashSet<>();
    this.model = Objects.requireNonNull(model);
  }

  /**
   * Highlight a specific tile by adding the coordinate to the highlighted coords list. Adding
   * multiple highlighted coords will highlight both; it is only cleared upon a player making an
   * action.
   *
   * @param coord the position to highlight
   * @throws IndexOutOfBoundsException if the coordinate is out of bounds
   * @throws NullPointerException      if the coordinate is null
   */
  public void highlightTile(HexPlaneCoord coord)
      throws IndexOutOfBoundsException, NullPointerException {
    Objects.requireNonNull(coord);
    if (!this.model.getHexPlane().getKnownCoords().contains(coord)) {
      throw new IndexOutOfBoundsException("Cannot highlight out of bounds coordinate");
    }
    highlightedCoords.add(coord);
  }

  @Override
  public void clearHighlights() {
    highlightedCoords.clear();
  }

  @Override
  public void accept(ViewCommand command) throws NullPointerException {
    Objects.requireNonNull(command);
    command.performViewCommand(this, model);
  }
}
