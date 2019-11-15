/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgenerator.mapgenerator;

import java.util.ArrayList;

/**
 *
 * @author konstakallama
 */
public class Location {
    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.x;
        hash = 59 * hash + this.y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }
    /**
     * Changes this location to be the one 1 tile in direction d.
     * @param d 
     */
    public void move(Direction d) {
        this.x += d.xVal();
        this.y += d.yVal();
    }
    /**
     * Returns the location 1 tile in direction d.
     * @param d
     * @return the location 1 tile in direction d.
     */
    public Location locInDir(Direction d) {
        return new Location(this.x + d.xVal(), this.y + d.yVal());
    }
    /**
     * Returns the Manhattan distance from this location to l.
     * @param l
     * @return the Manhattan distance from this location to l.
     */
    public int manhattanDistance(Location l) {
        return Math.abs(this.x - l.getX()) + Math.abs(this.y - l.getY());
    }
    /**
     * Returns the Manhattan distance from this location to the location represented by x, y.
     * @param x
     * @param y
     * @return the Manhattan distance from this location to the location represented by x, y.
     */
    public int manhattanDistance(int x, int y) {
        return this.manhattanDistance(new Location(x, y));
    }

    @Override
    public String toString() {
        return "Location{" + "x=" + x + ", y=" + y + '}';
    }
    /**
     * Returns a list of the 4 locations adjacent to this one.
     * @return a list of the 4 locations adjacent to this one.
     */
    public ArrayList<Location> getAdjacent() {
        ArrayList<Location> a = new ArrayList<>();
        
        Direction d = Direction.DOWN;
        
        for (int i = 0; i < 4; i++) {
            a.add(this.locInDir(d));
            d = d.getClockwiseTurn();
        }
        
        return a;
    }
    
    public Direction getClosestDir(Location to) {
        int fromX = this.x;
        int fromY = this.y;
        int toX = to.getX();
        int toY = to.getY();
        
        if (Math.abs(fromX - toX) >= Math.abs(fromY - toY)) {
            if (fromX - toX < 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (fromY - toY < 0) {
                return Direction.DOWN;
            } else if (fromY - toY > 0) {
                return Direction.UP;
            } else {
                return Direction.NONE;
            }
        }
    }
    
    
    
}
