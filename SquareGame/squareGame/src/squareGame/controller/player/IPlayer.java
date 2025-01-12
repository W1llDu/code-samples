package squareGame.controller.player;

import squareGame.model.Move;
import squareGame.model.SquareGameModel;

public interface IPlayer {

  Move getMove(SquareGameModel model) throws QuitException;
}
