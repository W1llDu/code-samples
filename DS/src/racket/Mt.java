package racket;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Mt<T> implements RList<T> {
  @Override
  public Optional<T> find(Predicate<T> pred) {
    return Optional.empty();
  }

  @Override
  public void addAllTo(List<T> tlist) {
  }

  @Override
  public RList<T> remove(Predicate<T> pred) {
    return new Mt<>();
  }

  @Override
  public int length() {
    return 0;
  }
}
