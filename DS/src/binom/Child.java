package binom;

import java.util.Optional;

interface Child<K, V> {

  Optional<Node<K, V>> find(K k);
  Optional<Node<K, V>> min();
  Child<K, V> merge(Child<K, V> child);
  Child<K, V> mergeNode(Node<K, V> node);
  Child<K, V> reverse(Child<K, V> acc);

  String asString(String s);
}
