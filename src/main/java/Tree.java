import java.util.List;

public interface Tree<K, V> {

	int size();
	void clear();
	boolean add(K key, V value);
	boolean contains(K key);
	V get(K key);
	V remove(K key);

	List<Entry<K, V>> preorder();
	List<Entry<K, V>> inorder();
	List<Entry<K, V>> postorder();
	List<Entry<K, V>> levelOrder();

	class Entry<K, V> {
		K key;
		V value;

		public Entry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		public K getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "(" + key + ", " + value + ")";
		}
	}
}
