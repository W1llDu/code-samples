package rb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import hash.HashTable;

public class RBDemo {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    RBTree<Integer, Integer> rbt = new RBTree<>(Integer::compareTo);
    while (sc.hasNext()) {
      String cmd = sc.next();
      switch (cmd) {
        case "ins":
          int a = sc.nextInt();
          System.out.println(rbt.ins(a, a));
          break;
        case "search":
          int b = sc.nextInt();
          rbt.search(b).ifPresentOrElse(System.out::println,
              () -> System.out.println(false));
          break;
        case "del":
          rbt.del(sc.nextInt()).ifPresentOrElse(System.out::println,
              () -> System.out.println(false));
          break;
        case "min":
          rbt.min().ifPresentOrElse(System.out::println,
              () -> System.out.println(false));
          break;
        case "max":
          rbt.max().ifPresentOrElse(System.out::println,
              () -> System.out.println(false));
          break;
        case "succ":
          rbt.succ(sc.nextInt()).ifPresentOrElse(System.out::println,
              () -> System.out.println(false));
          break;
        case "pred":
          rbt.pred(sc.nextInt()).ifPresentOrElse(System.out::println,
              () -> System.out.println(false));
          break;
        case "file":
          try {
            BufferedReader r = new BufferedReader(new FileReader(sc.next()));
            for (String line : r.lines().collect(Collectors.toList())) {
              Scanner l = new Scanner(line);
              while (l.hasNext()) {
                int c = l.nextInt();
                rbt.ins(c, c);
                System.out.println(c);
              }
            }
          } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
          }
          break;
        case "sort":
          System.out.println(rbt.sort());
          break;
        case "print":
          System.out.println(rbt);
          break;
        case "reset":
          rbt = new RBTree<>(Integer::compareTo);
          break;
        default:
          System.out.println("Invalid command");
      }
    }
  }
}
