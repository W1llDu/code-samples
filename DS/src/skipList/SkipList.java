package skipList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SkipList<K, V> {
  private final Head<K, V> head;

  public SkipList(Comparator<K> comp) {
    this.head = new Head<>(comp, new Tail<>());
  }

  public boolean ins(K k, V v) {
    List<Boolean> flips = new ArrayList<>();
    Random random = new Random();
    while (random.nextBoolean()) {
      flips.add(true);
    }
    flips.add(false);
    boolean[] arr = new boolean[flips.size()];
    for (int i = 0; i < flips.size(); i++) arr[i] = flips.get(i);
    return ins(k, v, arr);
  }

  public boolean ins(K k, V v, boolean... flips) {
    int heads = 0;
    for (boolean flip : flips) {
      if (flip) {
        heads++;
      } else {
        break;
      }
    }
    if (heads == flips.length) {
      throw new IllegalArgumentException("No tails given.");
    }
    return head.ins(k, v, heads + 1);
  }

  public Optional<V> find(K k) {
    return head.find(k).map(Node::getV);
  }

  public Optional<V> del(K k) {
    return head.del(k);
  }

  public String toString() {
    return head.toString();
  }
}
