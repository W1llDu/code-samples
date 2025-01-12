package binom;

import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

import rb.RBTree;

class Node<K, V> extends Parent<K, V> implements Child<K, V> {
  K k;
  V v;
  private int degree;
  Parent<K, V> parent;
  Child<K, V> down;

  Node(Comparator<K> comp, Parent<K, V> parent, int degree, K k, V v) {
    super(comp);
    this.parent = parent;
    this.degree = degree;
    this.k = Objects.requireNonNull(k);
    this.v = Objects.requireNonNull(v);
    this.right = new Leaf<>();
    this.down = new Leaf<>();
  }

  V getV() {
    return v;
  }

  void link(Node<K, V> other) {
    parent = other;
    right = other.down;
    other.down = this;
    other.degree++;
  }

  @Override
  public Optional<Node<K, V>> find(K k) {
    if (comp.compare(this.k, k) == 0) {
      return Optional.of(this);
    }
    if (down.find(k).isPresent()) return down.find(k);
    else return right.find(k);
  }

  @Override
  public Optional<Node<K, V>> min() {
    Optional<Node<K, V>> ret = right.min();
    if (ret.isPresent()) {
      if (comp.compare(k, ret.get().k) <= 0) {
        return Optional.of(this);
      } else {
        return ret;
      }
    } else return Optional.of(this);
  }

  @Override
  public Child<K, V> merge(Child<K, V> child) {
    return child.mergeNode(this);
  }

  @Override
  public Child<K, V> mergeNode(Node<K, V> node) {
    if (degree < node.degree) {
      this.right = this.right.mergeNode(node);
      return this;
    } else {
      node.right = node.right.mergeNode(this);
      return node;
    }
  }

  @Override
  public Child<K, V> reverse(Child<K, V> acc) {
    this.parent = new Root<>(comp);
    Child<K, V> rest = right;
    this.right = acc;
    return rest.reverse(this);
  }

  public void combine(Parent<K, V> prev) {
    if (right instanceof Node) {
      if (degree != ((Node<K, V>) right).degree
          || (((Node<K, V>) right).right instanceof Node
          && ((Node<K, V>) ((Node<K, V>) right).right).degree == degree)) {
        ((Node<K, V>) right).combine(this);
      } else {
        if (comp.compare(k, ((Node<K, V>) right).k) <= 0) {
          Node<K, V> temp = (Node<K, V>) this.right;
          this.right = ((Node<K, V>) this.right).right;
          temp.link(this);
          this.combine(prev);
        } else {
          prev.right = right;
          Node<K, V> temp = (Node<K, V>) this.right;
          this.link((Node<K, V>) right);
          temp.combine(prev);
        }
      }
    }
  }

  public String asString(String s) {
    StringBuilder sb = new StringBuilder();
    if (right instanceof Leaf) {
      sb.append(s).append("└──").append(k).append(':').append(v);
      if (down instanceof Node) sb.append("\n").append(down.asString(s + "   "));
    }
    else {
      sb.append(s).append("├──").append(k).append(':').append(v);
      if (down instanceof Node) sb.append("\n").append(down.asString(s + "│  "));
    }
    if (right instanceof Node) sb.append("\n").append(right.asString(s));
    return sb.toString();
  }

  void dec(K nk) {
    if (comp.compare(k, nk) < 0) throw new IllegalArgumentException();
    parent.swap(this, nk, this.v);
  }

  void up() {
    parent.raise(this, k, this.v);
  }

  @Override
  protected void swap(Node<K, V> down, K nk, V v) {
    if (comp.compare(this.k, nk) > 0) {
      down.k = this.k;
      down.v = this.v;
      parent.swap(this, nk, v);
    } else {
      down.k = nk;
      down.v = v;
    }
  }

  @Override
  void raise(Node<K, V> down, K k, V v) {
    down.k = this.k;
    down.v = this.v;
    parent.raise(this, k, v);
  }
}
