package reversi;

import reversi.controller.AsynchronousReversiController;
import reversi.controller.Player;
import reversi.controller.ReversiController;
import reversi.controller.agent.AbsoluteAgent;
import reversi.controller.agent.GraphicalPlayerAgent;
import reversi.model.MutableReversiModel;
import reversi.model.ReversiModel;
import reversi.provider.adapters.controller.ProviderPlayerAdapter;
import reversi.provider.adapters.model.ProviderModelAdapter;
import reversi.provider.adapters.controller.ProviderAgentAIAdapter;
import reversi.provider.adapters.view.ProviderViewAdapter;
import reversi.provider.model.players.MinMax;
import reversi.view.ReversiGraphicsView;
import reversi.view.ReversiView;

/**
 * A class that runs the Reversi Game.
 */
public final class Reversi {

  /**
   * Main method for Reversi Game.
   */
  public static void main(String[] args) {
    if (args.length < 4 && false) {
      System.err.println("Expected more arguments: " +
          "(easy | medium | hard | providerHard | providerVeryHard | client) " +
          "(easy | medium | hard | providerHard | providerVeryHard | client) " +
          "(tui | gui | providerGui) " +
          "(natural number) " +
          "[X | O]");
      System.exit(1);
    }

    ////////////////// FACTORY //////////////////
    ReversiGameFactory.Skill p1Skill = parseSkill(args[0].toLowerCase());
    ReversiGameFactory.Skill p2Skill = parseSkill(args[1].toLowerCase());
    ReversiGameFactory.ViewMode viewMode = parseViewMode(args[2].toLowerCase());
    int radius = Integer.parseInt(args[3]);
    Player firstPlayer = null;
    if (args.length >= 5) {
      firstPlayer = parsePlayer(args[4].toLowerCase());
    }

    ReversiController controller = new ReversiGameFactory(
        radius, p1Skill, p2Skill, firstPlayer, viewMode)
        .create();

    controller.startGame();
  }

  private static Player parsePlayer(String lowerCaseStr) {
    switch (lowerCaseStr) {
      case "x":
        return Player.PLAYER1;
      case "o":
        return Player.PLAYER2;
      default:
        throw new IllegalArgumentException(
            "Cannot parse first player string \"" + lowerCaseStr + "\"");
    }
  }

  private static ReversiGameFactory.ViewMode parseViewMode(String lowerCaseStr) {
    switch (lowerCaseStr) {
      case "gui":
        return ReversiGameFactory.ViewMode.GUI;
      case "providergui":
        return ReversiGameFactory.ViewMode.ProviderGui;
      case "tui":
        return ReversiGameFactory.ViewMode.TUI;
      default:
        throw new IllegalArgumentException(
            "Cannot parse view mode string \"" + lowerCaseStr + "\"");
    }
  }

  private static ReversiGameFactory.Skill parseSkill(String lowerCaseStr) {
    switch (lowerCaseStr) {
      case "easy":
        return ReversiGameFactory.Skill.Easy;
      case "medium":
        return ReversiGameFactory.Skill.Medium;
      case "hard":
        return ReversiGameFactory.Skill.Hard;
      case "providerhard":
        return ReversiGameFactory.Skill.ProviderHard;
      case "providerveryhard":
        return ReversiGameFactory.Skill.ProviderVeryHard;
      case "client":
        return ReversiGameFactory.Skill.Client;
      default:
        throw new IllegalArgumentException(
            "Cannot parse difficulty string \"" + lowerCaseStr + "\"");
    }
  }
}