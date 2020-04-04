package lc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class LC181 {
    public int maxNumberOfFamilies(int n, int[][] rs) {
        int ans = 2 * n;

        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int i = 0; i < rs.length; i++) {
            if (!map.containsKey(rs[i][0])) {
                map.put(rs[i][0], new HashSet<>());
            }
            map.get(rs[i][0]).add(rs[i][1]);
        }
        for (int i = 0; i < rs.length; i++) {
            if (map.containsKey(i)) {
                Set<Integer> set = map.get(i);
                if (set.contains(2) || set.contains(3) || set.contains(4) || set.contains(5)) {
                    if (set.contains(6) || set.contains(7) || set.contains(8) || set.contains(9)) {
                        if (set.contains(6) || set.contains(7) || set.contains(4) || set.contains(5)) {
                            ans -= 2;
                        } else {
                            ans -= 1;
                        }
                    } else {
                        ans -= 1;
                    }
                } else {
                    if (set.contains(6) || set.contains(7) || set.contains(8) || set.contains(9)) {
                        ans -= 1;
                    }
                }
            }
        }
        return ans;
    }

    public int maxSizeSlices(int[] slices) {
        if (slices == null || slices.length == 0) return 0;
        int n = slices.length;
        int k = n / 3;
        int[][][] dp = new int[n][n][k + 1];
        int ans = mss(slices, 0, n - 1, k, dp);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                for (int l = 0; l <= k; l++) {
                    System.out.print(dp[i][j][l]);
                    System.out.print(",");
                }
                System.out.print("\t");
            }
            System.out.println();
        }
        System.out.println();
        return ans;
    }

    int mss(int[] sls, int i, int j, int k, int[][][] dp) {
        if (k == 0) return 0;
        if (j - i + 1 < 2 * k - 1) return 0;
        if (dp[i][j][k] != 0) return dp[i][j][k];
        //boolean leftover = j - i + 1 < 3 * k;
        boolean leftover = i == 0 ? false : true;
        //take i
        int takei = mss(sls, i + 2, leftover ? j : j - 1, k - 1, dp) + sls[i];
        int noi = mss(sls, i + 1, j, k, dp);
        dp[i][j][k] = Math.max(takei, noi);
        System.out.format("i:%d,j:%d,k:%d: %d ", i, j, k, dp[i][j][k]);
        if (dp[i][j][k] == takei) {
            System.out.format("(take i:%d)", sls[i]);
        } else {
            System.out.format("(not take i:%d)", sls[i]);
        }
        System.out.println();
        return dp[i][j][k];
    }

    public int sumFourDivisors(int[] nums) {
        int ans = 0;
        for (int n : nums) {
            ans += sum(n);
        }
        return ans;
    }

    int sum(int n) {
        int f1 = 0, f2 = 0;
        int sqrt = (int)Math.sqrt(n);
        for (int i = 2; i <= sqrt; i++) {
            if (n % i == 0) {
                if (f1 == 0) {
                    f1 = i;
                    f2 = n / i;
                    if (f1 == f2) return 0;
                } else {
                    return 0;
                }
            }
        }
        return f1 == 0 ? 0 : f1 + f2 + 1 + n;
    }

    public static void main(String[] args) {
        //new LC181().maxNumberOfFamilies(3, new int[][]{{1,2}, {1,3}, {1,8}, {2,6}, {3,1}, {3,10}});
        //System.out.println(new LC181().maxSizeSlices(new int[] {10,1,1,2,1,10,3,10,2,8,4,10,8,8,2,9,9,9,10,10,7,6,5,6,3,8,2,6,8,10}));
//        System.out.println(new LC181().maxSizeSlices(new int[] {8,4,3,8,3,9,7,10,5,9,7,7,1,8,9}));
        System.out.println(new LC181().sumFourDivisors(new int[] {1,2,3,4,5,6,7,8,9,10}));
    }
}
