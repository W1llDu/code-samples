package reversi.command;

import java.util.Objects;

/**
 * A {@link CommandIdentifier} is a record containing the name of the Command and its default
 * keybinding.
 */
public class CommandIdentifier {
  public final String name;
  public final int key;

  /**
   * Create a new {@link CommandIdentifier} given the name of the command and its associated
   * keycode.
   *
   * @param name the command name, which is what will be used by players to invoke this command
   * @param key  the key to press on the keyboard to invoke this command. See
   *             {@link java.awt.event.KeyEvent} for documentation of the key identifiers.
   * @throws IllegalArgumentException if the key is not positive
   * @throws NullPointerException     if the name is null
   */
  public CommandIdentifier(String name, int key) {
    if (key <= 0) {
      throw new IllegalArgumentException("CommandIdentifier key must be positive");
    }
    this.name = Objects.requireNonNull(name);
    this.key = key;
  }

  /**
   * Create a new {@link CommandIdentifier} given the name of the command. This CommandIdentifier
   * will have no keyboard command.
   *
   * @param name the command name, which is what will be used by players ot invoke this command
   * @throws NullPointerException if the name is null
   */
  public CommandIdentifier(String name) {
    this.name = Objects.requireNonNull(name);
    this.key = -1;
  }

  /**
   * Create a new {@link CommandIdentifier} given the key of the command. This
   * {@link CommandIdentifier} will have the name {@code ""}.
   *
   * @param key the keycode pressed
   */
  public CommandIdentifier(int key) {
    this.name = "";
    this.key = key;
  }

  /**
   * Determine if the command name matches a string.
   *
   * @param other the name of the other command
   * @return whether the other command name matches this command identifier
   * @throws NullPointerException if other is null
   */
  public boolean nameIs(String other) {
    return Objects.requireNonNull(other).equals(name);
  }

  /**
   * Determine if the command keypress identifier matches another keypress identifier.
   *
   * @param other the keypress identifier of the other command
   * @return whether the other identifier matches this keypress identifier
   */
  public boolean keyIs(int other) {
    return this.key == other;
  }
}
