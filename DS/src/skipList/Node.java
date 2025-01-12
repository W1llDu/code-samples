package skipList;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class Node<K, V> extends Predecessor<K, V> implements Successor<K, V> {
  private final K k;
  private V v;

  Node(Comparator<K> comp, K k, V v, Head<K, V> head, Tail<K, V> tail) {
    super(comp, tail);
    this.k = Objects.requireNonNull(k);
    this.v = Objects.requireNonNull(v);
    this.head = head;
  }

  public V getV() {
    return v;
  }

  @Override
  public Optional<Node<K, V>> find(K k) {
    int res = compare(k);
    if (res == 0) return Optional.of(this);
    return super.find(k);
  }

  @Override
  public void del(LinkedList<Predecessor<K, V>> predecessors) {
    for (int i = 0; i < successors.size(); i++) {
      predecessors.get(i).successors.set(i, successors.get(i));
    }
  }

  @Override
  public int compare(K k) {
    return comp.compare(k, this.k);
  }

  @Override
  public void addToString(List<StringBuilder> sbs) {
    String kv = k + ":" + v;
    if (successors.get(0) instanceof Node) kv += " ";
    sbs.get(0).append(kv);
    for (int i = 1; i < successors.size() - 1; i++) {
      sbs.get(i).append("─".repeat(k.toString().length()))
          .append("┼")
          .append("─".repeat(v.toString().length()))
          .append("─");
    }
    if (successors.size() > 1) {
      sbs.get(successors.size() - 1).append("─".repeat(k.toString().length()))
          .append("┬")
          .append("─".repeat(v.toString().length()))
          .append("─");
    }
    for (int i = successors.size(); i < sbs.size(); i++) {
      sbs.get(i).append("─".repeat(kv.length()));
    }
    successors.get(0).addToString(sbs);
  }

  void setV(V v) {
    this.v = Objects.requireNonNull(v);
  }

  K getK() {
    return k;
  }
}
