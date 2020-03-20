package lc;

import epi.Knapsack;

import java.util.*;

public class Correlation {
    static class ItemCount implements Comparable<ItemCount> {
        String item;
        int count;

        public ItemCount(String item, int count) {
            this.item = item;
            this.count = count;
        }

        @Override
        public int compareTo(ItemCount o) {
            if (count != o.count) return count - o.count;
            return item.compareTo(o.item);
        }
    }
    Map<String, List<String>> findMostRelatedItems(List<List<String>> allItems) {
        Map<String, TreeSet<ItemCount>> map = new HashMap<>();
        for (List<String> items: allItems ) {
            for (int i = 0; i < items.size(); i++) {
                String item1 = items.get(i);
                for (int j = i + 1; j < items.size(); j++) {
                    String item2 = items.get(j);

                }
            }
        }
        Map<String, List<String>> result = new HashMap<>();
        return result;
    }
}
