package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class KthLargestInArray {
  // The numbering starts from one, i.e., if A = [3,1,-1,2] then
  // findKthLargest(A, 1) returns 3, findKthLargest(A, 2) returns 2,
  // findKthLargest(A, 3) returns 1, and findKthLargest(A, 4) returns -1.
  @EpiTest(testDataFile = "kth_largest_in_array.tsv")
  public static int findKthLargest(int k, List<Integer> A) {
    int left = 0, right = A.size() - 1;
    Random r = new Random();
    while (left < right) {
      int pIndex = left + r.nextInt(right - left + 1);
      int newPIndex = partition(A, pIndex, left, right);
      if (newPIndex == k - 1) {
        return A.get(newPIndex);
      } else if (newPIndex > k - 1) {
        right = newPIndex - 1;
      } else {
        left = newPIndex + 1;
      }
    }
    return A.get(left);
  }

  private static int partition(List<Integer> A, int pIndex, int left, int right) {
    int newPIndex = left;
    int pivot = A.get(pIndex);
    Collections.swap(A, pIndex, right);
    for (int i = left; i < right; i++) {
      if (COMPARATOR.compare(A.get(i), pivot) < 0 ) {
        Collections.swap(A, i, newPIndex++);
      }
    }
    Collections.swap(A, newPIndex, right);
    return newPIndex;
  }

  static Comparator<Integer> COMPARATOR = Comparator.<Integer>comparingInt(a ->a).reversed();
  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "KthLargestInArray.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
