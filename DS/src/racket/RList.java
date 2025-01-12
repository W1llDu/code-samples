package racket;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface RList<T> {

  Optional<T> find(Predicate<T> pred);

  void addAllTo(List<T> tlist);

  RList<T> remove(Predicate<T> pred);

  int length();
}
