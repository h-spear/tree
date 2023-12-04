import java.util.Comparator;
import java.util.Random;

public class Treap<K, V> extends BinarySearchTree<K, V> {

	public Treap() {
		this(null);
	}

	public Treap(Comparator<? super K> comparator) {
		super(comparator);
	}

	@Override
	public boolean add(K key, V value) {
		if (contains(key))
			return false;

		if (comparator != null) {
			root = addUsingComparator(root, new TreapNode<>(key, value), comparator);
		} else {
			root = addUsingComparable(root, new TreapNode<>(key, value));
		}
		return true;
	}

	@Override
	public int size() {
		return ((TreapNode<K, V>) root).size;
	}

	@Override
	public V remove(K key) {
		V oldValue = get(key);
		if (comparator != null) {
			root = deleteUsingComparator(root, key, comparator);
		} else {
			root = deleteUsingComparable(root, key);
		}
		return oldValue;
	}

	private TreeNode<K, V> addUsingComparator(TreeNode<K, V> _root, TreeNode<K, V> _node, Comparator<? super K> comparator) {
		if (_root == null)
			return _node;

		TreapNode<K, V> root = (TreapNode<K, V>) _root;
		TreapNode<K, V> node = (TreapNode<K, V>) _node;

		if (root.priority < node.priority) {
			TreeNodePair<K, V> splited = splitUsingComparator(root, node.key, comparator);
			node.setLeft(splited.first);
			node.setRight(splited.second);
			return node;
		}
		int comp = comparator.compare(root.key, node.key);
		if (comp > 0) {
			root.setLeft(addUsingComparator(root.left, node, comparator));
		} else {
			root.setRight(addUsingComparator(root.right, node, comparator));
		}
		return root;
	}

	private TreeNode<K, V> addUsingComparable(TreeNode<K, V> _root, TreeNode<K, V> _node) {
		if (_root == null)
			return _node;

		TreapNode<K, V> root = (TreapNode<K, V>) _root;
		TreapNode<K, V> node = (TreapNode<K, V>) _node;

		if (root.priority < node.priority) {
			TreeNodePair<K, V> splited = splitUsingComparable(root, node.key);
			node.setLeft(splited.first);
			node.setRight(splited.second);
			return node;
		}
		Comparable<? super K> compKey = (Comparable<? super K>) root.key;
		if (compKey.compareTo(node.key) > 0) {
			root.setLeft(addUsingComparable(root.left, node));
		} else {
			root.setRight(addUsingComparable(root.right, node));
		}
		return root;
	}

	private TreeNodePair<K, V> splitUsingComparator(TreeNode<K, V> root, K key, Comparator<? super K> comparator) {
		if (root == null)
			return new TreeNodePair<>(null, null);

		int comp = comparator.compare(root.key, key);
		if (comp < 0) {
			TreeNodePair<K, V> rs = splitUsingComparator(root.right, key, comparator);
			root.setRight(rs.first);
			return new TreeNodePair<>(root, rs.second);
		} else {
			TreeNodePair<K, V> ls = splitUsingComparator(root.left, key, comparator);
			root.setLeft(ls.second);
			return new TreeNodePair<>(ls.first, root);
		}
	}

	private TreeNodePair<K, V> splitUsingComparable(TreeNode<K, V> root, K key) {
		if (root == null)
			return new TreeNodePair<>(null, null);

		Comparable<? super K> compKey = (Comparable<? super K>) root.key;
		if (compKey.compareTo(key) < 0) {
			TreeNodePair<K, V> rs = splitUsingComparable(root.right, key);
			root.setRight(rs.first);
			return new TreeNodePair<>(root, rs.second);
		} else {
			TreeNodePair<K, V> ls = splitUsingComparable(root.left, key);
			root.setLeft(ls.second);
			return new TreeNodePair<>(ls.first, root);
		}
	}

	private TreeNode<K, V> deleteUsingComparator(TreeNode<K, V> _root, K key, Comparator<? super K> comparator) {
		if (_root == null)
			return null;

		int comp = comparator.compare(_root.key, key);
		if (comp == 0) {
			return merge(_root.left, _root.right);
		}

		if (comp > 0) {
			_root.setLeft(deleteUsingComparable(_root.left, key));
		} else {
			_root.setRight(deleteUsingComparable(_root.right, key));
		}
		return _root;
	}

	private TreeNode<K, V> deleteUsingComparable(TreeNode<K, V> _root, K key) {
		if (_root == null)
			return null;

		Comparable<? super K> compKey = (Comparable<? super K>) _root.key;
		if (compKey.compareTo(key) == 0) {
			return merge(_root.left, _root.right);
		}

		if (compKey.compareTo(key) > 0) {
			_root.setLeft(deleteUsingComparable(_root.left, key));
		} else {
			_root.setRight(deleteUsingComparable(_root.right, key));
		}
		return _root;
	}

	private TreeNode<K, V> merge(TreeNode<K, V> _a, TreeNode<K, V> _b) {
		if (_a == null) return _b;
		if (_b == null) return _a;

		TreapNode<K, V> a = (TreapNode<K, V>) _a;
		TreapNode<K, V> b = (TreapNode<K, V>) _b;
		if (a.priority < b.priority) {
			b.setLeft(merge(a, b.left));
			return b;
		} else {
			a.setRight(merge(a.right, b));
			return a;
		}
	}

	private static class TreeNodePair<K, V> {
		TreeNode<K, V> first;
		TreeNode<K, V> second;

		public TreeNodePair(TreeNode<K, V> first, TreeNode<K, V> second) {
			this.first = first;
			this.second = second;
		}
	}

	private static class TreapNode<K, V> extends BinarySearchTree.TreeNode<K, V> {
		private static final int MAX_PRIORITY = 1234567;
		int priority, size;

		TreapNode(K key, V value) {
			super(key, value);
			this.priority = new Random().nextInt(MAX_PRIORITY);
			this.size = 1;
		}

		@Override
		void setLeft(TreeNode<K, V> child) {
			super.setLeft(child);
			calcSize();
		}

		@Override
		void setRight(TreeNode<K, V> child) {
			super.setRight(child);
			calcSize();
		}

		void calcSize() {
			size = 1;
			if (left != null)
				size += ((TreapNode<K, V>) left).size;
			if (right != null)
				size += ((TreapNode<K, V>) right).size;
		}
	}
}
