import java.util.*;

public class TestScraches {
    static class Solution {
        public static void main(String[] args) {
            System.out.println(new Solution().minFlips(new int[][]{{0,0},{0,1}}));
        }
        public int minFlips(int[][] mat) {
            int u = 0;
            int n = mat.length, m = mat[0].length;
            for(int i = 0;i < mat.length;i++){
                for(int j = 0;j < mat[0].length;j++){
                    u |= mat[i][j]<<i*mat[0].length+j;
                }
            }
            int[] ptns = new int[n*m];
            for(int i = 0;i < n;i++){
                for(int j = 0;j < m;j++){
                    for(int k = 0;k < n;k++){
                        for(int l = 0;l < m;l++){
                            if(Math.abs(i-k) + Math.abs(j-l) <= 1){
                                ptns[i*m+j] |= 1<<k*m+l;
                            }
                        }
                    }
                }
            }

            int ans = 99;
            for(int i = 0;i < 1<<n*m;i++){
                int v = u;
                for(int j = 0;j < n*m;j++){
                    if(i<<~j<0){
                        v ^= ptns[j];
                    }
                }
                if(v == 0){
                    ans = Math.min(ans, Integer.bitCount(i));
                }
            }
            if(ans == 99)return -1;
            return ans;
        }
    }

    class Solution1 {

        public int getPattern(int[][] mat) {
            int u = 0;
            int m = mat.length;
            int n = mat[0].length;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++){
                    u |= mat[i][j] << (n * i + j);
                }
            }
            return u;
        }

        public int[] getPatterns(int[][] mat) {
            int m = mat.length;
            int n = mat[0].length;
            int[] ptns = new int[m*n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++){
                    for (int x = 0; x < m; x++) {
                        for (int y = 0; y < n; y++){
                            if (Math.abs(x - i) + Math.abs(y - j) <= 1) {
                                ptns[n * i + j] |= 1 << (n * x + y);
                            }
                        }
                    }
                }
            }
            return ptns;
        }

        public int minFlips(int[][] mat) {
            Map<Integer, Integer> dp = new HashMap<>();
            int min = minFlipsHelper(getPattern(mat), 0, dp, getPatterns(mat));
            return min == Integer.MAX_VALUE ? -1 : min;
        }

        public int minFlipsHelper(int p, int ind, Map<Integer, Integer> dp, int[] ptns) {
            if (p == 0) return 0;

            if (dp.get(p) != null) return dp.get(p);

            if (ind == ptns.length) return Integer.MAX_VALUE;
            //not flip ind
            int without = minFlipsHelper(p, ind + 1, dp, ptns);
            //flip ind
            int v = p ^ ptns[ind];
            int with = minFlipsHelper(v, ind + 1, dp, ptns);
            if (with != Integer.MAX_VALUE) {
                with++;
            }
            int ans = Math.min(without, with);
            dp.put(p, ans);

            return ans;
        }
    }

    static class Solution2 {

        public int getPattern(int[][] mat) {
            int u = 0;
            int m = mat.length;
            int n = mat[0].length;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++){
                    u |= mat[i][j] << (n * i + j);
                }
            }
            return u;
        }

        public int[] getPatterns(int[][] mat) {
            int m = mat.length;
            int n = mat[0].length;
            int[] ptns = new int[m*n];
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++){
                    for (int x = 0; x < m; x++) {
                        for (int y = 0; y < n; y++){
                            if (Math.abs(x - i) + Math.abs(y - j) <= 1) {
                                ptns[n * i + j] |= 1 << (n * x + y);
                            }
                        }
                    }
                }
            }
            return ptns;
        }

        public int minFlips(int[][] mat) {
            int p = getPattern(mat);
            int[] ptns = getPatterns(mat);
            Queue<Integer> q = new LinkedList<>(Arrays.asList(p));
            Set<Integer> seen = new HashSet<>();
            for (int steps = 0; steps <= ptns.length && !q.isEmpty(); steps++) {
                for (int sz = q.size(); sz > 0; sz--) {
                    int curr = q.poll();
                    if (curr == 0) {
                        return steps;
                    }
                    for (int ind = 0; ind < ptns.length; ind++) {
                        int v = curr ^ ptns[ind];
                        if (seen.add(v)) {
                            q.add(v);
                        }
                    }
                }
            }
            return -1;
        }

        public static void main(String[] args) {
            System.out.println(new Solution2().minFlips(new int[][]{{1}}));
        }

    }
}
