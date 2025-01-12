package reversi.exceptions;

import reversi.command.ModelCommand;

/**
 * An {@link IllegalCommandException} indicates a {@link ModelCommand} has been used improperly and
 * cannot complete its intended action.
 */
public class IllegalCommandException extends Exception {

  /**
   * Create a new {@link IllegalCommandException} given the reason for throwing and the exception
   * that lead to this exception.
   *
   * @param message the reason for the exception
   * @param cause   the exception that caused this exception
   */
  public IllegalCommandException(String message, Throwable cause) {
    super(message, cause);
  }
}
