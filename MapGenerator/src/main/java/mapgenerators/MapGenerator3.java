/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgenerators;

import java.util.ArrayDeque;
import java.util.Random;
import support.generic.LocationQueue;
import support.map.Location;
import support.map.Map;
import support.map.Room;
import support.map.Terrain;

/**
 * Create a map by first filling it randomly and then using cellular automata to
 * refine the terrain.
 *
 * @author konstakallama
 */
public class MapGenerator3 {

    Random r = new Random();
    Location r0loc;
    Map m;

    public Map createMap(int w, int h, double floorChance, double floorRatio, int wallAddIts, int noWallAddIts, boolean manhattan, long randomSeed) {
        r.setSeed(randomSeed);
        return this.createMap(w, h, floorChance, floorRatio, wallAddIts, noWallAddIts, manhattan);
    }

    public Map createMap(int w, int h, double floorChance, double floorRatio, int wallAddIts, int noWallAddIts, boolean manhattan) {
        Terrain[][] t;
        m = new Map(w, h);
        while (true) {
            t = this.generateTerrain(w, h, floorChance, wallAddIts, noWallAddIts, manhattan);
//            this.printMap(t);
//            System.out.println("");
            if (overXPercentFloor(t, floorRatio)) {
                break;
            }
        }
        Room[] rooms = new Room[1];
        Room r0 = new Room(this.r0loc, 1, 1);
        rooms[0] = r0;
        m.setRooms(rooms);
        return m;
    }

    private Terrain[][] generateTerrain(int w, int h, double floorChance, int wallAddIts, int noWallAddIts, boolean manhattan) {
        Terrain[][] t = this.initT(w, h);
        t = randomFill(floorChance, t);
        m.setT(t);
        m.recordHistory();
//        this.printMap(t);
//        System.out.println("");

        for (int i = 0; i < wallAddIts; i++) {
            if (manhattan) {
                t = manhattanIteration(t, true);
            } else {
                t = iteration(t, true);
            }
            
            m.recordHistory();
//            this.printMap(t);
//            System.out.println("");
        }

        for (int i = 0; i < noWallAddIts; i++) {
            if (manhattan) {
                t = manhattanIteration(t, false);
            } else {
                t = iteration(t, false);
            }
            m.recordHistory();
//            this.printMap(t);
//            System.out.println("");
        }

        t = fillUnreachable(t);
        m.recordHistory();

        return t;
    }

    /**
     * Initialize t to contain only wall.
     */
    private Terrain[][] initT(int w, int h) {
        Terrain[][] t = new Terrain[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                t[i][j] = Terrain.WALL;
            }
        }
        return t;
    }

    private Terrain[][] randomFill(double floorChance, Terrain[][] t) {
        for (int i = 1; i < t.length - 1; i++) {
            for (int j = 1; j < t[0].length - 1; j++) {
                if (r.nextDouble() < floorChance) {
                    t[i][j] = Terrain.FLOOR;
                }
            }
        }
        return t;
    }

    private Terrain[][] iteration(Terrain[][] t, boolean wallAddition) {
        for (int i = 1; i < t.length - 1; i++) {
            for (int j = 1; j < t[0].length - 1; j++) {
                int wc1 = this.wallCount(i, j, 1, 1, t);
                int wc2 = this.wallCount(i, j, 2, 2, t);
                if (wc1 >= 5) {
                    t[i][j] = Terrain.WALL;
                } else if (wallAddition && wc2 <= 2) {
                    t[i][j] = Terrain.WALL;
                } else if (wc1 <= 2) {
                    t[i][j] = Terrain.FLOOR;
                }
            }
        }
        return t;
    }
    
    private Terrain[][] manhattanIteration(Terrain[][] t, boolean wallAddition) {
        for (int i = 1; i < t.length - 1; i++) {
            for (int j = 1; j < t[0].length - 1; j++) {
                int wc1 = this.manhattanWallCount(i, j, 1, t);
                int wc2 = this.manhattanWallCount(i, j, 2, t);
                if (wc1 >= 3) {
                    t[i][j] = Terrain.WALL;
                } else if (wallAddition && wc2 <= 1) {
                    t[i][j] = Terrain.WALL;
                } else if (wc1 <= 1) {
                    t[i][j] = Terrain.FLOOR;
                } else if (!wallAddition && wc2 <= 2) {
                    t[i][j] = Terrain.FLOOR;
                }
            }
        }
        return t;
    }

    private int manhattanWallCount(int x, int y, int radius, Terrain[][] t) {
        int count = 0;
        for (int i = x - radius; i <= x + radius; i++) {
            for (int j = y - radius; j <= y + radius; j++) {
                if (Math.abs(x - i) + Math.abs(y - j) <= radius) {
                    if (!this.outOfBounds(i, j, t.length, t[0].length)) {
                        if (t[i][j] == Terrain.WALL) {
                            count++;
                        }
                    }
                }
            }
        }
        return count;
    }

    private int wallCount(int x, int y, int xScope, int yScope, Terrain[][] t) {
        int count = 0;
        for (int i = x - xScope; i <= x + xScope; i++) {
            for (int j = y - yScope; j <= y + yScope; j++) {
                if (!this.outOfBounds(i, j, t.length, t[0].length)) {
                    if (t[i][j] == Terrain.WALL) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    private boolean outOfBounds(int x, int y, int w, int h) {
        if (x < 0) {
            return true;
        } else if (x >= w) {
            return true;
        } else if (y < 0) {
            return true;
        } else if (y >= h) {
            return true;
        }

        return false;
    }

    private Terrain[][] fillUnreachable(Terrain[][] t) {
        Location l = this.getNonWallLocation(t);
        this.r0loc = l;
        boolean[][] reachable = this.bfs(t, l);
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                if (!reachable[i][j]) {
                    t[i][j] = Terrain.WALL;
                }
            }
        }
        return t;
    }

    private Location getNonWallLocation(Terrain[][] t) {
        int counter = 0;
        Location l = new Location(r.nextInt(t.length), r.nextInt(t[0].length));
        while (t[l.getX()][l.getY()] == Terrain.WALL && counter < 10000) {
            l = new Location(r.nextInt(t.length), r.nextInt(t[0].length));
            counter++;
        }
        return l;
    }

    private boolean[][] bfs(Terrain[][] t, Location l) {
        boolean[][] reachable = new boolean[t.length][t[0].length];
        LocationQueue q = new LocationQueue();
        q.enqueue(l);
        while (!q.empty()) {
            l = q.dequeue();
            if (reachable[l.getX()][l.getY()]) {
                continue;
            }
            reachable[l.getX()][l.getY()] = true;
            for (Location a : l.getAdjacent()) {
                if (!outOfBounds(a.getX(), a.getY(), t.length, t[0].length)) {
                    if (!reachable[a.getX()][a.getY()] && t[a.getX()][a.getY()] != Terrain.WALL) {
                        q.enqueue(a);
                    }
                }
            }
        }
        return reachable;
    }

    private boolean overXPercentFloor(Terrain[][] t, double x) {
        double floorCount = 0;
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                if (t[i][j] != Terrain.WALL) {
                    floorCount += 1;
                }
            }
        }
//        System.out.println("fc: " + floorCount);
//        System.out.println("rt: " + floorCount / (t.length * t[0].length));
        return floorCount / (t.length * t[0].length) > x;
    }

    private void printMap(Terrain[][] t) {
        for (int i = 0; i < t.length; i++) {
            for (int j = 0; j < t[0].length; j++) {
                String k = "#";
                if (t[i][j] == Terrain.FLOOR) {
                    k = ".";
                } else if (t[i][j] == Terrain.CORRIDOR) {
                    k = "o";
                }
                System.out.print(k + " ");
            }
            System.out.println("");
        }
    }

}
