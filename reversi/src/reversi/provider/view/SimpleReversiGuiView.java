package reversi.provider.view;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import reversi.provider.model.ReadOnlyReversiModel;
import reversi.provider.model.board.TileColor;
import reversi.provider.model.players.Player;

/**
 * Simple view creates the basis of which the gui will be put on.
 */

public class SimpleReversiGuiView extends JFrame implements ReversiGuiView {
  protected final TileColor color;
  protected final ReadOnlyReversiModel model;
  protected final Player player;
  protected final ReversiGuiPanel panel;

  protected ViewListener listener;


  /**
   * Creates a view based on the given model.
   */
  public SimpleReversiGuiView(ReadOnlyReversiModel model, Player player) {
    this.player = player;
    this.color = player.getColor();
    this.model = model;
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.panel = new ReversiGuiPanel(model, color, player);
    panel.setBackground(Color.DARK_GRAY);
    panel.addKeyListener(new KeyControl());
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
    //nothing to do
    if (this.player.equals(player)) {
      if (color.equals(TileColor.BLACK)) {
        this.setTitle("Player 1: Your Turn");
      } else {
        this.setTitle("Player 2: Your Turn");
      }
    } else {
      if (color.equals(TileColor.BLACK)) {
        this.setTitle("Player 1: Other turn");
      } else {
        this.setTitle("Player 2: Other turn");
      }
    }
  }

  private class KeyControl implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
      //System.out.println("Key typed");
    }

    @Override
    public void keyReleased(KeyEvent e) {
      //System.out.println("Key released");
    }

    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        //System.out.println("Enter pressed");
        //When pressed, if turn it valid, place marker.
        //If not, put in message.
        ReversiGuiPanel.Element element = panel.getElement();

        if (element != null) {
          if (model.canPlay(player, element.getRow(), element.getCol())) {
            //listener.someoneSaidHello("pressed enter");
            listener.makePlay(player, element.getRow(), element.getCol());
          } else {
            //implement not allowable
            JFrame frame = new JFrame("Warning");
            JOptionPane.showMessageDialog(frame, "Illegal move!");
          }
        }
      }

      if (e.getKeyCode() == KeyEvent.VK_P) {
        listener.pass(player);
      }
    }
  }
}
