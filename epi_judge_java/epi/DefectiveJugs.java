package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class DefectiveJugs {
  @EpiUserType(ctorParams = {int.class, int.class})

  public static class Jug {
    public int low, high;

    public Jug() {}

    public Jug(int low, int high) {
      this.low = low;
      this.high = high;
    }
  }

  public static class Range {
    public int low, high;

    public Range(int low, int high) {
      this.low = low;
      this.high = high;
    }

    @Override
    public int hashCode() {
      return Objects.hash(low, high);
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != getClass()) {
        return false;
      }
      if (obj == this) return true;
      return low == ((Range)obj).low && high == ((Range)obj).high;
    }
  }

  @EpiTest(testDataFile = "defective_jugs.tsv")

  public static boolean checkFeasible(List<Jug> jugs, int L, int H) {
    HashSet<Range> cache = new HashSet<>();
    return checkFeasibleHelper(jugs, L, H, cache);
  }

  private static boolean checkFeasibleHelper(List<Jug> jugs, int l, int h, HashSet<Range> cache) {
    if (l > h || cache.contains(new Range(l, h)) || (l < 0 && h < 0)) return false;
    for (Jug j : jugs) {
      if ((j.low >= l && j.high <= h) //base case
        || checkFeasibleHelper(jugs, l - j.low, h - j.high, cache)) {
        return true;
      }
    }

    cache.add(new Range(l, h));
    return false;
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "DefectiveJugs.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
