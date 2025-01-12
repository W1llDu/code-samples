package publictest.summarizers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import publictest.MockHexPlane;
import publictest.MockReversiModel;
import reversi.command.commands.Move;
import reversi.command.commands.Pass;
import reversi.controller.Player;
import reversi.controller.agent.AbsoluteAgent;
import reversi.controller.agent.AgentBuilder;
import reversi.controller.agent.AvoidNextToCornersAIAgent;
import reversi.controller.agent.GreedyAIAgent;
import reversi.controller.agent.LoggingAgent;
import reversi.controller.agent.PasteAIAgent;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.plane.HexPlane;
import reversi.hex.plane.HexPlaneImpl;
import reversi.model.ReversiModel;

/**
 * Tests for {@link reversi.controller.agent.AgentBuilder} {@code andThen()}.
 */
public class CombinedAgentTests {

  StringBuilder log;

  @Before
  public void init() {
    log = new StringBuilder();
  }

  @Test
  public void testPasteAndGreedyYieldsPrimaryAfterUnificationWithDifferentValues() {
    HexPlane<Player> plane = new MockHexPlane<>(new HexPlaneImpl<>(5), log);
    plane.setElementAt(new AxialCoordinate(0, 0), Player.PLAYER2);
    plane.setElementAt(new AxialCoordinate(-1, 0), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(-1, -1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(0, -1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(1, 0), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(0, -2), Player.PLAYER1);
    MockReversiModel model = new MockReversiModel(plane, Player.PLAYER1, log);

    AbsoluteAgent agent = AgentBuilder
            .create(new LoggingAgent(new GreedyAIAgent(Player.PLAYER2, model), log))
            .andThenUnify(new LoggingAgent(new PasteAIAgent(Player.PLAYER2, model), log))
            .getFirstMoveOrPassFinalizer()
            .build();
    Assert.assertEquals(new Move(new AxialCoordinate(0, -3)),
            agent.getAndResetCommand().getModelCommand().get());
    Assert.assertTrue(log.toString().contains(
            "Summarizer reversi.hex.summarizer.strategy.PasteStrategySummarizer run"));
    Assert.assertTrue(log.toString().contains(
            "Summarizer reversi.hex.summarizer.strategy.GreedyStrategySummarizer run"));
  }

  @Test
  public void testPasteAndGreedyYieldsSharedAfterUnificationWithSimilarValues() {
    HexPlane<Player> plane = new MockHexPlane<>(new HexPlaneImpl<>(5), log);
    plane.setElementAt(new AxialCoordinate(0, 0), Player.PLAYER2);
    plane.setElementAt(new AxialCoordinate(-1, 0), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(-1, -1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(0, -1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(1, 0), Player.PLAYER1);
    ReversiModel model = new ReversiModel(plane, Player.PLAYER1);

    AbsoluteAgent agent = AgentBuilder
            .create(new LoggingAgent(new GreedyAIAgent(Player.PLAYER2, model), log))
            .andThenUnify(new LoggingAgent(new PasteAIAgent(Player.PLAYER2, model), log))
            .getFirstMoveOrPassFinalizer()
            .build();
    Assert.assertEquals(new Move(new AxialCoordinate(0, -2)),
            agent.getAndResetCommand().getModelCommand().get());
  }

  @Test
  public void testNoMoves() {
    HexPlane<Player> plane = new MockHexPlane<>(new HexPlaneImpl<>(6), log);
    MockReversiModel model = new MockReversiModel(plane, Player.PLAYER1, log);
    AbsoluteAgent agent = AgentBuilder
            .create(new LoggingAgent(new GreedyAIAgent(Player.PLAYER2, model), log))
            .andThenUnify(new LoggingAgent(new PasteAIAgent(Player.PLAYER2, model), log))
            .getFirstMoveOrPassFinalizer()
            .build();
    Assert.assertEquals(new Pass(),
            agent.getAndResetCommand().getModelCommand().get());
    Assert.assertTrue(log.toString().contains(
            "Summarizer reversi.hex.summarizer.strategy.PasteStrategySummarizer run"));
    Assert.assertTrue(log.toString().contains(
            "Summarizer reversi.hex.summarizer.strategy.GreedyStrategySummarizer run"));
  }

  @Test
  public void testAvoidNextToCornerAndGreedyAvoidsCornerNoMoves() {
    HexPlane<Player> plane = new MockHexPlane<>(new HexPlaneImpl<>(3), log);
    plane.setElementAt(new AxialCoordinate(0, 0), Player.PLAYER2);
    plane.setElementAt(new AxialCoordinate(-1, 0), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(-1, -1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(1, 0), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(0, 1), Player.PLAYER1);

    MockReversiModel model = new MockReversiModel(plane, Player.PLAYER1, log);

    AbsoluteAgent agent = AgentBuilder
            .create(new LoggingAgent(new AvoidNextToCornersAIAgent(Player.PLAYER1, model), log))
            .andThenUnify(new LoggingAgent(new GreedyAIAgent(Player.PLAYER1, model), log))
            .getFirstMoveOrPassFinalizer()
            .build();
    Assert.assertEquals(new Pass(),
            agent.getAndResetCommand().getModelCommand().get());
    Assert.assertTrue(log.toString().contains(
            "Summarizer reversi.hex.summarizer.strategy.AvoidNextToCornersStrategySummarizer run"));
    Assert.assertTrue(log.toString().contains(
            "Summarizer reversi.hex.summarizer.strategy.GreedyStrategySummarizer run"));
  }

  @Test
  public void testAvoidNextToCornerAndGreedyAvoidsCorner() {
    HexPlane<Player> plane = new MockHexPlane<>(new HexPlaneImpl<>(3), log);
    plane.setElementAt(new AxialCoordinate(-1, 0), Player.PLAYER2);
    plane.setElementAt(new AxialCoordinate(-1, 1), Player.PLAYER2);
    plane.setElementAt(new AxialCoordinate(0, 1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(0, 0), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(-2, 2), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(-1, 0), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(-1, -1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(1, 0), Player.PLAYER2);

    MockReversiModel model = new MockReversiModel(plane, Player.PLAYER1, log);

    AbsoluteAgent agent = AgentBuilder
            .create(new LoggingAgent(new AvoidNextToCornersAIAgent(Player.PLAYER1, model), log))
            .andThenUnify(new LoggingAgent(new GreedyAIAgent(Player.PLAYER1, model), log))
            .getFirstMoveOrPassFinalizer()
            .build();
    Assert.assertEquals(new Move(new AxialCoordinate(2, 0)),
            agent.getAndResetCommand().getModelCommand().get());
    System.out.println(log.toString());
    Assert.assertTrue(log.toString().contains(
            "Summarizer reversi.hex.summarizer.strategy.AvoidNextToCornersStrategySummarizer run"));
    Assert.assertTrue(log.toString().contains(
            "Summarizer reversi.hex.summarizer.strategy.GreedyStrategySummarizer run"));
  }
}
