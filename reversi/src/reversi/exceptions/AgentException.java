package reversi.exceptions;

/**
 * An AgentException represents any kind of exception an Agent can throw.
 */
public class AgentException extends Exception {

  /**
   * Create a new {@link AgentException} given the reason for the exception and the exception that
   * caused it.
   *
   * @param message the reason for the exception
   * @param cause   the throwable that caused this exception
   */
  public AgentException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Create a new {@link AgentException} given the reason for the exception.
   *
   * @param message the reason for the exception
   */
  public AgentException(String message) {
    super(message);
  }
}
