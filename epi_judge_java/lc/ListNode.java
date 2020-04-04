package lc;

public class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
        val = x;
        next = null;
    }

    public static ListNode createList(int[] vals, int loop) {
        ListNode dummy = new ListNode(0);
        ListNode curr = dummy;
        ListNode loopNode = null;
        for (int i = 0; i < vals.length; i++) {
            ListNode n = new ListNode(vals[i]);
            curr.next = n;
            curr = n;
            if (loop == i) loopNode = n;
        }
        curr.next = loopNode;
        return dummy.next;
    }
}
