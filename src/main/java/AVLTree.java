import java.util.Comparator;

public class AVLTree<K, V> extends BinarySearchTree<K, V> {

	public AVLTree() {
		this(null);
	}

	public AVLTree(Comparator<? super K> comparator) {
		super(comparator);
	}

	@Override
	public boolean add(K key, V value) {
		if (contains(key))
			return false;

		if (comparator != null) {
			root = addUsingComparator(root, key, value, comparator);
		} else {
			root = addUsingComparable(root, key, value);
		}
		++size;
		return true;
	}

	@Override
	public V get(K key) {
		if (key == null)
			throw new NullPointerException();

		if (comparator != null) {
			return getUsingComparator(key, comparator);
		} else {
			return getUsingComparable(key);
		}
	}

	@Override
	public V remove(K key) {
		V oldValue = get(key);
		if (comparator != null) {
			deleteUsingComparator(root, key, comparator);
		} else {
			deleteUsingComparable(root, key);
		}
		if (oldValue != null)
			--size;
		return oldValue;
	}

	private TreeNode<K, V> addUsingComparator(TreeNode<K, V> node, K key, V value, Comparator<? super K> comparator) {
		if (node == null) {
			return new AVLTreeNode<>(key, value);
		}

		int comp = comparator.compare(key, node.key);
		if (comp < 0) {
			node.left = addUsingComparator(node.left, key, value, comparator);
		} else if (comp > 0) {
			node.right = addUsingComparator(node.right, key, value, comparator);
		} else {
			return node;
		}

		heightUpdate(node);
		int balance = getBalance(node);
		return restructureUsingComparator(node, key, balance, comparator);
	}

	private TreeNode<K, V> addUsingComparable(TreeNode<K, V> node, K key, V value) {
		if (node == null) {
			return new AVLTreeNode<>(key, value);
		}

		Comparable<? super K> compKey = (Comparable<? super K>) key;
		int comp = compKey.compareTo(node.key);
		if (comp < 0) {
			node.left = addUsingComparable(node.left, key, value);
		} else if (comp > 0) {
			node.right = addUsingComparable(node.right, key, value);
		} else {
			return node;
		}

		heightUpdate(node);
		int balance = getBalance(node);
		return restructureUsingComparable(node, key, balance);
	}

	private V getUsingComparator(K key, Comparator<? super K> comparator) {
		int comp;
		TreeNode<K, V> p = root;
		while (p != null) {
			comp = comparator.compare(key, p.key);
			if (comp < 0) {
				p = p.left;
			} else if (comp > 0) {
				p = p.right;
			} else {
				return p.value;
			}
		}
		return null;
	}

	private V getUsingComparable(K key) {
		Comparable<? super K> compKey = (Comparable<? super K>) key;
		int comp;
		TreeNode<K, V> p = root;
		while (p != null) {
			comp = compKey.compareTo(p.key);
			if (comp < 0) {
				p = p.left;
			} else if (comp > 0) {
				p = p.right;
			} else {
				return p.value;
			}
		}
		return null;
	}

	private TreeNode<K, V> deleteUsingComparator(TreeNode<K, V> node, K key, Comparator<? super K> comparator) {
		if (node == null) {
			return null;
		}

		int comp = comparator.compare(key, node.key);
		if (comp < 0) {
			node.left = deleteUsingComparator(node.left, key, comparator);
		} else if (comp > 0) {
			node.right = deleteUsingComparator(node.right, key, comparator);
		} else {
			if (node.left == null && node.right == null) {
				node = null;
			} else {
				if (node.left != null) {
					TreeNode<K, V> target = findMaxNode(node.left);
					node.key = target.key;
					node.value = target.value;
					node.left = deleteUsingComparator(node.left, target.key, comparator);
				} else {
					node = node.right;
				}
			}
		}

		if (node == null) {
			return null;
		}

		heightUpdate(node);
		int balance = getBalance(node);
		return restructureUsingComparator(node, key, balance, comparator);
	}

	private TreeNode<K, V> deleteUsingComparable(TreeNode<K, V> node, K key) {
		if (node == null) {
			return null;
		}

		Comparable<? super K> compKey = (Comparable<? super K>) key;
		int comp = compKey.compareTo(node.key);
		if (comp < 0) {
			node.left = deleteUsingComparable(node.left, key);
		} else if (comp > 0) {
			node.right = deleteUsingComparable(node.right, key);
		} else {
			if (node.left == null && node.right == null) {
				node = null;
			} else {
				if (node.left != null) {
					TreeNode<K, V> target = findMaxNode(node.left);
					node.key = target.key;
					node.value = target.value;
					node.left = deleteUsingComparable(node.left, target.key);
				} else {
					node = node.right;
				}
			}
		}

		if (node == null) {
			return null;
		}

		heightUpdate(node);
		int balance = getBalance(node);
		return restructureUsingComparable(node, key, balance);
	}

	private TreeNode<K, V> restructureUsingComparator(TreeNode<K, V> node, K key, int balance, Comparator<? super K> comparator) {
		// LL
		if (balance > 1 && comparator.compare(key, node.left.key) < 0) {
			return rightRotate(node);
		}

		// RR
		if (balance < -1 && comparator.compare(key, node.right.key) > 0) {
			return leftRotate(node);
		}

		// LR
		if (balance > 1 && comparator.compare(key, node.left.key) > 0) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}

		// RL
		if (balance < -1 && comparator.compare(key, node.right.key) < 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}
		return node;
	}

	private TreeNode<K, V> restructureUsingComparable(TreeNode<K, V> node, K key, int balance) {
		Comparable<? super K> compKey = (Comparable<? super K>) key;

		// LL
		if (balance > 1 && compKey.compareTo(node.left.key) < 0) {
			return rightRotate(node);
		}

		// RR
		if (balance < -1 && compKey.compareTo(node.right.key) > 0) {
			return leftRotate(node);
		}

		// LR
		if (balance > 1 && compKey.compareTo(node.left.key) > 0) {
			node.left = leftRotate(node.left);
			return rightRotate(node);
		}

		// RL
		if (balance < -1 && compKey.compareTo(node.right.key) < 0) {
			node.right = rightRotate(node.right);
			return leftRotate(node);
		}
		return node;
	}

	private int getBalance(TreeNode<K, V> node) {
		if (node == null)
			return 0;
		return height(node.left) - height(node.right);
	}

	private TreeNode<K, V> findMaxNode(TreeNode<K, V> node) {
		if (node.right != null) {
			return findMaxNode(node.right);
		} else {
			return node;
		}
	}

	private TreeNode<K, V> leftRotate(TreeNode<K, V> parentNode) {
		TreeNode<K, V> newParentNode = parentNode.right;
		TreeNode<K, V> T2 = newParentNode.left;

		newParentNode.left = parentNode;
		parentNode.right = T2;

		heightUpdate(parentNode);
		heightUpdate(newParentNode);

		return newParentNode;
	}

	private TreeNode<K, V> rightRotate(TreeNode<K, V> parentNode) {
		TreeNode<K, V> newParentNode = parentNode.left;
		TreeNode<K, V> T2 = newParentNode.right;

		newParentNode.right = parentNode;
		parentNode.left = T2;

		heightUpdate(parentNode);
		heightUpdate(newParentNode);

		return newParentNode;
	}

	private void heightUpdate(TreeNode<K, V> node) {
		if (node instanceof AVLTreeNode<K, V> avlNode) {
			avlNode.height = Math.max(height(node.left),height(node.right)) + 1;
		}
	}

	private int height(TreeNode<K, V> node) {
		if (node instanceof AVLTreeNode<K, V>)
			return ((AVLTreeNode<K, V>) node).height;
		return -1;
	}

	private static class AVLTreeNode<K, V> extends TreeNode<K, V> {
		int height;

		AVLTreeNode(K key, V value) {
			super(key, value);
			this.height = 0;
		}
	}
}
