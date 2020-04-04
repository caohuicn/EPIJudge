package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.List;
public class PickingUpCoins {
  @EpiTest(testDataFile = "picking_up_coins.tsv")

  public static int pickUpCoins(List<Integer> coins) {
    return maxPickUpValueInRange(coins, 0, coins.size() - 1, new int[coins.size()][coins.size()]);
  }

  public static int maxPickUpValueInRange(List<Integer> coins, int a, int b, int[][] maxValueRanges) {
    if (a>b) return 0;
    if (maxValueRanges[a][b] == 0) {
      int maxA = coins.get(a) + Math.min(maxPickUpValueInRange(coins, a + 1, b - 1, maxValueRanges),
              maxPickUpValueInRange(coins, a + 2, b, maxValueRanges));
      int maxB = coins.get(b) + Math.min(maxPickUpValueInRange(coins, a + 1, b - 1, maxValueRanges),
              maxPickUpValueInRange(coins, a, b - 2, maxValueRanges));
      maxValueRanges[a][b] = Math.max(maxA, maxB);
    }
    return maxValueRanges[a][b];
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "PickingUpCoins.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
