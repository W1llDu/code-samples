package skipList;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

interface Successor<K, V> {

  void ins(K k, V v, int depth, List<Predecessor<K, V>> predecessors);

  Optional<Node<K, V>> find(K k);

  int compare(K k);

  void addToString(List<StringBuilder> sbs);

  void del(K k, LinkedList<Predecessor<K, V>> predecessors);

  void del(LinkedList<Predecessor<K, V>> predecessors);
}
