package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LongestSubarrayWithAverageConstraint {
  //@EpiTest(testDataFile = "longest_subarray_with_sum_constraint.tsv")

  public static int findLongestSubarrayAvgLessEqualK(List<Integer> A, int k) {
    int n = A.size();
    List<Integer> sums = new ArrayList<>(n);
    int sum = 0;
    for (int i =0; i < n; i++) {
      sum += A.get(i) - k;
      sums.add(sum);
    }
    if (sums.get(n - 1) <= k) return n;

    List<Integer> minPrefixSum = new ArrayList<>(sums);
    for (int i = n - 2; i >= 0; i--) {
      minPrefixSum.set(i, Math.min(minPrefixSum.get(i), minPrefixSum.get(i + 1)));
    }

    int a = 0, b = 0, maxLen = 0;
    while (a < n && b < n) {
      int minSumAB = a > 0 ? minPrefixSum.get(b) - sums.get(a - 1) : minPrefixSum.get(b);
      if (minSumAB <= 0) {
        int len = b - a + 1;
        if (len > maxLen) {
          maxLen = len;
        }
        ++b;
      } else {
        ++a;
      }
    }
    return maxLen;
  }

  public static void main(String[] args) {
//    System.exit(
//        GenericTest
//            .runFromAnnotations(args, "LongestSubarrayWithSumConstraint.java",
//                                new Object() {}.getClass().getEnclosingClass())
//            .ordinal());
    Integer[] arr = new Integer[]{12, -5, -4, 9, 8, -11, -5, 10, -9, 11, -4, 11, -2};
    List<Integer> list = new ArrayList<>(Arrays.asList(arr));
    System.out.println(findLongestSubarrayAvgLessEqualK(list, 1));
  }
}
