package reversi.command;

import java.util.Objects;
import java.util.Optional;

/**
 * A UnifiedCommand represents an action that can modify both the model and the view. This is a
 * common behavior among {@link reversi.controller.agent.AgentBuilder}s.
 *
 * <p>For example, an Agent may want to be able to tell the view about the result of its action,
 * so it will store its desired update in a UnifiedCommand.
 */
public class UnifiedCommand {
  protected ModelCommand modelCommand;
  protected ViewCommand viewCommand;

  /**
   * Create a new {@link UnifiedCommand} given a {@link ModelCommand}. This instance will not have
   * an associated {@link ViewCommand}.
   *
   * @param modelCommand the {@link ModelCommand} to use in this command
   * @throws NullPointerException if any argument is null
   */
  public UnifiedCommand(ModelCommand modelCommand) {
    this.modelCommand = Objects.requireNonNull(modelCommand);
  }

  /**
   * Create a new {@link UnifiedCommand} given a {@link ViewCommand}. This instance will not have an
   * associated {@link ViewCommand}.
   *
   * @param viewCommand the {@link ViewCommand} to use in this command
   * @throws NullPointerException if any argument is null
   */
  public UnifiedCommand(ViewCommand viewCommand) {
    this.viewCommand = Objects.requireNonNull(viewCommand);
  }

  /**
   * Create a new {@link UnifiedCommand} given both command types. This instance will have a command
   * associated with each.
   *
   * @param modelCommand the {@link ModelCommand} to use in this command
   * @param viewCommand  the {@link ViewCommand} to use in this command
   * @throws NullPointerException if any argument is null
   */
  public UnifiedCommand(ModelCommand modelCommand, ViewCommand viewCommand) {
    this.modelCommand = Objects.requireNonNull(modelCommand);
    this.viewCommand = Objects.requireNonNull(viewCommand);
  }

  /**
   * Create the nothing command. Useful for asynchronous Agents.
   */
  public UnifiedCommand() {
    this.modelCommand = null;
    this.viewCommand = null;
  }

  /**
   * Returns the {@link ViewCommand} associated with this command.
   */
  public Optional<ViewCommand> getViewCommand() {
    if (viewCommand == null) {
      return Optional.empty();
    }
    return Optional.of(viewCommand);
  }

  /**
   * Returns the {@link ModelCommand} associated with this command.
   */
  public Optional<ModelCommand> getModelCommand() {
    if (modelCommand == null) {
      return Optional.empty();
    }
    return Optional.of(modelCommand);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    UnifiedCommand that = (UnifiedCommand) o;
    return Objects.equals(getModelCommand(), that.getModelCommand())
        && Objects.equals(getViewCommand(), that.getViewCommand());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getModelCommand(), getViewCommand());
  }
}
