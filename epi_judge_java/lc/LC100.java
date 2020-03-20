package lc;

import java.util.*;

public class LC100 {
    public String decodeString(String s) {
        char[] cs = s.toCharArray();
        Deque<Character> dq = new LinkedList<>();
        for (int i = 0; i < cs.length; i++) {
            if (cs[i] == ']') {
                StringBuilder sb = new StringBuilder();
                char c = dq.pollFirst();
                while ( c != '[') {
                    sb.insert(0, c);
                    c = dq.pollFirst();
                }
                int repeat = 0;
                int power = 1;
                while (!dq.isEmpty() && Character.isDigit(dq.peekFirst())) {
                    c = dq.pollFirst();
                    repeat += (c - '0') * power;
                    power *= 10;
                }
                for (int j = 0; j < repeat; j++) {
                    for (int k = 0; k < sb.length(); k++) {
                        dq.offerFirst(sb.charAt(k));
                    }
                }
            } else {
                dq.offerFirst(cs[i]);
            }
        }
        StringBuilder sb = new StringBuilder();
        while (!dq.isEmpty()) {
            sb.insert(0, dq.poll());
        }
        return sb.toString();
    }

    public int findDuplicate(int[] nums) {
        int slow = nums[0], fast = nums[nums[0]];
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[nums[fast]];
        }
        slow = 0;
        while (slow != fast) {
            slow = nums[slow];
            fast = nums[fast];
        }
        return slow;
    }

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        return sumHelper(candidates, target, 0);
    }

    List<List<Integer>> sumHelper(int[] candidates, int target, int start) {
        if (target <= 0) return null;
        if (start >= candidates.length) return null;

        List<List<Integer>> ans = new ArrayList<>();
        int c = candidates[start];
        int nt = target;
        List<Integer> list = new ArrayList<>();
        while (nt >= 0) {
            if (nt == 0) {
                ans.add(new ArrayList<>(list));
                break;
            } else {
                List<List<Integer>> sublist = sumHelper(candidates, nt, start + 1);
                if (sublist != null && !sublist.isEmpty()) {
                    for (List<Integer> l: sublist) {
                        List<Integer> nl = new ArrayList<>(list);
                        nl.addAll(l);
                        ans.add(nl);
                    }

                }
            }
            list.add(c);
            nt -= c;
        }
        return ans;
    }

    public void rotate(int[][] matrix) {
        int n = matrix.length;
        int start = 0, end = n;
        while (start < end - 1) {
            for (int j = start; j < end - 1; j++) {
                int move = j - start;
                int temp = matrix[start][j];
                matrix[start][j] = matrix[end - 1 - move][start];
                matrix[end - 1 - move][start] = matrix[end - 1][end - 1 - move];
                matrix[end - 1][end - 1 - move] = matrix[j][end - 1];
                matrix[j][end - 1] = temp;
            }
            start++;
            end--;
        }
    }

    public static void main(String[] args) {
//        System.out.println(new LC100().decodeString("2[abc]3[cd]ef"));
//        System.out.println(new LC100().findDuplicate(new int[]{1,3,4,2,2}));
//        new LC100().combinationSum(new int[]{2,3,6,7}, 7);
        new LC100().rotate(new int[][]{{5,1,9,11},{2,4,8,10},{13,3,6,7},{15,14,12,16}});
    }
}
