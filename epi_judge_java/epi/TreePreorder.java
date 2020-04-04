package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
public class TreePreorder {

  private static class NodeAndState {
    public BinaryTreeNode<Integer> node;
    public Boolean nodeProcessed;

    public NodeAndState(BinaryTreeNode<Integer> node, Boolean nodeProcessed) {
      this.node = node;
      this.nodeProcessed = nodeProcessed;
    }
  }

  @EpiTest(testDataFile = "tree_preorder.tsv")
  public static List<Integer> preorderTraversal(BinaryTreeNode<Integer> tree) {
    List<Integer> ans = new LinkedList<>();
    Deque<BinaryTreeNode<Integer>> deque = new LinkedList<>();
    BinaryTreeNode<Integer> node = tree;
    while (!deque.isEmpty() || node != null) {
      while (node != null) {
        ans.add(node.data);
        deque.addFirst(node);
        node = node.left;
      }
      if (!deque.isEmpty()) {
        BinaryTreeNode<Integer> next =  deque.removeFirst();
        node = next.right;
      }
    }
    return ans;
  }
  public static List<Integer> preorderTraversal2(BinaryTreeNode<Integer> tree) {
    List<Integer> ans = new LinkedList<>();
    Deque<BinaryTreeNode<Integer>> deque = new LinkedList<>();
    if (tree != null) {
      deque.addFirst(tree);
    }
    while (!deque.isEmpty()) {
      BinaryTreeNode<Integer> node = deque.removeFirst();
      ans.add(node.data);
      if (node.right != null) {
        deque.addFirst(node.right);
      }
      if (node.left != null) {
        deque.addFirst(node.left);
      }
    }
    return ans;
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "TreePreorder.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
