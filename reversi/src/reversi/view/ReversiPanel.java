package reversi.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import reversi.command.CommandIdentifier;
import reversi.command.ViewCommand;
import reversi.command.commands.Highlight;
import reversi.controller.ControllerCallback;
import reversi.controller.Player;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.hex.plane.HexPlane;
import reversi.hex.summarizer.HexCoordToPixelSummarizer;
import reversi.model.ReadOnlyReversiModel;

/**
 * Package-private Panel for use in {@link ReversiGraphicsView}. Handles most of the logic behind
 * the interface and the actual function of the View; most of the methods in the View simply forward
 * to this class.
 */
class ReversiPanel extends JPanel {
  private final ReadOnlyReversiModel<Player> model;
  private ControllerCallback controllerCallback;
  private final int radius;
  private final Set<HexPlaneCoord> highlightedCoords;
  private final java.util.List<PolyCoord> drawnPolygons;

  /**
   * Create a new {@link ReversiPanel} given the model to display.
   *
   * @param model the read-only model to display
   */
  public ReversiPanel(ReadOnlyReversiModel<Player> model,
                      Set<HexPlaneCoord> highlightedCoords,
                      int radius) {
    this.model = Objects.requireNonNull(model);
    this.radius = radius;
    this.highlightedCoords = highlightedCoords;
    this.drawnPolygons = new ArrayList<>();
    Runnable callback = this::repaint;

    // SET UP EVENT LISTENERS
    this.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseReleased(MouseEvent e) {
        super.mouseReleased(e);
        requestFocusInWindow();
        Point clickPoint = e.getPoint();
        int button = e.getButton();
        // iterate over drawn polygons
        boolean clickInBounds = false;
        for (PolyCoord polyCoord : drawnPolygons) {
          HexPlaneCoord coord = polyCoord.coord;
          Polygon poly = polyCoord.poly;
          // check if we clicked in a poly and if the factory is present, sending the event if so
          if (poly.contains(clickPoint) && button == MouseEvent.BUTTON1) {
            sendToController(new Highlight(coord));
            clickInBounds = true;
            break;
          }
        }
        if (!clickInBounds) {
          highlightedCoords.clear();
        }
        callback.run();
      }
    });

    this.addKeyListener(new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        super.keyReleased(e);
        int key = e.getKeyCode();

        // lookup the command and send it
        if (highlightedCoords.size() == 1) {
          sendToController(new CommandIdentifier(key), new ArrayList<>(highlightedCoords).get(0));
        } else {
          sendToController(new CommandIdentifier(key), null);
        }
      }
    });
  }

  @Override
  protected void paintComponent(Graphics gBasic) {
    super.paintComponent(gBasic);
    this.drawnPolygons.clear();
    Graphics2D g = (Graphics2D) gBasic.create();
    g.translate(this.getWidth() / 2, this.getHeight() / 2);

    HexPlane<Point2D> pointMap = new HexCoordToPixelSummarizer<Player>(radius)
        .apply(model.getHexPlane());

    // DRAWING LOOP
    for (HexPlaneCoord coord : model.getHexPlane().getKnownCoords()) {
      // get the translated point from the map of translations. This is a known-safe operation.
      Point2D point = pointMap.getAtHex(coord).orElseThrow();
      Polygon p;
      ////////////////////////// HEXAGONS /////////////////////////////////
      // draw the actual polygons, highlighting them if necessary
      if (highlightedCoords.contains(coord)) {
        p = drawHexagon(g, (int) point.getX(), (int) point.getY(),
            radius, Color.CYAN, true);
      } else {
        p = drawHexagon(g, (int) point.getX(), (int) point.getY(),
            radius, Color.LIGHT_GRAY, true);
      }
      drawnPolygons.add(new PolyCoord(p, coord));
      //////////////////////// OUTLINE ////////////////////////////////////
      // draw the black outline
      drawHexagon(g, (int) point.getX(), (int) point.getY(),
          radius, Color.BLACK, false);
      // draw the players
      /////////////////////// PLAYERS ////////////////////////////////////
      if (model.getAtHex(coord).equals(Optional.of(Player.PLAYER1))) {
        drawCircle(g, (int) point.getX(), (int) point.getY(),
            radius / 2, Color.BLACK, true);
      } else if (model.getAtHex(coord).equals(Optional.of(Player.PLAYER2))) {
        drawCircle(g, (int) point.getX(), (int) point.getY(),
            radius / 2, Color.WHITE, true);
      }
    }
  }


  /**
   * Set the controller callback. The callback may be null.
   */
  public void setControllerCallback(ControllerCallback controllerCallback) {
    this.controllerCallback = controllerCallback;
  }

  public void showErrorMessage(String message) {
    Objects.requireNonNull(message);
    JOptionPane.showConfirmDialog(null,
        message,
        "Error!",
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.ERROR_MESSAGE);
  }

  private void drawCircle(Graphics g, int x, int y, int radius, Color color, boolean filled) {
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.translate(x, y);
    g2d.setColor(color);
    Shape circle = new Ellipse2D.Double(
        -radius,     // left
        -radius,     // top
        2 * radius,  // width
        2 * radius); // height
    if (filled) {
      g2d.fill(circle);
    } else {
      g2d.draw(circle);
    }
  }

  private Polygon drawHexagon(Graphics g, int x, int y, int radius, Color color, boolean filled) {
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.translate(x, y);
    g2d.setColor(color);
    Polygon p = new Polygon();
    for (int i = 0; i < 6; i++) {
      p.addPoint((int) Math.round(radius * Math.sin(i * Math.PI / 3)),
          (int) Math.round(radius * Math.cos(i * Math.PI / 3)));
    }
    if (filled) {
      g2d.fill(p);
    } else {
      g2d.draw(p);
    }
    Polygon retPoly = new Polygon();
    retPoly.xpoints = p.xpoints.clone();
    retPoly.ypoints = p.ypoints.clone();
    retPoly.npoints = p.npoints;
    retPoly.translate(x + this.getWidth() / 2, y + this.getHeight() / 2);
    return retPoly;
  }

  private void sendToController(CommandIdentifier identifier, HexPlaneCoord coord) {
    if (controllerCallback != null) {
      controllerCallback.accept(identifier, coord);
    }
  }

  private void sendToController(ViewCommand command) {
    if (controllerCallback != null) {
      controllerCallback.accept(command);
    }
  }

  private static class PolyCoord {
    public final Polygon poly;
    public final HexPlaneCoord coord;

    public PolyCoord(Polygon poly, HexPlaneCoord coord) {
      this.poly = Objects.requireNonNull(poly);
      this.coord = Objects.requireNonNull(coord);
    }
  }
}