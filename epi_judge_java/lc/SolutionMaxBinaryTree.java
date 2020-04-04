package lc;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;

public class SolutionMaxBinaryTree {
    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;
        TreeNode(int x) { val = x; }
    }

    public TreeNode constructMaximumBinaryTreeStack(int[] nums) {
        if (nums == null || nums.length == 0) {
            return null;
        }

        Deque<TreeNode> stack = new LinkedList<>();
        for (int i = 0; i <= nums.length; i++) {
            int val = i == nums.length ? Integer.MAX_VALUE : nums[i];
            TreeNode cur = new TreeNode(val);

            while (!stack.isEmpty() && val > stack.peek().val) {
                TreeNode node = stack.pop();
                if (stack.isEmpty() || val < stack.peek().val) {
                    cur.left = node;
                } else {
                    stack.peek().right = node;
                }
            }

            stack.push(cur);
        }

        return stack.peek().left;
    }

    public TreeNode constructMaximumBinaryTree(int[] nums) {
        TreeNode root = null;
        for (int i = 0; i < nums.length; i++) {
            TreeNode n = new TreeNode(nums[i]);
            if (root == null) {
                root = n;
            } else {
                if (nums[i] > root.val) {
                    n.left = root;
                    root = n;
                } else {
                    TreeNode c = root.right;
                    TreeNode p = root;
                    while (c != null && c.val > nums[i]) {
                        p = c;
                        c = c.right;
                    }
                    if (c == null) {
                        p.right = n;
                    } else {
                        p.right = n;
                        n.left = c;
                    }
                }
            }
        }
        return root;
    }

    public static void main(String[] args) throws InterruptedException {
        while (true) {
            profile();
            Thread.sleep(1000);
        }
    }

    private static void profile() {
        int[] nums = new int[10000000];
        Random r = new Random();

        for(int i = 0; i < nums.length; ++i) {
            nums[i] = r.nextInt();
        }

        SolutionMaxBinaryTree s = new SolutionMaxBinaryTree();
        long start = System.nanoTime();
        s.constructMaximumBinaryTree(nums);
        System.out.println(System.nanoTime() - start);
        start = System.nanoTime();
        s.constructMaximumBinaryTreeStack(nums);
        System.out.println(System.nanoTime() - start);
    }
}