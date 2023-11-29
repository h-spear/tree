import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

public class BinarySearchTree<K, V> implements Tree<K, V> {

	private int size;
	private TreeNode<K, V> root;
	private final Comparator<? super K> comparator;

	public BinarySearchTree() {
		this(null);
	}

	public BinarySearchTree(Comparator<? super K> comparator) {
		this.comparator = comparator;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void clear() {
		root = null;
		size = 0;
	}

	@Override
	public boolean add(K key, V value) {
		if (key == null)
			throw new NullPointerException();

		if (root == null) {
			root = new TreeNode<>(key, value);
			++size;
			return true;
		}

		if (comparator != null) {
			return addUsingComparator(key, value, comparator);
		} else {
			return addUsingComparable(key, value);
		}
	}

	@Override
	public boolean contains(K key) {
		if (key == null)
			throw new NullPointerException();
		return get(key) != null;
	}

	@Override
	public V get(K key) {
		if (key == null)
			throw new NullPointerException();

		if (root == null)
			return null;

		TreeNode<K, V> find;
		if (comparator != null) {
			find = getNodeUsingComparator(key, comparator);
		} else {
			find = getNodeUsingComparable(key);
		}
		return Objects.equals(Objects.requireNonNull(find).key, key) ? find.value : null;
	}

	@Override
	public V remove(K key) {
		if (key == null)
			throw new NullPointerException();

		if (root == null)
			return null;

		TreeNode<K, V> p;
		if (comparator != null) {
			p = getNodeUsingComparator(key, comparator);
		} else {
			p = getNodeUsingComparable(key);
		}

		if (p != null && Objects.equals(p.key, key)) {
			delete(p);
			return p.value;
		} else {
			return null;
		}
	}

	private boolean addUsingComparator(K key, V value, Comparator<? super K> comparator) {
		TreeNode<K, V> find = getNodeUsingComparator(key, comparator);
		if (Objects.equals(find, null) || Objects.equals(find.key, key))
			return false;

		int comp = comparator.compare(key, find.key);
		TreeNode<K, V> newNode = new TreeNode<>(key, value);
		if (comp < 0) {
			find.setLeft(newNode);
		} else {
			find.setRight(newNode);
		}
		++size;
		return true;
	}

	private boolean addUsingComparable(K key, V value) {
		TreeNode<K, V> find = getNodeUsingComparable(key);
		if (Objects.equals(find, null) || Objects.equals(find.key, key))
			return false;

		Comparable<? super K> compKey = (Comparable<? super K>) key;
		int comp = compKey.compareTo(find.key);
		TreeNode<K, V> newNode = new TreeNode<>(key, value);
		if (comp < 0) {
			find.setLeft(newNode);
		} else {
			find.setRight(newNode);
		}
		++size;
		return true;
	}

	private void delete(TreeNode<K, V> removeNode) {
		if (removeNode == null)
			return;

		--size;
		TreeNode<K, V> parentNode = removeNode != root ? removeNode.parent : null;
		TreeNode<K, V> replacementNode;

		if (removeNode.left == null && removeNode.right == null) {
			replacementNode = null;
		} else if (removeNode.left == null) {
			replacementNode = removeNode.right;
		} else if (removeNode.right == null) {
			replacementNode = removeNode.left;
		} else {
			// Node has two children, find successor
			replacementNode = successor(removeNode);
			TreeNode<K, V> parentOfReplacementNode = replacementNode.parent;
			if (replacementNode != removeNode.right) {
				// If successor is not the right child of the removed node
				parentOfReplacementNode.setLeft(replacementNode.right);
				replacementNode.setRight(removeNode.right);
			}
			replacementNode.setLeft(removeNode.left);
		}

		if (replacementNode != null) {
			replacementNode.parent = parentNode;
		}

		if (removeNode == root) {
			root = replacementNode;
		} else if (removeNode == parentNode.left) {
			parentNode.setLeft(replacementNode);
		} else {
			parentNode.setRight(replacementNode);
		}
	}

	private TreeNode<K, V> successor(TreeNode<K, V> p) {
		if (p == null)
			return null;

		// If the right subtree is not null, find the leftmost node in the right subtree
		if (p.right != null) {
			p = p.right;
			while (p.left != null) {
				p = p.left;
			}
			return p;
		}

		// If the right subtree is null, find the nearest ancestor whose left child is also an ancestor of p.
		while (p.parent != null && p == p.parent.right) {
			p = p.parent;
		}
		return p.parent;
	}

	private TreeNode<K, V> getNodeUsingComparator(K key, Comparator<? super K> comparator) {
		if (comparator != null) {
			int comp;
			TreeNode<K, V> p = root;
			while (p != null) {
				comp = comparator.compare(key, p.key);
				if (comp < 0) {
					if (p.left == null)
						return p;
					else
						p = p.left;
				} else if (comp > 0) {
					if (p.right == null)
						return p;
					else
						p = p.right;
				} else {
					return p;
				}
			}
		}
		return null;
	}

	private TreeNode<K, V> getNodeUsingComparable(K key) {
		Comparable<? super K> compKey = (Comparable<? super K>) key;
		int comp;
		TreeNode<K, V> p = root;
		while (p != null) {
			comp = compKey.compareTo(p.key);
			if (comp < 0) {
				if (p.left == null)
					return p;
				else
					p = p.left;
			} else if (comp > 0) {
				if (p.right == null)
					return p;
				else
					p = p.right;
			} else {
				return p;
			}
		}
		return null;
	}

	@Override
	public List<Entry<K, V>> preorder() {
		List<Entry<K, V>> result = new ArrayList<>(size);
		traversePreorder(root, result);
		return result;
	}

	@Override
	public List<Entry<K, V>> inorder() {
		List<Entry<K, V>> result = new ArrayList<>(size);
		traverseInorder(root, result);
		return result;
	}

	@Override
	public List<Entry<K, V>> postorder() {
		List<Entry<K, V>> result = new ArrayList<>(size);
		traversePostorder(root, result);
		return result;
	}

	@Override
	public List<Entry<K, V>> levelOrder() {
		List<Entry<K, V>> result = new ArrayList<>(size);
		traverseLevelOrder(root, result);
		return result;
	}

	private void traversePreorder(TreeNode<K, V> node, List<Entry<K, V>> result) {
		if (node == null)
			return;
		result.add(new Entry<>(node.key, node.value));
		traversePreorder(node.left, result);
		traversePreorder(node.right, result);
	}

	private void traverseInorder(TreeNode<K, V> node, List<Entry<K, V>> result) {
		if (node == null)
			return;
		traverseInorder(node.left, result);
		result.add(new Entry<>(node.key, node.value));
		traverseInorder(node.right, result);
	}

	private void traversePostorder(TreeNode<K, V> node, List<Entry<K, V>> result) {
		if (node == null)
			return;
		traversePostorder(node.left, result);
		traversePostorder(node.right, result);
		result.add(new Entry<>(node.key, node.value));
	}

	private void traverseLevelOrder(TreeNode<K, V> node, List<Entry<K, V>> result) {
		Queue<TreeNode<K, V>> queue = new ArrayDeque<>();
		queue.add(node);
		while (!queue.isEmpty()) {
			TreeNode<K, V> curr = queue.poll();
			result.add(new Entry<>(curr.key, curr.value));
			if (curr.left != null)
				queue.add(curr.left);
			if (curr.right != null)
				queue.add(curr.right);
		}
	}

	private static class TreeNode<K, V> {
		K key;
		V value;
		TreeNode<K, V> parent;
		TreeNode<K, V> left;
		TreeNode<K, V> right;

		TreeNode(K key, V value) {
			this.key = key;
			this.value = value;
		}

		void setLeft(TreeNode<K, V> child) {
			left = child;
			if (child != null)
				child.parent = this;
		}

		void setRight(TreeNode<K, V> child) {
			right = child;
			if (child != null)
				child.parent = this;
		}
	}
}
