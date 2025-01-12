package binom;

import java.util.Comparator;
import java.util.Optional;
import java.util.function.Function;

class Head<K, V> extends Parent<K, V> {

  Head(Comparator<K> comp) {
    super(comp);
  }

  @Override
  protected void swap(Node<K, V> down, K nk, V v) {
    throw new IllegalStateException("unreachable");
  }

  @Override
  void raise(Node<K, V> down, K k, V v) {
    throw new IllegalStateException("unreachable");
  }

  void insert(K k, V v) {
    Node<K, V> newNode = new Node<>(comp, new Root<>(comp), 0, k, v);
    Head<K, V> newHead = new Head<>(comp);
    newHead.right = newNode;
    right = this.union(newHead).right;
  }

  Optional<Node<K, V>> find(K k) {
    return right.find(k);
  }

  Optional<Node<K, V>> min() {
    return right.min();
  }

  @Override
  public String toString() {
    return "head\n" + right.asString("");
  }

  Head<K,V> union(Head<K, V> other) {
    Head<K, V> newHead = new Head<>(comp);
    newHead.right = this.right.merge(other.right);
    if (newHead.right instanceof Leaf) {
      return newHead;
    }
    assert newHead.right instanceof Node;
    ((Node<K, V>) newHead.right).combine(newHead);
    return newHead;
  }

  Optional<V> eMin() {
    if (right instanceof Leaf) {
      return Optional.empty();
    }
    assert right.min().isPresent();
    Node<K, V> min = right.min().get();
    Parent<K, V> pred = this;
    while (pred.right instanceof Node) {
      if (pred.right == min) {
        break;
      }
      pred = (Node<K, V>) pred.right;
    }
    pred.right = min.right;
    Head<K, V> temp = new Head<>(comp);
    temp.right = min.down.reverse(new Leaf<>());
    right = this.union(temp).right;
    return Optional.of(min.v);
  }
}
