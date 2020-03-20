package epi;

import java.util.*;

class Solution {
    public static int findCheapestPrice(int n, int[][] flights, int src, int dst, int K) {
        int[][] table = new int[K+1][n];
        for (int i=0;i<=K;i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0) {
                    if (j == src) {
                        table[i][j] = 0;
                    } else {
                        table[i][j] = flights[src][j] > 0 ? flights[src][j] : Integer.MAX_VALUE;
                    }
                } else {
                    int min = table[i-1][j];
                    for (int k = 0; k < n; k++) {
                        if (k != src && k != j && flights[k][j] > 0 && flights[k][j] < Integer.MAX_VALUE && table[i-1][k] != Integer.MAX_VALUE) {
                            min = Math.min(min, flights[k][j] + table[i-1][k]);
                        }
                    }
                    table[i][j] = min;
                }
            }

        }
        if (table[K][dst] == Integer.MAX_VALUE) return -1;
        return table[K][dst];
    }

    static int spanningTree1(int V, int E, ArrayList<ArrayList<Integer>> graph) {
        Set<Integer> set = new HashSet<>();
        Map<Integer, Integer> dist = new HashMap<>();
        PriorityQueue<Map.Entry<Integer, Integer>> q = new PriorityQueue<>(Map.Entry.comparingByValue());
        dist.put(0, 0);//starting from node 0
        q.add(new AbstractMap.SimpleEntry<Integer, Integer>(0, 0));
        int sum = 0;
        while (!q.isEmpty()) {
            Map.Entry<Integer, Integer> entry = q.poll();
            set.add(entry.getKey());
            sum += entry.getValue();
            ArrayList<Integer> edges = graph.get(entry.getKey());
            for (int i = 0; i < V; i++) {
                if (set.contains(i)) continue;
                int e = edges.get(i);
                int d = dist.getOrDefault(i, Integer.MAX_VALUE);
                if (e < d) {
                    if (dist.containsKey(i)) {
                        q.remove(new AbstractMap.SimpleEntry<Integer, Integer>(i, d));
                    }
                    dist.put(i, e);
                    q.add(new AbstractMap.SimpleEntry<Integer, Integer>(i, e));
                }
            }

        }
        return sum;
    }

    static class VD {
        int v;
        int d;
        public VD(int v, int d) {
            this.v = v;
            this.d = d;
        }
        static Comparator<VD> distComparator(){
            return Comparator.comparingInt(vd -> vd.d);
        }
    }
    static int spanningTree(int V, int E, ArrayList<ArrayList<Integer>> graph)
    {
        Set<Integer> set = new HashSet<>();
        Map<Integer, VD> dist = new HashMap<>();
        PriorityQueue<VD> q = new PriorityQueue<>(VD.distComparator());
        VD vd = new VD(0, 0);//starting from node 0
        dist.put(0, vd);
        q.add(vd);
        int sum = 0;
        while (!q.isEmpty()) {
            vd = q.poll();
            set.add(vd.v);
            dist.remove(vd.v);//no longer needed
            sum += vd.d;
            ArrayList<Integer> edges = graph.get(vd.v);
            for (int i = 0; i < V; i++) {
                if (set.contains(i)) continue;
                int e = edges.get(i);
                VD c = dist.get(i);
                int d = c == null ? Integer.MAX_VALUE : c.d;
                if (e < d) {
                    if (dist.containsKey(i)) {
                        q.remove(dist.get(i));
                    }
                    VD nvd = new VD(i, e);
                    dist.put(i, nvd);
                    q.add(nvd);
                }
            }

        }
        return sum;
    }

    public static int networkDelayTimeBellman(int[][] times, int N, int K) {
        //bellman ford
        int[] dist = new int[N + 1]; // dist[0] is just placeholder
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[K] = 0;
        dist[0] = 0;
        for (int i = 1; i<= N; i++ ) {
            for (int j = 0; j < times.length; j++) {
                int[] e = times[j];
                if (dist[e[0]] == Integer.MAX_VALUE) continue;
                int d = dist[e[0]] + e[2];
                if (d < dist[e[1]]) {
                    dist[e[1]] = d;
                }
            }
        }
        int max = 0;
        for (int i: dist) {
            if (i == Integer.MAX_VALUE) return -1;
            max = Math.max(max, i);
        }
        return max;
    }

    public static class ND implements Comparable<ND>{
        int n;
        int d;
        public ND(int n, int d) {
            this.n = n;
            this.d = d;
        }
        public int compareTo(ND nd) {
            return Integer.compare(this.d, nd.d);
        }
    }
    public static int networkDelayTime(int[][] times, int N, int K) {
        //dijkstra
        Map<Integer, ND> map = new HashMap<>();
        boolean[] set = new boolean[N + 1];
        PriorityQueue<ND> q = new PriorityQueue<ND>();
        ND nd = new ND(K, 0);
        q.add(nd);
        map.put(K, nd);
        int max = 0;
        while (!q.isEmpty()) {
            nd = q.poll();
            map.remove(nd);
            set[nd.n] = true;
            max = Math.max(max, nd.d);
            for (int j = 0; j < times.length; j++) {
                int[] e = times[j];
                if (e[0] != nd.n || set[e[1]]) continue;
                int d = nd.d + e[2];
                ND targetND = map.get(e[1]);
                int targetDist = targetND == null? Integer.MAX_VALUE: targetND.d;
                if (d < targetDist) {
                    if (targetND != null) {
                        q.remove(targetND);
                    }
                    ND nnd = new ND(e[1], d);
                    q.add(nnd);
                    map.put(e[1], nnd);
                }
            }
        }
        for (int i = 1; i< set.length; i++)
            if (set[i] == false) return -1;

        return max;
    }
    public static void main(String[] args) {
//        int[][] flights = new int[3][3];
//        flights[0][1] = 100;
//        flights[0][2] = 500;
//        flights[1][2] = 100;
//        System.out.println(findCheapestPrice(3, flights, 0, 2, 1));
        //int[][] times = {{3,5,78},{2,1,1},{1,3,0},{4,3,59},{5,3,85},{5,2,22},{2,4,23},{1,4,43},{4,5,75},{5,1,15},{1,5,91},{4,1,16},{3,2,98},{3,4,22},{5,4,31},{1,2,0},{2,5,4},{4,2,51},{3,1,36},{2,3,59}};
        //System.out.println(networkDelayTime(times, 5,5));
        int[][] times = {{1,2,1},{2,3,2},{1,3,4}};
        System.out.println(networkDelayTime(times, 3,1));
    }
}