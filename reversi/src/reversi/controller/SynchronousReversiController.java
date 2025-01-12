package reversi.controller;

import reversi.controller.agent.AbsoluteAgent;
import reversi.model.MutableReversiModel;
import reversi.view.ReversiView;

/**
 * Synchronous Controller for Reversi. Typically used in applications involving TUIs instead of
 * GUIs, where the players share the same view.
 */
public class SynchronousReversiController extends AbstractReversiController {
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
   * @throws NullPointerException if any argument is null
   */
  public SynchronousReversiController(AbsoluteAgent agent1,
                                      AbsoluteAgent agent2,
                                      ReversiView view1,
                                      ReversiView view2,
                                      MutableReversiModel model) {
    super(agent1, agent2, view1, view2, model);
  }

  @Override
  protected void pollAgents() {
    boolean aiGameStalemate = false;
    while (!isGameOver() && !aiGameStalemate) {
      // Player 1's turn
      boolean p1Passed = false;
      while (model.getPlayer() == agent1.getPlayer()) {
        view1.showErrorMessage("Your move, Player " + agent1.getPlayer());
        p1Passed = !performCommands(agent1, view1);
        renderViews();
      }
      // Player 2's turn
      boolean p2Passed = false;
      while (model.getPlayer() == agent2.getPlayer()) {
        view2.showErrorMessage("Your move, Player " + agent2.getPlayer());
        p2Passed = !performCommands(agent2, view2);
        renderViews();
      }
      aiGameStalemate = p1Passed && p2Passed && !agent1.viewIsVisible() && !agent2.viewIsVisible();
    }
    view1.gameEnd();
    if (view1 != view2) {
      view2.gameEnd();
    }
  }

}