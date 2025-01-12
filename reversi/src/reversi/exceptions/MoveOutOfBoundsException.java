package reversi.exceptions;


/**
 * A {@link MoveOutOfBoundsException} indicates some game action (usually a {@code Move}) is invalid
 * because the requested coordinate does not exist in the current game.
 */
public class MoveOutOfBoundsException extends IndexOutOfBoundsException {

  /**
   * Create a {@link MoveOutOfBoundsException} with the given message.
   *
   * @param message the message of the throwable
   */
  public MoveOutOfBoundsException(String message) {
    super(message);
  }
}
