Overview
This Reversi Game implementation provides a model-view-controller (MVC) architecture for playing
the classic Reversi game. Reversi is a two-player board game where the players take turns placing
colored discs (black or white) on a grid. The game ends when there are no more valid moves left
or the board is fully occupied. The winner is the player with the most discs of their color
on the board.

~~High-Level Assumptions
- Basic familiarity with the rules of Reversi.
- Java programming knowledge for extending and customizing the codebase.
- SIZE OF THE BOARD IS EQUAL TO THE NUMBER OF TILES IN THE FIRST ROW
- BOARD IS SET UP AS A LIST OF ROWS WHERE EACH ROW IS A LIST OF TILES


///////////////////////////////////////////////////////////////////////////////////////////////////

Quick Start
To get started with the Reversi game, you can create a new instance of the BasicReversiModel class
and set up the game by specifying the players and the board size.
Here's an example of how to start a game:

public class Main {
    public static void main(String[] args) {
        Player blackPlayer = new ReversiPlayer(TileColor.BLACK);
        Player whitePlayer = new ReversiPlayer(TileColor.WHITE);
        ReversiBoard board = new ReversiBoard(8);
        ReversiModel game = new BasicReversiModel();
        game.startGame(blackPlayer, whitePlayer, board);
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////

Key Components
~~Model
The model represents the game logic, including the board state, player turns, and game rules.
It enforces the game invariants and calculates the score.

~~View
The view handles the user interface, providing ways to visualize the game board and interact with
players. In this implementation, a textual view (ReversiTextualView) is provided to display the
game state in the console.

~~Controller
The controller manages the flow of the game, handling user input and updating the model
accordingly. It ensures valid moves and passes player turns.

///////////////////////////////////////////////////////////////////////////////////////////////////

Key Subcomponents
~~ReversiBoard
- Represents the game board as a hexagonal grid.
- Manages the state of individual tiles on the board.
~~HexTile
- Represents a hexagonal tile on the game board.
- Stores the color of the tile and its neighboring tiles.
~~Player
- Represents a player in the Reversi game.
- Provides the color of tiles the player is playing with.

///////////////////////////////////////////////////////////////////////////////////////////////////

Source Organization
- cs3500.reversi.model: Contains the model classes, including BasicReversiModel, ReversiBoard,
HexTile, and Player.
- cs3500.reversi.view: Contains the view classes, including ReversiTextualView.
- cs3500.reversi.controller: Contains the controller classes (if any).

///////////////////////////////////////////////////////////////////////////////////////////////////

Invariants
~~Class Invariant for AbstractReversiModel
Invariant: At any point during the game, the AbstractReversiModel class guarantees that the board
state is consistent and valid. Each tile on the board is in one of three states:
Black, White, or None (empty).
Enforcement: The AbstractReversiModel class enforces this invariant by ensuring the following:
- Initialization
- Valid Moves
- Updating Board State
- Game Over Handling
- Score Calculation
By adhering to these enforcement mechanisms, the AbstractReversiModel class maintains a consistent
board state where each tile is always in one of three valid states:
Black, White, or None. This consistency is vital for the integrity of the game and ensures that
players' moves and scores are accurately represented throughout the gameplay.

~~Class Invariant for BasicReversiModel
Invariant: The game must follow the standard Reversi rules, including valid moves, alternating
player turns, and correct score calculation.
Enforcement: The BasicReversiModel class enforces these rules by validating moves, managing player
turns, and calculating scores based on the game state.

~~Class Invariant for HexTile
Invariant: Each tile must have correct references to its neighboring tiles.
Enforcement: The HexTile class sets and maintains references to neighboring tiles during board
initialization, ensuring correct connectivity.

///////////////////////////////////////////////////////////////////////////////////////////////////

Changes for part 2

Split the ReversiModel interface into two parts, ReversiModel and ReadOnlyReversiModel.
This is so that we can reliably control the model.

Also split the Board interface into two parts, Board and ReadOnlyBoard.
This lets also lets us reliably control the board.

Created two new interface methods in ReversiModel: createBoard() and copyBoard().
This is so that we can have the ability to create a board of a given size in its default
initial state and also have the ability to create a copy of a board.

For keyboard input: Pressing "Enter"confirms placement of the tile
                    Pressing "P/p" lets the user pass

///////////////////////////////////////////////////////////////////////////////////////////////////

Changes for part 3

For the view, the new classes we created we ModelListener, RobotView and ViewListener.
The ModelListener is an interface that connects the interface and the model.
The ViewListener checks to see if anything gets changed in the view, and servers as the connection
between the view and the controller.
The RobotView creates a view for the robot. The reason why we made a separate view for the robot
is due to the fact that a player should not be able to click on any of the bots hex.

We also made some changes to the ReversiGuiView. We added a couple of methods to it, mainly
so that we could repaint both views simultaneously after changes were made after the player made a move.