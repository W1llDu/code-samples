package binom;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BDemo {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    BHeap<Integer, Integer> bh = new BHeap<>(Integer::compareTo);
    while (sc.hasNext()) {
      String cmd = sc.next();
      switch (cmd) {
        case "ins":
          int a = sc.nextInt();
          bh.ins(a, a);
          break;
        case "find":
          int b = sc.nextInt();
          bh.find(b).ifPresentOrElse(System.out::println,
              () -> System.out.println(false));
          break;
        case "del":
          bh.del(sc.nextInt()).ifPresentOrElse(System.out::println,
              () -> System.out.println(false));
          break;
        case "dec":
          bh.dec(sc.nextInt(), sc.nextInt());
          break;
        case "min":
          bh.min().ifPresentOrElse(System.out::println,
              () -> System.out.println(false));
          break;
        case "emin":
          bh.eMin().ifPresentOrElse(System.out::println,
              () -> System.out.println(false));
          break;
        case "file":
          try {
            BufferedReader r = new BufferedReader(new FileReader(sc.next()));
            for (String line : r.lines().collect(Collectors.toList())) {
              Scanner l = new Scanner(line);
              while (l.hasNext()) {
                int c = l.nextInt();
                bh.ins(c, c);
                System.out.println(c);
              }
            }
          } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
          }
          break;
        case "print":
          System.out.println(bh);
          break;
        case "reset":
          bh = new BHeap<>(Integer::compare);
          break;
        default:
          System.out.println("Invalid command");
      }
    }
  }
}
