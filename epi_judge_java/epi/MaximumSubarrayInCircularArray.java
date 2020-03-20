package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Comparator;
import java.util.List;
public class MaximumSubarrayInCircularArray {
  @EpiTest(testDataFile = "maximum_subarray_in_circular_array.tsv")

  public static int maxSubarraySumInCircular(List<Integer> A) {
    int sum = 0;
    for (Integer i : A) {
      sum += i;
    }
    return Math.max(subarraySum(A, new MaxComparator()), sum - subarraySum(A, new MinComparator()));
  }

  private static int subarraySum(List<Integer> A, Comparator<Integer> c) {
    int maxTill = 0;
    int max = 0;
    for (Integer i: A) {
      maxTill = c.compare(maxTill + i, i);
      max = c.compare(max, maxTill);
    }
    return max;
  }

  static class MaxComparator implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
      return (o1 > o2 ? o1 : o2);
    }
  }

  static class MinComparator implements Comparator<Integer> {
    @Override
    public int compare(Integer o1, Integer o2) {
      return (o1 < o2 ? o1 : o2);
    }
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "MaximumSubarrayInCircularArray.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
