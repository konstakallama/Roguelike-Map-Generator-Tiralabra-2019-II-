/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support.generic;

/**
 *
 * @author konstakallama
 */
public class IntegerStack {

    private int[] a;
    private int top;

    public IntegerStack() {
        this.top = 0;
        this.a = new int[10];
    }

    public void push(int i) {
        if (top < a.length) {
            a[top] = i;
            top++;
        } else {
            this.increaseArraySize();
            this.push(i);
        }
    }

    private void increaseArraySize() {
        int[] newA = new int[a.length * 2];
        for (int i = 0; i < top; i++) {
            newA[i] = a[i];
        }
        a = newA;
    }

    public int pop() {
        top--;
        return a[top];
    }

    public boolean empty() {
        return top == 0;
    }

}
