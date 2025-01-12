package squareGame.controller;

import java.util.List;

import squareGame.model.SquareGameModel;

public interface SquareGameController {

  void playGame(SquareGameModel model, String player1, String player2);
}
