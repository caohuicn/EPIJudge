package lc;

import java.util.*;

public class LC179 {
    public int numTimesAllBlue(int[] light) {
        int n= light.length;
        if (n == 0) return 0;
        int ans = 0;
        PriorityQueue<Integer> off = new PriorityQueue<>();
        for (int i = 1; i <= n; i++) {
            off.add(i);
        }
        PriorityQueue<Integer> on = new PriorityQueue<>(Comparator.reverseOrder());
        for (int i = 0; i < light.length; i++) {
            off.remove(light[i]);
            on.add(light[i]);
            if (off.isEmpty() || off.peek() > on.peek()) {
                ans++;
            }
        }

        return ans;
    }

    public double frogPosition1(int n, int[][] edges, int t, int target) {
        LinkedList<Integer> path = new LinkedList<>();
        Map<Integer, List<Integer>> map = new HashMap<>();
        Map<Integer, Integer> pMap = new HashMap<>();
        double p = 1.0;
        for (int[] e : edges) {
            if (!map.containsKey(e[0])) {
                map.put(e[0], new ArrayList<>());
            }
            map.get(e[0]).add(e[1]);
            pMap.put(e[1], e[0]);
        }
        int child = target;
        while (pMap.containsKey(child)) {
            child = pMap.get(child);
            path.addFirst(child);
        }
        if (t < path.size()) return 0;
        if (t > path.size() && map.containsKey(target)) return 0;
        for (int stop : path) {
            List<Integer> children = map.get(stop);
            p *= 1.0/(double)children.size();
        }

        return p;
    }

    public double frogPosition(int n, int[][] edges, int t, int target) {
        List<Integer>[] graph = new List[n + 1];
        for (int i = 0; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] e : edges) {
            graph[e[0]].add(e[1]);
            graph[e[1]].add(e[0]);
        }
        double[] p = new double[n + 1];
        p[1] = 1.0;
        boolean[] v = new boolean[n + 1];
        v[1] = true;
        Queue<Integer> q = new LinkedList<>();
        q.offer(1);
        while (!q.isEmpty() && t-- > 0) {
            for (int size = q.size(); size > 0; size--) {
                int curr = q.poll();
                int children = curr == 1 ? graph[curr].size() : graph[curr].size() - 1;
                for (int child : graph[curr]) {
                    if (v[child]) continue; //this is the parent
                    p[child] = p[curr] * 1.0 / (double)children;
                    v[child] = true;
                    q.offer(child);
                }
                if (children > 0) p[curr] = 0;
                if (curr == target) break;
            }
        }
        return p[target];
    }

    public int findTheLongestSubstring(String s) {
        int res = 0, pat = 0;
        char[] cs = s.toCharArray();
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        for (int i = 0; i < cs.length; i++) {
            pat ^= 1 << ("aeiou".indexOf(cs[i]) + 1) >> 1;
            map.putIfAbsent(pat, i);
            res = Math.max(res, i - map.get(pat));
        }
        return res;
    }

    public static void main(String[] args) {
        //System.out.println(new LC179().numTimesAllBlue(new int[]{4,1,2,3}));
//        7
////                [[1,2],[1,3],[1,7],[2,4],[2,6],[3,5]]
////        2
////        4
        System.out.println(new LC179().frogPosition(7, new int[][]{{1,2},{1,3},{1,7},{2,4},{2,6},{3,5}}, 2, 4));
//        System.out.println(new LC179().findTheLongestSubstring("eleetminicoworoep"));
    }
}
