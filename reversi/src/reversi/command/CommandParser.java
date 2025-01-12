package reversi.command;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

import reversi.controller.Player;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;

/**
 * Utility class for implementers of {@link ModelCommand}. Supplies convenience functions for
 * actions such as parsing a {@link reversi.hex.coordinates.HexPlaneCoord} from a
 * {@link java.util.Scanner}.
 */
public class CommandParser {
  /**
   * Helper for commands that may want to parse themselves. Waits until a valid int is received and
   * returns it.
   *
   * @param scanner the scanner to read from
   * @return the int read in from the scanner
   */
  public static int waitUntilParseInt(Scanner scanner) {
    Objects.requireNonNull(scanner);
    Integer retInt = null;
    do {
      try {
        retInt = scanner.nextInt();
      } catch (NoSuchElementException ex) {
        continue;
      }
    }
    while (retInt == null);

    return retInt;
  }

  /**
   * Helper for commands that want to parse a string from a set of possibilities. Waits until an
   * input matches one of the tokens, returning the matched value. This method is not
   * case-sensitive.
   *
   * @param scanner the scanner to read from
   * @param tokens  the valid tokens
   * @return the token that matched
   */
  public static String waitUntilParse(Scanner scanner, String... tokens) {
    Objects.requireNonNull(scanner);
    Objects.requireNonNull(tokens);
    String retStr = null;
    List<String> lowerCaseTokens = Arrays.stream(tokens)
        .map(String::toLowerCase)
        .collect(Collectors.toList());
    do {
      try {
        String temp = scanner.next();
        if (lowerCaseTokens.contains(temp.toLowerCase())) {
          retStr = temp;
        }
      } catch (NoSuchElementException ex) {
        continue;
      }
    }
    while (retStr == null);

    return retStr;
  }

  /**
   * Parse a {@link HexPlaneCoord} from a {@link Scanner}. Only 2 integers are required; the third
   * is ignored.
   *
   * @param scanner the scanner to read from
   * @return the HexPlaneCoord read from the input
   */
  public static HexPlaneCoord parseCoord(Scanner scanner) {
    Objects.requireNonNull(scanner);
    int q = waitUntilParseInt(scanner);
    int r = waitUntilParseInt(scanner);

    return new AxialCoordinate(q, r);
  }

  /**
   * Parse a {@link Player} enum from the scanner. "P1" and "P2" are the only valid strings.
   * TODO: document player parsing for tty in readme
   *
   * @param scanner the scanner to read from
   * @return the parsed Player
   */
  public static Player parsePlayer(Scanner scanner) {
    Objects.requireNonNull(scanner);
    String str = waitUntilParse(scanner, "P1", "P2");
    if (str.equals("P1")) {
      return Player.PLAYER1;
    } else {
      return Player.PLAYER2;
    }
  }
}
