package reversi.provider.view;

import java.io.IOException;

import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.Board;
import reversi.provider.model.board.TileColor;

/**
 * A simple text-based rendering of the Reversi game.
 */
public class ReversiTextualView implements TextualView {

  private Appendable output;
  private final ReversiModel model;
  private int size;
  private Board board;

  /**
   * A constructor for the ReversiTextualView.
   *
   * @param model takes in a ReversiModel
   */
  public ReversiTextualView(ReversiModel model) {
    this.model = model;
  }

  /**
   * Constructs a new ReversiTextualView instance.
   * This class is responsible for displaying the Reversi game state in a textual format.
   *
   * @param model   The abstract Reversi model representing the game state.
   * @param output  An Appendable object to which the textual representation of the game state
   *                will be appended.
   */
  public ReversiTextualView(ReversiModel model, Appendable output) {
    this.model = model;
    this.output = output;
    board =  model.getBoard();
    this.size = board.getSize();
  }

  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   *
   * @throws IOException if the rendering fails for some reason
   */
  @Override
  public void render() throws IOException {
    output.append(this.toString());
  }

  /**
   * Generates a textual representation of the current state of a card game model.
   * This method constructs a string that displays information about the cards in the draw pile,
   * the cards in the foundation piles, and the cards in the game's tableau.
   *
   * @return A string representation of the card game model's state.
   */
  public String toString() {
    String result = "";
    int row = 0;
    for (int length = size; length <= 2 * size - 1; length++) {
      for (int i = 0; i < 2 * (size) - 1 - length; i++) {
        result += " ";
      }
      for (int col = 0; col < length; col++) {
        if (board.getTileAt(row, col).getColor().equals(TileColor.NONE)) {
          result += "_";
          if (col < length - 1) {
            result += " ";
          }
        } else if (board.getTileAt(row, col).getColor().equals(TileColor.BLACK)) {
          result += "X";
          if (col < length - 1) {
            result += " ";
          }
        } else if (board.getTileAt(row, col).getColor().equals(TileColor.WHITE)) {
          result += "O";
          if (col < length - 1) {
            result += " ";
          }
        }
      }
      row++;
      result += "\n";
    }

    for (int length = 2 * size - 2; length > size - 1; length--) {
      for (int i = 0; i < 2 * (size) - 1 - length; i++) {
        result += " ";
      }
      for (int col = 0; col < length; col++) {
        if (board.getTileAt(row, col).getColor().equals(TileColor.NONE)) {
          result += "_";
          if (col < length - 1) {
            result += " ";
          }
        } else if (board.getTileAt(row, col).getColor().equals(TileColor.BLACK)) {
          result += "X";
          if (col < length - 1) {
            result += " ";
          }
        } else if (board.getTileAt(row, col).getColor().equals(TileColor.WHITE)) {
          result += "O";
          if (col < length - 1) {
            result += " ";
          }
        }
      }
      row++;
      result += "\n";
    }
    return result;
  }
}
