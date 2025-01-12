package reversi.provider.adapters.hex;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import reversi.controller.Player;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.provider.model.board.Board;
import reversi.provider.model.board.Tile;
import reversi.provider.model.board.TileColor;

/**
 * {@link BoardAdapter} is an Object Adapter used to adapt a {@link HexPlane} to a {@link Board}.
 * It delegates to the provided HexPlane, substituting its Players for Tiles when necessary.
 */
public class BoardAdapter implements HexPlane<Player>, Board {
  private final HexPlane<Player> plane;

  /**
   * Create a new {@link BoardAdapter} given the {@link HexPlane} to use.
   *
   * @param plane the plane to use to implement {@link Board}
   */
  public BoardAdapter(HexPlane<Player> plane) {
    this.plane = Objects.requireNonNull(plane);
  }

  @Override
  public Map<HexPlaneCoord, Optional<Player>> getPlane() {
    return plane.getPlane();
  }

  @Override
  public int getRadius() {
    return plane.getRadius() + 1;
  }

  @Override
  public Set<HexPlaneCoord> getKnownCoords() {
    return plane.getKnownCoords();
  }

  @Override
  public void setElementAt(HexPlaneCoord coord, Player elem)
      throws NullPointerException, IndexOutOfBoundsException {
    plane.setElementAt(coord, elem);
  }

  @Override
  public <R> HexPlane<R> keysetMap(Function<HexPlaneCoord, R> func) {
    return plane.keysetMap(func);
  }

  @Override
  public Optional<Player> getAtHex(HexPlaneCoord coordinate) throws IndexOutOfBoundsException {
    return plane.getAtHex(coordinate);
  }

  ///////////////////////////////////////// BOARD /////////////////////////////////////////
  @Override
  public void updateBoard(TileColor color, int row, int col) {
    setElementAt(new ProviderHexCoordAdapter(col, row, plane.getRadius()), null);
  }

  @Override
  public Tile getTileAt(int row, int col) {
    return this.getBoard().get(row).get(col);
  }

  @Override
  public int getSize() {
    return getRadius();
  }

  @Override
  public List<List<Tile>> getBoard() {
    List<List<Tile>> ret = new ArrayList<>();
    int radius = getRadius() - 1;
    for (int r = -radius; r <= radius; r++) {
      List<Tile> row = new ArrayList<>();
      for (int q = Math.max(-radius - r, -radius); q <= Math.min(radius - r, radius); q++) {
        row.add(new PlayerToTileAdapter(plane
            .getAtHex(new AxialCoordinate(q, r))
            .orElse(null)));
      }
      ret.add(row);
    }
    return ret;
  }


  private class PlayerToTileAdapter implements Tile {
    private final Player player;

    /**
     * Create a new {@link PlayerToTileAdapter} given the Player to use internally.
     *
     * @param player the Player enum representing the value at this location
     */
    public PlayerToTileAdapter(Player player) {
      this.player = player;
    }

    /**
     * Gets the player of this cell. This is for internal use in {@link BoardAdapter}.
     */
    public Player getPlayer() {
      return player;
    }

    @Override
    public void setNeighbor(String position, Tile tile) {
      throw new UnsupportedOperationException("setNeighbor() should never be used and" +
          "is not supported by BoardAdapter.PlayerToTileAdapter");
      // FIXME: check for usages
    }

    @Override
    public TileColor getColor() {
      // FIXME: check for usages
      if (player == null) {
        return TileColor.NONE;
      }
      switch (player) {
        case PLAYER1:
          return TileColor.BLACK;
        case PLAYER2:
          return TileColor.WHITE;
        default:
          throw new IllegalStateException("Unreachable in BoardAdapter.");
      }
    }

    @Override
    public List<Tile> getNeighbors() {
      return null;
    }

    @Override
    public void setColor(TileColor color) {

    }
  }
}
