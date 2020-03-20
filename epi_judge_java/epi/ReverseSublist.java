package epi;
import epi.test_framework.EpiTest;
import epi.test_framework.GenericTest;
import epi.test_framework.serialization_traits.IntegerTraits;

public class ReverseSublist {
  @EpiTest(testDataFile = "reverse_sublist.tsv")

  public static ListNode<Integer> reverseSublist(ListNode<Integer> L, int start,
                                                 int finish) {
    ListNode<Integer> dummy = new ListNode<>(0, L);
    //points to the previous node of the sublist, keep changing its next field to point to the new sublist head
    ListNode<Integer> prev = dummy;
    int k = 1;
    while (k++ < start) {
      prev = prev.next;
    }
    //newTail never changes, but its next keep pointing to the next node to be processed (new head)
    ListNode<Integer> newTail = prev.next;
    while (start++ < finish) {
      ListNode<Integer> newHead = newTail.next;
      newTail.next = newHead.next;
      newHead.next = prev.next;
      prev.next = newHead;
    }
    return dummy.next;
  }

  public static void main(String[] args) {
    System.exit(
        GenericTest
            .runFromAnnotations(args, "ReverseSublist.java",
                                new Object() {}.getClass().getEnclosingClass())
            .ordinal());
  }
}
