package lc;

import java.util.*;

public class LinePoints {
    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object p) {
            return p != null && p.getClass() == Point.class && ((Point) p).x == x && ((Point) p).y == y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    static class Rational {
        int n, d;

        Rational(int n, int d) {
            this.n = n;
            this.d = d;
            normalize();
        }

        private void normalize() {
            if (d == 0) return;
            int gcd = gcd(Math.abs(n), Math.abs(d));
            n /= gcd;
            d /= gcd;
            if (d < 0) {
                d = -d;
                n = -n;
            }
        }

        private int gcd(int a, int b) {
            if (a == 0) return b;
            if (b == 0) return a;
            if (a == b) {
                return a;
            } else if (a > b) {
                return gcd(a % b, b);
            } else {
                return gcd(a, b % a);
            }
        }


        @Override
        public boolean equals(Object r) {
            return r != null && r.getClass() == Rational.class && ((Rational) r).n == n && ((Rational) r).d == d;
        }

        @Override
        public int hashCode() {
            return Objects.hash(n, d);
        }

    }

    static class Line {
        Rational yi, slope;

        Line(Point a, Point b) {
            slope = new Rational(b.y - a.y, b.x - a.x);
            if (b.x == a.x) {
                yi = new Rational(a.x, 0);
            } else {
                yi = new Rational(b.x * a.y - a.x * b.y, b.x - a.x);
            }
        }

        @Override
        public boolean equals(Object l) {
            return l != null && l.getClass() == Line.class && ((Line) l).yi.equals(yi) && ((Line) l).slope.equals(slope);
        }

        @Override
        public int hashCode() {
            return Objects.hash(yi, slope);
        }

    }

    public int maxPoints(int[][] points) {
        Map<Line, Set<Point>> map = new HashMap<>();
        for (int i = 0; i < points.length; i++) {
            Point a = new Point(points[i][0], points[i][1]);
            for (int j = i + 1; j < points.length; j++) {
                Point b = new Point(points[j][0], points[j][1]);
                Line l = new Line(a, b);
                if (!map.containsKey(l)) {
                    map.put(l, new HashSet<>());
                }
                map.get(l).add(a);
                map.get(l).add(b);
            }
        }
        int ans = 2;
        for (Set set : map.values()) {
            ans = Math.max(ans, set.size());
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(new LinePoints().maxPoints(new int[][]{{1, 1}, {2, 2}, {3, 3}}));
    }

}
