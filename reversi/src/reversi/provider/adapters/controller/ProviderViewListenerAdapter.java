package reversi.provider.adapters.controller;

import java.awt.event.KeyEvent;
import java.util.Objects;

import reversi.command.CommandIdentifier;
import reversi.controller.ControllerCallback;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.provider.adapters.hex.ProviderHexCoordAdapter;
import reversi.provider.model.players.Player;
import reversi.provider.view.ViewListener;

public class ProviderViewListenerAdapter implements ViewListener {
  private final ControllerCallback delegate;
  private final int size;

  public ProviderViewListenerAdapter(ControllerCallback controllerCallback, int size) {
    this.delegate = Objects.requireNonNull(controllerCallback);
    this.size = size;
  }

  @Override
  public void makePlay(Player player, int row, int col) {
    delegate.accept(new CommandIdentifier(KeyEvent.VK_ENTER),
        new ProviderHexCoordAdapter(row, col, size));
  }

  @Override
  public void pass(Player player) {
      delegate.accept(new CommandIdentifier(KeyEvent.VK_P), (HexPlaneCoord) null);
  }
}
