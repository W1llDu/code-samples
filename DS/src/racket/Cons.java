package racket;


import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Cons<T> extends Pair<T, RList<T>> implements RList<T> {
  public Cons(T first, RList<T> rest) {
    super(first, rest);
  }

  @Override
  public Optional<T> find(Predicate<T> pred) {
    if (pred.test(this.x)) {
      return Optional.of(this.x);
    } else {
      return this.y.find(pred);
    }
  }

  @Override
  public void addAllTo(List<T> tlist) {
    tlist.add(this.x);
    this.y.addAllTo(tlist);
  }

  @Override
  public RList<T> remove(Predicate<T> pred) {
    if (pred.test(this.x)) {
      return this.y;
    } else {
      return new Cons<>(this.x, this.y.remove(pred));
    }
  }

  @Override
  public int length() {
    return 1 + this.y.length();
  }
}
