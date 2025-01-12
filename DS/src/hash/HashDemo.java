package hash;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class HashDemo {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.println("Enter number of buckets");
    HashTable ht = new HashTable(sc.nextInt());
    while (sc.hasNext()) {
      String cmd = sc.next();
      switch (cmd) {
        case "ins":
          ht.ins(sc.next(), sc.nextInt());
          break;
        case "inc":
          ht.inc(sc.next());
          break;
        case "find":
        {
          String s = sc.next();
          if (ht.contains(s)) {
            System.out.println(ht.find(s));
          } else {
            System.out.println("Key not present in table");
          }
          break;
        }
        case "del":
          ht.del(sc.next());
          break;
        case "keys":
          System.out.println(ht.keys());
          break;
        case "file":
          try {
            BufferedReader r = new BufferedReader(new FileReader(sc.next()));
            for (String line : r.lines().collect(Collectors.toList())) {
              Scanner l = new Scanner(line);
              while (l.hasNext()) {
                String s = l.next();
                if (!ht.contains(s)) {
                  ht.ins(s, 0);
                }
                ht.inc(s);
                System.out.println(s);
              }
            }
          } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
          }
          break;
        case "print":
          System.out.println(ht);
          break;
        case "len":
          System.out.println(ht.lengths());
          break;
        case "hist":
          List<Integer> lengths = ht.lengths();
          int m = lengths.stream().max(Integer::compare).get();
          int i = 0;
          boolean seen = false;
          while (i <= m) {
            int c = Collections.frequency(lengths, i);
            if (c != 0) {
              seen = true;
            }
            if (seen) {
              System.out.println(i + ": " + "*".repeat(c));
            }
            i++;
          }
          double a = lengths.stream().reduce(0, Integer::sum) / (double) lengths.size();
          System.out.println(lengths);
          System.out.println(a);
          System.out.println(lengths.stream().map(x -> (x - a) * (x - a))
              .reduce(0.0, Double::sum) / (lengths.size() - 1.0));
          break;
        case "reset":
          ht = new HashTable(sc.nextInt());
          break;
        default:
          System.out.println("Invalid command");
      }
    }
  }
}
