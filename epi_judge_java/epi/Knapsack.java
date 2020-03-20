package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.EpiUserType;
import epi.test_framework.GenericTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Knapsack {
  @EpiUserType(ctorParams = {Integer.class, Integer.class})

  public static class Item {
    public Integer weight;
    public Integer value;

    public Item(Integer weight, Integer value) {
      this.weight = weight;
      this.value = value;
    }
  }

  @EpiTest(testDataFile = "knapsack.tsv")

  public static int optimumSubjectToCapacity(List<Item> items, int w) {
    int step = getStep(items);
    //key: weight constraint, value: value
    Map<Integer, Integer> currentValueUnderWeight = new HashMap<>(w + 1);
    for (int i = 0; i < w + 1; i = i+step) {
      currentValueUnderWeight.put(i, 0);
    }
    System.out.println("step:" + step + " size:" + currentValueUnderWeight.size());
    for (int i = 0; i < items.size(); i++) {
      Map<Integer, Integer> prev = new HashMap<>(currentValueUnderWeight);
      Item item = items.get(i);
      for (int j = 0; j < w + 1; j = j + step) {
        int withCurrent = (item.weight <= j) ? item.value + prev.get(j - item.weight): 0;
        int withoutCurrent = prev.get(j);
        currentValueUnderWeight.put(j, Math.max(withCurrent, withoutCurrent));
      }
    }

    return currentValueUnderWeight.get(w);

  }

  public static int getStep(List<Item> items) {
    int lcd = 0;
    for (int i = 0; i < items.size(); i++) {
      lcd = getLCD(lcd, items.get(i).weight);
    }
    return lcd > 0 ? lcd : 1;
  }

  public static int getLCD(Integer i1, Integer i2) {
    if (i1 == i2) return i1;
    if (i1 == 0) return i2;
    if (i2 == 0) return i1;
    int big = i1>i2? i1: i2;
    int small = i1>i2? i2: i1;
    int mod = big % small;
    if (mod == 0) return small;
    return getLCD(mod, small);
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "Knapsack.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
