package reversi.provider.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.event.MouseInputAdapter;

import reversi.provider.model.board.Board;
import reversi.provider.model.ReadOnlyReversiModel;
import reversi.provider.model.board.TileColor;
import reversi.provider.model.players.Player;

/**
 * Creates a panel filled with hexagons.
 */
public class ReversiGuiPanel extends JPanel {

  protected final ReadOnlyReversiModel model;
  //  private final int size;
  protected final int radius = 30;
  //  private int clicked = 0;
  protected Element clicked;

  protected final static List<Element> centers = new ArrayList<>();
  protected final TileColor color;

  protected final Player player;

  /**
   * Creates a panel based on the model given.
   */
  public ReversiGuiPanel(ReadOnlyReversiModel model, TileColor color, Player player) {
    this.color = color;
    this.model = Objects.requireNonNull(model);
    this.player = player;
    generateBoard();
    //    this.size = model.getSize();

    //check if robot or not
    if (player.isHuman()) {
      MouseEventsListener listener = new MouseEventsListener();
      this.addMouseListener(listener);
      this.addMouseMotionListener(listener);
    }
  }

  /**
   * Creates a hexagon element that saves important information.
   */
  public class Element {
    public Point2D point;
    public Color color;
    public TileColor tileColor;
    public Boolean clicked = false;
    private final int row;
    private final int col;

    /**
     * Creates a hexagon element based on the parameters.
     *
     * @param point     of center for hexagon.
     * @param color     of the hexagon.
     * @param tileColor of the circle on the hexagon.
     */
    public Element(Point2D point, Color color, TileColor tileColor, int row, int col) {
      this.point = point;
      this.color = color;
      this.tileColor = tileColor;
      this.row = row;
      this.col = col;
    }

    public int getRow() {
      return row;
    }

    public int getCol() {
      return col;
    }

  }

  /**
   * Generates a board of the given size.
   */
  public void generateBoard() {
    int size = model.getSize();
    Board board = (Board) model.getBoard();

    int colsize = 0;
    int height = 2 * size - 1;

    for (int row = 0; row < height; row++) {
      // compute colum size:
      if (row < size) {
        // increase
        colsize = row;
      } else {
        // decrease
        colsize--;
      }

      // compute the tab
      int tab = (height - (size + colsize)) * radius * 2 + (size + colsize) * (radius);

      for (int col = 0; col < size + colsize; col++) {
        int x = col * radius * 2 + tab - 250;
        int y = row * -(radius) - row * radius * 2 / 2 + 100;

        TileColor tileColor = board.getTileAt(row, col).getColor();
        centers.add(new Element(new Point2D.Double(x, y), Color.LIGHT_GRAY, tileColor, row, col));
      }
    }
  }

  /**
   * This method tells Swing what the "natural" size should be
   * for this panel.  Here, we set it to 400x400 pixels.
   *
   * @return Our preferred *physical* size.
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(1000, 1000);
  }

  /**
   * Conceptually, we can choose a different coordinate system
   * and pretend that our panel is 40x40 "cells" big. You can choose
   * any dimension you want here, including the same as your physical
   * size (in which case each logical pixel will be the same size as a physical
   * pixel, but perhaps your calculations to position things might be trickier)
   *
   * @return Our preferred *logical* size.
   */
  private Dimension getPreferredLogicalSize() {
    return new Dimension(1000, 1000);
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.transform(transformLogicalToPhysical());

    // Draw your calibration pattern here
    Rectangle bounds = this.getBounds();
    //    g2d.setColor(Color.RED);
    //    g2d.drawLine(bounds.x, bounds.y, bounds.x + bounds.width, bounds.y + bounds.height);
    //    g2d.setColor(Color.BLUE);
    //    g2d.drawLine(bounds.x + bounds.width, bounds.y, bounds.x, bounds.y + bounds.height);

    //    g2d.translate(bounds.x, bounds.y);
    //    g2d.scale(1.5, 1.5);


    //    for (Point2D point : CIRCLE_CENTERS) {
    //      drawCircle(g2d, Color.red, point.getX(), point.getY());
    //    }
    for (Element e : centers) {
      drawHex(g2d, e.color, e.point.getX(), e.point.getY(), e.tileColor, e.clicked);
    }
  }

  private void drawHex(Graphics2D g2d, Color color, double x, double y,
                       TileColor tileColor, boolean clicked) {
    AffineTransform oldTransform = g2d.getTransform();
    g2d.setColor(color);
    g2d.translate(x, y);
    //    Shape circle = new Ellipse2D.Double(
    //            -CIRCLE_RADIUS,     // left
    //            -CIRCLE_RADIUS,     // top
    //            2 * CIRCLE_RADIUS,  // width
    //            2 * CIRCLE_RADIUS); // height
    Polygon p = new Polygon();

    for (int i = 0; i < 6; i++) {
      var angle_deg = 60 * i - 30;
      var angle_rad = Math.PI / 180 * angle_deg;
      p.addPoint((int) (radius * Math.cos(angle_rad)), (int) (radius * Math.sin(angle_rad)));
    }

    //if clicked change hex color to cyan
    if (clicked) {
      g2d.setColor(Color.CYAN);
    }
    g2d.fillPolygon(p);
    g2d.setColor(color);

    //draw circle
    if (tileColor == TileColor.BLACK) {
      g2d.setColor(Color.BLACK);
      Shape circle = new Ellipse2D.Double(
              -10,     // left
              -10,     // top
              2 * 10,  // width
              2 * 10); // height

      g2d.fill(circle);
      //      g2d.draw(circle);
    }

    if (tileColor == TileColor.WHITE) {
      g2d.setColor(Color.WHITE);
      Shape circle = new Ellipse2D.Double(
              -10,     // left
              -10,     // top
              2 * 10,  // width
              2 * 10); // height
      g2d.fill(circle);
      //      g2d.draw(circle);
    }

    //handle player
    g2d.setTransform(oldTransform);
  }

  /**
   * Computes the transformation that converts board coordinates
   * (with (0,0) in center, width and height our logical size)
   * into screen coordinates (with (0,0) in upper-left,
   * width and height in pixels).
   * This is the inverse of {@link ReversiGuiPanel#transformPhysicalToLogical()}.
   *
   * @return The necessary transformation
   */
  private AffineTransform transformLogicalToPhysical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.translate(getWidth() / 2., getHeight() / 2.);
    ret.scale(getWidth() / preferred.getWidth(), getHeight() / preferred.getHeight());
    ret.scale(1, -1);
    return ret;
  }

  /**
   * Computes the transformation that converts screen coordinates
   * (with (0,0) in upper-left, width and height in pixels)
   * into board coordinates (with (0,0) in center, width and height
   * our logical size).
   * This is the inverse of {@link ReversiGuiPanel#transformLogicalToPhysical()}.
   *
   * @return The necessary transformation
   */
  private AffineTransform transformPhysicalToLogical() {
    AffineTransform ret = new AffineTransform();
    Dimension preferred = getPreferredLogicalSize();
    ret.scale(1, -1);
    ret.scale(preferred.getWidth() / getWidth(), preferred.getHeight() / getHeight());
    ret.translate(-getWidth() / 2., -getHeight() / 2.);
    return ret;
  }

  private class MouseEventsListener extends MouseInputAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
      //if robot, return
      if (!player.isHuman()) {
        return;
      } else {
        System.out.println("human");
      }

      Player playerTurn = model.getTurn();

      if (!playerTurn.getColor().equals(color)) {
        JFrame frame = new JFrame("Warning");
        JOptionPane.showMessageDialog(frame, "Not your turn");
        return;
      }

      // This point is measured in actual physical pixels
      Point physicalP = e.getPoint();
      // For us to figure out which circle it belongs to, we need to transform it
      // into logical coordinates
      Point2D logicalP = transformPhysicalToLogical().transform(physicalP, null);
      if (true);

      for (Element element : centers) {
        if ((element.point.getX() - 20 <= logicalP.getX() &&
                logicalP.getX() <= element.point.getX() + 20)
                && (element.point.getY() - 20 <= logicalP.getY() &&
                logicalP.getY() <= element.point.getY() + 20)) {
          System.out.println("Mouse Clicked at : " + element.getRow() + " " + element.getCol());

          if (clicked == element) {
            element.clicked = false;
            clicked = null;
          } else {
            element.clicked = true;
            clicked = element;
          }

        } else {
          element.clicked = false;
        }
      }
      ReversiGuiPanel.this.repaint();
    }
  }

  /**
   * Getter method for element.
   *
   * @return an element
   */
  public Element getElement() {
    for (Element element : centers) {
      if (element.clicked) {
        return element;
      }
    }
    return null;
  }
}
