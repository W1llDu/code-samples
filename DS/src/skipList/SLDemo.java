package skipList;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SLDemo {

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    SkipList<Integer, Integer> sl = new SkipList<>(Integer::compare);
    while (sc.hasNext()) {
      String cmd = sc.next();
      System.out.println(cmd);
      switch (cmd) {
        case "ins":
          sl.ins(sc.nextInt(), sc.nextInt());
          System.out.println(sl);
          break;
        case "find":
          System.out.println(sl.find(sc.nextInt()));
          break;
        case "del":
          sl.del(sc.nextInt());
          System.out.println(sl);
          break;
        case "reset":
          sl = new SkipList<>(Integer::compare);
          break;
        case "demo":
          sl = new SkipList<>(Integer::compare);
          testSkipList(sl);
          break;
        case "print":
          System.out.println(sl);
          break;
        case "file":
          try {
            BufferedReader r = new BufferedReader(new FileReader(sc.next()));
            for (String line : r.lines().collect(Collectors.toList())) {
              Scanner l = new Scanner(line);
              while (l.hasNext()) {
                int c = l.nextInt();
                sl.ins(c, c);
                System.out.println(c);
              }
            }
          } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
          }
          break;
        default:
          System.out.println("Invalid command, options are ins, find, del, and file");
      }
    }
  }

  private static void testSkipList(SkipList<Integer, Integer> skipList) {
    skipList.ins(20, 2, true, true, false);
    System.out.println(skipList);
    skipList.ins(40, 4);
    System.out.println(skipList);
    skipList.ins(10, 1);
    System.out.println(skipList);
    System.out.println(skipList.ins(20, 2));
    skipList.ins(5, -5);
    System.out.println(skipList);
    skipList.ins(80, 8);
    System.out.println(skipList);
    skipList.del(20);
    System.out.println(skipList);
    skipList.ins(100, 10);
    System.out.println(skipList);
    skipList.ins(20, 2, true, false);
    System.out.println(skipList);
    skipList.ins(30, 3);
    System.out.println(skipList);
    System.out.println(skipList.del(5));
    System.out.println(skipList.del(5));
    skipList.ins(50, 5);
    System.out.println(skipList);
    System.out.println(skipList.find(80));
  }
}
