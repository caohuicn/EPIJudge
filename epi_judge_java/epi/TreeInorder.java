package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public class TreeInorder {

  private static class NodeAndState {
    public BinaryTreeNode<Integer> node;
    public Boolean leftSubtreeTraversed;

    public NodeAndState(BinaryTreeNode<Integer> node,
                        Boolean leftSubtreeTraversed) {
      this.node = node;
      this.leftSubtreeTraversed = leftSubtreeTraversed;
    }
  }

  @EpiTest(testDataFile = "tree_inorder.tsv")
  public static List<Integer> inorderTraversal(BinaryTreeNode<Integer> tree) {
    List<Integer> ans = new LinkedList<>();
    Deque<BinaryTreeNode<Integer>> deque = new LinkedList<>();
    BinaryTreeNode<Integer> node = tree;
    while (!deque.isEmpty() || node != null) {
      if (node != null) {
        deque.addFirst(node);
        node = node.left;
      } else {
        BinaryTreeNode<Integer> next =  deque.removeFirst();
        ans.add(next.data);
        node = next.right;
      }
    }
    return ans;
  }

  public static List<Integer> inorderTraversal1(BinaryTreeNode<Integer> tree) {
    List<Integer> ans = new LinkedList<>();
    Deque<BinaryTreeNode<Integer>> deque = new LinkedList<>();
    BinaryTreeNode<Integer> node = tree;
    while (!deque.isEmpty() || node != null) {
      while (node != null) {
        deque.addFirst(node);
        node = node.left;
      }
      if (!deque.isEmpty()) {
        BinaryTreeNode<Integer> next =  deque.removeFirst();
        ans.add(next.data);
        node = next.right;
      }
    }
    return ans;
  }

  public static List<Integer> inorderTraversal3(BinaryTreeNode<Integer> tree) {
    List<Integer> ans = new LinkedList<>();
    Deque<BinaryTreeNode<Integer>> deque = new LinkedList<>();
    while (tree != null) {
      deque.addFirst(tree);
      tree = tree.left;
    }
    while (!deque.isEmpty()) {
      BinaryTreeNode<Integer> node = deque.removeFirst();
      ans.add(node.data);
      if (node.right != null) {
        BinaryTreeNode<Integer> next = node.right;
        while (next != null) {
          deque.addFirst(next);
          next = next.left;
        }
      }
    }
    return ans;
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "TreeInorder.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
