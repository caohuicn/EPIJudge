package lc;

import java.util.*;

public class LC180 {

    public List<Integer> luckyNumbers (int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            int min = 0;
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] < matrix[i][min]) {
                    min = j;
                }
            }
            boolean isMax = true;
            for (int j = 0; j < m; j++) {
                if (matrix[j][min] > matrix[i][min]) {
                    isMax = false;
                    break;
                }
            }
            if (isMax) {
                ans.add(matrix[i][min]);
            }
        }
        return ans;
    }

   public class TreeNode {
      int val;
      TreeNode left;
      TreeNode right;
      TreeNode(int x) { val = x; }
  }

    public TreeNode balanceBST(TreeNode root) {
        ArrayList<Integer> list = new ArrayList<>();
        visit(root, list);
        return build(list, 0, list.size());
    }

    TreeNode build(List<Integer> list, int start, int end) {
        if (start >= end) return null;
        int mid = start + (end - start) / 2;
        TreeNode nr = new TreeNode(list.get(mid));
        nr.left = build(list, start, mid);
        nr.right = build(list, mid + 1, end);
        return nr;
    }

    void visit(TreeNode root, List<Integer> list) {
        if (root == null) return;
        visit(root.left, list);
        list.add(root.val);
        visit(root.right, list);
    }

    public int maxPerformance(int n, int[] speed, int[] efficiency, int k) {
        int[][] ess = new int[n][];
        for (int i = 0; i < n; i++) {
            ess[i] = new int[]{efficiency[i], speed[i]};
        }
        Arrays.sort(ess, (a,b) -> b[0] - a[0]);
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        long s = 0;
        long max = 0;
        for (int i = 0; i < n; i++) {
            s += ess[i][1];
            pq.add(ess[i][1]);
            if (pq.size() > k) s -= pq.poll();
            max = Math.max(max, s * ess[i][0]);
        }
        return (int) (max % 1000000007);
    }

    public static void main(String[] args) {
        //System.out.println(new LC180().luckyNumbers(new int[][]{{3,7,8},{9,11,13},{15,16,17}}));
        System.out.println(new LC180().maxPerformance(7,
                new int[]{1,4,1,9,4,4,4},
new int[]{8,2,1,7,1,8,4},
        6));
    }
}

class CustomStack {
    Deque<Integer> dq;
    int maxSize;
    public CustomStack(int maxSize) {
        dq = new LinkedList<>();
        this.maxSize = maxSize;
    }

    public void push(int x) {
        if (dq.size() < maxSize) {
            dq.offerFirst(x);
        }
    }

    public int pop() {
        if (dq.isEmpty()) return -1;
        return dq.pollFirst();
    }

    public void increment(int k, int val) {
        Deque<Integer> temp = new LinkedList<>();
        while (!dq.isEmpty()) {
            temp.offerFirst(dq.pollFirst());
        }
        while (!temp.isEmpty()) {
            int v = temp.pollFirst();
            dq.offerFirst(k-- > 0 ? v + val : v);
        }
    }
}
