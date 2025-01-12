package reversi.provider.view;

import java.io.IOException;

/**
 * Represents a textual view interface for rendering a model.
 */
public interface TextualView {
  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  void render() throws IOException;
}