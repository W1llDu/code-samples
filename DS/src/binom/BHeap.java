package binom;

import java.util.Comparator;
import java.util.Optional;

public class BHeap<K, V> {
  private final Head<K, V> head;

  public BHeap(Comparator<K> comp) {
    this.head = new Head<>(comp);
  }

  private BHeap(Head<K, V> head) {
    this.head = head;
  }

  public void ins(K k, V v) {
    head.insert(k, v);
  }

  public Optional<V> find(K k) {
    return head.find(k).map(Node::getV);
  }

  public Optional<V> min() {
    return head.min().map(Node::getV);
  }

  public Optional<V> eMin() {
    return head.eMin();
  }

  public Optional<V> del(K k) {
    Optional<Node<K, V>> node = head.find(k);
    if (node.isEmpty()) return Optional.empty();
    node.get().up();
    Parent<K, V> pred = this.head;
    while (pred.right instanceof Node) {
      if (head.comp.compare(((Node<K, V>) pred.right).k, k) == 0) {
        node = Optional.of((Node<K, V>) pred.right);
        break;
      }
      pred = (Node<K, V>) pred.right;
    }

    pred.right = node.get().right;
    Head<K, V> temp = new Head<>(this.head.comp);
    temp.right = node.get().down.reverse(new Leaf<>());
    this.head.right = this.head.union(temp).right;
    return Optional.of(node.get().v);
  }

  public void dec(K k, K nk) {
    head.find(k).ifPresent(n -> n.dec(nk));
  }

  public BHeap<K, V> union(BHeap<K, V> other) {
    return new BHeap<>(head.union(other.head));
  }

  public String toString() {
    return head.toString();
  }
}
