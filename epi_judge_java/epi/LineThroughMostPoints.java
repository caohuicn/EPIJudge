package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;
import java.math.BigInteger;
import java.util.*;
public class LineThroughMostPoints {
  @EpiUserType(ctorParams = {int.class, int.class})

  public static class Point {
    public int x, y;

    public Point(int x, int y) {
      this.x = x;
      this.y = y;
    }
  }

  public static class Rational {
    int a, b;

    public Rational(int a, int b) {
      canonize(a, b);
    }
    private void canonize(int a, int b) {
      int gcd = BigInteger.valueOf(a).gcd(BigInteger.valueOf(b)).intValue();
      this.a = a/gcd;
      this.b = b/gcd;
      if (this.b < 0) {
        this.b = -b;
        this.a = -a;
      }
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != Rational.class) {
        return false;
      }
      return ((Rational)obj).a == a && ((Rational)obj).b == b;
    }

    @Override
    public int hashCode() {
      return Objects.hash(a, b);
    }
  }
  public static class Line {
    Rational slope;
    Rational yInterception;
    public Line(Point a, Point b) {
      if (a.x == b.x) {
        slope = new Rational(1, 0);
        yInterception = new Rational(a.x, 1);
      } else {
        slope = new Rational(b.y - a.y, b.x - a.x);
        yInterception = new Rational(b.x * a.y - a.x * b.y, b.x - a.x);
      }
    }

    @Override
    public boolean equals(Object obj) {
      if (obj == null || obj.getClass() != Line.class) {
        return false;
      }
      return ((Line)obj).slope.equals(slope) && ((Line)obj).yInterception.equals(yInterception);
    }

    @Override
    public int hashCode() {
      return Objects.hash(slope, yInterception);
    }
  }
  @EpiTest(testDataFile = "line_through_most_points.tsv")

  public static int findLineWithMostPoints(List<Point> points) {
    if (points == null || points.size() == 0) return 0;
    if (points.size() == 1) return 1;
    //equivalent to canonize: b.x >= a.x
    Collections.sort(points, Comparator.comparingInt(a -> a.x));
//    Map<Line, Integer> map = new HashMap<>();
////    for (int i = 0; i < points.size(); i++) {
////      for (int j = i + 1; j < points.size(); j++) {
////        Line l = new Line(points.get(i), points.get(j));
////        map.computeIfPresent(l, (k, v) -> v + 1);
////        map.putIfAbsent(l, 1);
////      }
////    }
////    return Collections.max(map.entrySet(), Comparator.comparingInt(e -> e.getValue())).getValue();
    Map<Line, Set<Point>> map = new HashMap<>();
    for (int i = 0; i < points.size(); i++) {
      Point a = points.get(i);
      for (int j = i + 1; j < points.size(); j++) {
        Point b = points.get(j);
        Line l = new Line(a, b);
        map.putIfAbsent(l, new HashSet<>());
        map.computeIfPresent(l, (k, v) -> {v.add(a); v.add(b); return v;});
      }
    }

    return Collections.max(map.entrySet(), Comparator.comparingInt(e -> e.getValue().size())).getValue().size();
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "LineThroughMostPoints.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
