package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public class MinimumWeightPathInATriangle {
  @EpiTest(testDataFile = "minimum_weight_path_in_a_triangle.tsv")

  public static int minimumPathTotal(List<List<Integer>> triangle) {
    if (triangle.isEmpty() || triangle.get(0).isEmpty()) {
      return 0;
    }
    List<Integer> prev = new ArrayList<>();
    prev.add(triangle.get(0).get(0));
    for (int i = 1; i < triangle.size(); i++) {
      List<Integer> row = triangle.get(i);
      List<Integer> curr = new ArrayList<>(row.size());
      curr.add(row.get(0) + prev.get(0));
      for (int j = 1; j < row.size() - 1; j++) {
        curr.add(row.get(j) + Math.min(prev.get(j-1), prev.get(j)));
      }
      curr.add( row.get(row.size() - 1) + prev.get(prev.size() - 1));
      prev = curr;
    }
    return Collections.min(prev);
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "MinimumWeightPathInATriangle.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
