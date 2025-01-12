package rb;

interface Parent<K, V> {
  void replaceChild(Child<K, V> oldChild, Child<K, V> newChild);
}
