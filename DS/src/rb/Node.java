package rb;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

class Node<K, V> extends Child<K, V> implements Parent<K, V> {
  private K k;
  private V v;
  Child<K, V> left;
  Child<K, V> right;

  Node(Comparator<K> comp, Parent<K, V> parent, K k, V v, RBTree.Color color) {
    super(comp, parent, color);
    this.k = Objects.requireNonNull(k);
    this.v = Objects.requireNonNull(v);
    this.left = new Leaf<K, V>(comp, this);
    this.right = new Leaf<K, V>(comp, this);
  }

  K getK() {
    return k;
  }

  V getV() {
    return v;
  }

  @Override
  boolean insert(K k, V v) {
    int res = comp.compare(k, this.k);
    if (res == 0) {
      this.k = k;
      this.v = v;
      return false;
    }
    else if (res > 0) return right.insert(k, v);
    else return left.insert(k, v);
  }

  @Override
  Optional<Node<K, V>> search(K k) {
    int res = comp.compare(k, this.k);
    if (res == 0) {
      return this.option();
    }
    else if (res > 0) return right.search(k);
    else return left.search(k);
  }

  @Override
  Optional<Node<K, V>> largest() {
    Optional<Node<K, V>> ret = right.largest();
    if (ret.isPresent()) return ret;
    else return this.option();
  }

  @Override
  Optional<Node<K, V>> smallest() {
    Optional<Node<K, V>> ret = left.smallest();
    if (ret.isPresent()) return ret;
    else return this.option();
  }

  @Override
  public void replaceChild(Child<K, V> oldChild, Child<K, V> newChild) {
    if (oldChild == left) {
      left = newChild;
      newChild.parent = this;
      return;
    }
    else if (oldChild == right) {
      right = newChild;
      newChild.parent = this;
      return;
    }
    throw new IllegalArgumentException("Must give valid child");
  }

  public void repair() {
    if (this.parent instanceof Node) {
      if (((Node<K, V>) this.parent).color == RBTree.Color.Red) {
        if (((Node<K, V>) this.parent).parent instanceof Node) {
          if (this.parent == ((Node<K, V>) ((Node<K, V>) this.parent).parent).left) {
            Child<K, V> y = ((Node<K, V>) ((Node<K, V>) this.parent).parent).right;
            if (y.color == RBTree.Color.Red) {
              ((Node<K, V>) this.parent).color = RBTree.Color.Black;
              y.color = RBTree.Color.Black;
              ((Node<K, V>) ((Node<K, V>) this.parent).parent).color = RBTree.Color.Red;
              ((Node<K, V>) ((Node<K, V>) this.parent).parent).repair();
            } else {
              if (this == ((Node<K, V>) this.parent).right) {
                Node<K, V> temp = (Node<K, V>) this.parent;
                ((Node<K, V>) this.parent).lRotate();
                temp.repair();
              } else {
                ((Node<K, V>) this.parent).color = RBTree.Color.Black;
                ((Node<K, V>) ((Node<K, V>) this.parent).parent).color = RBTree.Color.Red;
                ((Node<K, V>) ((Node<K, V>) this.parent).parent).rRotate();
              }
            }
          } else {
            if (((Node<K, V>) this.parent).parent instanceof Node) {
              Child<K, V> y = ((Node<K, V>) ((Node<K, V>) this.parent).parent).left;
              if (y.color == RBTree.Color.Red) {
                ((Node<K, V>) this.parent).color = RBTree.Color.Black;
                y.color = RBTree.Color.Black;
                ((Node<K, V>) ((Node<K, V>) this.parent).parent).color = RBTree.Color.Red;
                ((Node<K, V>) ((Node<K, V>) this.parent).parent).repair();
              } else {
                if (this == ((Node<K, V>) this.parent).left) {
                  Node<K, V> temp = (Node<K, V>) this.parent;
                  ((Node<K, V>) this.parent).rRotate();
                  temp.repair();
                } else {
                  ((Node<K, V>) this.parent).color = RBTree.Color.Black;
                  ((Node<K, V>) ((Node<K, V>) this.parent).parent).color = RBTree.Color.Red;
                  ((Node<K, V>) ((Node<K, V>) this.parent).parent).lRotate();
                }
              }
            }
          }
        }
      }
    } else {
      this.color = RBTree.Color.Black;
    }
  }

  void del() {
    Node<K, V> y = this;
    RBTree.Color c = this.color;
    Child<K, V> x;
    if (this.left.option().isEmpty()) {
      x = this.right;
      this.parent.replaceChild(this, this.right);
    } else if (this.right.option().isEmpty()) {
      x = this.left;
      this.parent.replaceChild(this, this.left);
    } else {
      assert this.right.smallest().isPresent();
      y = this.right.smallest().get();
      c = y.color;
      x = y.right;
      if (y != this.right) {
        y.parent.replaceChild(y, y.right);
        y.replaceChild(y.right, this.right);
      } else {
        x.parent = y;
      }
      this.parent.replaceChild(this, y);
      y.replaceChild(y.left, this.left);
      c = this.color;
    }
    if (c == RBTree.Color.Black) x.fixup();
  }

  @Override
  String asString(String acc) {
    StringBuilder sb = new StringBuilder();
    if (this.color == RBTree.Color.Red) {
      sb.append("\u001B[31m");
    }
    sb.append(k).append(':').append(v);
    if (this.color == RBTree.Color.Red) {
      sb.append("\u001B[0m");
    }
    String r = right.asString(acc + "│  ");
    String l = left.asString(acc + "   ");
    if (right.option().isPresent() || left.option().isPresent()) {
      if (!r.isEmpty()) sb.append('\n').append(acc).append("├──").append(r);
      if (!l.isEmpty()) sb.append('\n').append(acc).append("└──").append(l);
    }
    return sb.toString();
  }

  @Override
  Optional<Node<K, V>> pred(K k) {
    int res = comp.compare(k, this.k);
    if (res == 0) {
      return this.left.option();
    } else if (res > 0) {
      if (this.right.option().isEmpty() || comp.compare(this.right.option().get().k, k) >= 0) {
        return this.option();
      } else {
        return this.right.pred(k);
      }
    }
    else return this.left.pred(k);
  }

  @Override
  Optional<Node<K, V>> succ(K k) {
    int res = comp.compare(k, this.k);
    if (res == 0) {
      return this.right.option();
    } else if (res < 0) {
      if (this.left.option().isEmpty() || comp.compare(this.left.option().get().k, k) <= 0) {
        return this.option();
      } else {
        return this.left.succ(k);
      }
    }
    else return this.right.succ(k);
  }

  @Override
  List<V> sort() {
    List<V> res = this.left.sort();
    res.add(this.v);
    res.addAll(this.right.sort());
    return res;
  }

  @Override
  Optional<Node<K, V>> option() {
    return Optional.of(this);
  }

  boolean lRotate() {
    if (this.right.option().isEmpty()) {
      return false;
    }
    Child<K, V> b = this.right.option().get().left;
    this.parent.replaceChild(this, this.right);
    this.right.option().get().replaceChild(b, this);
    this.replaceChild(this.right, b);
    return true;
  }

  boolean rRotate() {
    if (this.left.option().isEmpty()) {
      return false;
    }
    Child<K, V> b = this.left.option().get().right;
    this.parent.replaceChild(this, this.left);
    this.left.option().get().replaceChild(b, this);
    this.replaceChild(this.left, b);
    return true;
  }
}
