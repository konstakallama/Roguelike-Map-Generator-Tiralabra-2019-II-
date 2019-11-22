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
public class MapGenerator2Parameters {
    public int maxW;
    public int maxH;
    public int steps;
    public int minRoomW;
    public int minRoomH;
    public int maxRoomW;
    public int maxRoomH;
    public int minCorridorLen;
    public int maxCorridorLen;
    public double roomChance;
    public int connectDistance;
    
    public MapGenerator2Parameters(int maxW, int maxH, int steps, int minRoomW, int minRoomH, int maxRoomW, int maxRoomH, int minCorridorLen, int maxCorridorLen, double roomChance, int connectDistance) {
        if (maxW != 0) {
            this.maxW = maxW;
        } else {
            this.maxW = 50;
        }
        
        if (maxH != 0) {
            this.maxH = maxH;
        } else {
            this.maxH = 50;
        }
        
        if (steps != 0) {
            this.steps = steps;
        } else {
            this.steps = 1000;
        }
        
        if (minRoomW != 0) {
            this.minRoomW = minRoomW;
        } else {
            this.minRoomW = 3;
        }
        
        if (minRoomH != 0) {
            this.minRoomH = minRoomH;
        } else {
            this.minRoomH = 3;
        }
        
        if (maxRoomW != 0) {
            this.maxRoomW = maxRoomW;
        } else {
            this.maxRoomW = maxW/5;
        }
        
        if (maxRoomH != 0) {
            this.maxRoomH = maxRoomH;
        } else {
            this.maxRoomH = maxH/5;
        }
        
        if (minCorridorLen != 0) {
            this.minCorridorLen = minCorridorLen;
        } else {
            this.minCorridorLen = 5;
        }
        
        if (maxCorridorLen != 0) {
            this.maxCorridorLen = maxCorridorLen;
        } else {
            this.maxCorridorLen = 15;
        }
        
        if (roomChance != 0.0) {
            this.roomChance = roomChance;
        } else {
            this.roomChance = 0.7;
        }
        
        if (connectDistance != 0) {
            this.connectDistance = connectDistance;
        } else {
            this.connectDistance = 10;
        }
    }

    @Override
    public String toString() {
        return "MapGenerator2Parameters{" + "maxW=" + maxW + ", maxH=" + maxH + ", steps=" + steps + ", minRoomW=" + minRoomW + ", minRoomH=" + minRoomH + ", maxRoomW=" + maxRoomW + ", maxRoomH=" + maxRoomH + ", minCorridorLen=" + minCorridorLen + ", maxCorridorLen=" + maxCorridorLen + ", roomChance=" + roomChance + ", connectDistance=" + connectDistance + '}';
    }
    
}
