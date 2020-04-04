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

    public int[] searchRange(int[] nums, int target) {
        int left = 0, right = nums.length;
        int start = -1, end = -1;
        while (left < right) {
            int m = left + (right - left) / 2;
            if (nums[m] == target) {
                start = findStart(nums, target, left, m + 1);
                end = findEnd(nums, target, m, right);
                break;
            } else if (nums[m] > target) {
                right = m;
            } else {
                left = m + 1;
            }
        }
        return new int[]{start, end};
    }

    int findStart(int[] nums, int t, int left, int right) {
        while (left < right) {
            int m = left + (right - left) / 2;
            if (nums[m] == t) {
                right = m;
            } else {
                left = m + 1;
            }
        }
        return left;
    }

    int findEnd(int[] nums, int t, int left, int right) {
        while (left < right) {
            int m = left + (right - left) / 2;
            if (nums[m] == t) {
                left = m + 1;
            } else {
                right = m;
            }
        }
        return left - 1;
    }

    public ListNode detectCycle(ListNode head) {
        if (head == null) return null;
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) break;
        }
        if (fast == null || fast.next == null) return null;
        slow = head;
        while (slow != fast) {
            slow = slow.next;
            fast = fast.next;
        }
        return slow;
    }

    public List<String> wordBreak(String s, List<String> wordDict) {
        List[] dp = new List[s.length() + 1];
        dp[0] = new ArrayList();
        for (int i = 1; i < s.length() + 1; i++) {
            List<Integer> li = new ArrayList<>();
            for (int j = 0; j < i; j++) {
                if (dp[j] != null && wordDict.contains(s.substring(j, i))) {
                    li.add(j);
                }
            }
            if (!li.isEmpty()) {
                dp[i] = li;
            }
        }
        if (dp[s.length()] == null) return null;
        List<String> ans = new LinkedList<>();
        buildSentence(s, dp, s.length(), "", ans);
        return ans;
    }

    void buildSentence(String s, List[] dp, int i, String sf, List<String> res) {
        if (i == 0) {
            res.add(sf.trim());
            return;
        }
        List<Integer> li = (List<Integer>)dp[i];
        if (li == null) return;
        for (int ind : li) {
            buildSentence(s, dp, ind, s.substring(ind, i) + " " + sf, res);
        }
    }

    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode slow = head, fast = head.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode head2 = slow.next;
        //cut the link
        slow.next = null;
        ListNode first = sortList(head);
        ListNode second = sortList(head2);
        return merge(first, second);
    }

    ListNode merge(ListNode first, ListNode second) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        while (first != null && second != null) {
            if (first.val <= second.val) {
                curr.next = first;
                first = first.next;
            } else {
                curr.next = second;
                second = second.next;
            }
            curr = curr.next;
        }
        if (first != null) {
            curr.next = first;
        } else {
            curr.next = second;
        }
        return dummy.next;
    }

    public List<Integer> findAnagrams(String s, String p) {
        List<Integer> ans = new LinkedList<>();
        if (p == null || s == null || s.length() == 0 || p.length() == 0 || p.length() > s.length()) return ans;
        int[] nums = new int[26];
        char[] pcs = p.toCharArray();
        for (char pc : pcs) {
            nums[pc - 'a']++;
        }
        int i = 0, j = 0, len = p.length();
        int counter = 0;
        for (int n = 0; n < nums.length; n++) {
            if (nums[n] > 0) {
                counter++;
            } else {
                nums[n] = Integer.MIN_VALUE;
            }
        }
        while (j < s.length()) {
            char c = s.charAt(j);
            if (nums[c - 'a'] != Integer.MIN_VALUE) {
                if (--nums[c - 'a'] == 0) {
                    counter--;
                }
            }
            j++;
            //while all chars are included
            while (counter == 0) {
                char bc = s.charAt(i);
                if (nums[bc - 'a'] != Integer.MIN_VALUE) {
                    if (++nums[bc - 'a'] == 1) {
                        counter++;
                    }
                }
                if (j - i == len) {
                    ans.add(i);
                }
                i++;
            }
        }
        return ans;
    }

    public boolean canFinish1(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> g = new HashMap<>();
        for (int[] e : prerequisites) {
            if (!g.containsKey(e[0])) {
                g.put(e[0], new LinkedList<>());
            }
            g.get(e[0]).add(e[1]);
        }
        int[] v = new int[numCourses]; // 0, 1, 2
        for (int key : g.keySet()) {
            if (v[key] == 0 && !dfs(g, key, v)) {
                return false;
            }
        }
        return true;
    }

    boolean dfs(Map<Integer, List<Integer>> g, int c, int[] v) {
        if (v[c] == 2) return true;
        if (v[c] == 1) return false;
        v[c] = 1;
        List<Integer> pres = g.get(c);
        if (pres != null) {
            for(int pre : pres) {
                if (!dfs(g, pre, v)) {
                    return false;
                }
            }
        }

        v[c] = 2;
        return true;
    }

    public boolean canFinish(int numCourses, int[][] prerequisites) {
        Map<Integer, List<Integer>> g = new HashMap<>();
        int[] incoming = new int[numCourses];
        for (int[] e : prerequisites) {
            if (!g.containsKey(e[0])) {
                g.put(e[0], new LinkedList<>());
            }
            g.get(e[0]).add(e[1]);
            incoming[e[1]]++;
        }
        Queue<Integer> q = new LinkedList<>();
        for (int ic : incoming) {
            if (ic == 0) {
                q.offer(ic);
            }
        }

        int count = 0;
        while (!q.isEmpty()) {
            int course = q.poll();
            if (g.containsKey(course)) {
                for (int d : g.get(course)) {
                    incoming[d]--;
                    count++;
                    if (incoming[d] == 0) {
                        q.offer(d);
                    }
                }
            }
        }
        return count == prerequisites.length;
    }

    public static void main(String[] args) {
//        System.out.println(new LC100().decodeString("2[abc]3[cd]ef"));
//        System.out.println(new LC100().findDuplicate(new int[]{1,3,4,2,2}));
//        new LC100().combinationSum(new int[]{2,3,6,7}, 7);
        //new LC100().rotate(new int[][]{{5,1,9,11},{2,4,8,10},{13,3,6,7},{15,14,12,16}});
        //new LC100().searchRange(new int[]{5,7,7,8,8,10}, 8);
        //ListNode head = ListNode.createList(new int[]{3, 2, 0, -4}, 1);
        //new LC100().detectCycle(head);

//        new LC100().wordBreak("catsanddog",
//                Arrays.asList("cat", "cats", "and", "sand", "dog"));
        //new LC100().sortList(ListNode.createList(new int[]{4,2,1,3}, -2));
        new LC100().canFinish(2, new int[][]{{1,0}});
    }
}
