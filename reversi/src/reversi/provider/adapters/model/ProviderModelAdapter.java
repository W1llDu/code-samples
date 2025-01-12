package reversi.provider.adapters.model;

import java.util.Optional;
import java.util.Objects;

import reversi.exceptions.MoveOutOfBoundsException;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.BoardSizeSummarizer;
import reversi.hex.summarizer.reversiinfo.IsGameOverSummarizer;
import reversi.hex.summarizer.reversiinfo.IsMoveLegalSummarizer;
import reversi.hex.summarizer.reversiinfo.PointsFromMoveAtPointSummarizer;
import reversi.hex.summarizer.reversiinfo.TotalPointsSummarizer;
import reversi.model.MutableReversiModel;
import reversi.provider.adapters.controller.ProviderPlayerAdapter;
import reversi.provider.adapters.hex.ProviderHexCoordAdapter;
import reversi.provider.adapters.hex.BoardAdapter;
import reversi.provider.model.ReversiModel;
import reversi.provider.model.board.Board;
import reversi.provider.model.players.Player;

/**
 * An Object Adapter between {@link reversi.provider.model.ReversiModel} and
 * {@link MutableReversiModel}.
 */
public class ProviderModelAdapter implements ReversiModel, MutableReversiModel {
  private final MutableReversiModel model;

  /**
   * Create a new {@link ProviderModelAdapter} given the {@link MutableReversiModel} to adapt
   * into a {@link reversi.model.ReversiModel}.
   *
   * @param model the model to adapt
   */
  public ProviderModelAdapter(MutableReversiModel model) {
    this.model = Objects.requireNonNull(model);
  }

  @Override
  public Board getBoard() {
    HexPlane<reversi.controller.Player> plane = model.getHexPlane();
    return new BoardAdapter(plane);
  }

  @Override
  public void startGame(Player black, Player white, Board board) {
    // do nothing. we do not need this method
  }

  @Override
  public reversi.provider.model.ReversiModel cloneModel() {
    // this will work? hopefully!
    return new ProviderModelAdapter(
        new reversi.model.ReversiModel(model.getHexPlane(), model.getPlayer()));
  }

  @Override
  public Player getTurn() {
    return new ProviderPlayerAdapter(model.getPlayer());
  }

  @Override
  public boolean canPlay(Player player, int row, int col) {
    try {
      return new IsMoveLegalSummarizer(
          new ProviderHexCoordAdapter(row, col, model),
          model.getPlayer()).apply(model.getHexPlane());
    } catch (RuntimeException e) {
      return false;
    }
  }

  @Override
  public void makeMove(Player player, int row, int col) {
    this.makeMoveAsPlayer(new ProviderHexCoordAdapter(row, col, model),
        ProviderPlayerAdapter.getEnumFromPlayer(player));
  }

  @Override
  public void pass(Player player) {
    this.passAsPlayer(ProviderPlayerAdapter.getEnumFromPlayer(player));
  }

  @Override
  public int getScore(Player player) {
    return new TotalPointsSummarizer(ProviderPlayerAdapter.getEnumFromPlayer(player))
        .apply(model.getHexPlane());
  }

  @Override
  public int getPotentialScore(Player player, int row, int col) {
    return new PointsFromMoveAtPointSummarizer(
        new ProviderHexCoordAdapter(row, col, model),
        ProviderPlayerAdapter.getEnumFromPlayer(player))
        .apply(model.getHexPlane());
  }

  @Override
  public boolean isGameOver() {
    return new IsGameOverSummarizer().apply(model.getHexPlane());
  }

  @Override
  public int getSize() {
    return new BoardSizeSummarizer<reversi.controller.Player>().apply(model.getHexPlane()) + 1;
  }

  @Override
  public void makeMoveAsPlayer(HexPlaneCoord coordinate, reversi.controller.Player player)
      throws MoveOutOfBoundsException, IllegalStateException {
    model.makeMoveAsPlayer(coordinate, player);
  }

  @Override
  public void passAsPlayer(reversi.controller.Player player) throws IllegalStateException {
    model.passAsPlayer(player);
  }

  @Override
  public HexPlane<reversi.controller.Player> getHexPlane() {
    return model.getHexPlane();
  }

  @Override
  public reversi.controller.Player getPlayer() {
    return model.getPlayer();
  }

  @Override
  public Optional<reversi.controller.Player> getAtHex(HexPlaneCoord coordinate)
      throws IndexOutOfBoundsException, NullPointerException {
    return model.getAtHex(coordinate);
  }
}
