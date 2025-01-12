package publictest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import reversi.ReversiGameFactory;
import reversi.command.UnifiedCommand;
import reversi.command.commands.Move;
import reversi.command.commands.Pass;
import reversi.controller.AsynchronousReversiController;
import reversi.controller.Player;
import reversi.controller.ReversiController;
import reversi.controller.SynchronousReversiController;
import reversi.controller.agent.AbsoluteAgent;
import reversi.exceptions.IllegalCommandException;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.coordinates.CubicalCoordinate;
import reversi.hex.summarizer.reversiinfo.TotalPointsSummarizer;
import reversi.model.MutableReversiModel;
import reversi.model.ReversiModel;
import reversi.view.ReversiView;

/**
 * Tests for the Controller. Both {@link AsynchronousReversiController} and the
 * {@link SynchronousReversiController} are tested here.
 */
public class PublicReversiControllerTests {
  private ReversiController cont;
  private StringBuilder log;
  private MutableReversiModel model;

  @Before
  public void initSync() {
    log = new StringBuilder();
    model = new MockReversiModel(3, log);
    ReversiView view = new MockReversiView(log);
    cont = new NoExtraPollSyncController(
        new DoNothingAgent(Player.PLAYER1),
        new DoNothingAgent(Player.PLAYER1),
        view,
        view,
        model);
  }

  @Test
  public void testSyncNull() {
    Assert.assertThrows(NullPointerException.class,
        () -> new SynchronousReversiController(
            null,
            new DoNothingAgent(Player.PLAYER1),
            new MockReversiView(log),
            new MockReversiView(log),
            new ReversiModel(5)));
    Assert.assertThrows(NullPointerException.class,
        () -> new SynchronousReversiController(
            new DoNothingAgent(Player.PLAYER1),
            null,
            new MockReversiView(log),
            new MockReversiView(log),
            new ReversiModel(5)));
    Assert.assertThrows(NullPointerException.class,
        () -> new SynchronousReversiController(
            new DoNothingAgent(Player.PLAYER1),
            new DoNothingAgent(Player.PLAYER1),
            null,
            new MockReversiView(log),
            new ReversiModel(5)));
    Assert.assertThrows(NullPointerException.class,
        () -> new SynchronousReversiController(
            new DoNothingAgent(Player.PLAYER1),
            new DoNothingAgent(Player.PLAYER1),
            new MockReversiView(log),
            null,
            new ReversiModel(5)));
    Assert.assertThrows(NullPointerException.class,
        () -> new SynchronousReversiController(
            new DoNothingAgent(Player.PLAYER1),
            new DoNothingAgent(Player.PLAYER1),
            new MockReversiView(log),
            new MockReversiView(log),
            null));
  }

  @Test
  public void testSyncGameOver() {
    log = new StringBuilder();
    model = new MockReversiModel(3, log);
    ReversiView view = new MockReversiView(log);
    cont = new SynchronousReversiController(
        new SetMovesAgent(Player.PLAYER1,
            List.of(new Move(new CubicalCoordinate(-1, -1, 2)),
                    new Move(new AxialCoordinate(2, -1)),
                    new Move(new AxialCoordinate(-1, 2)),
                    new Pass())
                .iterator()),
        new SetMovesAgent(Player.PLAYER2,
            List.of(new Move(new CubicalCoordinate(1, 1, -2)),
                    new Move(new AxialCoordinate(1, -2)),
                    new Move(new AxialCoordinate(-2, 1)),
                    new Pass())
                .iterator()),
        view,
        view,
        model);
    cont.startGame();
    Assert.assertEquals("Move at AxialCoordinate{q=2, r=-1, s=-1} as X.",
        getLine(13));
    Assert.assertEquals("Move at AxialCoordinate{q=-1, r=2, s=-1} as X.",
        getLine(23));
    Assert.assertEquals("gameEnd",
        getLine(30));
    Assert.assertEquals(Integer.valueOf(4),
        new TotalPointsSummarizer(Player.PLAYER1).apply(model.getHexPlane()));
    Assert.assertEquals(Integer.valueOf(8),
        new TotalPointsSummarizer(Player.PLAYER2).apply(model.getHexPlane()));
  }

  @Test
  public void testSyncPass() {
    initSync();
    try {
      cont.accept(new UnifiedCommand(new Pass()), Player.PLAYER1);
      Assert.assertEquals("Pass as X.",
          getLine(0));
      Assert.assertEquals(model.getPlayer(), Player.PLAYER2);
      cont.accept(new UnifiedCommand(new Pass()), Player.PLAYER1);
      Assert.assertEquals("Pass command failed",
          getLine(3));
      Assert.assertEquals("It is not X's turn, cannot make an action.",
          getLine(4));
      cont.accept(new UnifiedCommand(new Pass()), Player.PLAYER2);
      Assert.assertEquals(model.getPlayer(), Player.PLAYER1);
      cont.accept(new UnifiedCommand(new Pass()), Player.PLAYER2);
      Assert.assertEquals("Pass command failed",
          getLine(8));
      Assert.assertEquals("It is not O's turn, cannot make an action.",
          getLine(9));
      Assert.assertEquals(10, log.toString().split("\n").length);
    } catch (IllegalCommandException e) {
      Assert.fail();
    }
  }

  @Test
  public void testSyncInvalidMove() {
    initSync();
    try {
      //none
      cont.accept(new UnifiedCommand(new Move(new AxialCoordinate(0, 2))), Player.PLAYER1);
      Assert.assertEquals("Invalid move: attempted move does not flip any pieces.",
          getLine(2));
      //overwrite
      cont.accept(new UnifiedCommand(new Move(new AxialCoordinate(0, 1))), Player.PLAYER1);
      Assert.assertEquals("Invalid move: can't overwrite existing piece.",
          getLine(5));
      //out of bounds
      cont.accept(new UnifiedCommand(new Move(new AxialCoordinate(0, 3))), Player.PLAYER1);
      Assert.assertEquals("MoveOutOfBoundsException: AxialCoordinate{q=0, r=3, s=-3}",
          getLine(8));
      cont.accept(new UnifiedCommand(new Move(new AxialCoordinate(1, 1))), Player.PLAYER2);
      Assert.assertEquals("It is not O's turn, cannot make an action.",
          getLine(11));
    } catch (IllegalCommandException e) {
      Assert.fail();
    }
  }

  /**
   * Initiate the Asynchronous Controller.
   */
  protected void initAsync() {
    log = new StringBuilder();
    model = new MockReversiModel(3, log);
    ReversiView view = new MockReversiView(log);
    cont = new NoExtraPollAsyncController(
        new DoNothingAgent(Player.PLAYER1),
        new DoNothingAgent(Player.PLAYER1),
        view,
        view,
        model);
  }

  @Test
  public void testAsyncNull() {
    Assert.assertThrows(NullPointerException.class,
        () -> new AsynchronousReversiController(
            null,
            new DoNothingAgent(Player.PLAYER1),
            new MockReversiView(log),
            new MockReversiView(log),
            new ReversiModel(5)));
    Assert.assertThrows(NullPointerException.class,
        () -> new AsynchronousReversiController(
            new DoNothingAgent(Player.PLAYER1),
            null,
            new MockReversiView(log),
            new MockReversiView(log),
            new ReversiModel(5)));
    Assert.assertThrows(NullPointerException.class,
        () -> new AsynchronousReversiController(
            new DoNothingAgent(Player.PLAYER1),
            new DoNothingAgent(Player.PLAYER1),
            null,
            new MockReversiView(log),
            new ReversiModel(5)));
    Assert.assertThrows(NullPointerException.class,
        () -> new AsynchronousReversiController(
            new DoNothingAgent(Player.PLAYER1),
            new DoNothingAgent(Player.PLAYER1),
            new MockReversiView(log),
            null,
            new ReversiModel(5)));
    Assert.assertThrows(NullPointerException.class,
        () -> new AsynchronousReversiController(
            new DoNothingAgent(Player.PLAYER1),
            new DoNothingAgent(Player.PLAYER1),
            new MockReversiView(log),
            new MockReversiView(log),
            null));
  }

  @Test
  public void testAsyncGameOver() {
    log = new StringBuilder();
    model = new MockReversiModel(3, log);
    ReversiView view = new MockReversiView(log);
    cont = new AsynchronousReversiController(
        new SetMovesAgent(Player.PLAYER1,
            List.of(new Move(new CubicalCoordinate(-1, -1, 2)),
                    new Move(new AxialCoordinate(2, -1)),
                    new Move(new AxialCoordinate(-1, 2)),
                    new Pass())
                .iterator()),
        new SetMovesAgent(Player.PLAYER2,
            List.of(new Move(new CubicalCoordinate(1, 1, -2)),
                    new Move(new AxialCoordinate(1, -2)),
                    new Move(new AxialCoordinate(-2, 1)),
                    new Pass())
                .iterator()),
        view,
        view,
        model);
    cont.startGame();
    Assert.assertEquals("Move at AxialCoordinate{q=2, r=-1, s=-1} as X.",
        getLine(5));
    Assert.assertEquals("Move at AxialCoordinate{q=-1, r=2, s=-1} as X.",
        getLine(9));
    Assert.assertEquals("gameEnd",
        getLine(16));
    Assert.assertEquals(Integer.valueOf(4),
        new TotalPointsSummarizer(Player.PLAYER1).apply(model.getHexPlane()));
    Assert.assertEquals(Integer.valueOf(8),
        new TotalPointsSummarizer(Player.PLAYER2).apply(model.getHexPlane()));
  }

  @Test
  public void testAsyncPass() {
    initAsync();
    try {
      cont.accept(new UnifiedCommand(new Pass()), Player.PLAYER1);
      Assert.assertEquals("Pass as X.",
          getLine(0));
      Assert.assertEquals(model.getPlayer(), Player.PLAYER2);
      cont.accept(new UnifiedCommand(new Pass()), Player.PLAYER1);
      Assert.assertEquals("Pass command failed",
          getLine(3));
      Assert.assertEquals("It is not X's turn, cannot make an action.",
          getLine(4));
      cont.accept(new UnifiedCommand(new Pass()), Player.PLAYER2);
      Assert.assertEquals(model.getPlayer(), Player.PLAYER1);
      cont.accept(new UnifiedCommand(new Pass()), Player.PLAYER2);
      Assert.assertEquals("Pass command failed",
          getLine(8));
      Assert.assertEquals("It is not O's turn, cannot make an action.",
          getLine(9));
      Assert.assertEquals(10, log.toString().split("\n").length);
    } catch (IllegalCommandException e) {
      Assert.fail();
    }
  }

  @Test
  public void testAsyncInvalidMove() {
    initAsync();
    try {
      //none
      cont.accept(new UnifiedCommand(new Move(new AxialCoordinate(0, 2))), Player.PLAYER1);
      Assert.assertEquals("Invalid move: attempted move does not flip any pieces.",
          getLine(2));
      //overwrite
      cont.accept(new UnifiedCommand(new Move(new AxialCoordinate(0, 1))), Player.PLAYER1);
      Assert.assertEquals("Invalid move: can't overwrite existing piece.",
          getLine(5));
      //out of bounds
      cont.accept(new UnifiedCommand(new Move(new AxialCoordinate(0, 3))), Player.PLAYER1);
      Assert.assertEquals("MoveOutOfBoundsException: AxialCoordinate{q=0, r=3, s=-3}",
          getLine(8));
      cont.accept(new UnifiedCommand(new Move(new AxialCoordinate(1, 1))), Player.PLAYER2);
      Assert.assertEquals("It is not O's turn, cannot make an action.",
          getLine(11));
    } catch (IllegalCommandException e) {
      Assert.fail();
    }
  }

  @Test
  public void testEasyEasyAIScore() {
    ReversiGameFactory factory = new ReversiGameFactory(
        8,
        ReversiGameFactory.Skill.Easy,
        ReversiGameFactory.Skill.Easy,
        Player.PLAYER1,
        ReversiGameFactory.ViewMode.TUI);
    factory.setOut(log);
    String xExpected = "X: " + 63 + " points.";
    String oExpected = "O: " + 67 + " points.";

    factory.create().startGame();
    String[] out = log.toString().split("\n");
    int len = out.length - 1;
    Assert.assertEquals(out[len], oExpected);
    Assert.assertEquals(out[len - 1], xExpected);
  }

  @Test
  public void testAIAreDeterministic() {
    ReversiGameFactory.Skill[] allSkills = ReversiGameFactory.Skill.values();
    // avoid using Client because we are testing AI only
    ReversiGameFactory.Skill[] skills = new ReversiGameFactory.Skill[3];
    System.arraycopy(allSkills, 0, skills, 0, 3);

    // this will take a while... but at least its thorough!
    for (ReversiGameFactory.Skill p1Skill : skills) {
      for (ReversiGameFactory.Skill p2Skill : skills) {
        for (int size = 1; size < 20; size++)  {
          StringBuilder game1Log = new StringBuilder();
          ReversiGameFactory factory = new ReversiGameFactory(
              size,
              p1Skill,
              p2Skill,
              Player.PLAYER1,
              ReversiGameFactory.ViewMode.TUI);
          factory.setOut(game1Log);
          factory.create().startGame();

          String[] game1Out = game1Log.toString().split("\n");
          int game1Len = game1Out.length - 1;
          String p1ScoreGame1 = game1Out[game1Len - 1];
          String p2ScoreGame1 = game1Out[game1Len];

          StringBuilder game2Log = new StringBuilder();
          factory = new ReversiGameFactory(
              size,
              p1Skill,
              p2Skill,
              Player.PLAYER1,
              ReversiGameFactory.ViewMode.TUI);
          factory.setOut(game2Log);
          factory.create().startGame();

          String[] game2Out = game2Log.toString().split("\n");
          int game2Len = game2Out.length - 1;
          String p1ScoreGame2 = game2Out[game2Len - 1];
          String p2ScoreGame2 = game2Out[game2Len];

          Assert.assertEquals(p1ScoreGame1, p1ScoreGame2);
          Assert.assertEquals(p2ScoreGame1, p2ScoreGame2);
        }
      }
    }
  }

  /////////////////////////////////// HELPERS //////////////////////////////////////////

  private String getLine(int n) {
    String[] lines = log.toString().split("\n");
    return lines[n];
  }

  // Prevent the extra polling of pollAgents(). It makes gameplay work smoothly, but gets in the way
  // of tests.
  private static class NoExtraPollSyncController extends SynchronousReversiController {

    /**
     * Superclass constructor for Controllers. The Agents are required for obvious reasons, but the
     * Views are more complex. The subclass needn't require the View for its constructor, but should
     * provide one to the superclass anyway. If a single View is used, simply pass the same View to
     * both arguments.
     *
     * @param agent1 the agent for the first player
     * @param agent2 the agent for the second player
     * @param view1  the view for the first player
     * @param view2  the view for the second player
     * @param model  the model to use to play the game
     * @throws NullPointerException if any argument is null
     */
    public NoExtraPollSyncController(AbsoluteAgent agent1,
                                     AbsoluteAgent agent2,
                                     ReversiView view1,
                                     ReversiView view2,
                                     MutableReversiModel model) {
      super(agent1, agent2, view1, view2, model);
    }

    @Override
    protected void pollAgents() {
      // do nothing here to prevent consuming events that occur at the same time
    }
  }

  // Prevent the extra polling of pollAgents(). It makes gameplay work smoothly, but gets in the way
  // of tests.
  private static class NoExtraPollAsyncController extends AsynchronousReversiController {

    /**
     * Superclass constructor for Controllers. The Agents are required for obvious reasons, but the
     * Views are more complex. The subclass needn't require the View for its constructor, but should
     * provide one to the superclass anyway. If a single View is used, simply pass the same View to
     * both arguments.
     *
     * @param agent1 the agent for the first player
     * @param agent2 the agent for the second player
     * @param view1  the view for the first player
     * @param view2  the view for the second player
     * @param model  the model to use to play the game
     * @throws NullPointerException if any argument is null
     */
    public NoExtraPollAsyncController(AbsoluteAgent agent1,
                                      AbsoluteAgent agent2,
                                      ReversiView view1,
                                      ReversiView view2,
                                      MutableReversiModel model) {
      super(agent1, agent2, view1, view2, model);
    }

    @Override
    protected void pollAgents() {
      // do nothing here to prevent consuming events that occur at the same time
    }
  }
}
