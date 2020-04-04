package lc;

import java.util.*;

public class LC100Hard {
    public int longestValidParentheses(String s) {
        int ans = 0;
        int start = 0;
        Deque<Integer> dq = new LinkedList<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                dq.offerFirst(i);
            } else {
                if (dq.isEmpty()) {
                    start = i + i;
                } else {
                    dq.pollFirst();
                    int len = 0;
                    if (dq.isEmpty()) {
                        len = i - start + 1;
                    } else {
                        len = i - dq.peekFirst();
                    }
                    ans = Math.max(ans, len);
                }
            }
        }
        return ans;
    }

    public int numTeams(int[] rating) {
        if (rating == null || rating.length < 3) return 0;
        int n = rating.length;
        int ans = 0;
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                if (rating[j] > rating[i]) {
                    for (int k = j + 1; k < n; k++) {
                        if (rating[k] > rating[j]) {
                            ans++;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                if (rating[j] < rating[i]) {
                    for (int k = j + 1; k < n; k++) {
                        if (rating[k] < rating[j]) {
                            ans++;
                        }
                    }
                }
            }
        }
        return ans;
    }

    static final int M = 1000000007;
    public int findGoodStrings(int n, String s1, String s2, String evil) {
        int n1 = getNum(s1);
        int n2 = getNum(s2);
        if (n1 > n2) return 0;
        if (s1.startsWith(evil) && s2.startsWith(evil)) return 0;
        int total = (M + n2 - n1 + 1) % M;
        int m = evil.length();
        int sub = all(n - m);
        for (int i = 0; i <= n - evil.length(); i++) {
            int compS1 = evil.compareTo(s1.substring(i, m));
            int compS2 = evil.compareTo(s2.substring(i, m));
            String low = s1.substring(0, i);
            String high = s2.substring(0, i);

            int nlo = getNum(low);
            int nhi = getNum(high);
            int d = nhi - nlo;

            if (compS1 < 0) {
                d--;
            }
            if (compS2 > 0) {
                d--;
            }
            if (d <= 0) d = 1;
            d *= all(n - m - i);
            if (compS1 == 0) {
                d -= getNum(s1.substring(i + m)) - getNum(nc(n - m - i, 'a')) + 1;
            }
            if (compS2 == 0) {
                d -= getNum(nc(n - m - i, 'z')) - getNum(s2.substring(i + m)) + 1;
            }
            total -= d;
        }
        return total;
    }

    int all(int n) {
        int sub = 1;
        for (int i = 0; i < n; i++) {
            sub = (sub * 26) % M;
        }
        return sub;
    }
    String nc(int n, char c) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    int getNum(String s) {
        int n = 0;
        for (int i = 0; i < s.length(); i++) {
            n = (n * 26 + (s.charAt(i) - 'a' + 1)) % M;
        }
        return n;
    }

    public int maximalRectangle(char[][] mt) {
        if (mt == null || mt.length == 0 || mt[0].length == 0) return 0;
        int m = mt.length, n = mt[0].length;
        int[] h = new int[n + 1];
        int[] stack = new int[n + 1];
        int maxR = 0;
        for (int i = 0; i < m; i++) {
            int is = -1;
            for (int j = 0; j <= n; j++) {
                if (j < n && mt[i][j] == '1') {
                    h[j]++;
                } else {
                    h[j] = 0;
                }
                while (is != -1 && h[j] < h[stack[is]]) {
                    int pop = stack[is--];
                    int w = is == -1 ? j : j - stack[is] - 1;
                    maxR = Math.max(maxR, w * h[pop]);
                }
                stack[++is] = j;
            }
        }
        return maxR;
    }

    static class UF {
        int[] id;
        int[] sz;
        int maxSize;

        UF(int n) {
            id = new int[n];
            sz = new int[n];
            for (int i = 0; i < n; i++) {
                id[i] = i;
                sz[i] = 1;
            }
            maxSize = 1;
        }

        int find(int x) {
            if (id[x] == x) return x;
            id[x] = find(id[x]);
            return id[x];
        }

        void union(int p, int q) {
            int pid = find(p);
            int qid = find(q);
            if (pid == qid) return;

            if (sz[pid] < sz[qid]) {
                id[pid] = qid;
            } else {
                id[qid] = pid;
            }
            sz[pid] += sz[qid];
            sz[qid] = sz[pid];

            maxSize= Math.max(maxSize, sz[pid]);
        }

        int getMaxSize() {
            return maxSize;
        }
    }
    public int longestConsecutive(int[] nums) {
        if (nums == null || nums.length == 0) return 0;
        int n = nums.length;

        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i);
        }
        UF uf = new UF(n);
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            if (map.get(num) != i) continue;
            if (map.containsKey(num - 1)) {
                uf.union(map.get(num - 1), i);
            }
            if (map.containsKey(num + 1)) {
                uf.union(map.get(num + 1), i);
            }
        }
        return uf.getMaxSize();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public List<Integer> countSmaller(int[] nums) {
        if (nums == null || nums.length == 0) return new ArrayList<>();
        int n = nums.length;
        Integer[] ans = new Integer[n];
        Arrays.fill(ans, 0);
        int[][] ni = new int[n][2];
        for (int i = 0; i < n; i++) {
            ni[i][0] = i;
            ni[i][1] = nums[i];
        }
        mergeSort(ni, ans);
        return Arrays.asList(ans);
    }

    int[][] mergeSort(int[][] ni, Integer[] ans) {
        if (ni.length <= 1) return ni;
        int mid = ni.length / 2;
        int[][] left = mergeSort(Arrays.copyOfRange(ni, 0, mid), ans);
        int[][] right = mergeSort(Arrays.copyOfRange(ni, mid, ni.length), ans);
        int[][] sorted = new int[ni.length][2];
        for (int i = 0, j = 0; i < left.length || j < right.length;) {
            if (j == right.length || i < left.length && left[i][1] <= right[j][1]) {
                sorted[i + j][0] = left[i][0];
                sorted[i + j][1] = left[i][1];
                ans[left[i][0]] += j;
                i++;
            } else {
                sorted[i + j][0] = right[j][0];
                sorted[i + j][1] = right[j][1];
                j++;
            }
        }
        return sorted;
    }

    public int longestIncreasingPath(int[][] matrix) {
        int ans = 0;
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) return ans;
        int m = matrix.length, n = matrix[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                ans = Math.max(ans, dfs(matrix, i, j, dp));
            }
        }

        return ans;
    }

    int dfs(int[][] matrix, int i, int j, int[][] dp) {
        int m = matrix.length, n = matrix[0].length;
        if (i < 0 || i >= m || j < 0 || j >= n) return 0;
        if (dp[i][j] != 0) return dp[i][j];
        int ans = 0, val = matrix[i][j];

        if (i > 0 && matrix[i - 1][j] > val) {
            ans = Math.max(ans, dfs(matrix, i - 1, j, dp));
        }
        if (i < m - 1 && matrix[i + 1][j] > val) {
            ans = Math.max(ans, dfs(matrix, i + 1, j, dp));
        }

        if (j > 0 && matrix[i][j - 1] > val) {
            ans = Math.max(ans, dfs(matrix, i, j - 1, dp));
        }
        if (j < n - 1 && matrix[i][j + 1] > val) {
            ans = Math.max(ans, dfs(matrix, i, j + 1, dp));
        }

        dp[i][j] = ++ans;
        return ans;
    }

    public static void main(String[] args) {
        //new LC100Hard().longestValidParentheses(")()())");
        //System.out.println(new LC100Hard().findGoodStrings(2, "aa", "da", "b"));
        //System.out.println(new LC100Hard().maximalRectangle(new char[][]{{'1','0','1','0','0'},{'1','0','1','1','1'},{'1','1','1','1','1'},{'1','0','0','1','0'}}));
        //System.out.println(new LC100Hard().longestConsecutive(new int[]{0,3,7,2,5,8,4,6,0,1}));
        System.out.println(new LC100Hard().longestIncreasingPath(new int[][]{{7,8,9},{9,7,6},{7,2,3}}));
    }
}
