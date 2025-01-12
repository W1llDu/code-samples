package rb;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

class Head<K, V> implements Parent<K, V> {
  private Child<K, V> child;

  Head(Comparator<K> comp) {
    this.child = new Leaf<K, V>(comp, this);
  }

  boolean insert(K k, V v) {
    return child.insert(k, v);
  }

  Optional<Node<K, V>> search(K k) {
    return child.search(k);
  }

  Optional<V> del(K k) {
    Optional<Node<K, V>> toDel = child.search(k);
    if (toDel.isEmpty()) return Optional.empty();
    toDel.get().del();
    return Optional.of(toDel.get().getV());
  }

  @Override
  public void replaceChild(Child<K, V> oldChild, Child<K, V> newChild) {
    if (child != oldChild) throw new IllegalArgumentException("Must give valid child");
    child = newChild;
    newChild.parent = this;
  }

  @Override
  public String toString() {
    return child.asString("");
  }

  public Optional<Node<K, V>> min() {
    return child.smallest();
  }

  public Optional<Node<K, V>> max() {
    return child.largest();
  }

  public Optional<Node<K, V>> pred(K k) {
    return child.pred(k);
  }

  public Optional<Node<K, V>> succ(K k) {
    return child.succ(k);
  }

  public List<V> sort() {
    return child.sort();
  }
}
