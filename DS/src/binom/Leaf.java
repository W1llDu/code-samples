package binom;

import java.util.Optional;

class Leaf<K, V> implements Child<K, V> {

  @Override
  public Optional<Node<K, V>> find(K k) {
    return Optional.empty();
  }

  @Override
  public Optional<Node<K, V>> min() {
    return Optional.empty();
  }

  @Override
  public Child<K, V> merge(Child<K, V> child) {
    return child;
  }

  @Override
  public Child<K, V> mergeNode(Node<K, V> node) {
    return node;
  }

  @Override
  public Child<K, V> reverse(Child<K, V> acc) {
    return acc;
  }

  @Override
  public String asString(String s) {
    return "";
  }

}
