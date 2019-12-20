/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support.map;

/**
 * A room on a map.
 *
 * @author konstakallama
 */
public class Room {

    private Location location;
    private int w;
    private int h;

    public Room(Location location, int w, int h) {
        this.location = location;
        this.w = w;
        this.h = h;
    }

    public Location getLocation() {
        return location;
    }


    public int getW() {
        return w;
    }


    public int getH() {
        return h;
    }


    /**
     * Returns an approximate middle point for this room.
     *
     * @return
     */
    public Location getMiddle() {
        return new Location(this.getLocation().getX() + (this.getW() / 2), this.getLocation().getY() + (this.getH() / 2));
    }

    /**
     * Returns true if l is inside this room.
     *
     * @param l
     * @return
     */
    public boolean isInside(Location l) {
        if (this.h == 0 || this.w == 0) {
            return false;
        }
        return (l.getX() >= this.location.getX() && l.getX() < this.location.getX() + w && l.getY() >= this.location.getY() && l.getY() < this.location.getY() + h);
    }

    /**
     * Returns true if any location at manhattan distance 1 from l is inside
     * this room.
     *
     * @param l
     * @return
     */
    public boolean isNextTo(Location l) {

        Direction d = Direction.DOWN;
        for (int i = 0; i < 4; i++) {
            if (this.isInside(l.locInDir(d))) {
                return true;
            }
            d = d.getClockwiseTurn();
        }
        return false;
    }

    @Override
    public String toString() {
        return "Room{" + "location=" + location + ", w=" + w + ", h=" + h + '}';
    }
    /**
     * Returns the northeast corner of this room.
     * @return 
     */
    public Location getNE() {
        return new Location(this.location.getX() + w - 1, this.location.getY());
    }
    /**
     * Returns the southwest corner of this room.
     * @return 
     */
    public Location getSW() {
        return new Location(this.location.getX(), this.location.getY() + h - 1);
    }
    /**
     * Returns the southeast corner of this room.
     * @return 
     */
    public Location getSE() {
        return new Location(this.location.getX() + w - 1, this.location.getY() + h - 1);
    }
    /**
     * Returns the northwest corner of this room.
     * @return 
     */
    public Location getNW() {
        return this.location;
    }
}
