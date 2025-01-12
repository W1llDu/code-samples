package reversi.provider.view;

import reversi.provider.model.players.Player;

/**
 * ViewListener serves as the connection between the view and the controller.
 */
public interface ViewListener {

  void makePlay(Player player, int row, int col);

  void pass(Player player);
}
