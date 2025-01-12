package reversi.hex.summarizer;

import java.util.Objects;

import reversi.hex.plane.HexPlane;

/**
 * Summarizes the size of a board using its radius.
 */
public class BoardSizeSummarizer<T> implements HexPlaneSummarizer<T, Integer> {

  @Override
  public Integer apply(HexPlane<T> plane)
      throws NullPointerException, IndexOutOfBoundsException {
    Objects.requireNonNull(plane);
    return plane.getRadius();
  }
}
