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
public class Map {

    private Terrain[][] t;
    private Room[] rooms;
    private int w;
    private int h;

    public Map(Terrain[][] t, Room[] rooms, int w, int h) {
        this.t = t;
        this.rooms = rooms;
        this.h = h;
        this.w = w;
    }

    public Terrain[][] getT() {
        return t;
    }

    public Room[] getRooms() {
        return rooms;
    }

    public int getW() {
        return w;
    }

    public int getH() {
        return h;
    }
}
