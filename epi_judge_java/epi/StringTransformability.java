package epi;

import epi.test_framework.EpiTest;

import java.util.*;
public class StringTransformability {
  @EpiTest(testDataFile = "string_transformability.tsv")
  public static int transformString(Set<String> D, String s, String t) {
    Queue<StringWithDist> q = new LinkedList<>();
    D.remove(s);
    q.add(new StringWithDist(s, 0));
    while (!q.isEmpty()) {
      StringWithDist top = q.poll();
      if (top.s.equals(t)) {
        return top.dist;
      }
      for (int i = 0; i < top.s.length(); i++) {
        String pre = i == 0? "":top.s.substring(0,i);
        String suf = i==top.s.length()-1?"":top.s.substring(i+1);
        for (char c='a'; c<= 'z'; c++) {
          String modS =  pre + c + suf;
          if (D.remove(modS)) {
            q.add(new StringWithDist(modS, top.dist + 1));
          }
        }
      }
    }
    return -1;
  }

  public static class StringWithDist {
    String s;
    int dist;

    public StringWithDist(String s, int dist) {
      this.s = s;
      this.dist = dist;
    }
  }

  public static class IntWithDist {
    int i;
    int dist;

    public IntWithDist(int i, int dist) {
      this.i = i;
      this.dist = dist;
    }
  }

  public static int expSteps(int n) {
    if (n < 1) return -1;
    Set<Integer> set = new HashSet<>();
    Queue<IntWithDist> q = new LinkedList<>();
    set.add(1);
    q.add(new IntWithDist(1,0));
    while (!q.isEmpty()){
      IntWithDist i = q.poll();
      if (i.i == n) return i.dist;
      Set<Integer> ns = new HashSet<>();
      for (Integer j: set) {
        int k = i.i + j;
        if (!set.contains(k)) {
          ns.add(k);
        }
      }
      for (Integer k: ns) {
        //TODO: WRONG!!! dist is not determined by i only
        q.add(new IntWithDist(k, i.dist + 1));
        set.add(k);
      }
    }
    return -1;
  }

  public static int expSteps2(int n) {
    if (n < 1) return -1;
    List<List<Integer>>[] allComs =  new List[n];
    List<List<Integer>> coms = expCombinations(n, allComs);
    int min = Integer.MAX_VALUE;
    for (List<Integer> com: coms) {
      if (com.size() < min) {
        min = com.size();
      }
    }
    for (List<Integer> com: coms) {
      if (com.size() == min) {
        System.out.println(com);
      }
    }
    return min;
  }

  public static List<List<Integer>> expCombinations(int n, List<List<Integer>>[] allComs) {
    if (allComs[n-1] != null) return allComs[n-1];
    List<List<Integer>> coms = new ArrayList<>();
    if (n == 1) {
      List<Integer> one = new ArrayList<>();
      one.add(1);
      coms.add(one);
      allComs[0] = coms;
      return coms;
    }
    for (int i = 0; i < n/2; i++) {
      List<List<Integer>> com1s = expCombinations(i + 1, allComs);
      List<List<Integer>> com2s = expCombinations(n - i -1, allComs);
      for (List<Integer> com1: com1s) {
        for (List<Integer> com2: com2s) {
          Set<Integer> com = new TreeSet<>();
          com.addAll(com1);
          com.addAll(com2);
          com.add(n);
          coms.add(new ArrayList<>(com));
        }
      }
    }
    allComs[n-1] = coms;
    return coms;
  }

  public static void main(String[] args) {
    System.out.println(expSteps2(0));
    System.out.println(expSteps2(1));
    System.out.println(expSteps2(2));
    System.out.println(expSteps2(3));
    System.out.println(expSteps2(4));
    System.out.println(expSteps2(5));
    System.out.println(expSteps2(15));
//    System.exit(
//        GenericTest
//            .runFromAnnotations(args, "StringTransformability.java",
//                                new Object() {}.getClass().getEnclosingClass())
//            .ordinal());
  }
}
