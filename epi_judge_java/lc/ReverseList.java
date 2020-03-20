package lc;

public class ReverseList {

    static class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

    public boolean isPalindrome(ListNode head) {
        int n = getLength(head);
        if (n == 0 || n == 1) return true;
        ListNode tail = head;
        for (int i = 0; i < n / 2 - 1; i++) {
            tail = tail.next;
        }
        tail = reverse(tail);
        while (head != null && tail != null) {
            if (head.val != tail.val) return false;
            head = head.next;
            tail = tail.next;
        }
        return true;
    }

    ListNode reverse(ListNode tail) {
        if (tail == null || tail.next == null) return tail;
        ListNode head = tail.next;
        tail.next = null;
        ListNode next = head.next;
        ListNode prev = null;
        while (next != null) {
            head.next = prev;
            prev = head;
            head = next;
            next = next.next;
        }
        head.next = prev;

        return head;
    }

    int getLength(ListNode head) {
        int c = 0;
        while (head != null) {
            head = head.next;
            c++;
        }
        return c;
    }

    public static void main(String[] args) {
        ListNode head = new ListNode(1);
        ListNode curr = head;
        ListNode next = new ListNode(1);
        curr.next = next;
        curr = next;
        next = new ListNode(2);
        curr.next = next;
        curr = next;
        next = new ListNode(1);
        curr.next = next;
        curr = next;

        System.out.println(new ReverseList().isPalindrome(head));
    }
}
