package lc;

import java.util.HashSet;
import java.util.Set;

public class LC30 {
    public boolean isHappy(int n) {
        Set<Integer> v = new HashSet<>();
        while (!v.contains(n) && n != 1) {
            v.add(n);
            n = convert(n);
        }
        return n == 1;
    }

    int convert(int n) {
        int nn = 0;
        while (n != 0) {
            nn += (n % 10) * (n % 10);
            n /= 10;
        }
        return nn;
    }

    public static void main(String[] args) {
        System.out.println(new LC30().isHappy(19));
    }
}
