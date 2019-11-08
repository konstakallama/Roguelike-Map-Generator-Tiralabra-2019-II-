/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgenerator.mapgenerator;

/**
 *
 * @author konstakallama
 */
public enum Direction {
    UP, DOWN, RIGHT, LEFT, NONE;
    /**
     * Returns the change in the x coordinate when moving to this direction.
     * @return the change in the x coordinate when moving to this direction.
     */
    public int xVal() {
        if (this == Direction.UP) {
            return 0;
        } else if (this == Direction.DOWN) {
            return 0;
        } else if (this == Direction.RIGHT) {
            return 1;
        } else if (this == Direction.LEFT) {
            return -1;
        } else {
            return 0;
        }
    }
    /**
     * Returns the change in the y coordinate when moving to this direction.
     * @return the change in the y coordinate when moving to this direction.
     */
    public int yVal() {
        if (this == Direction.UP) {
            return -1;
        } else if (this == Direction.DOWN) {
            return 1;
        } else if (this == Direction.RIGHT) {
            return 0;
        } else {
            return 0;
        }
    }
    /**
     * Returns the direction opposite to this one.
     * @return the direction opposite to this one.
     */
    public Direction getOpposite() {
        if (this == Direction.UP) {
            return Direction.DOWN;
        } else if (this == Direction.DOWN) {
            return Direction.UP;
        } else if (this == Direction.RIGHT) {
            return Direction.LEFT;
        } else if (this == Direction.LEFT) {
            return Direction.RIGHT;
        } else {
            return Direction.NONE;
        }
    }
    /**
     * Returns the clockwise turn from this direction.
     * @return the clockwise turn from this direction.
     */
    public Direction getClockwiseTurn() {
        if (this == Direction.UP) {
            return Direction.RIGHT;
        } else if (this == Direction.DOWN) {
            return Direction.LEFT;
        } else if (this == Direction.RIGHT) {
            return Direction.DOWN;
        } else if (this == Direction.LEFT) {
            return Direction.UP;
        } else {
            return Direction.NONE;
        }
    }
    /**
     * Returns the counterclockwise turn from this direction.
     * @return the counterclockwise turn from this direction.
     */
    public Direction getCounterclockwiseTurn() {
        if (this == Direction.UP) {
            return Direction.LEFT;
        } else if (this == Direction.DOWN) {
            return Direction.RIGHT;
        } else if (this == Direction.RIGHT) {
            return Direction.UP;
        } else if (this == Direction.LEFT) {
            return Direction.DOWN;
        } else {
            return Direction.NONE;
        }
    }
    
}
