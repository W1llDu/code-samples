package reversi.hex.summarizer;

import java.util.function.Function;

import reversi.hex.plane.HexPlane;
import reversi.model.ReadOnlyReversiModel;

/**
 * A HexPlaneSummarizer is a function object for {@link HexPlane}s. For example, A
 * HexPlaneSummarizer may find the score after making a move at a point, or may determine the score
 * of a specific player.
 *
 * @param <T> the type of the underlying HexPlane
 * @param <R> the result type of the summarizing operation
 */
public interface HexPlaneSummarizer<T, R> extends Function<HexPlane<T>, R> {

  /**
   * Apply the {@link HexPlane} according to the strategy implemented by the underlying
   * implementation. The HexPlane passed to this {@link HexPlaneSummarizer} will not result in
   * mutation of the Model.
   *
   * @param plane the HexPlane to reduce to a summary value
   * @return the result after reduction
   * @throws NullPointerException if {@code plane} is null
   * @see ReadOnlyReversiModel
   */
  @Override
  R apply(HexPlane<T> plane) throws NullPointerException, IndexOutOfBoundsException;
}
