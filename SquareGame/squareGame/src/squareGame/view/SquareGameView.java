package squareGame.view;

import java.io.IOException;

/** A marker interface for text-based views. */
public interface SquareGameView {

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   * @throws IOException if the rendering fails for some reason
   */
  void render(int player) throws IOException;
}
