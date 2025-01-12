package reversi.model;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import reversi.command.commands.Pass;
import reversi.controller.Player;
import reversi.controller.agent.AbsoluteAgent;
import reversi.controller.agent.AgentBuilder;
import reversi.controller.agent.AvoidNextToCornersAIAgent;
import reversi.controller.agent.GreedyAIAgent;
import reversi.controller.agent.PasteAIAgent;
import reversi.hex.plane.HexPlane;
import reversi.hex.plane.HexPlaneImpl;

/**
 * Tests for summarizers/agents on an empty grid.
 */
public class EmptyGridTest {

  HexPlane<Player> plane;
  MutableReversiModel model;

  @Before
  public void init() {
    plane = new HexPlaneImpl<>(5);
    model = new ReversiModel(plane, Player.PLAYER1);
  }

  @Test
  public void testGreedyEmptyGridPass() {
    AbsoluteAgent agent = AgentBuilder.create(new GreedyAIAgent(Player.PLAYER1, model))
        .getFirstMoveOrPassFinalizer().build();
    Assert.assertEquals(new Pass(), agent.getAndResetCommand().getModelCommand().get());
  }

  @Test
  public void testPasteEmptyGridPass() {
    AbsoluteAgent agent = AgentBuilder.create(new PasteAIAgent(Player.PLAYER1, model))
        .getFirstMoveOrPassFinalizer().build();
    Assert.assertEquals(new Pass(), agent.getAndResetCommand().getModelCommand().get());
  }

  @Test
  public void testNextToEmptyGridPass() {
    AbsoluteAgent agent = AgentBuilder.create(new AvoidNextToCornersAIAgent(Player.PLAYER1, model))
        .getFirstMoveOrPassFinalizer().build();
    Assert.assertEquals(new Pass(), agent.getAndResetCommand().getModelCommand().get());
  }
}
