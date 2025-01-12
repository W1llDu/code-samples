package hash;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import racket.Cons;
import racket.RList;
import racket.Mt;
import racket.Pair;

public class HashTable {
  private final List<RList<Pair<String, Integer>>> table;
  private final int m;

  public HashTable(int m) {
    this.m = m;
    this.table = new ArrayList<>();
    for (int i = 0; i < m; i++) table.add(new Mt<>());
  }

  public void ins(String k, Integer v) {
    table.get(hash(k, m)).find(s -> s.x.equals(k))
        .ifPresentOrElse(p -> p.y = v,
            () -> table.set(hash(k, m), new Cons<>(new Pair<>(k, v), table.get(hash(k, m)))));
  }

  public void del(String k) {
    table.get(hash(k, m)).find(s -> s.x.equals(k))
        .ifPresent(p -> table.set(hash(k, m), table.get(hash(k, m)).remove(c -> c.x.equals(k))));
  }

  public void inc(String k) {
    table.get(hash(k, m)).find(s -> s.x.equals(k))
        .ifPresent(p -> p.y++);
  }

  public int find(String k) {
    return table.get(hash(k, m)).find(s -> s.x.equals(k))
        .orElseThrow(IllegalArgumentException::new).y;
  }

  public boolean contains(String k) {
    return table.get(hash(k, m)).find(s -> s.x.equals(k)).isPresent();
  }

  public List<String> keys() {
    List<Pair<String, Integer>> keys = new ArrayList<>();
    for (RList<Pair<String, Integer>> column : table) column.addAllTo(keys);
    return keys.stream().map(p -> p.x).collect(Collectors.toList());
  }

  public static int hash(String s, int m) {
    int h = 0xA5A5A5A5;
    int xor = 0x5A5A5A5A;
    for (char c : s.toCharArray()) {
      xor ^= c;
      h += xor * c;
    }
    return (m + (h % m)) % m;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < m; i++) {
      sb.append(i).append(": ").append(table.get(i)).append('\n');
    }
    return sb.toString();
  }

  public List<Integer> lengths() {
    return table.stream().map(l -> l.length()).collect(Collectors.toList());
  }
}
