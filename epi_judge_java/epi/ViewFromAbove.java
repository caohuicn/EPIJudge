package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;

import java.util.*;

public class ViewFromAbove {

    @EpiUserType(ctorParams = {int.class, int.class, int.class, int.class})
    public static class LineSegment implements Comparable<LineSegment>{
        public int left, right; // Specifies the interval.
        public int color;
        public int height;

        public LineSegment(int left, int right, int color, int height) {
            this.left = left;
            this.right = right;
            this.color = color;
            this.height = height;
        }

        @Override
        public String toString() {
            return "[" + left + "," + right + "," + color + "," + height + "]";
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || obj.getClass() != this.getClass()) {
                return false;
            }
            LineSegment s = (LineSegment)obj;
            return left == s.left && right == s.right && color == s.color && height == s.height;
        }

        @Override
        public int compareTo(LineSegment o) {
            return Integer.compare(height, o.height);
        }
    }

    public static class Endpoint implements Comparable<Endpoint> {
        private boolean isLeft;
        private LineSegment line;

        public Endpoint(boolean isLeft, LineSegment line) {
            this.isLeft = isLeft;
            this.line = line;
        }

        public int value() {
            return isLeft ? line.left : line.right;
        }

        @Override
        public int compareTo(Endpoint o) {
            if (value() != o.value()) {
                return Integer.compare(value(),
                        o.value());
            }
            return isLeft ? (o.isLeft ? 0 : 1) : (o.isLeft ? -1 : 0);
        }
    }

    @EpiTest(testDataFile = "view_from_above.tsv")
    public static List<LineSegment> calculateViewFromAbove(List<LineSegment> A) {
        if (A == null || A.size() == 0) {
            return new ArrayList<>();
        }
        List<Endpoint> sortedEndpoints = new ArrayList<>();
        for (LineSegment a : A) {
            sortedEndpoints.add(new Endpoint(true, a));
            sortedEndpoints.add(new Endpoint(false, a));
        }
        Collections.sort(sortedEndpoints);
        List<LineSegment> result = new ArrayList<>();
        TreeSet<LineSegment> set = new TreeSet<>();
        Endpoint start = null;
        //add to result when: new segment is higher or current segment ends
        //TODO do we have to merge segments with the same color?
        for (Endpoint endpoint : sortedEndpoints) {
            if (endpoint.isLeft) {
                if (start == null) {
                    start = endpoint;
                } else if (endpoint.line.height >= set.last().height) {
                    //when a higher segment appears, current segment is blocked
                    if (endpoint.value() > start.value()) {
                        result.add(new LineSegment(start.value(), endpoint.value(), start.line.color, start.line.height));
                    }
                    start = endpoint;
                }
                set.add(endpoint.line);
            } else {
                if (start != null) {
                    LineSegment top = set.last();
                    set.remove(endpoint.line);//might return false because it might have been removed
                    if (endpoint.line.height == start.line.height) {
                        //segment ends
                        if (endpoint.value() > start.value()) {
                            result.add(new LineSegment(start.value(), endpoint.value(), start.line.color, start.line.height));
                        }
                        //pop until a segment is longer
                        while (!set.isEmpty()) {
                            top = set.last();
                            if (top.right == endpoint.value()) {
                                set.remove(top);
                            } else {
                                break;
                            }
                        }
                        if (set.isEmpty()) {
                            start = null;
                        } else {
                            //when segment ends, underlying segment is exposed
                            LineSegment next = new LineSegment(endpoint.value(), top.right, top.color, top.height);
                            start = new Endpoint(true, next);
                        }
                    }
                }

            }
        }
        return result;
    }
    public static List<LineSegment> calculateViewFromAbove1(List<LineSegment> A) {
        if (A == null || A.size() == 0) {
            return new ArrayList<>();
        }
        List<Endpoint> sortedEndpoints = new ArrayList<>();
        for (LineSegment a : A) {
            sortedEndpoints.add(new Endpoint(true, a));
            sortedEndpoints.add(new Endpoint(false, a));
        }
        Collections.sort(sortedEndpoints);
        List<LineSegment> result = new ArrayList<>();
        int prevXAxis = sortedEndpoints.get(0).value(); // Leftmost end point.
        LineSegment prev = null;//left: prevPrevX, right: prevX
        TreeMap<Integer, LineSegment> activeLineSegments = new TreeMap<>();//saves original line segments
        for (Endpoint endpoint : sortedEndpoints) {
            if (!activeLineSegments.isEmpty() && prevXAxis != endpoint.value()) {
                if (prev == null) { // Found first segment.
                    prev = new LineSegment(
                            prevXAxis, endpoint.value(),
                            activeLineSegments.lastEntry().getValue().color,
                            activeLineSegments.lastEntry().getValue().height);
                } else {
                    if (prev.height == activeLineSegments.lastEntry().getValue().height
                            && prev.color == activeLineSegments.lastEntry().getValue().color
                            && prevXAxis == prev.right) {
                        //prev is still the highest segment, extend to current endpoint
                        prev.right = endpoint.value();
                    } else {
                        //either color change, or another higher segment is added into the map
                        result.add(prev);
                        prev = new LineSegment(
                                prevXAxis, endpoint.value(),
                                activeLineSegments.lastEntry().getValue().color,
                                activeLineSegments.lastEntry().getValue().height);
                    }
                }
            }
            prevXAxis = endpoint.value();
            if (endpoint.isLeft) { // Left end point.
                activeLineSegments.put(endpoint.line.height, endpoint.line);
            } else { // Right end point.
                activeLineSegments.remove(endpoint.line.height);
            }
        }
// Output the remaining segment (if any).
        if (prev != null) {
            result.add(prev);
        }
        return result;
    }

    public static void main(String[] args) {
        System.exit(
                GenericTest
                        .runFromAnnotations(args, "ViewFromAbove.java",
                                new Object() {}.getClass().getEnclosingClass())
                        .ordinal());
    }
}
