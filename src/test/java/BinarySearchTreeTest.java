import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import data.CardinalNumber;

public class BinarySearchTreeTest {
	
	Tree<Integer, String> tree = new BinarySearchTree<>();

	@BeforeEach
	void beforeEach() {
		tree.add(50, CardinalNumber._50);
		tree.add(30, CardinalNumber._30);
		tree.add(70, CardinalNumber._70);
		tree.add(20, CardinalNumber._20);
		tree.add(40, CardinalNumber._40);
		tree.add(60, CardinalNumber._60);
		tree.add(80, CardinalNumber._80);
		tree.add(90, CardinalNumber._90);
		tree.add(100, CardinalNumber._100);
		tree.add(10, CardinalNumber._10);
	}
	
	@AfterEach
	void afterEach() {
		tree.clear();
	}

	@Test
	@DisplayName("원소 삽입 연산 후에 올바른 Traversal 결과를 제공한다.")
	void addTest() throws Exception {
		// given: beforeEach에서 원소 삽입

		// then
		List<Integer> preorderResult = tree.preorder().stream().map(Tree.Entry::getKey).toList();
		List<Integer> inorderResult = tree.inorder().stream().map(Tree.Entry::getKey).toList();
		List<Integer> postorderResult = tree.postorder().stream().map(Tree.Entry::getKey).toList();
		List<Integer> levelOrderResult = tree.levelOrder().stream().map(Tree.Entry::getKey).toList();

		assertThat(tree.size()).isEqualTo(10);
		assertThat(preorderResult).containsExactly(50, 30, 20, 10, 40, 70, 60, 80, 90, 100);
		assertThat(inorderResult).containsExactly(10, 20, 30, 40, 50, 60, 70, 80, 90, 100);
		assertThat(postorderResult).containsExactly(10, 20, 40, 30, 60, 100, 90, 80, 70, 50);
		assertThat(levelOrderResult).containsExactly(50, 30, 70, 20, 40, 60, 80, 10, 90, 100);
	}

	@Test
	@DisplayName("원소 삽입 시 이미 존재하는 key로 삽입하는 경우 원소를 삽입하지 않는다.")
	void addTest2() throws Exception {
		// given: beforeEach에서 원소 삽입

		// when
		boolean result1 = tree.add(30, "newValue1");
		boolean result2 = tree.add(50, "newValue2");
		boolean result3 = tree.add(70, "newValue3");

		// then
		assertThat(tree.size()).isEqualTo(10);
		assertThat(result1).isFalse();
		assertThat(result2).isFalse();
		assertThat(result3).isFalse();
	}

	@Test
	@DisplayName("원소 삭제 연산 후에 올바른 Traversal 결과를 제공한다.")
	void removeTest() throws Exception {
		// when
		int keyForRemove = 30;
		tree.remove(keyForRemove);

		// then
		List<Integer> preorderResult = tree.preorder().stream().map(Tree.Entry::getKey).toList();
		List<Integer> inorderResult = tree.inorder().stream().map(Tree.Entry::getKey).toList();
		List<Integer> postorderResult = tree.postorder().stream().map(Tree.Entry::getKey).toList();
		List<Integer> levelOrderResult = tree.levelOrder().stream().map(Tree.Entry::getKey).toList();

		assertThat(tree.size()).isEqualTo(9);
		assertThat(preorderResult).containsExactly(50, 40, 20, 10, 70, 60, 80, 90, 100);
		assertThat(inorderResult).containsExactly(10, 20, 40, 50, 60, 70, 80, 90, 100);
		assertThat(postorderResult).containsExactly(10, 20, 40, 60, 100, 90, 80, 70, 50);
		assertThat(levelOrderResult).containsExactly(50, 40, 70, 20, 60, 80, 10, 90, 100);
	}

	@Test
	@DisplayName("원소 삭제 시 존재하지 않는 key를 사용하면 삭제 연산을 수행하지 않고, null을 반환한다.")
	void removeTest2() throws Exception {
		// when
		int keyForRemove = 33;
		String result = tree.remove(keyForRemove);

		// then
		assertThat(tree.size()).isEqualTo(10);
		assertThat(result).isNull();
	}

	@Test
	@DisplayName("원소 조회 연산에 대해 올바른 결과를 제공한다.")
	void getTest() throws Exception {
		// when
		String result1 = tree.get(30);
		String result2 = tree.get(50);
		String result3 = tree.get(80);

		// then
		assertThat(result1).isEqualTo(CardinalNumber._30);
		assertThat(result2).isEqualTo(CardinalNumber._50);
		assertThat(result3).isEqualTo(CardinalNumber._80);
	}

	@Test
	@DisplayName("원소 조회 연산 시 존재하지 않는 key로 조회하면 null을 반환한다.")
	void getTest2() throws Exception {
		// when
		String result1 = tree.get(33);
		String result2 = tree.get(30);

		// then
		assertThat(result1).isNull();
		assertThat(result2).isNotNull();
	}

	@Test
	@DisplayName("원소 존재 여부 연산에 대해 올바른 결과를 제공한다.")
	void containsTest() throws Exception {
		// when
		boolean result1 = tree.contains(33);
		boolean result2 = tree.contains(30);

		// then
		assertThat(result1).isFalse();
		assertThat(result2).isTrue();
	}

	@Test
	@DisplayName("연산 수행 시 null을 key로 제공하면 NullPointerException이 발생한다.")
	void nullPointerTest() throws Exception {
		// add
		assertThatThrownBy(() -> tree.add(null, "null"))
			.isInstanceOf(NullPointerException.class);

		// remove
		assertThatThrownBy(() -> tree.remove(null))
			.isInstanceOf(NullPointerException.class);

		// contains
		assertThatThrownBy(() -> tree.contains(null))
			.isInstanceOf(NullPointerException.class);

		// get
		assertThatThrownBy(() -> tree.get(null))
			.isInstanceOf(NullPointerException.class);
	}
}
