package reversi.command;

import java.util.Objects;
import java.util.Scanner;
import java.util.function.BiFunction;
import java.util.function.Function;

import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;

/**
 * A CommandFactory can create a UnifiedCommand through various means, such as text and coordinates.
 * Used by commands to add themselves to the {@link CommandAggregator}.
 *
 * <p>Commands are created either via Scanners (for text-based games) or via a
 * {@link CommandDetailedFunction}, which specifies conditions the Command should take into account
 * when being instantiated.
 */
public class CommandFactory {
  private final UnifiedCommand defaultInstance;
  private final CommandDetailedFunction detailedInstance;
  private final Function<Scanner, UnifiedCommand> scannerInstance;

  /**
   * Create a new {@link CommandFactory} given a default instance, a callback to create an instance
   * given a specific coordinate, and a callback to create an instance with a scanner.
   *
   * @param defaultInstance  the instance that will be created when no coordinate is supplied
   * @param detailedInstance an instance created using a specific coordinate
   * @param scannerInstance  an instance created using a scanner with unconsumed input
   * @throws NullPointerException if any parameter is null
   */
  public CommandFactory(UnifiedCommand defaultInstance,
                        CommandDetailedFunction detailedInstance,
                        Function<Scanner, UnifiedCommand> scannerInstance) {
    this.defaultInstance = Objects.requireNonNull(defaultInstance);
    this.detailedInstance = Objects.requireNonNull(detailedInstance);
    this.scannerInstance = Objects.requireNonNull(scannerInstance);
  }

  /**
   * Create a new {@link CommandFactory} a callback to create an instance given a specific
   * coordinate and a callback to create an instance with a scanner.
   *
   * @param detailedInstance an instance created using a specific coordinate
   * @throws NullPointerException if any parameter is null
   */
  public CommandFactory(CommandDetailedFunction detailedInstance,
                        Function<Scanner, UnifiedCommand> scannerInstance) {
    this.defaultInstance = null;
    this.detailedInstance = Objects.requireNonNull(detailedInstance);
    this.scannerInstance = Objects.requireNonNull(scannerInstance);
  }

  /**
   * Create a CommandFactory for commands with only default instances. The detailed instance and
   * scanner instance ignore the arguments and return the default instance.
   *
   * @param defaultInstance the instance this command will always result in
   */
  public CommandFactory(UnifiedCommand defaultInstance) {
    this.defaultInstance = Objects.requireNonNull(defaultInstance);
    this.detailedInstance = (ignore, ignore2) -> defaultInstance;
    this.scannerInstance = ignore -> defaultInstance;
  }


  /**
   * Get the default UnifiedCommand. Not all Commands will have default instances. In the case there
   * doesn't exist a default instance, an exception will be thrown.
   *
   * @throws UnsupportedOperationException if the command does not have a sensible default instance
   */
  public UnifiedCommand getDefaultInstance() {
    if (defaultInstance == null) {
      throw new UnsupportedOperationException("This command does not support a default instance");
    }
    return defaultInstance;
  }

  /**
   * Given a coordinate, create an instance of the command.
   *
   * @param coord the coordinate to use to create the command
   * @return a {@link UnifiedCommand} at the coordinate
   */
  public UnifiedCommand getDetailedInstance(HexPlaneCoord coord, Player player) {
    return detailedInstance.apply(coord, player);
  }

  /**
   * Given a scanner with some amount of unconsumed input, parse it and return a UnifiedCommand
   * parameterized by the scanner callback output.
   *
   * @param scanner the scanner to use to produce the {@link UnifiedCommand}
   * @return the {@link UnifiedCommand} created via a scanner
   */
  public UnifiedCommand getScannerInstance(Scanner scanner) {
    return scannerInstance.apply(scanner);
  }

  /**
   * A BiFunction interface for defining factories for {@link UnifiedCommand}s. Allows for Commands
   * to contain more complex information about the game when they are instantiated. The coordinate
   * may be null in the case none can be provided.
   */
  public interface CommandDetailedFunction extends
      BiFunction<HexPlaneCoord, Player, UnifiedCommand> {
    /**
     * Apply the function to arguments to make a {@link UnifiedCommand}. Not all arguments may be
     * required, but are provided for convenience. The coordinate may be null.
     *
     * @param coord  The coord the command occurred at. May be null.
     * @param player The player who executed the command.
     * @return A {@link UnifiedCommand} representing the fully constructed command.
     */
    UnifiedCommand apply(HexPlaneCoord coord, Player player);
  }
}
