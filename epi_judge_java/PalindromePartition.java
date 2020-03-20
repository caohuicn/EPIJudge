import java.util.Arrays;

public class PalindromePartition {
    public int palindromePartition(String s, int k) {
        int n = s.length();
        char[] chars = s.toCharArray();

        int[][] cost = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                for (int left = i, right = j; left < right; left++, right--) {
                    if (chars[left] != chars[right]) {
                        cost[i][j]++;
                    }
                }
            }
        }

        int[] dp = new int[n + 1];
        for (int i = 1; i < n + 1; i++) {
            dp[i] = cost[0][i - 1];
        }

        for (int r = 2; r < k + 1; r++) {
            int[] ndp = new int[n + 1];
            Arrays.fill(ndp, 999);
            for (int c = 0; c < n + 1; c++) {
                if (c < r ) {
                    ndp[c] = 999;
                } else if (c == r) {
                    ndp[c] = 0;
                } else {
                    for (int j = 0; j < c; j++) {
                        ndp[c] = Math.min(ndp[c], dp[j] + cost[j][c - 1]);
                    }
                }
            }
            dp = ndp;
        }
        return dp[n];
    }

    public static void main(String[] args) {
        System.out.println(new PalindromePartition().palindromePartition("ihhyviwv", 7));
    }
}
