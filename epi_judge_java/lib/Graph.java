package lib;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    public static class GraphVertex {
        public List<GraphVertex> edges;

        public GraphVertex() { edges = new ArrayList<>(); }
    }

    public static class Edge {
        public int from;
        public int to;

        public Edge(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }

    public static List<GraphVertex> buildGraph(List<Edge> edges, int k) {
        List<GraphVertex> graph = new ArrayList<>();
        for (int i = 0; i < k; i++) {
            graph.add(new GraphVertex());
        }
        for (Edge e : edges) {
            if (e.from < 0 || e.from >= k || e.to < 0 || e.to >= k) {
                throw new RuntimeException("Invalid vertex index");
            }
            graph.get(e.from).edges.add(graph.get(e.to));
        }
        return graph;
    }
}
