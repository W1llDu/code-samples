package squareGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import squareGame.controller.SquareGameController;
import squareGame.controller.SquareGameTextualController;
import squareGame.model.SimpleSquareGame;
import squareGame.model.SquareGameModel;

public class Main {

  public static void main(String[] args) throws FileNotFoundException {
    Scanner sc = new Scanner(System.in);
    System.out.println("File (1) or random (2)?");
    SquareGameModel model;
    switch (sc.nextInt()) {
      case 1:
        List<Integer> rows = new ArrayList<>();
        System.out.println("Enter filePath:");
        Scanner file = new Scanner(new File(sc.next()));
        while (file.hasNextInt()) {
          rows.add(file.nextInt());
        }
        model = new SimpleSquareGame(rows);
        break;
      case 2:
        System.out.println("Enter rows and maxLength:");
        model = new SimpleSquareGame(sc.nextInt(),
                sc.nextInt());
        break;
      default:
        throw new IllegalStateException();
    }
    String p1, p2;
    System.out.println("human vs human (1), human vs ai (2), or ai vs ai (3)?");
    switch (sc.nextInt()) {
      case 1:
        p1 = "human";
        p2 = "human";
        break;
      case 2:
        for (int row : model.getRows()) {
          System.out.println("[] ".repeat(row).substring(0, Math.max(0, row * 3 - 1)));
        }
        System.out.println("Do you want to go first (1) or second (2)?");
        switch (getTurn()) {
          case "1":
            p1 = "human";
            p2 = "ai";
            break;
          case "2":
            p1 = "ai";
            p2 = "human";
            break;
          default:
            throw new IllegalStateException();
          }
        break;
      case 3:
        p1 = "ai";
        p2 = "computer";
        break;
      default:
        throw new IllegalStateException();
    }
    try {
      SquareGameController controller =
              new SquareGameTextualController(new InputStreamReader(System.in),
                      System.out);
      controller.playGame(model, p1, p2);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String getTurn() {
    Scanner sc = new Scanner(System.in);
    String result;
    do {
      result = sc.next();
      if (List.of("1", "2").contains(result)) {
        return result;
      }
    } while (sc.hasNext());
    throw new NoSuchElementException();
  }
}
