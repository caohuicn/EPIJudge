package lc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Runners {
    /**
     *
     * @param runners [0] speed, [1] location
     * @param stop
     * @return
     */
    public int numOfPasses(int[][] runners, int stop) {
        //sort by location: short -> long, runner can only pass others in front of him/her (smaller index)
        //if the time to finish is shorter
        Arrays.sort(runners, (a, b) -> b[1] - a[1]);
        return numOfPasses(runners, stop, 0, runners.length);
    }

    int numOfPasses(int[][] runners, int stop, int start, int end) {
        if (end - start <= 1) return 0;
        int mid = start + (end - start) / 2;
        return numOfPasses(runners, stop, 0, mid) +
                numOfPasses(runners, stop, mid, end) +
                merge(runners, stop, start, mid, end);
    }

    private int merge(int[][] runners, int stop, int start, int mid, int end) {
        int left = start, right = mid, passes = 0;
        List<int[]> list = new ArrayList<>(end - start);
        while (left < mid && right < end) {
            double leftTime = (double)(stop - runners[left][1]) / runners[left][0];
            double rightTime = (double)(stop - runners[right][1]) / runners[right][0];
            if (leftTime <= rightTime) {
                list.add(runners[left++]);
            } else {
                //if two runners start at the same location, don't count it
                if (runners[left][1] != runners[right][1]) {
                    passes += mid - left;
                }
                list.add(runners[right++]);
            }
        }
        while (left < mid) {
            list.add(runners[left++]);
        }
        while (right < end) {
            list.add(runners[right++]);
        }
        for (int i = start; i < end; i++) {
            runners[i] = list.get(i - start);
        }
        return passes;
    }

    public int kInversePairs(int n, int k) {
        if (n == 0) return 0;
        if (k == 0) return 1;
        int[] dp = new int[k + 1];
        Arrays.fill(dp, 1);
        int M = 1000000007;
        for (int i = 1; i <= n; i++) {
            int[] temp = new int[k + 1];
            temp[0] = 1;
            for (int j = 1; j <= k; j++) {
                int val = (M + dp[j] - ((j >= i) ? dp[j - i] : 0)) % M;
                temp[j] = (val + temp[j - 1]) % M;
            }
            dp = temp;
        }
        return (M + dp[k] - dp[k - 1]) % M;
    }

    public static void main(String[] args) {
//        System.out.println(new Runners().numOfPasses(new int[][]{{1, 1}, {2, 2}, {3, 3}, {4, 4}}, 10));
//        System.out.println(new Runners().numOfPasses(new int[][]{{1, 4}, {2, 4}, {3, 4}, {4, 4}}, 10));
//        System.out.println(new Runners().numOfPasses(new int[][]{{4, 1}, {3, 2}, {2, 3}, {1, 4}}, 10));
        System.out.println(new Runners().kInversePairs(3, 1));
    }
}
