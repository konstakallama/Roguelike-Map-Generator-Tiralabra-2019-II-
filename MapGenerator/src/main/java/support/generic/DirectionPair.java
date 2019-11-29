/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support.generic;

import support.map.Direction;

/**
 *
 * @author konstakallama
 */
public class DirectionPair {
    private Direction d1;
    private Direction d2;

    public DirectionPair(Direction d1, Direction d2) {
        this.d1 = d1;
        this.d2 = d2;
    }

    public Direction getD1() {
        return d1;
    }

    public Direction getD2() {
        return d2;
    }
    
}
