package binom;

import java.util.Comparator;

class Root<K, V> extends Parent<K, V> {

  Root(Comparator<K> comp) {
    super(comp);
  }

  @Override
  protected void swap(Node<K, V> down, K nk, V v) {
    down.k = nk;
    down.v = v;
  }

  @Override
  void raise(Node<K, V> down, K k, V v) {
    down.k = k;
    down.v = v;
  }
}
