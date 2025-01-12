package reversi.provider.adapters.controller;

import java.util.Objects;

import reversi.command.UnifiedCommand;
import reversi.command.commands.Move;
import reversi.command.commands.Pass;
import reversi.controller.agent.AbsoluteAgent;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.provider.adapters.hex.ProviderHexCoordAdapter;
import reversi.provider.adapters.model.ProviderModelAdapter;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.TileColor;
import reversi.provider.model.board.TilePosition;
import reversi.provider.model.players.Player;
import reversi.provider.model.players.ReversiRobotPlayer;

/**
 * A {@link ProviderAgentAIAdapter} is an Object Adapter for
 * {@link reversi.controller.agent.AbsoluteAgent}s. It adapts an AbsoluteAgent to be compatible with
 * {@link reversi.provider.model.players.Player}.
 */
public class ProviderAgentAIAdapter implements Player, AbsoluteAgent {
  private final reversi.provider.model.players.Player agent;
  private final reversi.controller.Player player;
  private final ReversiModel model;
  private final TileColor color;

  /**
   * Create a new Adapter given the {@link reversi.provider.model.players.InfallibleReversiStrategy}
   * to adapt.
   *
   * @param agent the agent to wrap as a {@link Player}
   * @param model the model given to the agent. It must implement both the provider and our
   *              interfaces. That is, it must be an adapter
   * @param player the player to play as
   * @throws NullPointerException if the argument is null
   */
  public ProviderAgentAIAdapter(
      reversi.provider.model.players.InfallibleReversiStrategy agent,
      ProviderModelAdapter model,
      reversi.controller.Player player) {
    Objects.requireNonNull(agent);
    this.model = Objects.requireNonNull(model);
    this.player = Objects.requireNonNull(player);
    if (player == reversi.controller.Player.PLAYER1) {
      this.color = TileColor.BLACK;
    } else {
      this.color = TileColor.WHITE;
    }
    this.agent = new ReversiRobotPlayer(this.color, agent, model);
  }

  @Override
  public UnifiedCommand getAndResetCommand() {
    TilePosition pos = agent.makeMove();
    int row = pos.getRow();
    int col = pos.getCol();
    if (row == -1 && col == -1) {
      return new UnifiedCommand(new Pass());
    }
    HexPlaneCoord coord = new ProviderHexCoordAdapter(row, col, model.getSize() - 1);
    return new UnifiedCommand(new Move(coord));
  }

  @Override
  public boolean viewIsVisible() {
    return isHuman();
  }

  @Override
  public reversi.controller.Player getPlayer() {
    return player;
  }

  @Override
  public TileColor getColor() {
    return color;
  }

  @Override
  public Boolean isHuman() {
    return false;
  }

  @Override
  public TilePosition makeMove() {
    return agent.makeMove();
  }
}
