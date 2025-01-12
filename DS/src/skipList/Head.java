package skipList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class Head<K, V> extends Predecessor<K, V> {

  Head(Comparator<K> comp, Tail<K, V> tail) {
    super(comp, tail);
    this.head = this;
    successors.add(tail);
  }

  boolean ins(K k, V v, int depth) {
    Optional<Node<K, V>> node = find(k);
    if (node.isEmpty()) {
      super.ins(k, v, depth, new LinkedList<>());
      return true;
    } else {
      node.get().setV(v);
      return false;
    }
  }

  public Optional<V> del(K k) {
    Optional<Node<K, V>> toDel = find(k);
    if (toDel.isEmpty()) return Optional.empty();
    super.del(k, new LinkedList<>());
    return Optional.of(toDel.get().getV());
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    int height = successors.size();
    List<StringBuilder> sbs = new ArrayList<>();
    for (int i = 0; i < height; i++) sbs.add(new StringBuilder());
    successors.get(0).addToString(sbs);
    for (int i = height - 1; i > 0; i--) {
      sb.append(sbs.get(i).toString().stripTrailing()).append('\n');
    }
    sb.append(sbs.get(0));
    return sb.toString();
  }
}
