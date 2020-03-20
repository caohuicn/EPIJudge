package lc;

public class BuildTree {

  private static class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }
    private int pi, ii;
    public TreeNode buildTree(int[] io, int[] po) {
        if (io == null || io.length == 0) return null;
        int n = io.length;
        pi = n - 1;
        ii = n - 1;
        return helper(io, po, Integer.MIN_VALUE);
    }

    //make sure pi and ii are set to correct value before calling this helper
    private TreeNode helper(int[] io, int[] po, int stop) {
        if (pi < 0 || ii < 0) return null;
        if (io[ii] == stop) {
            ii--;
            return null;
        }
        TreeNode root = new TreeNode(po[pi--]);
        root.right = helper(io, po, root.val);
        root.left = helper(io, po, stop);
        return root;
    }

    public static void main(String[] args) {
        BuildTree bt = new BuildTree();
        int[] io = new int[]{9,3,15,20,7};
        int[] po = new int[]{9,15,7,20,3};
        bt.buildTree(io, po);
    }
}
