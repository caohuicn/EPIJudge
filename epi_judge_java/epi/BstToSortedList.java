package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.TestFailure;
import epi.test_framework.TimedExecutor;
import java.util.ArrayList;
import java.util.List;
public class BstToSortedList {
  private static BstNode<Integer> head;
  private static BstNode<Integer> prev;
  public static BstNode<Integer> bstToDoublyLinkedList1(BstNode<Integer> tree) {
    head = null;
    prev = null;
    visit1(tree);
    return head;
  }

  public static void visit1(BstNode<Integer> tree) {
    if (tree == null) return;
    visit(tree.left);
    if (head == null) {
      //first node
      head = tree;
      tree.left = null;
    } else {
      prev.right = tree;
      tree.left = prev;
    }
    prev = tree;
    visit(tree.right);
  }

  private static BstNode<Integer> dummy;
  private static BstNode<Integer> curr;
  public static BstNode<Integer> bstToDoublyLinkedList(BstNode<Integer> tree) {
    dummy = new BstNode<>();
    curr = dummy;
    visit(tree);
    if (dummy.right != null) {
      dummy.right.left = null;//break the link
    }
    return dummy.right;
  }

  public static void visit(BstNode<Integer> tree) {
    if (tree == null) return;
    visit(tree.left);
    curr.right = tree;
    tree.left = curr;
    curr = tree;
    visit(tree.right);
  }

  @EpiTest(testDataFile = "bst_to_sorted_list.tsv")
  public static List<Integer>
  bstToDoublyLinkedListWrapper(TimedExecutor executor, BstNode<Integer> tree)
      throws Exception {
    BstNode<Integer> list = executor.run(() -> bstToDoublyLinkedList(tree));

    if (list != null && list.left != null)
      throw new TestFailure(
          "Function must return the head of the list. Left link must be null");
    List<Integer> v = new ArrayList<>();
    while (list != null) {
      v.add(list.data);
      if (list.right != null && list.right.left != list)
        throw new RuntimeException("List is ill-formed");
      list = list.right;
    }
    return v;
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "BstToSortedList.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
