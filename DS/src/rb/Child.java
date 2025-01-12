package rb;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

abstract class Child<K, V> {
  protected final Comparator<K> comp;
  protected Parent<K, V> parent;
  RBTree.Color color;

  Child(Comparator<K> comp, Parent<K, V> parent, RBTree.Color color) {
    this.comp = Objects.requireNonNull(comp);
    this.parent = Objects.requireNonNull(parent);
    this.color = color;
  }

  abstract boolean insert(K k, V v);

  abstract Optional<Node<K, V>> search(K k);

  abstract Optional<Node<K, V>> largest();

  abstract Optional<Node<K, V>> smallest();

  abstract String asString(String acc);

  abstract Optional<Node<K, V>> pred(K k);

  abstract Optional<Node<K, V>> succ(K k);

  abstract List<V> sort();

  abstract Optional<Node<K, V>> option();

  void fixup() {
    if (this.parent instanceof Node && this.color == RBTree.Color.Black) {
      if (this == ((Node<K, V>) this.parent).left) {
        Child<K, V> w = ((Node<K, V>) this.parent).right;
        if (w.color == RBTree.Color.Red) {
          w.color = RBTree.Color.Black;
          ((Node<K, V>) this.parent).color = RBTree.Color.Red;
          ((Node<K, V>) this.parent).lRotate();
          w = ((Node<K, V>) this.parent).right;
        }
        assert w instanceof Node;
        if (((Node<K, V>) w).left.color == RBTree.Color.Black
            && ((Node<K, V>) w).right.color == RBTree.Color.Black) {
          w.color = RBTree.Color.Red;
          ((Node<K, V>) this.parent).fixup();
        } else {
          if (((Node<K, V>) w).right.color == RBTree.Color.Black) {
            ((Node<K, V>) w).left.color = RBTree.Color.Black;
            w.color = RBTree.Color.Red;
            ((Node<K, V>) w).rRotate();
            w = ((Node<K, V>) this.parent).right;
          }
          w.color = ((Node<K, V>) this.parent).color;
          ((Node<K, V>) this.parent).color = RBTree.Color.Black;
          ((Node<K, V>) w).right.color = RBTree.Color.Black;
          ((Node<K, V>) this.parent).lRotate();
          Node<K, V> temp = (Node<K, V>) w;
          while (temp.parent instanceof Node) {
            temp = (Node<K, V>) temp.parent;
          }
          temp.fixup();
        }
      } else {
        Child<K, V> w = ((Node<K, V>) this.parent).left;
        if (w.color == RBTree.Color.Red) {
          w.color = RBTree.Color.Black;
          ((Node<K, V>) this.parent).color = RBTree.Color.Red;
          ((Node<K, V>) this.parent).rRotate();
          w = ((Node<K, V>) this.parent).left;
        }
        assert w instanceof Node;
        if (((Node<K, V>) w).left.color == RBTree.Color.Black
            && ((Node<K, V>) w).right.color == RBTree.Color.Black) {
          w.color = RBTree.Color.Red;
          ((Node<K, V>) this.parent).fixup();
        } else {
          if (((Node<K, V>) w).left.color == RBTree.Color.Black) {
            ((Node<K, V>) w).right.color = RBTree.Color.Black;
            w.color = RBTree.Color.Red;
            ((Node<K, V>) w).lRotate();
            w = ((Node<K, V>) this.parent).left;
          }
          w.color = ((Node<K, V>) this.parent).color;
          ((Node<K, V>) this.parent).color = RBTree.Color.Black;
          ((Node<K, V>) w).left.color = RBTree.Color.Black;
          ((Node<K, V>) this.parent).rRotate();
          Node<K, V> temp = (Node<K, V>) w;
          while (temp.parent instanceof Node) {
            temp = (Node<K, V>) temp.parent;
          }
          temp.fixup();
        }
      }
    } else {
      this.color = RBTree.Color.Black;
    }
  }
}
