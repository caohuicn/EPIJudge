package lc;

import java.util.*;

public class LC178 {
    public int minCost(int[][] grid) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] cost = new int[m][n];
        for (int[] c : cost) {
            Arrays.fill(c, -1);
        }
        int costSoFar = 0;
        visit(grid, cost, 0, 0, costSoFar);
        while (cost[m - 1][n - 1] == -1) {
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (cost[i][j] == -1) {
                        visitFromNeighbor(grid, cost, i, j, costSoFar);
                    }
                }
            }
            costSoFar++;
        }

        return cost[m - 1][n - 1];
    }

    boolean visitFromNeighbor(int[][] grid, int[][] cost, int r, int c, int costSoFar) {
        int m = grid.length;
        int n = grid[0].length;
        if (r > 0 && cost[r - 1][c] == costSoFar ||
                r < m - 1 && cost[r + 1][c] == costSoFar ||
                c > 0 && cost[r][c - 1] == costSoFar ||
                c < n - 1 && cost[r][c + 1] == costSoFar) {
            visit(grid, cost, r, c, costSoFar + 1);
            return true;
        }
        return false;
    }

    void visit(int[][] grid, int[][] cost, int r, int c, int costSoFar) {
        int m = grid.length;
        int n = grid[0].length;
        if (r < 0 || r >= m || c < 0 || c >= n) {
            return;
        }
        if (cost[r][c] != -1) return;
        cost[r][c] = costSoFar;
        if (grid[r][c] == 1) {
            visit(grid, cost, r, c + 1, costSoFar);
        } else if (grid[r][c] == 2) {
            visit(grid, cost, r, c - 1, costSoFar);
        } else if (grid[r][c] == 3) {
            visit(grid, cost, r + 1, c, costSoFar);
        } else if (grid[r][c] == 4) {
            visit(grid, cost, r - 1, c, costSoFar);
        }
    }

    public String rankTeams(String[] votes) {
        int[][] v = new int[26][26];
        for (int i = 0; i < votes[0].length(); ++i) {
            for (String w : votes) {
                v[i][w.charAt(i)-'A']++;
            }
        }
        String[] vs = new String[26];
        for (int i = 0; i < 26; ++i) {
            for (int j = 0; j < 26; ++j) {
                vs[i] += String.format("%04d", v[j][i]) + " ";
            }
            vs[i] += (char)(((int)'Z')-i);
        }
        Arrays.sort(vs);
        String ans = "";
        for (int i = 0; i < votes[0].length(); ++i) {
            ans += (char)((int)('Z'-vs[25-i].charAt(vs[25-i].length()-1))+(int)'A');
        }
        return ans;
    }
    static class ListNode {
      int val;
      ListNode next;
      ListNode(int x) { val = x; }
  }


  static class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }
    public boolean isSubPath(ListNode head, TreeNode root) {
        List<Integer> A = new ArrayList(), dp = new ArrayList();
        A.add(head.val);
        dp.add(0);
        int i = 0;
        head = head.next;
        while (head != null) {
            while (i > 0 && head.val != A.get(i))
                i = dp.get(i - 1);
            if (head.val == A.get(i)) ++i;
            A.add(head.val);
            dp.add(i);
            head = head.next;
        }
        return dfs(root, 0, A, dp);
    }

    private boolean dfs(TreeNode root, int i, List<Integer> A, List<Integer> dp) {
        if (root == null) return false;
        while (i > 0 && root.val != A.get(i))
            i = dp.get(i - 1);
        if (root.val == A.get(i)) ++i;
        return i == dp.size() ||
                dfs(root.left, i, A, dp) ||
                dfs(root.right, i, A, dp);
    }

    public static void main(String[] args) {
        LC178 lc178 = new LC178();
//        //int[][] grid = new int[][]{{1,1,1,1},{2,2,2,2},{1,1,1,1},{2,2,2,2}};
//        int[][] grid = new int[][] {{1,1,3},{3,2,2},{1,1,4}};
//        System.out.println(lc178.minCost(grid));
//        String[] votes = new String[]{"ABC","ACB","ABC","ACB","ACB"};
//        System.out.println(lc178.rankTeams(votes));
        //[4,2,8], root = [1,4,4,null,2,2,null,1,null,6,8,null,null,null,null,1,3]
        ListNode head = new ListNode(4);
        head.next = new ListNode(2);
        head.next.next = new ListNode(8);
        TreeNode root = build(new Integer[]{1,4,4,null,2,2,null,1,null,6,8,null,null,null,null,1,3});
        System.out.println(lc178.isSubPath(head, root));
    }

    //traverse by level
    static TreeNode build(Integer[] nodes) {
        TreeNode root = new TreeNode(nodes[0]);
        Deque<TreeNode> deque = new ArrayDeque<>();
        deque.add(root);
        int i = 0;
        while (i < nodes.length && !deque.isEmpty()) {
            TreeNode node = deque.removeFirst();
            if (i == nodes.length - 1) break;
            Integer left = nodes[++i];
            if (left != null) {
                node.left = new TreeNode(left);
                deque.addLast(node.left);
            }
            Integer right = nodes[++i];
            if (right != null) {
                node.right = new TreeNode(right);
                deque.addLast(node.right);
            }

        }
        return root;
    }
}
