package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
public class TreePostorder {
  @EpiTest(testDataFile = "tree_postorder.tsv")
  public static List<Integer> postorderTraversal(BinaryTreeNode<Integer> root) {
    LinkedList<Integer> rst = new LinkedList<>();
    Deque<BinaryTreeNode<Integer>> stack = new ArrayDeque<>();

    while (root != null || !stack.isEmpty()) {
      if (root != null) {
        rst.offerFirst(root.data);
        stack.push(root);
        root = root.right;
      } else {
        root = stack.pop().left;
      }
    }
    return rst;
  }

  public static List<Integer> postorderTraversal1(BinaryTreeNode<Integer> tree) {
    LinkedList<Integer> ans = new LinkedList<>();
    Deque<BinaryTreeNode<Integer>> deque = new LinkedList<>();
    BinaryTreeNode<Integer> node = tree;
    while (!deque.isEmpty() || node != null) {
      while (node != null) {
        deque.addFirst(node);
        ans.addFirst(node.data);
        node = node.right;
      }
      if (!deque.isEmpty()) {
        BinaryTreeNode<Integer> next =  deque.removeFirst();
        node = next.left;
      }
    }
    return ans;
  }

  // We use stack and previous node pointer to simulate postorder traversal.
  public static List<Integer> postorderTraversal2(BinaryTreeNode<Integer> tree) {
    List<Integer> ans = new LinkedList<>();
    if (tree == null) return ans;
    Deque<NodeStatus> stack = new LinkedList<>();
    stack.addFirst(new NodeStatus(tree));
    while (!stack.isEmpty()) {
      NodeStatus ns = stack.peek();
      if (!ns.childExamined) {
        ns.childExamined = true;
        if (ns.node.right != null) {
          stack.addFirst(new NodeStatus(ns.node.right));
        }
        if (ns.node.left != null) {
          stack.addFirst(new NodeStatus(ns.node.left));
        }
        if (ns.node.left == null && ns.node.right == null) {
          ans.add(ns.node.data);
          stack.removeFirst();
        }
      } else {
        ans.add(ns.node.data);
        stack.removeFirst();
      }
    }
    return ans;
  }

  static class NodeStatus{
    BinaryTreeNode<Integer> node;
    boolean childExamined;
    NodeStatus(BinaryTreeNode<Integer> node) {
      this(node, false);
    }

    NodeStatus(BinaryTreeNode<Integer> node,
            boolean childExamined) {
      this.node = node;
      this.childExamined = childExamined;
    }
  }

  static class TreeNodeStatus<T> {
    //BinaryTreeNode<T>
  }
  public static void main(String[] args) {
//    System.out.println(Integer.MIN_VALUE);
//    System.out.println(1<<31);
//    System.out.println(1<<31-1);
    System.exit(
        GenericTest
            .runFromAnnotations(args, "TreePostorder.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
