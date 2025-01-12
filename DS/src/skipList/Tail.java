package skipList;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

class Tail<K, V> implements Successor<K,V> {

  @Override
  public void ins(K k, V v, int depth, List<Predecessor<K, V>> predecessors) {
    throw new IllegalStateException("Unreachable");
  }

  @Override
  public Optional<Node<K, V>> find(K k) {
    throw new IllegalStateException("Unreachable");
  }

  @Override
  public void del(K k, LinkedList<Predecessor<K, V>> predecessors) {
    throw new IllegalStateException("Unreachable");
  }

  @Override
  public void del(LinkedList<Predecessor<K, V>> predecessors) {
    throw new IllegalStateException("Unreachable");
  }

  @Override
  public int compare(K k) {
    return -1;
  }

  @Override
  public void addToString(List<StringBuilder> sbs) {

  }
}
