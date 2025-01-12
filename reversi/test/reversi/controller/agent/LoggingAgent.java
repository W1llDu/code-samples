package reversi.controller.agent;

import java.util.List;

import reversi.command.UnifiedCommand;
import reversi.controller.Player;

/**
 * A {@link LoggingAgent} logs the Summarizer used by the provided AI Agent. It will output the
 * name of the summarizer and the fact it is being used any time
 * {@link MaybeAgent#getAndResetCommand()} is called.
 */
public class LoggingAgent implements MaybeAgent {
  private final AbstractAIAgent agent;
  private final StringBuilder log;
  private final String summarizerName;

  /**
   * Create a new {@link LoggingAgent} given the AI Agent to log and the sink for the log messages.
   *
   * @param agent the agent to log
   * @param log the output for the logs
   */
  public LoggingAgent(AbstractAIAgent agent, StringBuilder log) {
    this.agent = agent;
    this.log = log;
    this.summarizerName = agent.strategy.getClass().getName();
  }

  @Override
  public List<UnifiedCommand> getAndResetCommand() {
    log.append(String.format("Summarizer %s run.\n", summarizerName));
    return agent.getAndResetCommand();
  }

  @Override
  public boolean viewIsVisible() {
    return agent.viewIsVisible();
  }

  @Override
  public Player getPlayer() {
    return agent.getPlayer();
  }
}
