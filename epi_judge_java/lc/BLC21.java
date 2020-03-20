package lc;

import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeMap;

public class BLC21 {
    public String sortString(String s) {
        char[] cs = s.toCharArray();
        TreeMap<Character, Integer> map = new TreeMap<>();
        for (char c: cs) {
            map.put(c, map.getOrDefault(c, 0) + 1);
        }
        StringBuilder sb = new StringBuilder();
        boolean flag = true;
        while (!map.isEmpty()) {
            NavigableSet<Character> set = map.navigableKeySet();
            Iterator<Character> it;
            if (flag) {
                it = set.iterator();
            } else {
                it = set.descendingIterator();
            }
            flag = !flag;
            while (it.hasNext()) {
                Character key = it.next();
                sb.append(key);
                map.put(key, map.get(key) - 1);
                if (map.get(key) == 0) {
                    it.remove();
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(new BLC21().sortString("aaaabbbbcccc"));
    }
}
