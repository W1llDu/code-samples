package reversi.provider.adapters.controller;

import java.util.Objects;

import reversi.provider.model.board.TileColor;
import reversi.provider.model.board.TilePosition;
import reversi.provider.model.players.Player;

/**
 * A {@link ProviderPlayerAdapter} is not a true adapter, but instead more of a utility class
 * allow for the instantiation of a player via a {@link reversi.controller.Player}. Some things like
 * {@link #isHuman()} will always return true. Other methods like {@link #makeMove()} are not
 * supported.
 */
public class ProviderPlayerAdapter implements Player {
  private final TileColor color;

  /**
   * Create a new {@link ProviderPlayerAdapter} given the {@link reversi.controller.Player} to
   * adapt.
   *
   * @param player the provider's player to wrap
   */
  public ProviderPlayerAdapter(reversi.controller.Player player) {
    Objects.requireNonNull(player);

    if (player == reversi.controller.Player.PLAYER1) {
      color = TileColor.BLACK;
    } else {
      color = TileColor.WHITE;
    }
  }

  /**
   * Get the respective {@link reversi.controller.Player} given a {@link Player}.
   *
   * @param theirPlayer the {@link Player} to convert
   * @return the respective {@link reversi.controller.Player} according to theirPlayer's color
   */
  public static reversi.controller.Player getEnumFromPlayer(Player theirPlayer) {
    if (theirPlayer.getColor() == TileColor.BLACK) {
      return reversi.controller.Player.PLAYER1;
    }
    return reversi.controller.Player.PLAYER2;
  }

  @Override
  public TileColor getColor() {
    return color;
  }

  @Override
  public Boolean isHuman() {
    return true;
  }

  @Override
  public TilePosition makeMove() {
    throw new UnsupportedOperationException("ProviderPlayerAdapter is not meant to make moves");
  }
}
