import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.regex.*;

public class Solution {

    // Complete the candies function below.
    static long candies(int n, int[] arr) {
        long[] c = new long[arr.length];
        //forward
        c[0] = 1;
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] == arr[i - 1]) {
                c[i] = 1;
            } else if (arr[i] > arr[i - 1]) {
                c[i] = Math.max(2, c[i - 1] + 1);
            } else {
                c[i] = Math.min(1, c[i - 1] - 1);
            }
        }
        //backward
        for (int i = c.length - 1; i >= 0; i--) {
            if (c[i] < 1) {
                c[i] = 1;
                while (i > 0 && arr[i - 1] > arr[i]) {
                    c[i - 1] = Math.max(c[i - 1], c[i] + 1);
                    i--;
                }
            }
        }
        long sum = 0;
        for (int i = 0; i < c.length; i++) {
            sum += c[i];
        }
        return sum;
    }

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        int n = scanner.nextInt();
        scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");

        int[] arr = new int[n];

        for (int i = 0; i < n; i++) {
            int arrItem = scanner.nextInt();
            scanner.skip("(\r\n|[\n\r\u2028\u2029\u0085])?");
            arr[i] = arrItem;
        }

        long result = candies(n, arr);

        System.out.println(result);

        scanner.close();
    }
}
