package reversi.model;

import java.util.Optional;

import reversi.controller.Player;
import reversi.hex.coordinates.HexIndexable;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexPlaneSummarizer;

/**
 * A class implementing ReadOnlyReversiModel is able to use a {@link HexPlaneSummarizer} to obtain a
 * value provided by the Summarizer. They are also able to return values at a given
 * {@link reversi.hex.coordinates.HexPlaneCoord}.
 *
 * @param <T> The type of the values on the {@link HexPlane}
 * @see MutableReversiModel
 */
public interface ReadOnlyReversiModel<T> extends HexIndexable<T> {

  /**
   * Returns a new copy of the {@link HexPlane}. Changes to this HexPlane will not modify the Model
   * or its internal state.
   *
   * @return a fresh copy of the HexPlane
   */
  HexPlane<T> getHexPlane();

  /**
   * Get the player whose turn it is.
   *
   * @return the player who is allowed to make a move next
   */
  Player getPlayer();

  @Override
  Optional<T> getAtHex(HexPlaneCoord coordinate)
      throws IndexOutOfBoundsException, NullPointerException;
}
