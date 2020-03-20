package lc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LC177 {

    public String largestMultipleOfThree(int[] digits) {
        List<Integer>[] d3 = new ArrayList[3];
        for (int i = 0; i < 3; i++) {
            d3[i] = new ArrayList<Integer>();
        }
        for (int d : digits) {
            d3[d % 3].add(d);
        }
        if (d3[0].isEmpty() && (d3[1].isEmpty() || d3[2].isEmpty()) && d3[1].size() < 3 && d3[2].size() < 3) return "";
        for (int i = 0; i < 3; i++) {
            Collections.sort(d3[i], Comparator.reverseOrder());
        }
        StringBuilder sb = new StringBuilder();
        int i0 = 0, i1 = 0, i2 = 0;
        int mod = 0;
        while (i0 < d3[0].size() || i1 < d3[1].size() || i2 < d3[2].size()) {
            int d0 = i0 < d3[0].size() ? d3[0].get(i0) : -1;
            int d1 = i1 < d3[1].size() ? d3[1].get(i1) : -1;
            int d2 = i2 < d3[2].size() ? d3[2].get(i2) : -1;

            int max = Math.max(d0, Math.max(d1, d2));
            if (max == d0) {
                sb.append(max);
                i0++;
            } else {
                int av1 = d3[1].size() - i1;
                int av2 = d3[2].size() - i2;
                if (max == d1) {
                    if (mod == 0 && (av1 >= 3 || av2 > 0) ||
                            mod == 1 && (av1 >= 2 || av2 > 1) ||
                            mod == 2) {
                        sb.append(max);
                        mod = (mod + 1) % 3;
                    }
                    i1++;
                } else if (max == d2) {
                    if (mod == 0 && (av2 >= 3 || av1 > 0) ||
                            mod == 1 ||
                            mod == 2 && (av2 >= 2 || av1 > 1)) {
                        sb.append(max);
                        mod = (mod + 2) % 3;
                    }
                    i2++;
                }
            }
        }
        int s = 0;
        for (int i = 0; i < sb.length() - 1; i++) {
            if (sb.charAt(i) != '0') {
                s = i;
                break;
            } else {
                s++;
            }
        }
        return sb.substring(s);
    }

    public int daysBetweenDates(String date1, String date2) {
        long t1 = getTime(date1);
        long t2 = getTime(date2);
        long diff = Math.abs(t1 - t2) + 1;
        long unit = 86400;
        return (int)(diff / unit);
    }

    long getTime(String date1) {
        java.util.Calendar c = java.util.Calendar.getInstance();
        int y = Integer.parseInt(date1.substring(0, 4));
        int m = Integer.parseInt(date1.substring(5,7));
        int d = Integer.parseInt(date1.substring(8));
        c.set(y, m, d, 0, 0, 0);
        return c.getTimeInMillis() / 1000;
    }

    public static void main(String[] args) {
        LC177 lc = new LC177();
        //System.out.println(lc.largestMultipleOfThree(new int[]{0,0,0,0}));
        System.out.println(lc.daysBetweenDates("2074-09-12",
        "1983-01-08"));
    }
}
