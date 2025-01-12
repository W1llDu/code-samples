
# Table of Contents

1.  [Overview](#orgf6e1f4b)
2.  [Quick start](#org5857e2f)
3.  [Keybindings](#keybindings)
4.  [Commands (TTY Only)](#commands-tty-only)
5.  [Command Line Arguments](#command-line-arguments)
6.  [Key components](#org0115636)
    1.  [Controller](#org1bf86fb)
    2.  [Model](#org8b0201a)
    3.  [View](#org5bef0d3)
7.  [Key subcomponents](#orgbdc7145)
    1.  [Agents](#orgeb76263)
    2.  [Commands (Model & View)](#orgf22b5aa)
    3.  [HexPlane](#org488b6dd)
        1.  [Coordinates (Axial & Cubical)](#org4999441)
8.  [Source organization](#org6ced1bd)


<a id="orgf6e1f4b"></a>

# CHANGES FOR CUSTOMER
None. After sending, we received no further communications.

# WORKING AND NON-WORKING FEATURES
Some features, like highlighting, were not supported by the provided interfaces.
We did our best to accommodate all other features. Indeed, the only other
commands they had were as spartan as ours outside requesting possible moves. Therefore,
we were able to get both the passing and the making a move supported, even with our original 
factory implementation.

# Overview

This is a Java implementation of the game Reversi on a hexagonal grid. It
is built with extensibility in mind, but does not support alternative board
geometries (e.g. square). The codebase uses a Model-View-Controller
architecture. It is expected any who are attempting to hack on this codebase
have an intermediate understanding of Object Oriented Design and a fairly
solid grasp on Java.

# Changes from Part 1

Not many changes were made, but there are a few important additions. The model has a new
method to allow passing the turn, and a few new summarizers were added. Notably, there is now
a summarizer to return the radius of the board, to check the legality of a move, and to determine if
a player has any legal moves. There is also a new package-private ctor for the model to allow
unit testing in-progress games.

We chose to add them in this way because it best fit with out current design decisions.
In particular, summarizers are simple to implement and very extensible, so we decided to
implement as much as possible using them. Pass required a method because it impacts game state in a
manner a summarizer cannot.

We changed the agent interface by segmenting it into MaybeAgents and AbsoluteAgents, which
allow a level of granularity for AI versus players. The main Agent interface is now package private
to avoid others extending the main interface directly. We added a builder for the Agents, which
provides a very convenient interface to composing multiple Agents/Strategies together.

# Changes from Part 2

<b><u>NOTE:</u></b> our implementation does not require the instantiation of two Controllers.
Each player's View receives a `ControllerCallback` instead, which will manage this for us.
We had this reviewed by Prof. Lerner, who said "a little bit overkill and a little bit sketch,
but it is plausible it will work."

A few changes! Some of the things we planned for didn't integrate extremely smoothly, but otherwise 
the overall architecture is the same. Aside from the obvious additions (Controller and 
ControllerCallback), the rough outline remains the same. Some classes had names changed to be more
clear about what they actually were intended to be used for (`CoordsToFlipGivenMoveSummarizer` now
has a coherent class name).

The biggest change is the way Summarizers work. The interface that originally used them is now
called `ReadOnlyReversiModel`, and it no longer has the method `accept(Summarizer<Player>)`.
Instead, Summarizers are directly supplied the copy of the HexPlane with the new `getHexPlane()`
`ReadOnlyReversiModel` method. This change was made because of clarity; when we received a code
review, it was noted the implementation was far too ornate due to the several levels of indirect 
and copying. 

HexPlane's `getPlane()` method now returns a mutable map. Documentation has been updated to make
this apparent, and documentation for methods that rely on this method have been updated to note 
the HexPlane they are receiving will not change the Model's HexPlane, since they receive a copy
instead.

A few return types changed here and there. The actual interface for things like Agents remains 
the same. We added `isGameOver()` to the Controller interface. The types of the CommandFactory
changed a bit to allow for Commands to be created with more information available to them (namely 
the player making the command). ViewCommands are now given a `ReadOnlyReversoiModel<Player>`, 
since they weren't able to inspect the Model before (used to implement `ShowPotentialMoves`).

The Controller was originally planned to have a single implementation,
but that proved impractical when all things were considered. So instead, we made an Abstract class
to create the base implementations and build both {A}SynchronousControllers from it.

Of course, many of the TODOs left in the code are gone. Some of the TODOs from before were in the
code for the View, which we intended to replace in this assignment. Our foresight turned out to be 
very useful, as those changes were trivial to make and maintained the behavior from part 2
while integrating with the new Controller implementations seamlessly. 

<a id="org5857e2f"></a>

# Quick start

A good way to get familiarized with the codebase is through selected reading.
Specifically, the Summarizers offer a good introduction to the coordinate
systems present all over the codebase and encompass fundamental operations
to gameplay.

Also suggested is a UML diagram, which can be generated by
IntelliJ IDEA Ultimate. A pre-generated diagram can be found in the root
directory of the project.

Here is an example usage, which demonstrates how to use the HexPlane, View,
and Summarizer components. It prints the Model to stdout.

```java
public class Main {
  public static void renderToStdOut() {
    // create a new ReversiTextView, which will output the game board
    // managed by ReversiModel to stdout.
    ReversiView view = new ReversiTextView(System.out);
    // create a new HexPlane with radius 5, which is the side length
    // and the distance from the center to the outside edge
    HexPlane<Player> plane = new HexPlane<>(5);
    // iterate over the plane, setting various positions to various values
    for (int q = -2; q < 2; q++) {
      for (int r = -2; r < 2; r++) {
        if (r > 0 && q < 0) {
          plane.setElementAt(new AxialCoordinate(q, r), Player.PLAYER1);
        } else {
          plane.setElementAt(new AxialCoordinate(q, r), Player.PLAYER2);
        }
      }
    }
    // render the current state of the board, which should be empty
    view.render();
    /* Expected output to stdout:
             _ _ _ _ _
            _ _ _ _ _ _
           _ _ _ _ _ _ _
          _ _ _ _ _ _ _ _
         _ _ _ _ _ _ _ _ _
          _ _ _ _ _ _ _ _
           _ _ _ _ _ _ _
            _ _ _ _ _ _
             _ _ _ _ _
    */
    // update the view with the modified plane
    view.setPlane(plane);
    view.render();
    /* Expected output to stdout:
             _ _ _ _ _
            _ _ _ _ _ _
           O O O O _ _ _
          _ O O O O _ _ _
         _ _ O O O O _ _ _
          _ _ X X O O _ _
           _ _ _ _ _ _ _
            _ _ _ _ _ _
             _ _ _ _ _
    */
    // Use a Summarizer to find out the number of points that would be made
    // by a move at a given point
    int score = new PointsFromMoveAtPointSummarizer(
        new AxialCoordinate(-1, -3),
        Player.PLAYER1).summarize(plane);
    /* The move is being made here (represented by the χ):
             _ _ _ _ _
        --> χ _ _ _ _ _
           O O O O _ _ _
          _ O O O O _ _ _
         _ _ O O O O _ _ _
          _ _ X X O O _ _
           _ _ _ _ _ _ _
            _ _ _ _ _ _
             _ _ _ _ _
       Which can be seen to be worth 4 points, with 1 from the move itself and
       3 from flipping the Os between the χ and X (at q=2, r=-1). Because
       the move hasn't been made (just summarized), the board is unchanged and
       pieces unflipped.
    */
    System.out.println(score);
    /* Expected output to stdout:
       4
    */
  }
}
```

<a id="keybindings"></a>

# Keybindings
- `p` - pass your turn
- `h` - show possible moves - does not work with provider's view
- `ENTER` - commit selection and make a move

  <a id="Commands"></a>

# Commands (TTY only)
- `move q r` - move at the specified location, where `q` and `r` are axis of axial coordinates
- `pass` - pass your turn
- `show-moves [p1 | p2]` - show the potential moves for a particular player

  <a id="CLI"></a>

# Command Line Arguments
Arguments appear *in this order*. Client means a human is playing.
## Required
- Player 1 difficulty
  - easy
  - medium
  - hard
  - providerHard
  - providerVeryHard
  - client
- Player 2 difficulty
  - easy
  - medium
  - hard
  - providerHard
  - providerVeryHard
  - client
- View mode
  - tui
  - gui
  - providerGui
- Board size (natural number)
## Optional
- First player
  - X (black)
  - O (white)

<a id="org0115636"></a>

# Key components

*Click on the name of the component to navigate to its source.*


<a id="org1bf86fb"></a>

## [Controller](src/reversi/controller/ReversiController.java)

<a id="org9345504"></a>
The Controller is the crux of the machinery driving the game of Reversi.
It arbitrates between the Model and the View while managing necessary
subcomponents (e.g. the Agents). Depending on its agents, the Controller may
be synchronous or asynchronous. It implements the Observer pattern via the
CommandCallback it provides to the classes it overlooks.


<a id="org8b0201a"></a>

## [Model](src/reversi/model/MutableReversiModel.java)

<a id="orgc8ab319"></a>
Driven by the Controller. Supports games of any positive size. If the radius
is 1, the initial board configuration (see [the doc comment](src/reversi/model/ReversiModel.java)
for `reversi.model.ReversiModel`&rsquo;s constructor).

<a id="org5648727"></a>
The Model is fairly &ldquo;dumb&rdquo;; it simply does what it&rsquo;s told or throws an
exception. The most advanced machinery available to the Model are
Summarizers. Summarizers calculate various things about the Model and the
state of its HexPlane, including (but not limited to) the set of points that
would be flipped if a move were made at a given point. Summarizers are
also used by the Controller to determine the legality of a move.
Summarizers are not given mutable access to the Model, but rather are
given a copy of the Model&rsquo;s HexPlane.


<a id="org5bef0d3"></a>

## [View](src/reversi/view/ReversiView.java)

<a id="org13046af"></a>
Driven by the Controller. The View is responsible for displaying the current
state of the game to the user, where the current state is dictated by the
Controller. The View may also receive callbacks from the Controller to
forward asynchronous events.


<a id="orgbdc7145"></a>

# Key subcomponents

*Click on the name of the component to navigate to its source.*


<a id="orgeb76263"></a>

## [Agents](src/reversi/controller/agent/Agent.java)

<a id="orgf60527c"></a>
Agents represent the players. They can represent humans or AI
implementations. AI Agents will sort their most preferred moves in a list from
most preferred to least preferred. They can be composed using the AgentBuilder. 
Human Agents will either wait for human input (if the Controller is synchronous) 
or will return an empty command should no action be available at the time 
(and if the Controller is asynchronous). Synchronous agents (e.g. the human textual input) 
will typically block waiting for user input.


<a id="orgf22b5aa"></a>

## Commands ([Model](src/reversi/command/ModelCommand.java) & [View](src/reversi/command/ViewCommand.java))

<a id="orga51ee8f"></a>
Commands are ubiquitous across the project. They are often used as the
actions performed by a player or AI, or simply represent some compound
operation (e.g. a macro). Both the Model and the View have their version of
Commands, which can be used in tandem with the 
[UnifiedCommand](src/reversi/command/UnifiedCommand.java). A UnifiedCommand holds one or both
types of Command for dispatch to the Model and View by the Controller.

Commands can be created through the [CommandAggregator](src/reversi/command/CommandAggregator.java).
Commands that wish to be discoverable via help messages (like the one printed when an invalid 
command is entered) must register themselves using a static initialization block.
The CommandAggregator will ensure the ClassLoader does not ignore these blocks.
If the commands are not in the `reversi.command.commands` package, it is up to the programmer to
ensure the command is correctly registered in the CommandAggregator.


<a id="org488b6dd"></a>

## [HexPlane](src/reversi/hex/plane/HexPlane.java)

<a id="org580994e"></a>
The HexPlane is the &ldquo;board&rdquo; of the game. It has a integer radius equal to
its side length. It can be accessed with both Axial and Cubical coordinates.
The HexPlane can be queried about the presence of a value at a given point,
and can accept a value to be put into that position should it be empty.


<a id="org4999441"></a>

### [Coordinates](src/reversi/hex/coordinates/HexPlaneCoord.java) ([Axial](src/reversi/hex/coordinates/AxialCoordinate.java) & [Cubical](src/reversi/hex/coordinates/CubicalCoordinate.java))

<a id="orgfca83ad"></a>
There are two types of coordinates, Axial and Cubical.
Please see [this article](https://www.redblobgames.com/grids/hexagons) for an explanation of these coordinate systems.


<a id="org6ced1bd"></a>

# Source organization

To generate an interactive directory tree, open the readme in Emacs with
org-fstree installed (for Doom, use `(package! org-fstree)` in `packages.el`)
and type `C-c C-c` on the following block:
```orgmode
#+BEGIN_FSTREE ./src
#+END_FSTREE
```
<div class="FSTREE" id="orgf7dcec0">

</div>

Otherwise, refer to the following descriptions:  

## Command
Contains commands related to the View and the Model. Also contains the
CommandAggregator and the CommandFactory. Commands that must be looked up
by other components (usually the Controller or ControllerCallback) will
typically use a CommandIdentifier.

## Controller
Contains the Controller and Agents. See the above section for a description about Agents and their 
purpose. In addition, the ControllerCallback lives in this package. Its purpose is similar to that 
of a Features interface; it allows the Agents and Views to notify the Controller of any events it
may care about.

Note that only a single Controller is instantiated during the game. ControllerCallbacks are what
send and receive messages.

## Exceptions
Exceptions related to the game. The exceptions are checked whenever possible.

## Hex
Anything related to the HexPlane and the coordinate systems. Also contains the
Summarizers, which are critically important for the function of the game.
### Summarizer
Contains the Summarizers necessary for game function. The strategy subfolder
contains summarizers used by the Agents. The reversiInfo subfolder contains most of the
game state summarizers.

## Model
Contains the Model and its companion classes. Notably, it contains both the ReadOnlyReversiModel
and the MutableReversiModel, as well as the concrete implementation.

## View
Contains the View and its companion classes. Notably, it contains both the textual and graphical
views, as well as any dependent classes. New views are recommended to extend AbstractReversiView
or use it as a reference for how the basic functionality is expected to behave.