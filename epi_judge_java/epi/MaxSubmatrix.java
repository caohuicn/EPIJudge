package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
public class MaxSubmatrix {

  @EpiTest(testDataFile = "max_submatrix.tsv")
  public static int maxRectangleSubmatrixMN(List<List<Boolean>> A) {
    if (A == null || A.size() == 0) return 0;
    int[] h = new int[A.get(0).size()];
    int maxArea = 0;
    for (int i = 0; i< A.size(); i++) {
      for (int j = 0; j < A.get(i).size(); j++) {
        h[j] = A.get(i).get(j) ? h[j] + 1 : 0;
      }
      maxArea = Math.max(maxArea, maxRectCurrentLine(h));
    }
    return maxArea;
  }

  private static int maxRectCurrentLine(int[] h) {
    Deque<Integer> pillars = new LinkedList<>();
    int maxArea = 0;
    for (int i = 0; i <= h.length; i++) {
      while (!pillars.isEmpty() && shouldPop(h, i, pillars.peek())) {
        int pi = pillars.removeFirst();
        int height = h[pi];
        int width = pillars.isEmpty() ? i : i - pillars.peekFirst() - 1;
        maxArea = Math.max(maxArea, height * width);
      }
      pillars.addFirst(i);
    }
    return maxArea;
  }

  private static boolean shouldPop(int[] h, int i, Integer peek) {
    return i < h.length ? h[i] < h[peek] : true;
  }

//  private static int maxRectCurrentLine(int[] h) {
//    //0: index 1: height; increasing stack, calculate area when popped
//    Deque<int[]> pillars = new LinkedList<>();
//    int maxArea = 0;
//    for (int i = 0; i < h.length; i++) {
//      if (pillars.isEmpty() || h[i] > pillars.peek()[1]) {
//        pillars.addFirst(new int[]{i, h[i]});
//      } else {
//        int[] top = pillars.peek();
//        while (!pillars.isEmpty() && h[i] < pillars.peek()[1]) {
//          int[] prev = pillars.pop();
//          int width = top[0] - (pillars.isEmpty() ? -1 : pillars.peek()[0]);
//          int height = prev[1];
//          maxArea = Math.max(maxArea, width * height);
//        }
//        if (!pillars.isEmpty() && h[i] == pillars.peek()[1]) {
//          //update index
//          pillars.peek()[0] = i;
//        } else {
//          pillars.addFirst(new int[]{i, h[i]});
//        }
//      }
//    }
//
//    if (!pillars.isEmpty()) {
//      int[] top = pillars.peek();
//      while (!pillars.isEmpty()) {
//        int[] prev = pillars.pop();
//        int width = top[0] - (pillars.isEmpty() ? -1 : pillars.peek()[0]);
//        int height = prev[1];
//        maxArea = Math.max(maxArea, width * height);
//      }
//    }
//    return maxArea;
//  }

  public static int maxRectangleSubmatrixMN2(List<List<Boolean>> A) {
    if (A == null || A.size() == 0) return 0;
    MaxHW[][] maxHWLine = new MaxHW[A.size()][A.get(0).size()];
    for (int i = 0; i< A.size(); i++) {
      for (int j = 0; j < A.get(i).size(); j++) {
        if (A.get(i).get(j)) {
          int h = 1,w = 1;
          if (i > 0) {
            h = maxHWLine[i - 1][j].h + 1;
          }
          if (j > 0) {
            w = maxHWLine[i][j - 1].w + 1;
          }
          maxHWLine[i][j] = new MaxHW(h, w);
        } else {
          maxHWLine[i][j] = new MaxHW(0, 0);
        }
      }
    }

    int maxArea = 0;
    //reverse traversal is faster because we don't need to calculate smaller areas
    for (int i = A.size() - 1; i >= 0; i--) {
      for (int j = A.get(i).size() - 1; j >= 0; j--) {
        if (A.get(i).get(j)) {
          int h = maxHWLine[i][j].h;
          int w = maxHWLine[i][j].w;
          if (h * w > maxArea) {
            for (int k = 1; k <= h; k++) {
              w = Math.min(w, maxHWLine[i - k + 1][j].w);
              maxArea = Math.max(maxArea, k * w);
            }
          }
        }
      }
    }
//    for (int i = 0; i < A.size(); i++) {
//      for (int j = 0; j < A.get(i).size(); j++) {
//        if (A.get(i).get(j)) {
//          int h = maxHWLine[i][j].h;
//          int w = maxHWLine[i][j].w;
//          if (h * w > maxArea) {
//            for (int k = 1; k <= h; k++) {
//              w = Math.min(w, maxHWLine[i - k + 1][j].w);
//              maxArea = Math.max(maxArea, k * w);
//            }
//          }
//        }
//      }
//    }
    return maxArea;
  }

  //cannot guarantee maxH * maxW produces max area
//
//      for (int i = A.size() - 1; i++) {
//    for (int j = 0; j < A.get(i).size(); j++) {
//      if (A.get(i).get(j)) {
//        int h = maxHWLine[i][j].h,w = maxHWLine[i][j].w;
//        if (i > 0 && j > 0) {
//          h = Math.min(h, maxHW[i - 1][j - 1].h + 1);
//          w = Math.min(h, maxHW[i - 1][j - 1].w + 1);
//        }
//        maxHW[i][j] = new MaxHW(h, w);
//        maxArea = Math.max(h * w, maxArea);
//      } else {
//        maxHW[i][j] = new MaxHW(0, 0);
//      }
//    }
//  }
  static class MaxHW {
    int h, w;

    public MaxHW(int h, int w) {
      this.h = h;
      this.w = w;
    }
  }
  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "MaxSubmatrix.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
