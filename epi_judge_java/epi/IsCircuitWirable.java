package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;
import epi.test_framework.TimedExecutor;

import java.util.*;

public class IsCircuitWirable {

  public static class GraphVertex {
    public int d = -1;
    public List<GraphVertex> edges = new ArrayList<>();
  }

  public static boolean isAnyPlacementFeasible(List<GraphVertex> graph) {
    Map<GraphVertex, Integer> d = new HashMap<>();
    for(GraphVertex v: graph) {
      if (!d.containsKey(v)) {
        if (!BFS(v, d)) {
          return false;
        }
      }
    }
    return true;
  }

  public static boolean BFS(GraphVertex v, Map<GraphVertex, Integer> d) {
    Queue<GraphVertex> q = new LinkedList<>();
    q.add(v);
    d.put(v, 0);
    while (!q.isEmpty()) {
      GraphVertex c = q.remove();
      for(GraphVertex n: c.edges) {
        if (d.containsKey(n)) {
          if (d.get(c) == d.get(n)) return false;
        } else {
          d.put(n, d.get(c) + 1);
          q.add(n);
        }
      }
    }
    return true;
  }

  public static boolean isAnyPlacementFeasible1(List<GraphVertex> graph) {
    Set<GraphVertex> s = new HashSet<>();
    Set<GraphVertex> t = new HashSet<>();

    for(GraphVertex v: graph) {
      if (!s.contains(v) && !t.contains(v)) {
        if (!BFS(v, s,t )) {
          return false;
        }
      }
    }
    return true;
  }

  public static boolean BFS(GraphVertex v, Set<GraphVertex> s , Set<GraphVertex> t) {
    Queue<GraphVertex> q = new LinkedList<>();
    q.add(v);
    s.add(v);
    while (!q.isEmpty()) {
      GraphVertex c = q.remove();
      boolean addToS = !s.contains(c);
      for(GraphVertex n: c.edges) {
        boolean inS = s.contains(n);
        boolean inT = t.contains(n);
        if (addToS) {
          if (inT) return false;
          if (!inS) {
            q.add(n);
            s.add(n);
          }
        } else {
          if (inS) return false;
          if (!inT){
            q.add(n);
            t.add(n);
          }
        }

      }
    }
    return true;
  }

  @EpiUserType(ctorParams = {int.class, int.class})
  public static class Edge {
    public int from;
    public int to;

    public Edge(int from, int to) {
      this.from = from;
      this.to = to;
    }
  }

  @EpiTest(testDataFile = "is_circuit_wirable.tsv")
  public static boolean isAnyPlacementFeasibleWrapper(TimedExecutor executor,
                                                      int k, List<Edge> edges)
      throws Exception {
    if (k <= 0)
      throw new RuntimeException("Invalid k value");
    List<GraphVertex> graph = new ArrayList<>();
    for (int i = 0; i < k; i++)
      graph.add(new GraphVertex());
    for (Edge e : edges) {
      if (e.from < 0 || e.from >= k || e.to < 0 || e.to >= k)
        throw new RuntimeException("Invalid vertex index");
      graph.get(e.from).edges.add(graph.get(e.to));
    }

    return executor.run(() -> isAnyPlacementFeasible(graph));
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "IsCircuitWirable.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
