package rb;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class RBTree<K, V>{
  enum Color {
    Red, Black
  }

  private final Head<K, V> head;

  public RBTree(Comparator<K> comp) {
    this.head = new Head<>(comp);
  }

  public boolean ins(K k, V v) {
    return head.insert(k, v);
  }

  public Optional<V> search(K k) {
    return head.search(k).map(Node::getV);
  }

  public Optional<K> min() {
    return head.min().map(Node::getK);
  }

  public Optional<K> max() {
    return head.max().map(Node::getK);
  }

  public Optional<K> pred(K k) {
    return head.pred(k).map(Node::getK);
  }

  public Optional<K> succ(K k) {
    return head.succ(k).map(Node::getK);
  }

  public Optional<V> del(K k) {
    return head.del(k);
  }

  public String toString() {
    return head.toString();
  }

  public List<V> sort() {
    return head.sort();
  }
}
