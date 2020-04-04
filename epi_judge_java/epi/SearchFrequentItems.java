package epi;

import epi.test_framework.EpiTest;
import epi.test_framework.EpiTestComparator;
import epi.test_framework.GenericTest;

import java.util.*;
public class SearchFrequentItems {

  public static List<String> searchFrequentItems(int k,
                                                 Iterable<String> stream) {
    List<String> ans = new ArrayList<>();
    if (k == 0) return ans;
    Map<String, Integer> map = new HashMap<>(k);
    Iterator<String> it = stream.iterator();
    int n = 0;
    while (it.hasNext()) {
      String s = it.next();
      n++;
      map.put(s, map.containsKey(s) ? map.get(s) + 1 : 1);
      if (map.size() == k) {
        List<String> ones = new ArrayList<>();
        for (String key : map.keySet()) {
          if (map.get(key) == 1) {
            ones.add(key);
          }
        }
        for (String key: ones) {
          map.remove(key);
        }
        for (String key : map.keySet()) {
          map.put(key, map.get(key) - 1);
        }
      }
    }

    for (String key : map.keySet()) {
      map.put(key, 0);
    }
    it = stream.iterator();
    while (it.hasNext()) {
      String key = it.next();
      if (map.containsKey(key)) {
        map.put(key, map.get(key) + 1);
      }
    }
    for (String key : map.keySet()) {
      if (map.get(key) > n / k) {
        ans.add(key);
      }
    }

    return ans;
  }
  @EpiTest(testDataFile = "search_frequent_items.tsv")
  public static List<String> searchFrequentItemsWrapper(int k,
                                                        List<String> stream) {
    return searchFrequentItems(k, stream);
  }

  @EpiTestComparator
  public static boolean comp(List<String> expected, List<String> result) {
    if (result == null) {
      return false;
    }
    Collections.sort(expected);
    Collections.sort(result);
    return expected.equals(result);
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "SearchFrequentItems.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
