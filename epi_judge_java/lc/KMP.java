package lc;

import java.util.ArrayList;
import java.util.List;

public class KMP {
//    Examples of lps[] construction:
//    For the pattern “AAAA”,
//    lps[] is [0, 1, 2, 3]
//
//    For the pattern “ABCDE”,
//    lps[] is [0, 0, 0, 0, 0]
//
//    For the pattern “AABAACAABAA”,
//    lps[] is [0, 1, 0, 1, 2, 0, 1, 2, 3, 4, 5]
//
//    For the pattern “AAACAAAAAC”,
//    lps[] is [0, 1, 2, 0, 1, 2, 3, 3, 3, 4]
//
//    For the pattern “AAABAAA”,
//    lps[] is [0, 1, 2, 0, 1, 2, 3]
    public static int[] buildLPS(char[] ptn) {
        int[] lps = new int[ptn.length];
        //previous prefix length that is also a suffix
        int len = 0;
        lps[0] = 0;
        //current position in ptn
        int i = 1;
        while (i < ptn.length) {
            if (ptn[i] == ptn[len]) {
                lps[i++] = ++len;
            } else {
                if (len > 0) {
                    len = lps[len - 1];
                } else{
                    lps[i++] = len;
                }
            }
        }
        return lps;
    }

    static void printArray(int[] lps) {
        System.out.print('[');
        for(int i : lps) {
            System.out.print(i);
            System.out.print(",");
        }
        System.out.println(']');
    }

    public static List<Integer> search(String text, String pattern) {
        List<Integer> ans = new ArrayList<>();
        //i to iterate text, j to iterate pattern
        int i = 0, j = 0;
        int[] lps = buildLPS(pattern.toCharArray());
        while (i < text.length()) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;
                if (j == pattern.length()) {
                    ans.add(i - j);
                    j = lps[j - 1];
                }
            } else {
                if (j > 0) {
                    j = lps[j -1];
                } else {
                    i++;
                }
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        String ptn = "AAAA";
        printArray(buildLPS(ptn.toCharArray()));
        ptn = "ABCDE";
        printArray(buildLPS(ptn.toCharArray()));
        ptn = "AABAACAABAA";
        printArray(buildLPS(ptn.toCharArray()));
        ptn = "AAACAAAAAC";
        printArray(buildLPS(ptn.toCharArray()));
        ptn = "AAABAAA";
        printArray(buildLPS(ptn.toCharArray()));
        System.out.println(search("AAAAABAAABA", "AAAA"));
        System.out.println(search("ABABDABACDABABCABAB", "ABABCABAB"));
        System.out.println(search("AABAACAADAABAABA", "AABA"));
        System.out.println(search("ABC ABCDAB ABCDABCDABDE", "ABCDABD"));
    }
}
