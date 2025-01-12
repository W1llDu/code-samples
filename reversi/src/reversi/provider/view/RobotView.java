package reversi.provider.view;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import reversi.provider.model.ReadOnlyReversiModel;
import reversi.provider.model.board.TileColor;
import reversi.provider.model.board.TilePosition;
import reversi.provider.model.players.Player;

/**
 * Simple view creates the basis of which the gui will be put on.
 */

public class RobotView extends JFrame implements ReversiGuiView {
  private final Player player;
  private final ReversiGuiPanel panel;

  private ViewListener listener;


  /**
   * Creates a view based on the given model.
   */
  public RobotView(ReadOnlyReversiModel model, Player player) {
    this.player = player;
    TileColor color = player.getColor();
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.panel = new ReversiGuiPanel(model, color, player);
    panel.setBackground(Color.DARK_GRAY);
    panel.setFocusable(true);

    if (color.equals(TileColor.BLACK)) {
      this.setTitle("Player 1");
    } else {
      this.setTitle("Player 2");
    }

    this.add(panel);
    this.pack();
  }

  public void setListener(ViewListener listener) {
    this.listener = listener;
  }

  @Override
  public void paintAgain() {
    panel.generateBoard();
    this.repaint();
  }

  public void gameOver() {
    JFrame frame = new JFrame("Update");
    JOptionPane.showMessageDialog(frame, "Game over!");
  }

  @Override
  public void setTurn(Player player) {
    //:todo: if my turn,
    if (this.player.equals(player)) {
      //my turn
      TilePosition position = this.player.makeMove();
      if (position.getRow() == -1 && position.getCol() == -1) {
        //assume pass
        listener.pass(this.player);
      } else {
        listener.makePlay(this.player, position.getRow(), position.getCol());
      }
    }
  }
}
