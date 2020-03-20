package lc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TargetSum {
    public int findTargetSumWays(int[] nums, int S) {
        int sum = 0;
        for (int n : nums) {
            sum += n;
        }
        if (sum < S || (sum - S) % 2 != 0) return 0;
        //in case there is 0
        //if (sum == S) return 1;
        int t = (sum - S) / 2;
        List<Integer> smalls = new ArrayList<>();
        for (int n: nums) {
            if (n <= t) {
                smalls.add(n);
            }
        }
        if (smalls.isEmpty()) return sum == S ? 1 : 0;
        int[][] dp = new int[smalls.size()][t + 1];
        for(int[] dpr : dp) {
            Arrays.fill(dpr, -1);
        }
        return findTarget(smalls, 0, t, dp);
    }

    private int findTarget(List<Integer> nums, int s, int t, int[][] dp) {
        if (t < 0) return 0;
        if (s >= nums.size()) return t == 0 ? 1 : 0;

        if (dp[s][t] != -1) return dp[s][t];
        int ways = 0;
        ways += findTarget(nums, s + 1, t - nums.get(s), dp);
        ways += findTarget(nums, s + 1, t, dp);
        dp[s][t] = ways;
        return ways;
    }

    public static void main(String[] args) {
        TargetSum ts = new TargetSum();
        ts.findTargetSumWays(new int[]{1}, 1);
    }
}
