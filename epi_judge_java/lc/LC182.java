package lc;

import java.util.Arrays;

public class LC182 {
    public int maxSatisfaction(int[] s) {
        Arrays.sort(s);
        int n = s.length;
        if (s[n - 1] <= 0) return 0;
        int sum = 0;
        int i = n - 1;
        for (; i >=0; i--) {
            if (sum + s[i] < 0) {
                break;
            }
            sum += s[i];
        }
        int ans = 0;
        for (int j = i + 1; j < n; j++) {
            int w = j - i;
            ans += w * s[j];
        }
        return ans;
    }

    public static void main(String[] args) {
        System.out.println(new LC182().maxSatisfaction(new int[]{5, -9, -8,  0, -1}));
    }
}
