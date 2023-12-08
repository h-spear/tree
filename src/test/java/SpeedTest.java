import java.util.Random;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

public class SpeedTest {

	private static final int DATA_LENGTH = 100000;
	private final Tree<Integer, String> bst = new BinarySearchTree<>((o1, o2) -> Integer.compare(o1, o2));
	private final Tree<Integer, String> avl = new AVLTree<>((o1, o2) -> Integer.compare(o1, o2));
	private final Tree<Integer, String> treap = new Treap<>((o1, o2) -> Integer.compare(o1, o2));
	private final String[] treeNames = new String[] {"Binary Search Tree", "AVL Tree", "Treap"};
	private final Tree[] trees = new Tree[] {bst, avl, treap};

	@AfterEach
	void afterEach() {
		bst.clear();
		avl.clear();
		treap.clear();
	}

	@Test
	void addTest() {
		Random random = new Random();
		int[] randomArray = new int[DATA_LENGTH];
		for (int i = 0; i < DATA_LENGTH; ++i) {
			randomArray[i] = random.nextInt(1234567);
		}

		System.out.printf("트리 삽입 연산 (랜덤 값, 데이터 %d개)\n", DATA_LENGTH);
		testTreeOperation(randomArray, DATA_LENGTH, (arr, n, tree) -> {
			for (int i = 0; i < n; ++i) {
				tree.add(arr[i], "data");
			}
		});
	}

	@Test
	void addTest2() {
		int[] sequentialArray = new int[DATA_LENGTH];
		for (int i = 0; i < DATA_LENGTH; ++i) {
			sequentialArray[i] = i;
		}

		System.out.printf("트리 삽입 연산 (순차 값, 데이터 %d개)\n", DATA_LENGTH);
		testTreeOperation(sequentialArray, DATA_LENGTH, (arr, n, tree) -> {
			for (int i = 0; i < n; ++i) {
				tree.add(arr[i], "data");
			}
		});
	}

	@Test
	void removeTest() {
		Random random = new Random();
		int[] randomArray = new int[DATA_LENGTH];
		for (int i = 0; i < DATA_LENGTH; ++i) {
			randomArray[i] = random.nextInt(1234567);
			bst.add(randomArray[i], "data");
			avl.add(randomArray[i], "data");
			treap.add(randomArray[i], "data");
		}

		System.out.printf("트리 삭제 연산 (데이터 %d개)\n", DATA_LENGTH);
		testTreeOperation(randomArray, DATA_LENGTH, (arr, n, tree) -> {
			for (int i = 0; i < n; ++i) {
				tree.remove(arr[i]);
			}
		});
	}

	private void testTreeOperation(int[] arr, int n, Operation operation) {
		long beforeTime, afterTime, diffTime;
		int t = trees.length;
		for (int i = 0; i < t; ++i) {
			Tree tree = trees[i];
			beforeTime = System.currentTimeMillis();
			operation.operation(arr, n, tree);
			afterTime = System.currentTimeMillis();
			diffTime = afterTime - beforeTime;
			System.out.printf("- %-30s: %d\n", treeNames[i] + " 실행 시간(ms)", diffTime);
		}
	}

	private interface Operation {
		void operation(int[] arr, int n, Tree tree);
	}
}
