package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;

import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
public class LargestRectangleUnderSkyline {
  @EpiTest(testDataFile = "largest_rectangle_under_skyline.tsv")

  public static int calculateLargestRectangle(List<Integer> heights) {
    int maxArea = 0;
    //pillars keep the pillars so far, closest pillars on top (first), furthest pillar on bottom.
    //pillar: shortest heights higher than the last pillar. Once an old pillar is popped (new pillar is lower),
    //it's time to calculate the max area supported by the old pillar. In the end, we pop all.
    Deque<Integer> pillars = new LinkedList<>();
    for(int i = 0; i<= heights.size(); i++) {
      //pop
      while (!pillars.isEmpty() && (i == heights.size() || heights.get(i) < heights.get(pillars.peekFirst()))){
        int height = heights.get(pillars.removeFirst());
        int width = pillars.isEmpty()? i:i-pillars.peekFirst()-1;
        maxArea = Math.max(maxArea, height * width);
      }
      //(optional)pillars of the same height, use latest one so new pillar can get correct width to calculate area
      if (!pillars.isEmpty() && i<heights.size() && heights.get(i) == heights.get(pillars.peekFirst())) {
        pillars.removeFirst();
      }
      pillars.addFirst(i);
    }
    return maxArea;
  }

  /**
   *
   */
  public static int calculateLargestSquare(List<Integer> heights) {
    int maxSide = 0;
    int start = 0;
    int minHeight = 0;
    int minHeightInd = 0;
    for (int i = 0; i < heights.size(); i++) {
      if (heights.get(i) <= maxSide) {
        start = i + 1;
        minHeight = 0;
        continue;
      }
      if (minHeight == 0 || heights.get(i) <= minHeight) {
        minHeight = heights.get(i);
        minHeightInd = i;
      }

      int width = i + 1 - start;
      int side = Math.min(width, minHeight);
      maxSide = Math.max(maxSide, side);
      if (width>=minHeight) {
        //cannot always advance to i+1, e.g. when height is increasing
        start = minHeightInd + 1;
        minHeight = heights.get(start);
        int ind = start;
        while( ind <= i) {
          if (heights.get(ind) < minHeight) {
            minHeight = heights.get(ind);
            start = ind;
          }
          ind++;
        }
        minHeightInd = start;
      }
    }
    return maxSide * maxSide;
  }

  public static int calculateLargestSquare2(List<Integer> heights) {
    int maxSide = 0;
    int maxArea = 0;
    //pillars keep the pillars so far, closest pillars on top (first), furthest pillar on bottom.
    //pillar: shortest heights higher than the last pillar. Once an old pillar is popped (new pillar is lower),
    //it's time to calculate the max area supported by the old pillar. In the end, we pop all.
    Deque<Integer> pillars = new LinkedList<>();
    for(int i = 0; i<= heights.size(); i++) {
      //pop, because the additional height of previous higher building is not going to be used any more
      while (!pillars.isEmpty() && (i == heights.size() || heights.get(i) < heights.get(pillars.peekFirst()))){
        int height = heights.get(pillars.removeFirst());
        int width = pillars.isEmpty()? i:i-pillars.peekFirst()-1;
        maxSide = Math.max(maxSide, Math.min(height, width));
      }
      //pillars of the same height, use latest one so new pillar can get correct width to calculate area
      if (!pillars.isEmpty() && i<heights.size() && heights.get(i) == heights.get(pillars.peekFirst())) {
        pillars.removeFirst();
      }
      pillars.addFirst(i);
    }
    return maxSide * maxSide;
  }

  public static void main(String[] args) {
//    System.out.println(calculateLargestSquare2(Arrays.asList(1,2,3,4,5,6,5,4,3,2,1)));//16
//    System.out.println(calculateLargestSquare2(Arrays.asList(1,2,3,4,5,6,7,8,9)));//25
//    System.out.println(calculateLargestSquare2(Arrays.asList(9,8,7,6,5,4,3,2,1)));//25
//    System.out.println(calculateLargestSquare2(Arrays.asList(6,5,4,3,2,1,2,3,4,5,6)));//9
//    System.out.println(calculateLargestSquare2(Arrays.asList(3,3,3,5,5,5,5,5,2,2)));//25

    //System.out.println(calculateLargestSquare(Arrays.asList(1,4,2,5,6,3,2,6,6,5,2,1,3)));
    //System.out.println(calculateLargestRectangle(Arrays.asList(4,4,4,4,4)));
    System.exit(
        GenericTest
            .runFromAnnotations(args, "LargestRectangleUnderSkyline.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
