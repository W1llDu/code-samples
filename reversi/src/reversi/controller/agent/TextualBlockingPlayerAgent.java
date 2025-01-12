package reversi.controller.agent;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import reversi.command.CommandAggregator;
import reversi.command.UnifiedCommand;
import reversi.controller.Player;

/**
 * A {@link TextualBlockingPlayerAgent} is an Agent intended to play text-based games of Reversi.
 * This Agent will block until a command is parsed.
 */
public class TextualBlockingPlayerAgent implements AbsoluteAgent {
  private final Scanner scanner;
  private final Player player;

  /**
   * Create a new {@link TextualBlockingPlayerAgent} given a source to read input from and the
   * player this agent plays as.
   *
   * @param in     the source to read input from
   * @param player the player this Agent plays as
   * @throws NullPointerException if either argument is null
   */
  public TextualBlockingPlayerAgent(Readable in, Player player) {
    this.scanner = new Scanner(Objects.requireNonNull(in));
    this.player = Objects.requireNonNull(player);
  }

  @Override
  public UnifiedCommand getAndResetCommand() {
    String cmdName = scanner.next();
    if (CommandAggregator.getIdentifierByName(cmdName).isEmpty()) {
      throw new IllegalStateException("Cannot find command " + cmdName + ". Acceptable "
          + "command names are: "
          + Arrays.toString(
          CommandAggregator.getCommandIdentifiers()
              .stream()
              .map(c -> c.name)
              .toArray())
          + "\nPlayer of this Agent: " + player);
    }
    // this will block!
    return CommandAggregator.getByName(cmdName).orElseThrow().getScannerInstance(scanner);
  }

  @Override
  public boolean viewIsVisible() {
    return true;
  }

  @Override
  public Player getPlayer() {
    return player;
  }
}
