package lc;

import java.util.Arrays;

public class CaseDrops {
    public int minDrops(int floors, int cases) {
        //d(f, c) = min(max(d(fx, c-1), d(f - fx - 1, c))) + 1
        int[][] dp = new int[cases + 1][floors + 1];
        Arrays.fill(dp[0], -1);
        for (int i = 1; i <= cases; i++) {
            Arrays.fill(dp[i], floors);
            dp[i][0] = 0;
        }
        for (int i = 0; i <= floors; i++) {
            dp[1][i] = i;
        }
        for (int i = 2; i <= cases; i++) {
            for (int j = 1; j <= floors; j++) {
                //int half = j / 2;
                for (int k = 0; k < j; k++) {
                    dp[i][j] = Math.min(dp[i][j], Math.max(dp[i - 1][k], dp[i][j - k - 1]) + 1);
                }
            }
        }
        return dp[cases][floors];
    }

    public int minCases(int floors, int drops) {
        //c(f, d) = min(max(c(fx, d - 1) + 1, c(f - fx - 1, d - 1)))
        if (floors <= 0) return 0;
        if (drops <= 0) return -1;
        if (drops >= floors) return 1;
        int[][] dp = new int[drops + 1][floors + 1];
        Arrays.fill(dp[0], 9999);
        dp[0][0] = 0;
        for (int i = 1; i <= drops; i++) {
            Arrays.fill(dp[i], 9999);
            dp[i][0] = 0;
            dp[i][i] = 1;
        }
        for (int i = 1; i <= drops; i++) {
            for (int j = 1; j <= floors; j++) {
                for (int k = 0; k < j; k++) {
                    dp[i][j] = Math.min(dp[i][j], Math.max(dp[i - 1][k] + 1, dp[i - 1][j - k - 1]));
                }
            }
        }
        return dp[drops][floors] == 9999 ? -1 : dp[drops][floors];
    }

    public int maxFloors(int cases, int drops) {
        //f(c, d) = f(c - 1, d - 1) + 1 + f(c, d - 1)
        if (cases >= drops) cases = drops;
        int[][] dp = new int[cases + 1][drops + 1];

        for (int i = 1; i <= cases; i++) {
            for (int j = 1; j <= drops; j++) {
                dp[i][j] = dp[i - 1][j - 1] + 1 + dp[i][j - 1];
            }
        }

        return dp[cases][drops];
    }

    public static void main(String[] args) {
        CaseDrops cd = new CaseDrops();
//        System.out.println(cd.minDrops(100, 1));
//        System.out.println(cd.minDrops(100, 2));
//        System.out.println(cd.minDrops(100, 3));
//        System.out.println(cd.minDrops(100, 4));
//        System.out.println(cd.minDrops(100, 5));
//        System.out.println(cd.minDrops(100, 6));
//        System.out.println(cd.minDrops(100, 7));
//        System.out.println(cd.minDrops(100, 8));

//        System.out.println(cd.minCases(100, 1));
//        System.out.println(cd.minCases(100, 6));
//        System.out.println(cd.minCases(100, 7));
//        System.out.println(cd.minCases(100, 8));
//        System.out.println(cd.minCases(100, 9));
//        System.out.println(cd.minCases(100, 14));
//        System.out.println(cd.minCases(100, 51));
//        System.out.println(cd.minCases(100, 100));
        System.out.println(cd.maxFloors(0, 100));
        System.out.println(cd.maxFloors(100, 0));
        System.out.println(cd.maxFloors(3, 2));
        System.out.println(cd.maxFloors(1, 100));
        System.out.println(cd.maxFloors(2, 14));
        System.out.println(cd.maxFloors(2, 9));
        System.out.println(cd.maxFloors(3, 9));
        System.out.println(cd.maxFloors(3, 8));
        System.out.println(cd.maxFloors(4, 8));
        System.out.println(cd.maxFloors(4, 7));
        System.out.println(cd.maxFloors(5, 7));
        System.out.println(cd.maxFloors(5, 6));
    }
}
