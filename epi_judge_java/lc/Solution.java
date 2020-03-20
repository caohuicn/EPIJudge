package lc;

import java.util.*;

class Solution {
    public int minTaps(int n, int[] ranges) {
        List<R> rs = new ArrayList<>(n + 1);
        for (int i = 0; i <= n; i++) {
            R r = new R(i - ranges[i], i + ranges[i]);
            rs.add(r);
        }
        Collections.sort(rs, (r1, r2) -> {
            if (r1.s != r2.s) {
                return Integer.compare(r1.s, r2.s);
            }
            return Integer.compare(r1.e, r2.e);
        });
        int tap = 0;
        int end = 0;
        int start = 0;
        int open = 0;
        while (tap <= n) {
            R r = rs.get(tap);
            if (r.s > start){
                if (end < r.s) return -1;
                start = end;
                open++;
            }
            end = Math.max(r.e, end);
            if (end >= n) return ++open;
            tap++;
        }

        return open;
    }

    static class R{
        int s;
        int e;
        R(int s, int e) {
            this.s = s;
            this.e = e;
        }
    }

    public static void main(String[] args) {
        System.out.println(new Solution().minTaps(5, new int[]{
                3,4,1,1,0,0}));
    }

    static class StringPop {
        String pop(String s) {
            Deque<Character> dq = new LinkedList<>();
            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                if (dq.isEmpty()) {
                    dq.addFirst(c);
                } else {
                    char top = dq.peek();
                    if (top == c) {
                        dq.removeFirst();
                        int j = i + 1;
                        while (j < s.length() && s.charAt(j) == c) {
                            j++;
                        }
                        i = j - 1;
                    } else {
                        dq.addFirst(c);
                    }
                }
            }
            StringBuilder sb = new StringBuilder();
            while(!dq.isEmpty()) {
                sb.insert(0, dq.removeFirst());
            }
            return sb.toString();
        }

        String pop1(String s) {
            if (s == null || s.length() < 2) return s;
            char[] cs = s.toCharArray();
            int i = -1, j = 0;
            boolean dup = false;
            while (j < cs.length) {
                if (i < 0) {
                    i = 0;
                    cs[i] = cs[j++];
                    dup = false;
                    continue;
                }
                if (cs[j] == cs[i]) {
                    dup = true;
                    j++;
                } else {
                    if (dup) {
                        i--;
                    } else {
                        cs[++i] = cs[j++];
                    }
                    dup = false;
                }
            }
            if (dup) i--;
            return new String(cs, 0, i + 1);
        }

        String pop2(String s) {
            if (s == null || s.length() < 2) return s;
            char[] cs = s.toCharArray();
            int i = 0, j = 1;
            while (j < cs.length) {
                //not equal -> copy j to i+1, advance i,j
                //equal -> advance j until not equal, backup i
                if (i < 0 || cs[j] != cs[i]) {
                    cs[++i] = cs[j++];
                } else {
                    while (j < cs.length && cs[j] == cs[i]){
                        j++;
                    }
                    i--;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(cs, 0, i + 1);
            return sb.toString();
        }

        String dedup(String s) {
            if (s == null || s.length() < 2) return s;
            char[] cs = s.toCharArray();
            int end = 0;//write index
            int i = 0;//index to be examined
            while(i < cs.length) {
                if (i + 1 < cs.length && cs[i + 1] == cs[i]) {
                    //advance to a different character or end
                    i = i + 2;
                    while (i < cs.length && cs[i] == cs[i - 1]) {
                        i++;
                    }
                } else {
                    cs[end++] = cs[i++];
                }
            }
            return new String(cs, 0, end);
        }

        public static void main(String[] args) {
            StringPop sp = new StringPop();
            System.out.println(sp.pop("abaaba"));
            System.out.println(sp.pop("abccccbbbaaadd"));
            System.out.println(sp.pop("abccccbbbaaadddeefggffeed"));
            System.out.println(sp.pop1("abaaba"));
            System.out.println(sp.pop1("abccccbbbaaadd"));
            System.out.println(sp.pop1("abccccbbbaaadddeefggffeed"));
            System.out.println(sp.pop2("abaaba"));
            System.out.println(sp.pop2("abccccbbbaaadd"));
            System.out.println(sp.pop2("abccccbbbaaadddeefggffeed"));
            System.out.println(sp.dedup("abaaba"));
            System.out.println(sp.dedup("abccccbbbaaadd"));
            System.out.println(sp.dedup("abccccbbbaaadddeefggffeed"));
            System.out.println(sp.dedup("aabb"));
        }
    }
}