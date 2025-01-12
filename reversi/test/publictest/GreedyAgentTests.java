package publictest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import reversi.command.commands.Move;
import reversi.command.commands.Pass;
import reversi.controller.Player;
import reversi.controller.agent.AbsoluteAgent;
import reversi.controller.agent.AgentBuilder;
import reversi.controller.agent.GreedyAIAgent;
import reversi.controller.agent.LoggingAgent;
import reversi.hex.coordinates.AxialCoordinate;
import reversi.hex.plane.HexPlane;
import reversi.hex.plane.HexPlaneImpl;
import reversi.model.MutableReversiModel;

/**
 * Tests for the {@link GreedyAIAgent}.
 */
public class GreedyAgentTests {

  StringBuilder log;

  @Before
  public void init() {
    log = new StringBuilder();
  }

  // multiple moves that capture the same number of pieces,
  // break ties by choosing the move with the uppermost-leftmost coordinate
  @Test
  public void testMultipleMoves() {
    HexPlane<Player> plane = new HexPlaneImpl<>(6);
    plane.setElementAt(new AxialCoordinate(0, 0), Player.PLAYER2);
    plane.setElementAt(new AxialCoordinate(-1, 0), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(-1, -1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(0, -1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(1, 0), Player.PLAYER1);
    StringBuilder log = new StringBuilder();
    MutableReversiModel model = new MockReversiModel(plane, Player.PLAYER1, log);
    AbsoluteAgent agent = AgentBuilder
            .create(new LoggingAgent(new MockGreedy(Player.PLAYER2, model, this.log), log))
            .getFirstMoveOrPassFinalizer()
            .build();
    Assert.assertEquals(new Move(new AxialCoordinate(0, -2)),
            agent.getAndResetCommand().getModelCommand().get());
    Assert.assertEquals(log.toString(),
            "Summarizer publictest.MockGreedy$MockGreedySummarizer run.\n");
    Assert.assertEquals("Accessed: AxialCoordinate{q=0, r=-2, s=2}\n" +
            "Accessed: AxialCoordinate{q=-2, r=0, s=2}\n" +
            "Accessed: AxialCoordinate{q=2, r=0, s=-2}\n", this.log.toString());
  }

  // no valid moves, you should pass
  @Test
  public void testNoMoves() {
    HexPlane<Player> plane = new HexPlaneImpl<>(6);
    StringBuilder log = new StringBuilder();
    MutableReversiModel model = new MockReversiModel(plane, Player.PLAYER1, log);
    AbsoluteAgent agent = AgentBuilder
            .create(new LoggingAgent(new MockGreedy(Player.PLAYER2, model, log), log))
            .getFirstMoveOrPassFinalizer()
            .build();
    Assert.assertEquals(new Pass(),
            agent.getAndResetCommand().getModelCommand().get());
    Assert.assertEquals(log.toString(),
            "Summarizer publictest.MockGreedy$MockGreedySummarizer run.\n");
  }

  // Use a mock to check that the correct summarizer is used
  @Test
  public void testChecksAllPossible() {
    HexPlane<Player> plane = new HexPlaneImpl<>(6);
    plane.setElementAt(new AxialCoordinate(0, 0), Player.PLAYER2);
    plane.setElementAt(new AxialCoordinate(-1, 0), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(-1, -1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(0, -1), Player.PLAYER1);
    plane.setElementAt(new AxialCoordinate(1, 0), Player.PLAYER1);
    StringBuilder log = new StringBuilder();

    MutableReversiModel model = new MockReversiModel(plane, Player.PLAYER1, log);
    AbsoluteAgent agent = AgentBuilder
            .create(new LoggingAgent(new MockGreedy(Player.PLAYER2, model, this.log), log))
            .getFirstMoveOrPassFinalizer()
            .build();
    agent.getAndResetCommand();
    Assert.assertEquals(log.toString(),
            "Summarizer publictest.MockGreedy$MockGreedySummarizer run.\n");
  }

  @Test
  public void testGreedyOnStartingBoard() {
    MutableReversiModel model = new MockReversiModel(5, this.log);
    AbsoluteAgent agent = AgentBuilder
            .create(new LoggingAgent(new MockGreedy(Player.PLAYER1, model, this.log), log))
            .getFirstMoveOrPassFinalizer()
            .build();

    Assert.assertEquals(new Move(new AxialCoordinate(-1, -1)),
            agent.getAndResetCommand().getModelCommand().get());
    Assert.assertEquals("Summarizer publictest.MockGreedy$MockGreedySummarizer run.\n" +
            "Accessed: AxialCoordinate{q=-1, r=-1, s=2}\n" +
            "Accessed: AxialCoordinate{q=1, r=-2, s=1}\n" +
            "Accessed: AxialCoordinate{q=-2, r=1, s=1}\n" +
            "Accessed: AxialCoordinate{q=2, r=-1, s=-1}\n" +
            "Accessed: AxialCoordinate{q=-1, r=2, s=-1}\n" +
            "Accessed: AxialCoordinate{q=1, r=1, s=-2}\n", log.toString());
  }
}
