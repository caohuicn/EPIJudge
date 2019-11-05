package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;

import java.util.Arrays;
import java.util.List;
public class RoadNetwork {
  @EpiUserType(ctorParams = {int.class, int.class, int.class})

  public static class HighwaySection {
    public int x, y, distance;

    public HighwaySection(int x, int y, int distance) {
      this.x = x;
      this.y = y;
      this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      HighwaySection that = (HighwaySection)o;
      return x == that.x && y == that.y && distance == that.distance;
    }

    @Override
    public String toString() {
      return "[" + x + ", " + y + ", " + distance + "]";
    }
  }

  @EpiTest(testDataFile = "road_network.tsv")

  public static HighwaySection
  findBestProposals(List<HighwaySection> H, List<HighwaySection> P, int n) {
    int[][] dist = new int[n][n];
    for (int i = 0; i < dist.length; i++) {
      Arrays.fill(dist[i], Integer.MAX_VALUE);
      dist[i][i] = 0;
    }
    for (int i = 0; i < H.size(); i++) {
      HighwaySection hs = H.get(i);
      dist[hs.x][hs.y] = dist[hs.y][hs.x] = hs.distance;
    }
    //existing dist matrix with floyd
    for (int k = 0; k < n; k++) {
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          //does city k help?
          //either dist[i][k] + dist[k][j], or dist[i][j]
          if (dist[i][k] == Integer.MAX_VALUE || dist[k][j] == Integer.MAX_VALUE) continue;
          int distWithK = dist[i][k] + dist[k][j];
          if (distWithK < dist[i][j]) {
            dist[i][j] = distWithK;
          }
        }
      }
    }
    int total = getTotalDist(dist, n);
    int minTotal = total;
    HighwaySection result = null;
    for (int k = 0; k < P.size(); k++) {
      int[][] clone = cloneDist(dist, n);
      HighwaySection hs = P.get(k);
      clone[hs.x][hs.y] = clone[hs.y][hs.x] = hs.distance;
      for (int i = 0; i < n; i++) {
        for (int j = 0; j < n; j++) {
          int distWithXY = clone[i][hs.x] + hs.distance + clone[hs.y][j];
          int distWithYX = clone[i][hs.y] + hs.distance + clone[hs.x][j];
          int distWithK = Math.min(distWithXY, distWithYX);
          if (distWithK < clone[i][j]) {
            clone[i][j] = distWithK;
          }
        }
      }
      int pTotal = getTotalDist(clone, n);
      if (pTotal < minTotal) {
        result = hs;
        minTotal = pTotal;
      }
    }
    return result;
  }

  public static int getTotalDist(int[][] dist, int n) {
    int total = 0;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        total += dist[i][j];
      }
    }
    return total;
  }

  public static int[][] cloneDist(int[][] dist, int n) {
    int[][] clone = new int[n][n];
    for (int i = 0; i < n; i++) {
      System.arraycopy(dist[i], 0, clone[i], 0, n);
    }
    return clone;
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "RoadNetwork.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
