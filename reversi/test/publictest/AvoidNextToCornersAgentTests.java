package publictest;

import org.junit.Assert;
import org.junit.Test;

import reversi.command.commands.Move;
import reversi.command.commands.Pass;
import reversi.controller.agent.LoggingAgent;
import reversi.controller.Player;
import reversi.controller.agent.AbsoluteAgent;
import reversi.controller.agent.AgentBuilder;
import reversi.controller.agent.AvoidNextToCornersAIAgent;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.plane.HexPlane;
import reversi.hex.plane.HexPlaneImpl;
import reversi.model.MutableReversiModel;

/**
 * Tests for the {@link AvoidNextToCornersAIAgent}.
 */
public class AvoidNextToCornersAgentTests {

  @Test
  public void testTwoMoves() {
    HexPlane<Player> plane = new HexPlaneImpl<>(3);
    plane.setElementAt(new AxialCoordinate(1, 0), Player.PLAYER2);
    plane.setElementAt(new AxialCoordinate(1, -1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(0, 1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(0, 2), Player.PLAYER2);
    StringBuilder log = new StringBuilder();
    MutableReversiModel model = new MockReversiModel(plane, Player.PLAYER1, log);
    AbsoluteAgent agent = AgentBuilder
            .create(new LoggingAgent(new AvoidNextToCornersAIAgent(Player.PLAYER2, model), log))
            .getFirstMoveOrPassFinalizer()
            .build();
    Assert.assertEquals(new Move(new AxialCoordinate(0 , 0)),
            agent.getAndResetCommand().getModelCommand().get());
    Assert.assertEquals(log.toString(),
            "Summarizer reversi.hex.summarizer"
                    + ".strategy.AvoidNextToCornersStrategySummarizer run.\n");
  }

  @Test
  public void testOneInvalidMove() {
    HexPlane<Player> plane = new HexPlaneImpl<>(3);
    plane.setElementAt(new AxialCoordinate(1, 0), Player.PLAYER2);
    plane.setElementAt(new AxialCoordinate(1, -1), Player.PLAYER1);
    StringBuilder log = new StringBuilder();
    MutableReversiModel model = new MockReversiModel(plane, Player.PLAYER1, log);
    AbsoluteAgent agent = AgentBuilder
        .create(new LoggingAgent(new AvoidNextToCornersAIAgent(Player.PLAYER2, model), log))
        .getFirstMoveOrPassFinalizer()
        .build();
    Assert.assertEquals(new Pass(),
        agent.getAndResetCommand().getModelCommand().get());
    Assert.assertEquals(log.toString(),
        "Summarizer reversi.hex.summarizer"
            + ".strategy.AvoidNextToCornersStrategySummarizer run.\n");
  }

  @Test
  public void testNoMoves() {
    HexPlane<Player> plane = new HexPlaneImpl<>(6);
    StringBuilder log = new StringBuilder();
    MutableReversiModel model = new MockReversiModel(plane, Player.PLAYER1, log);
    AbsoluteAgent agent = AgentBuilder
        .create(new LoggingAgent(new AvoidNextToCornersAIAgent(Player.PLAYER2, model), log))
        .getFirstMoveOrPassFinalizer()
        .build();
    Assert.assertEquals(new Pass(),
        agent.getAndResetCommand().getModelCommand().get());
    Assert.assertEquals(log.toString(),
        "Summarizer reversi.hex.summarizer"
            + ".strategy.AvoidNextToCornersStrategySummarizer run.\n");
  }
}
