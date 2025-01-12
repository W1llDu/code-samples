package binom;

import java.util.Comparator;

abstract class Parent<K, V> {
  protected final Comparator<K> comp;
  protected Child<K, V> right;

  Parent(Comparator<K> comp) {
    this.comp = comp;
    right = new Leaf<>();
  }

  abstract void swap(Node<K, V> down, K nk, V v);

  abstract void raise(Node<K, V> down, K k, V v);
}
