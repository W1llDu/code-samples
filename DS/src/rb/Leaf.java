package rb;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class Leaf<K, V> extends Child<K, V> {

  Leaf(Comparator<K> comp, Parent<K, V> parent) {
    super(comp, parent, RBTree.Color.Black);
  }

  @Override
  boolean insert(K k, V v) {
    Node<K, V> newNode = new Node<>(comp, parent, k, v, RBTree.Color.Red);
    parent.replaceChild(this, newNode);
    newNode.repair();
    return true;
  }

  @Override
  Optional<Node<K, V>> search(K k) {
    return Optional.empty();
  }

  @Override
  Optional<Node<K, V>> largest() {
    return Optional.empty();
  }

  @Override
  Optional<Node<K, V>> smallest() {
    return Optional.empty();
  }

  @Override
  String asString(String acc) {
    return "none";
  }

  @Override
  Optional<Node<K, V>> pred(K k) {
    return Optional.empty();
  }

  @Override
  Optional<Node<K, V>> succ(K k) {
    return Optional.empty();
  }

  @Override
  List<V> sort() {
    return new ArrayList<>();
  }

  @Override
  Optional<Node<K, V>> option() {
    return Optional.empty();
  }
}
