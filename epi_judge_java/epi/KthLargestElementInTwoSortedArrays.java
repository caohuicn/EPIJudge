package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import java.util.List;
public class KthLargestElementInTwoSortedArrays {
  @EpiTest(testDataFile = "kth_largest_element_in_two_sorted_arrays.tsv")

  public static int findKthNTwoSortedArrays(List<Integer> A, List<Integer> B,
                                            int k) {
    int b = Math.max(0, k - B.size());
    int t = Math.min(A.size(), k);
    while (b < t) {
      int x = b + (t - b) / 2;
      int ax1 = x <= 0 ? Integer.MIN_VALUE : A.get(x - 1);
      int ax = x >= A.size() ? Integer.MAX_VALUE : A.get(x);
      int bkx1 = (k - x) <= 0 ? Integer.MIN_VALUE : B.get(k - x - 1);
      int bkx = (k - x) >= B.size() ? Integer.MAX_VALUE : B.get(k - x);
      if (ax < bkx1) {
        b = x + 1;
      } else if (ax1 > bkx) {
        t = x - 1;
      } else {
        //ax >= bkx1 && ax1 <= bkx
        return Math.max(bkx1, ax1);
      }
    }
    int ax = b <= 0 ? Integer.MIN_VALUE : A.get(b - 1);
    int bkx1 = (k - b - 1) < 0 ? Integer.MIN_VALUE : B.get(k - b - 1);
    return Math.max(ax, bkx1);
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "KthLargestElementInTwoSortedArrays.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
//    findKthNTwoSortedArrays(Arrays.asList(-42, -41, -40, -34, -33, -32, -32, -30, -30, -29, -27, -25, -17, -14, -12, -10, -5, 1, 4, 5, 6, 7, 8, 11, 12, 16, 17, 17, 18, 18, 23, 24, 26, 30, 30, 31, 32, 33, 33, 34, 38, 38),
//    Arrays.asList(-2, 2, 3),
//    21);
  }
}
