package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;
import org.w3c.dom.css.Rect;

import java.util.*;

public class DrawingSkyline {
  @EpiUserType(ctorParams = {int.class, int.class, int.class})

  public static class Rectangle {
    public int left, right, height;

    public Rectangle(int left, int right, int height) {
      this.left = left;
      this.right = right;
      this.height = height;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Rectangle rectangle = (Rectangle)o;

      if (left != rectangle.left) {
        return false;
      }
      if (right != rectangle.right) {
        return false;
      }
      return height == rectangle.height;
    }

    @Override
    public String toString() {
      return "[" + left + ", " + right + ", " + height + ']';
    }
  }

  static class Endpoint {
    Rectangle r;
    boolean start;
    public Endpoint(Rectangle r, boolean start) {
      this.r = r;
      this.start = start;
    }

    int x() {
      return start? r.left : r.right;
    }

    int y() {
      return r.height;
    }
  }

  @EpiTest(testDataFile = "drawing_skyline.tsv")
  public static List<Rectangle> drawingSkylines(List<Rectangle> buildings) {
    if (buildings.size() <= 1) return buildings;
    List<Endpoint> endpoints = getEndpoints(buildings);
    Collections.sort(endpoints, (e1, e2) -> {
      if (e1.x() != e2.x()) return e1.x() - e2.x();
      if (e1.start == e2.start) return 0;
      if (e1.start) return -1;
      return 1;
    });

    List<Rectangle> res = new ArrayList<>();
    //The comparator cannot just compare height, which will ignore rectangles with the same height
    TreeSet<Rectangle> heights = new TreeSet<>((r1, r2) -> {
      if (r1.height != r2.height) return r1.height - r2.height;
      if (r1.left != r2.left) return r1.left - r2.left;
      if (r1.right != r2.right) return r1.right - r2.right;
      return r1.hashCode() - r2.hashCode();
    });
    int left = endpoints.get(0).x();
//    int h = endpoints.get(0).y();
    for (Endpoint e : endpoints) {
      if (e.start) {
        int h = heights.isEmpty()? 0 : heights.last().height;
        heights.add(e.r);
        if (e.x() > left) {
          if (h < e.y()) {
            //have to merge to pass the test :(
            if (res.size() >= 1) {
              Rectangle last = res.get(res.size() - 1);
              if (last.height == h && last.right == left - 1) {
                last.right = e.x() - 1;
                left = e.x();
                continue;
              }
            }
            res.add(new Rectangle(left, e.x() - 1, h));
            left = e.x();
          }
        }
      } else {
        int h = heights.isEmpty()? 0 : heights.last().height;
        heights.remove(e.r);
        //removed the highest rect, ignore past items
        if (e.y() == h && e.x() >= left) {
          if (heights.isEmpty() || heights.last().height < h) {
            if (res.size() >= 1) {
              Rectangle last = res.get(res.size() - 1);
              if (last.height == h && last.right == left - 1) {
                last.right = e.x();
                left = e.x() + 1;
                continue;
              }
            }
            res.add(new Rectangle(left, e.x(), h));
            left = e.x() + 1;
//            h = heights.isEmpty() ? 0 : heights.last().height;
          }
        }

      }
    }
    return res;
  }

  private static List<Endpoint> getEndpoints(List<Rectangle> buildings) {
    List<Endpoint> eps = new ArrayList<>();
    for (Rectangle r : buildings) {
      eps.add(new Endpoint(r, true));
      eps.add(new Endpoint(r, false));
    }
    return eps;
  }

  public static List<Rectangle> drawingSkylines2(List<Rectangle> buildings) {
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    for (Rectangle b : buildings) {
      min = Math.min(min, b.left);
      max = Math.max(max, b.right);
    }
    if (buildings.isEmpty()) return new ArrayList<>();
    Collections.sort(buildings, Comparator.comparingInt(r -> r.left));
    List<Integer> heights = drawSkylines(buildings, 0, buildings.size());
    return getRectangles(min, max, heights);
  }

  /**
   *
   * @param buildings
   * @param start
   * @param end exclusive
   * @return
   */
  static List<Integer> drawSkylines(List<Rectangle> buildings, int start, int end) {
    List<Integer> res = new ArrayList<>();
    if (end - start <= 1) {
      Rectangle b = buildings.get(start);
      for (int i = b.left; i <= b.right; i++) {
          res.add(b.height);
      }
      return res;
    }
    int mid = (end - start) / 2 + start;
    List<Integer> left = drawSkylines(buildings, start, mid);
    List<Integer> right = drawSkylines(buildings, mid, end);
    int leftStart = buildings.get(start).left;
    int rightStart = buildings.get(mid).left;
    int i = 0, j = 0;

    while (i < left.size() && j < right.size()) {
      if (i + leftStart < j + rightStart){
        res.add(left.get(i++));
      } else if (i + leftStart > j + rightStart) {
        res.add(right.get(j++));
      } else {
        if (left.get(i) > right.get(j)) {
          res.add(left.get(i++));
          j++;
        } else {
          res.add(right.get(j++));
          i++;
        }
      }
    }
    while (i < left.size()) {
      res.add(left.get(i++));
    }
    while (j < right.size()) {
      res.add(right.get(j++));
    }
    return res;
  }
  public static List<Rectangle> drawingSkylines1(List<Rectangle> buildings) {
    int min = Integer.MAX_VALUE;
    int max = Integer.MIN_VALUE;
    for (Rectangle b : buildings) {
      min = Math.min(min, b.left);
      max = Math.max(max, b.right);
    }
    List<Integer> heights = new ArrayList<>(Collections.nCopies(max - min + 1, 0));
    for (Rectangle b : buildings) {
      for (int i = b.left; i <= b.right; i++) {
        heights.set(i - min, Math.max(b.height, heights.get(i - min)));
      }
    }

    List<Rectangle> res = getRectangles(min, max, heights);
    return res;
  }

  private static List<Rectangle> getRectangles(int min, int max, List<Integer> heights) {
    List<Rectangle> res = new ArrayList<>();
    int left = 0;
    for (int i = 1; i < heights.size(); i++) {
      if (heights.get(i) != heights.get(i - 1)) {
        res.add(new Rectangle(left + min, i - 1 + min, heights.get(i - 1)));
        left = i;
      }
    }
    res.add(new Rectangle(left + min, max, heights.get(heights.size() - 1)));
    return res;
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "DrawingSkyline.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
