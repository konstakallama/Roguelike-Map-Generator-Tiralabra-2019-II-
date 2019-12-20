/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package support.map;

/**
 *
 * @author konstakallama
 */
public class Map {

    private Terrain[][] t;
    private Terrain[][][] terrainHistory;
    private int hi;
    private int maxHi;
    private Room[] rooms;
    private int w;
    private int h;

    public Map(int w, int h) {
        this.w = w;
        this.h = h;
        this.terrainHistory = new Terrain[1000][w][h];
        this.hi = 0;
        this.maxHi = 1000;
    }

    public Map(Terrain[][] t, Room[] rooms, int w, int h) {
        this.t = t;
        this.rooms = rooms;
        this.h = h;
        this.w = w;
    }

    public Terrain[][] getT() {
        return t;
    }

    public Terrain getT(int x, int y) {
        return t[x][y];
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

    public void setT(Terrain[][] t) {
        this.t = t;
    }

    public void setRooms(Room[] rooms) {
        this.rooms = rooms;
    }

    /**
     * Record the current state of t to terrainHistory.
     */
    public void recordHistory() {
        if (hi < maxHi) {
            copyT();
            hi++;
        } else {
            increaseArraySize();
            recordHistory();
        }

    }

    /**
     * Increase the size of the z (time) dimension of the terrainHistory array.
     */
    private void increaseArraySize() {
        Terrain[][][] newTH = new Terrain[maxHi * 2][w][h];
        for (int i = 0; i < hi; i++) {
            newTH[i] = this.terrainHistory[i];
        }
        maxHi = maxHi * 2;
        this.terrainHistory = newTH;
    }

    public Terrain[][][] getTerrainHistory() {
        return terrainHistory;
    }

    public int getHi() {
        return hi;
    }

    /**
     * Copy the current state of t to terrainHistory (can't just assign because of mutability).
     */
    private void copyT() {
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                this.terrainHistory[hi][i][j] = t[i][j];
            }
        }
    }
}
