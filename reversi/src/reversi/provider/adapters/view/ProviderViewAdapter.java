package reversi.provider.adapters.view;

import java.util.Objects;

import reversi.command.ViewCommand;
import reversi.controller.ControllerCallback;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.summarizer.reversiinfo.TotalPointsSummarizer;
import reversi.provider.adapters.controller.ProviderViewListenerAdapter;
import reversi.provider.adapters.model.ProviderModelAdapter;
import reversi.provider.model.players.Player;
import reversi.provider.view.SimpleReversiGuiView;
import reversi.view.ReversiView;

import javax.swing.*;

/**
 * The {@link ProviderViewAdapter} is a class adapter meant to wrap {@link SimpleReversiGuiView}.
 * It implements the {@link ReversiView} interface to be able to communicate with the
 * {@link reversi.controller.ReversiController}.
 */
public class ProviderViewAdapter extends SimpleReversiGuiView implements ReversiView {
  private final ProviderModelAdapter model;

  /**
   * Creates a view based on the given model.
   *
   * @param model the model to use to implement the behaviors
   * @param player the player this view belongs to
   */
  public ProviderViewAdapter(ProviderModelAdapter model, Player player) {
    super(model, player);
    this.model = Objects.requireNonNull(model);
  }

  @Override
  public boolean render() {
    this.setVisible(true);
    this.paintAgain();
    //TODO: boolean stuff
    return false;
  }

  @Override
  public void setControllerCallback(ControllerCallback controllerCallback) {
    this.setListener(new ProviderViewListenerAdapter(controllerCallback, model.getSize() - 1));
  }

  @Override
  public void showErrorMessage(String message) throws NullPointerException {
    //TODO: wrong turn done in provider panel
  }

  @Override
  public void highlightTile(HexPlaneCoord coord)
      throws IndexOutOfBoundsException, NullPointerException {
    //TODO: done in provider panel
  }

  @Override
  public void clearHighlights() {
    //TODO: done in provider panel
  }

  @Override
  public void accept(ViewCommand command) throws NullPointerException {
    Objects.requireNonNull(command);
    command.performViewCommand(this, model);
  }

  @Override
  public void gameEnd() {
    if (!isVisible()) {
      return;
    }
    JOptionPane.showConfirmDialog(null,
            String.format("X: %s points.\nO: %s points.",
                    new TotalPointsSummarizer(reversi.controller.Player.PLAYER1).apply(model.getHexPlane()),
                    new TotalPointsSummarizer(reversi.controller.Player.PLAYER2).apply(model.getHexPlane())),
            "Game Over!",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE);
  }
}
