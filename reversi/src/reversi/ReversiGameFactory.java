package reversi;

import java.io.InputStreamReader;
import java.util.Objects;

import reversi.controller.AsynchronousReversiController;
import reversi.controller.Player;
import reversi.controller.ReversiController;
import reversi.controller.SynchronousReversiController;
import reversi.controller.agent.AbsoluteAgent;
import reversi.controller.agent.AgentBuilder;
import reversi.controller.agent.AvoidNextToCornersAIAgent;
import reversi.controller.agent.GraphicalPlayerAgent;
import reversi.controller.agent.GreedyAIAgent;
import reversi.controller.agent.PasteAIAgent;
import reversi.controller.agent.TextualBlockingPlayerAgent;
import reversi.model.MutableReversiModel;
import reversi.model.ReversiModel;
import reversi.provider.adapters.controller.ProviderAgentAIAdapter;
import reversi.provider.adapters.controller.ProviderPlayerAdapter;
import reversi.provider.adapters.model.ProviderModelAdapter;
import reversi.provider.adapters.view.ProviderViewAdapter;
import reversi.provider.model.players.AlwaysPass;
import reversi.provider.model.players.CaptureMax;
import reversi.provider.model.players.MinMax;
import reversi.provider.model.players.TryTwo;
import reversi.view.ReversiGraphicsView;
import reversi.view.ReversiTextView;
import reversi.view.ReversiView;

/**
 * Factory to produce a game of Reversi given the game parameters. Users should instantiate this
 * class and then call {@link #create()} to create a {@link ReversiController}. It is then ready to
 * start the game via {@link ReversiController#startGame()}.
 *
 * <p>By default, if the game is TUI-based, it will be output onto stdout. Use
 * {@link #setOut(Appendable)} to supply a custom {@link Appendable} destination for game output.
 */
public class ReversiGameFactory {
  private final int radius;
  private final Skill p1Skill;
  private final Skill p2Skill;
  private final Player firstPlayer;
  private final ViewMode viewMode;
  private Appendable out;

  /**
   * Skill level of the Agents. Easy makes the worst moves possible, Medium is purely greedy, Hard
   * makes better moves than Medium (but is still not particularly amazing), and Client is a human.
   */
  public enum Skill {
    Easy, Medium, Hard, ProviderHard, ProviderVeryHard, Client
  }

  /**
   * The way in which the game should be played. TUI is "Terminal UI," or is console based. Players
   * will take turns making their inputs on stdin, and the board will be printed on stdout. GUI is
   * for a graphical interface. At least one Frame will be shown, but up to two frames can be shown
   * at any given time (usually if both players are {@link Skill#Client}).
   */
  public enum ViewMode {
    TUI, GUI, ProviderGui
  }

  /**
   * Create a new {@link ReversiGameFactory} given the radius of the board, the skill of each
   * player, and the view to play with.
   *
   * @param radius      the radius of the board
   * @param p1Skill     the skill of the first player
   * @param p2Skill     the skill of the second player
   * @param firstPlayer the first player to make a move
   * @param viewMode    the way in which to view the board
   * @throws NullPointerException if any argument (except firstPlayer) is null
   */
  public ReversiGameFactory(int radius,
                            Skill p1Skill,
                            Skill p2Skill,
                            Player firstPlayer,
                            ViewMode viewMode) {
    this.radius = radius;
    this.p1Skill = Objects.requireNonNull(p1Skill);
    this.p2Skill = Objects.requireNonNull(p2Skill);
    this.firstPlayer = Objects.requireNonNullElse(firstPlayer, Player.PLAYER1);
    this.viewMode = Objects.requireNonNull(viewMode);
    this.out = System.out;
  }

  /**
   * Create the {@link ReversiController} given the parameters passed to the constructor.
   *
   * @return the finalized ReversiController according to the provided parameters
   */
  public ReversiController create() {
    MutableReversiModel model = new ReversiModel(radius, firstPlayer);
    AbsoluteAgent xAgent = createAgent(p1Skill, Player.PLAYER1, model);
    AbsoluteAgent oAgent = createAgent(p2Skill, Player.PLAYER2, model);
    AbsoluteAgent p1Agent;
    AbsoluteAgent p2Agent;
    // assign order to players with convenient names
    if (firstPlayer == Player.PLAYER1) {
      p1Agent = xAgent;
      p2Agent = oAgent;
    } else {
      p1Agent = oAgent;
      p2Agent = xAgent;
    }

    switch (viewMode) {
      case TUI: {
        ReversiView tty = new ReversiTextView(out, model);
        return new SynchronousReversiController(
            p1Agent, p2Agent, tty, tty, model);
      }
      case GUI: {
        ReversiView p1View = new ReversiGraphicsView(model, p1Agent.getPlayer());
        ReversiView p2View = new ReversiGraphicsView(model, p2Agent.getPlayer());
        return new AsynchronousReversiController(p1Agent, p2Agent, p1View, p2View, model);
      }
      case ProviderGui: {
        ProviderModelAdapter adaptedModel = new ProviderModelAdapter(model);
        ReversiView p1View = new ProviderViewAdapter(
            adaptedModel,
            new ProviderPlayerAdapter(p1Agent.getPlayer()));
        ReversiView p2View = new ProviderViewAdapter(
            adaptedModel,
            new ProviderPlayerAdapter(p2Agent.getPlayer()));
        // if the first player is p1, change the order of the views to show the correct one
        // in the case of player vs AI
        if (firstPlayer == Player.PLAYER1) {
          return new AsynchronousReversiController(
              p1Agent,
              p2Agent,
              p1View,
              p2View,
              model);
        } else {
          return new AsynchronousReversiController(
              p1Agent,
              p2Agent,
              p2View,
              p1View,
              model);
        }
      }
      default:
        throw new IllegalArgumentException("Unknown viewMode in create(): " + viewMode);
    }
  }

  /**
   * Set the output to the provided Appendable. By default, the output is set to stdout.
   *
   * @param out the Appendable the Controller this factory will produce will output to
   * @throws NullPointerException if the argument is null
   */
  public void setOut(Appendable out) {
    this.out = Objects.requireNonNull(out);
  }

  private AbsoluteAgent createAgent(Skill skill,
                                    Player player,
                                    MutableReversiModel model) {
    switch (skill) {
      case Easy:
        return AgentBuilder
            .create(new PasteAIAgent(player, model))
            .getFirstMoveOrPassFinalizer()
            .build();
      case Medium:
        return AgentBuilder
            .create(new GreedyAIAgent(player, model))
            .getFirstMoveOrPassFinalizer()
            .build();
      case Hard:
        return AgentBuilder
            .create(new AvoidNextToCornersAIAgent(player, model))
            .andThenUnify(new GreedyAIAgent(player, model))
            .getFirstMoveOrPassFinalizer()
            .build();
      case ProviderHard:
        return AgentBuilder
            .create(new ProviderAgentAIAdapter(
                new TryTwo(new CaptureMax(), new AlwaysPass()),
                new ProviderModelAdapter(model),
                player))
            .build();
      case ProviderVeryHard:
        return AgentBuilder
            .create(new ProviderAgentAIAdapter(
                new MinMax(),
                new ProviderModelAdapter(model),
                player))
            .build();
      case Client:
        // determine which mode we are playing in
        if (viewMode == ViewMode.TUI) {
          return new TextualBlockingPlayerAgent(new InputStreamReader(System.in), player);
        } else {
          return new GraphicalPlayerAgent(player);
        }
      default:
        throw new IllegalArgumentException("Cannot create agent with unknown skill: " + skill);
    }
  }
}