package lc;

import java.util.*;

public class Solution1 {
    static class TweetCounts {
        private Map<String, TreeSet<Integer>> tweets = new HashMap<>();

        public TweetCounts() {

        }

        public void recordTweet(String tweetName, int time) {
            TreeSet<Integer> times;
            if (tweets.containsKey(tweetName)) {
                times = tweets.get(tweetName);
            } else {
                times = new TreeSet<>();
                tweets.put(tweetName, times);
            }
            times.add(time);

        }

        public List<Integer> getTweetCountsPerFrequency(String freq, String tweetName, int startTime, int endTime) {
            int delta = 60;
            if ("hour".equals(freq)) {
                delta = 3600;
            } else if ("day".equals(freq)) {
                delta = 86400;
            }
            List<Integer> ans = new ArrayList<>();
            TreeSet<Integer> times = tweets.get(tweetName);
            if (times == null) {
                return ans;
            }
            List<Integer> list = new ArrayList<>(times);
            int ind = Collections.binarySearch(list, startTime);
            if (ind < 0) {
                ind = -(ind + 1);
            }
            if (ind == list.size()) {
                return ans;
            }
            int time = startTime;
            while (time <= endTime && ind < list.size()) {
                int c = 0;
                while (ind < list.size() && list.get(ind) < Math.min(endTime + 1, time + delta)) {
                    c++;
                    ind++;
                }
                ans.add(c);
                time += delta;
            }
            return ans;
        }

        public static void main(String[] args) {
            TweetCounts tweetCounts = new TweetCounts();
            tweetCounts.recordTweet("tweet3", 0);
            tweetCounts.recordTweet("tweet3", 60);
            tweetCounts.recordTweet("tweet3", 10);                             // All tweets correspond to "tweet3" with recorded times at 0, 10 and 60.
            List<Integer> c = tweetCounts.getTweetCountsPerFrequency("minute", "tweet3", 0, 59); // return [2]. The frequency is per minute (60 seconds), so there is one interval of time: 1) [0, 60> - > 2 tweets.
            System.out.println(c);
            c = tweetCounts.getTweetCountsPerFrequency("minute", "tweet3", 0, 60); // return [2, 1]. The frequency is per minute (60 seconds), so there are two intervals of time: 1) [0, 60> - > 2 tweets, and 2) [60,61> - > 1 tweet.
            System.out.println(c);
            tweetCounts.recordTweet("tweet3", 120);                            // All tweets correspond to "tweet3" with recorded times at 0, 10, 60 and 120.
            c = tweetCounts.getTweetCountsPerFrequency("hour", "tweet3", 0, 210);
            System.out.println(c);
        }
    }

    static class Solution {
        int m;
        int n;
        public int maxStudents(char[][] seats) {
            m = seats.length;
            n = seats[0].length;
            int[] keys = new int[m];
            int i = 0;
            for (char[] row : seats) {
                int key = 0;
                for (char c : row) {
                    key = key << 1;
                    if (c == '.') {
                        key++;
                    }
                }
                keys[i++] = key;
            }

            int[][] dp = new int[m][1<<n];
            for (int[] dpr : dp) {
                Arrays.fill(dpr, -1);
            }
            return max(keys, dp, 0, 0);
        }

        int max(int[] keys, int[][] dp, int row, int prevRowMask) {
            //int keys, 1 means seat; in mask, 1 means student
            if (row == m) return 0;
            if (dp[row][prevRowMask] != -1) return dp[row][prevRowMask];

            int stateSize = 1 << n;
            for (int i = 0; i < stateSize; i++) {
                //i is subset of keys[row] (seats available), and no adjacent bits
                if ((i & keys[row]) == i && ((i<<1) & i) == 0) {
                    //i & preRowMask don't have adjacent bits
                    if (((prevRowMask << 1) & i) == 0 && ((i<<1) & prevRowMask) == 0) {
                        dp[row][prevRowMask] = Math.max(dp[row][prevRowMask], max(keys, dp, row + 1, i) + Integer.bitCount(i));
                    }
                }
            }
            return dp[row][prevRowMask];
        }

        public static void main(String[] args) {
            Solution s = new Solution();
            //char[][] cs = new char[][] {{'#','.','#','#','.','#'},{'.','#','#','#','#','.'},{'#','.','#','#','.','#'}};
            //char[][] cs = new char[][] {{'.','#'},{'#','#'},{'#','.'},{'#','#'},{'.','#'}};
            char[][] cs = new char[][] {{'.','.','.','.','#','.','.','.'},{'.','.','.','.','.','.','.','.'},{'.','.','.','.','.','.','.','.'},{'.','.','.','.','.','.','#','.'},{'.','.','.','.','.','.','.','.'},{'.','.','#','.','.','.','.','.'},{'.','.','.','.','.','.','.','.'},{'.','.','.','#','.','.','#','.'}};
            System.out.println(s.maxStudents(cs));
        }
    }

    static class Solution0 {
        public int findTheCity(int n, int[][] edges, int dt) {
            int[][] d = new int[n][n];
            for (int i = 0; i < n; i++) {
                Arrays.fill(d[i], 100000);
                d[i][i] = 0;
            }
            //fw
            for (int[] e : edges) {
                d[e[0]][e[1]] = e[2];
                d[e[1]][e[0]] = e[2];
            }
            for (int k = 0; k < n; k++) {
                for (int i = 0; i < n; i++) {
                    for (int j = 0; j < n; j++) {
                        if (d[i][j] > d[i][k] + d[k][j]) {
                            d[i][j] = d[i][k] + d[k][j];
                        }
                    }
                }
            }
            //filter by dt
            int minNeighbors = n;
            int city = -1;
            for (int i = 0; i < n; i++) {
                int ns = 0;
                for (int j = 0; j < n; j++) {
                    if (i != j && d[i][j] <= dt) {
                        ns++;
                    }
                }
                if (ns <= minNeighbors) {
                    minNeighbors = ns;
                    city = i;
                }
            }
            return city;
        }

        public static void main(String[] args) {
            int[][] edges = new int[][]{{0,1,2},{0,4,8},{1,2,3},{1,4,2},{2,3,1},{3,4,1}};
            System.out.println(new Solution0().findTheCity(5, edges, 2));
        }

    }


       static class ProductOfNumbers {
           List<int[]> products;

           public ProductOfNumbers() {
               products = new ArrayList<>();
           }

           public void add(int num) {
               int size = products.size();
               if (products.isEmpty()) {
                   products.add(new int[]{0, num});
               } else {
                   int[] last = products.get(size - 1);
                   if (num == 0) {
                       products.add(new int[]{size, 0});
                   } else if (last[1] == 0) {
                       products.add(new int[]{size, num});
                   } else {
                       products.add(new int[]{last[0], num * last[1]});
                    }

                   }
               }

               public int getProduct(int k) {
                   int size = products.size();
                   int i = size - k;
                   int[] last = products.get(size - 1);
                   if (last[0] > i || last[1] == 0) return 0;
                   if (last[0] == i) return last[1];
                   int[] prev = products.get(i - 1);
                   return last[1] / prev[1];
               }

           public static void main(String[] args) {
               ProductOfNumbers productOfNumbers = new ProductOfNumbers();
               productOfNumbers.add(3);        // [3]
               productOfNumbers.add(0);        // [3,0]
               productOfNumbers.add(2);        // [3,0,2]
               productOfNumbers.add(5);        // [3,0,2,5]
               productOfNumbers.add(4);        // [3,0,2,5,4]
               productOfNumbers.getProduct(2); // return 20. The product of the last 2 numbers is 5 * 4 = 20
               productOfNumbers.getProduct(3); // return 40. The product of the last 3 numbers is 2 * 5 * 4 = 40
               productOfNumbers.getProduct(4); // return 0. The product of the last 4 numbers is 0 * 2 * 5 * 4 = 0
               productOfNumbers.add(8);        // [3,0,2,5,4,8]
               productOfNumbers.getProduct(2); // return 32. The product of the last 2 numbers is 4 * 8 = 32
           }
        }

        static class SolutionP {
            public boolean isPossible(int[] target) {
                int n = target.length;
                int ones = n;
                TreeSet<Integer> t = new TreeSet<>();
                for (int i = 0; i < n; i++) {
                    t.add(target[i]);
                    if (target[i] == 1) {
                        ones--;
                    }
                }
                List<Integer> c = new ArrayList<>();

                if (t.size() == 1 && t.first() == 1) return true;
                if (t.first() < n && t.first() != 1) return false;
                if (t.size() != n && (t.size() - 1 != ones)) return false; // duplicate
                if (t.first() == 1) t.remove(t.first());
                List<Set<Integer>> dp = new ArrayList<>();
                for (int i = 0; i < n; i++) {
                    dp.add(new HashSet<>());
                }
                return isP(new ArrayList<Integer>(t), c, ones, 0, n, dp);
            }

            boolean isP(List<Integer> t, List<Integer> c, int ones, int index, int sum, List<Set<Integer>> dp) {
                if (index == t.size()) return true;
                int cur = t.get(index);
//                if (dp.get(index).contains(sum)) {
//                    return false;
//                }
                if (sum > cur) {
                    //dp.get(index).add(sum);
                    return false;
                }

                if (sum == cur) {
                    for (int i = 0; i < c.size(); i++) {
                        Integer replaced = c.get(i);
                        List<Integer> newc = new ArrayList<>(c);
                        newc.remove(replaced);
                        if (isP(t, newc, ones, index + 1, sum + sum - replaced, dp)) {
                            return true;
                        }
                    }
                    if (c.isEmpty() && ones > 0) {
                        if (isP(t, c, ones - 1, index + 1, sum + sum - 1, dp)) {
                            return true;
                        }
                    }
                    return false;
                }

                c.add(sum);

                for (int i = 0; i < c.size() - 1; i++) {
                    Integer replaced = c.get(i);
                    List<Integer> newc = new ArrayList<>(c);
                    newc.remove(replaced);
                    if (isP(t, newc, ones, index, sum + sum - replaced, dp)) {
                        return true;
                    }
                }
                if (ones > 0) {
                    if (isP(t, c, ones - 1, index, sum + sum - 1, dp)) {
                        return true;
                    }
                }
                //dp.get(index).add(sum);
                return false;
            }

            public static void main(String[] args) {
                int[] targets = new int[]{1441,1,1101,11,1,41,2601,1,1,1,1};
                System.out.println(new SolutionP().isPossible(targets));
            }

        }
}
