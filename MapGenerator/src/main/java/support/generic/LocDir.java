/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support.generic;

import support.map.Direction;
import support.map.Location;

/**
 *
 * @author konstakallama
 */
public class LocDir {
    private Location l;
    private Direction d;

    public LocDir(Location l, Direction d) {
        this.l = l;
        this.d = d;
    }

    public Location getL() {
        return l;
    }

    public Direction getD() {
        return d;
    }
    
}
