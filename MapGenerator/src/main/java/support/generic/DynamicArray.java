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
public class DynamicArray {
    int[] array;
    int index;

    public DynamicArray(int length) {
        array = new int[length];
        index = 0;
    }
    
    public boolean add(int i) {
        if (index >= array.length - 1) {
            return false;
        }
        array[index] = i;
        index++;
        return true;
    }
    
    public int get(int i) {
        return array[i];
    }
    
    public int getIndex() {
        return index;
    }
    
    
    
    
}
