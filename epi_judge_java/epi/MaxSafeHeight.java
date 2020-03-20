package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MaxSafeHeight {
  @EpiTest(testDataFile = "max_safe_height.tsv")

  public static int getHeight(int cases, int drops) {
    List<List<Integer>> dp = new ArrayList<>();
    for (int i = 0; i < cases + 1; i++) {
      dp.add(new ArrayList<>(Collections.nCopies(drops + 1, -1 )));
    }
    return getHeightHelper(cases, drops, dp);
  }

  private static int getHeightHelper(int cases, int drops, List<List<Integer>> dp) {
    if (cases == 0 || drops == 0) return 0;
    if (cases == 1) return drops;
    if (dp.get(cases).get(drops) == -1) {
      dp.get(cases).set(drops, 1 + getHeightHelper(cases - 1, drops - 1, dp) + getHeightHelper(cases, drops - 1, dp));
    }
    return dp.get(cases).get(drops);
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "MaxSafeHeight.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
