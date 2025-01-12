package reversi.provider.view;

import reversi.provider.model.players.Player;

/**
 * Creates a view for the Gui.
 */
public interface ReversiGuiView {

  /**
   * Set the view to be visible.
   */
  void setVisible(boolean view);

  void setListener(ViewListener listener);

  void paintAgain();

  void gameOver();

  void setTurn(Player player);
}
