package skipList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

abstract class Predecessor<K, V> {
  protected final Comparator<K> comp;
  protected final List<Successor<K, V>> successors;
  protected Head<K, V> head;
  protected final Tail<K, V> tail;

  Predecessor(Comparator<K> comp, Tail<K, V> tail) {
    this.comp = Objects.requireNonNull(comp);
    this.successors = new ArrayList<>();
    this.tail = Objects.requireNonNull(tail);
  }

  public void ins(K k, V v, int depth, List<Predecessor<K, V>> predecessors) {
    for (int i = successors.size() - 1; i >= 0; i--) {
      Successor<K, V> successor = successors.get(i);
      int res = successor.compare(k);
      if (res > 0) {
        successor.ins(k, v, depth, predecessors);
        return;
      }
      predecessors.add(0, this);
    }
    Node<K, V> node = new Node<>(comp, k, v, head, tail);
    int n = Math.min(predecessors.size(), depth);
    for (int i = 0; i < n; i++) {
      node.successors.add(predecessors.get(i).successors.get(i));
      predecessors.get(i).successors.set(i, node);
    }
    for (int i = n; i < depth; i++) {
      node.successors.add(tail);
      head.successors.add(node);
    }
  }

  protected Optional<Node<K, V>> find(K k) {
    for (int i = successors.size() - 1; i >= 0; i--) {
      Successor<K, V> successor = successors.get(i);
      int res = successor.compare(k);
      if (res >= 0) {
        if (this instanceof Node) System.out.println(((Node<K, V>) this).getK());
        return successor.find(k);
      }
    }
    return Optional.empty();
  }

  public void del(K k, LinkedList<Predecessor<K, V>> predecessors) {
    for (int i = successors.size() - 1; i >= 0; i--) {
      Successor<K, V> successor = successors.get(i);
      int res = successor.compare(k);
      if (res > 0) {
        successor.del(k, predecessors);
        return;
      }
      predecessors.add(0, this);
    }
    successors.get(0).del(predecessors);
  }
}
