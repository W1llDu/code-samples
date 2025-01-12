package reversi.command;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * The CommandAggregator acts as a mechanism for storing commands intended to be used by players.
 * Commands register themselves with a function that parses their arguments and creates a valid
 * instance of itself. Commands' names ought to be a single token (i.e. a single word). Behavior is
 * undefined for any Commands whose names are not single tokens.
 *
 * <p>Any commands defined in the `reversi.command.commands` package will be automatically loaded
 * at runtime. Otherwise, it is up to the user to load the classes required, or to manually register
 * the classes upon initialization.
 */
public class CommandAggregator {
  // load the classes in the command package to ensure they are loaded by the ClassLoader
  static {
    loadAllClasses();
  }

  private static Map<CommandIdentifier, CommandFactory> plugins;
  /**
   * Determines if classes that register themselves are printed to stderr. Defaults to
   * {@code false}.
   */
  public static boolean DO_LOGGING = false;

  /**
   * Register a command with the aggregator. The command string ought to be a single word, but this
   * is not checked.
   *
   * @param command        the {@link CommandIdentifier} input by players to run the command
   * @param commandFactory a factory detailing creation of the command
   * @throws NullPointerException if any argument is null
   */
  public static void registerSelf(CommandIdentifier command, CommandFactory commandFactory)
      throws NullPointerException {
    if (plugins == null) {
      plugins = new HashMap<>();
    }
    plugins.put(Objects.requireNonNull(command), Objects.requireNonNull(commandFactory));
    if (DO_LOGGING) {
      System.err.println("CommandAggregator registered new command: " + command);
    }
  }

  /**
   * Get the current map of commands.
   *
   * @return a map of command names to the means to create the command
   */
  public static Map<CommandIdentifier, CommandFactory> getPlugins() {
    loadAllClasses();
    if (plugins == null) {
      return new HashMap<>();
    }
    return new HashMap<>(plugins);
  }

  /**
   * Convenience function for getting the list of command strings.
   *
   * @return a set of the command strings
   */
  public static Set<CommandIdentifier> getCommandIdentifiers() {
    if (plugins == null) {
      return new HashSet<>();
    }
    // keySet() returns a reference to the inner set
    return new HashSet<>(plugins.keySet());
  }

  /**
   * Convenience function to check if a name is present in the set of command identifiers.
   *
   * @param name the name to check
   * @return if the name is present in any command identifier
   * @throws NullPointerException if the name is null
   */
  public static boolean anyNamesMatch(String name) {
    Objects.requireNonNull(name);
    for (CommandIdentifier identifier : getCommandIdentifiers()) {
      if (identifier.nameIs(name)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Convenience function to get an identifier by its name.
   *
   * @param name the name to check
   * @return the command identifier if it is present, otherwise Optional.empty()
   * @throws NullPointerException if the name is null
   */
  public static Optional<CommandIdentifier> getIdentifierByName(String name) {
    Objects.requireNonNull(name);
    for (CommandIdentifier identifier : getCommandIdentifiers()) {
      if (identifier.nameIs(name)) {
        return Optional.of(identifier);
      }
    }
    return Optional.empty();
  }

  /**
   * Convenience function to get an identifier by its keycode.
   *
   * @param key the keycode to check
   * @return the command identifier if it is present, otherwise Optional.empty()
   */
  public static Optional<CommandIdentifier> getIdentifierByKey(int key) {
    for (CommandIdentifier identifier : getCommandIdentifiers()) {
      if (identifier.keyIs(key)) {
        return Optional.of(identifier);
      }
    }
    return Optional.empty();
  }

  /**
   * Convenience function to get the factory by its name.
   *
   * @param name the name to check
   * @return the factory if it is present, otherwise Optional.empty()
   * @throws NullPointerException if the name is null
   */
  public static Optional<CommandFactory> getByName(String name) {
    Objects.requireNonNull(name);
    for (CommandIdentifier identifier : getCommandIdentifiers()) {
      if (identifier.nameIs(name)) {
        return Optional.of(plugins.get(identifier));
      }
    }
    return Optional.empty();
  }

  /**
   * Convenience function to get the factory by its keycode.
   *
   * @param key the keycode to check
   * @return the factory if it is present, otherwise Optional.empty()
   */
  public static Optional<CommandFactory> getByKey(int key) {
    for (CommandIdentifier identifier : getCommandIdentifiers()) {
      if (identifier.keyIs(key)) {
        return Optional.of(plugins.get(identifier));
      }
    }
    return Optional.empty();
  }

  /**
   * Gets the CommandFactory via its {@link CommandIdentifier}. The name is looked up first, then
   * the key. The first match is returned. That is, if the name matches but the key doesn't, the
   * command matching the name is returned and the key is ignored.
   *
   * @param identifier the {@link CommandIdentifier} to look up
   * @return the respective CommandFactory, or Optional.empty()
   * @throws NullPointerException if the identifier is null
   */
  public static Optional<CommandFactory> getByIdentifier(CommandIdentifier identifier) {
    Objects.requireNonNull(identifier);
    Optional<CommandFactory> factory;
    factory = getByName(identifier.name);
    if (factory.isPresent()) {
      return factory;
    }
    factory = getByKey(identifier.key);
    return factory;
  }

  // Very ugly, but its to allow the ClassLoader to find stuff :)
  private static void loadAllClasses() {
    String pkgName = "reversi.command.commands";
    InputStream stream = ClassLoader
        .getSystemClassLoader()
        .getResourceAsStream(pkgName.replaceAll("[.]", "/"));
    assert stream != null;
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
    reader.lines()
        .filter(line -> line.endsWith(".class"))
        .forEach(line -> getClass(line, pkgName));
  }

  private static void getClass(String className, String packageName) {
    try {
      Class.forName(packageName + "."
          + className.substring(0, className.lastIndexOf('.')));
    } catch (ClassNotFoundException e) {
      // do nothing :)
      // or cry :)
      // in reality, it is likely this catch clause actually does catch some exceptions.
      // given this relies on reflection, that isn't entirely surprising.
      // however, it *does* work and doesn't rely on any super fancy tricks.
      // none of the exceptions caught here are of *any* interest whatsoever.
    }
  }
}
