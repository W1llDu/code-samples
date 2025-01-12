package publictest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import reversi.controller.Player;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.HexPlaneCoord;
import reversi.model.MutableReversiModel;
import reversi.model.ReversiModel;
import reversi.view.ReversiTextView;

/**
 * Public-scoped tests for Views.
 * TODO: refactor with factories (where an enum determines the type of view to make).
 */
public class PublicTextViewTests {

  private StringBuilder out;
  private ReversiTextView view;
  private MutableReversiModel model;

  @Before
  public void init() {
    this.out = new StringBuilder();
  }

  @Test
  public void testConstructorWithNullValuesException() {
    Assert.assertThrows(NullPointerException.class,
        () -> new ReversiTextView(null, new ReversiModel(5)));
    Assert.assertThrows(NullPointerException.class,
        () -> new ReversiTextView(new StringBuilder(), null));
  }


  @Test
  public void testSettingControllerCallbackToNullIsAllowed() {
    model = new ReversiModel(3);
    view = new ReversiTextView(out, model);
    assertNoThrow(() -> view.setControllerCallback(null));
  }

  @Test
  public void testHighlightPosition() {
    model = new ReversiModel(3);
    view = new ReversiTextView(out, model);

    String blankExpected =
            "  _ _ _ \n" +
                    " _ X O _ \n" +
                    "_ O _ X _ \n" +
                    " _ X O _ \n" +
                    "  _ _ _\n";

    // not highlighted
    view.render();
    Assert.assertEquals(blankExpected, out.toString());
    String highlightExpected =
            "  _ _ _ \n" +
                    " _ X O _ \n" +
                    "_ O \033[0;31m_ \033[0mX _ \n" +
                    " _ X O _ \n" +
                    "  _ _ _\n";

    // highlighted
    view.highlightTile(new AxialCoordinate(0, 0));
    view.render();
    Assert.assertEquals(blankExpected + highlightExpected, out.toString());
  }

  @Test
  public void testHighlightingNullThrows() {
    model = new ReversiModel(3);
    view = new ReversiTextView(out, model);
    Assert.assertThrows(NullPointerException.class, () -> view.highlightTile(null));
  }

  @Test
  public void testHighlightingOutOfBoundsCoordThrows() {
    model = new ReversiModel(3);
    view = new ReversiTextView(out, model);

    Assert.assertThrows(IndexOutOfBoundsException.class,
        () -> view.highlightTile(new AxialCoordinate(100, 100)));
  }

  private static void assertNoThrow(Runnable action) {
    try {
      action.run();
    } catch (Exception ex) {
      Assert.fail("Expected no exception, but exception was thrown");
    }
  }

  private void makeMove(HexPlaneCoord coord, Player player) {
    Player curPlayer = model.getPlayer();
    if (curPlayer != player) {
      model.passAsPlayer(player.getOther());
    }
    model.makeMoveAsPlayer(coord, player);
  }
}
