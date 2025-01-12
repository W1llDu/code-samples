package squareGame.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class SimpleSquareGame implements SquareGameModel {
  private final int[] rows;

  public SimpleSquareGame(int rows, int maxHeight) {
    this(makeRandomRows(rows, maxHeight));
  }

  private static int[] makeRandomRows(int length, int max) {
    int[] result = new int[length];
    Random random = new Random();
    for (int i = 0; i < length; i++) {
      result[i] = random.nextInt(max) + 1;
    }
    return result;
  }

  public SimpleSquareGame(List<Integer> rows) {
    if (rows == null) {
      throw new IllegalArgumentException();
    }
    this.rows = new int[rows.size()];
    for (int i = 0; i < rows.size(); i++) {
      this.rows[i] = rows.get(i);
    }
  }

  public SimpleSquareGame(int... rows) {
    if (rows == null) {
      throw new IllegalArgumentException();
    }
    this.rows = rows;
  }

  @Override
  public void move(Move move) throws IllegalArgumentException, IllegalStateException {
    if (move.row >= this.rows.length || move.row < 0) {
      throw new IllegalArgumentException();
    }
    if (this.rows[move.row] - move.numSquares < 0) {
      throw new IllegalStateException();
    }
    this.rows[move.row] -= move.numSquares;
  }

  @Override
  public List<Integer> getRows() {
    return Arrays.stream(this.rows).boxed().collect(Collectors.toList());
  }

  @Override
  public boolean isGameOver() {
    for (int row : this.rows) {
      if (row != 0) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isMoveValid(Move move) {
    return (move.row >= 0 && move.row < this.rows.length) && move.numSquares <= this.rows[move.row];
  }
}

