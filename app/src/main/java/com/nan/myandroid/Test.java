package com.nan.myandroid;

import android.util.Log;

/**
 * author：93289
 * date:2020/8/17
 *
 * dsc:
 */
class Test {
    public static void main(String[] args) {
        ListNode node1 = new ListNode(1);
        ListNode node2 = new ListNode(2);
        ListNode node3 = new ListNode(3);
        ListNode node4 = new ListNode(4);
        node1.next=node2;
        node2.next=node3;
        node3.next=node4;
        reverseList(node1);

    }

    private static ListNode reverseList(ListNode head) {
        ListNode prev = null;
        ListNode curr = head;
        while (curr != null) {
            ListNode nextTemp = curr.next;
            curr.next = prev;
            prev = curr;
            curr = nextTemp;
        }
        return prev;
    }

    static class ListNode {
        ListNode next;
        Integer value;

        public ListNode(int value) {
            this.value = value;
        }
        public void print(){
            System.out.println(value);
            if (next!=null){
                next.print();
            }
        }
    }
}
