/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support.generic;

import support.map.Location;

/**
 *
 * @author konstakallama
 */
public class LocationQueue {
    private Location[] a;
    private int head;
    private int tail;

    public LocationQueue() {
        this.head = 0;
        this.tail = 0;
        this.a = new Location[10];
    }

    public void enqueue(Location l) {
        if (tail < a.length) {
            a[tail] = l;
            tail++;
        } else {
            this.increaseArraySize();
            this.enqueue(l);
        }
    }

    private void increaseArraySize() {
        Location[] newA = new Location[a.length * 2];
        int j = 0;        
        for (int i = head; i < tail; i++) {
            newA[j] = a[i];
            j++;
        }
        tail = j;
        head = 0;   
        a = newA;
    }

    public Location dequeue() {
        head++;
        return a[head - 1];
    }

    public boolean empty() {
        return tail - head == 0;
    }
    
    public int size() {
        return tail - head;
    }
    
}
