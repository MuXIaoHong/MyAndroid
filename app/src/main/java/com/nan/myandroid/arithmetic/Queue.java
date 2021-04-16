package com.nan.myandroid.arithmetic;

import java.util.Stack;

/**
 * author：93289
 * date:2020/10/29
 * dsc:
 */

/**
 * 使用两个栈实现队列，拥有队列的基本操作
 * void push(int x) 将元素 x 推到队列的末尾
 * int pop() 从队列的开头移除并返回元素
 * int peek() 返回队列开头的元素
 * boolean empty() 如果队列为空，返回 true ；否则，返回 false
 */
class StackQueue {

    public static void main(String[] args) {

    }

    private static Stack<Integer> s1 = new Stack<>();
    private static Stack<Integer> s2 = new Stack<>();

    private static int front;

    /**
     * s1用来存放元素，新加入的元素都放在s1中
     * 时间复杂度：O(1)
     * 向栈压入元素的时间复杂度为O(1)
     *
     * 空间复杂度：O(n)
     * 需要额外的内存来存储队列元素
     */
    public void push(int x) {
        if (s1.empty())
            front = x;
        s1.push(x);
    }

    /**
     * s2用来取出元素，因为要先进先出
     * 所以把s1
     * 然后再由s2pop出，直到s2为空，再次将s1中元素全部pop出来push进s2
     *
     * 时间复杂度： 摊还复杂度 O(1)，最坏情况下的时间复杂度 O(n)
     * 在最坏情况下，s2 为空，算法需要从 s1 中弹出 n 个元素，然后再把这 n 个元素压入 s2，
     * 在这里n代表队列的大小。这个过程产生了 2n 步操作，时间复杂度为 O(n)。但当 s2 非空时，
     * 算法就只有 O(1)的时间复杂度。
     *
     * 空间复杂度 ：O(1)
     */
    public void pop() {
        if (s2.isEmpty()) {
            while (!s1.isEmpty())
                s2.push(s1.pop());
        }
        s2.pop();
    }


    /**
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     */
    public boolean empty() {
        return s1.isEmpty() && s2.isEmpty();
    }


    /**
     * 时间复杂度：O(1)
     * 队首元素要么是之前就被计算出来的，要么就是 s2 栈顶元素。因此时间复杂度为 O(1)。
     * 如果不使用front保存s1为空时第一个加入s1的元素
     * 当s2为空时，peek的时候就要和pop一样先将s1中元素pop出来push到s2中
     * n次入栈n次出栈时间复杂度就就是n了
     *
     * 空间复杂度：O(1)
     */
    public int peek() {
        if (!s2.isEmpty()) {
            return s2.peek();
        }
        return front;
    }

}
