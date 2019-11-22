/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import java.util.ArrayDeque;
import java.util.Random;
import support.map.Direction;
import support.map.Location;
import support.map.Map;
import mapgenerators.MapGenerator;
import mapgenerators.MapGenerator2;
import support.map.Terrain;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import support.generic.MapGenerator2Parameters;
import org.apache.commons.lang.time.StopWatch;

public class Tests {

    private Random r = new Random();
    private MapGenerator mg = new MapGenerator();
    private MapGenerator2 mg2 = new MapGenerator2();
    private StopWatch sw = new StopWatch();
    private long maxTime = 500;

    public Tests() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {

    }

    @After
    public void tearDown() {
    }

    @Test
    public void defaultParametersMg1() {
        sw.reset();
        sw.start();
        Map m = mg.createMap(50, 50, 5, 1);
        sw.stop();
        assertTrue(sw.getTime() < maxTime);
        this.everyTileReachable(m);
        this.noDeadEnds(m);
    }

    @Test
    public void largeRoomNMg1() {
        sw.reset();
        sw.start();
        Map m = mg.createMap(50, 50, 25, 1);
        sw.stop();
        assertTrue(sw.getTime() < maxTime);
        this.everyTileReachable(m);
        this.noDeadEnds(m);
    }

    @Test
    public void noRandomCorridorsMg1() {
        sw.reset();
        sw.start();
        Map m = mg.createMap(50, 50, 5, 0);
        sw.stop();
        assertTrue(sw.getTime() < maxTime);
        this.everyTileReachable(m);
        this.noDeadEnds(m);
    }

    @Test
    public void defaultParametersMg2() {
        sw.reset();
        sw.start();
        Map m = mg2.createMap(50, 50);
        sw.stop();
        assertTrue(sw.getTime() < maxTime);
        this.everyTileReachable(m);
        this.noDeadEnds(m);
    }

    @Test
    public void randomParametersMg2() {
        for (int i = 0; i < 100; i++) {
            //maxW, maxH, steps, minRoomW, minRoomH, maxRoomW, maxRoomH, minCorridorLen, maxCorridorLen, roomChance, connectDistance
            int maxW = r.nextInt(100) + 20;
            int maxH = r.nextInt(100) + 20;
            int maxRoomW = Math.min(r.nextInt(10) + 10, maxW - 1);
            int maxRoomH = Math.min(r.nextInt(10) + 10, maxH - 1);
            MapGenerator2Parameters par = new MapGenerator2Parameters(maxW, maxH,
                    r.nextInt(50000) + 50, r.nextInt(5) + 1, r.nextInt(5) + 1, maxRoomW, maxRoomH,
                    r.nextInt(10) + 1, r.nextInt(10) + 15, Math.min(r.nextDouble() + 0.1, 1.0), r.nextInt(10));
            sw.reset();
            sw.start();
            Map m = mg2.createMap(par);
            sw.stop();

            //this.printMap(m);
            //System.out.println("i: " + i);
            //System.out.println("T: " + sw.getTime());
            //System.out.println(par.toString());
            //System.out.println("");
            assertTrue(sw.getTime() < maxTime);
            this.everyTileReachable(m);
            this.noDeadEnds(m);
        }
    }

    private void noDeadEnds(Map m) {
        for (int i = 0; i < 50; i++) {
            for (int j = 0; j < 50; j++) {
                assertFalse(this.deadEnd(new Location(i, j), m.getT()));
            }
        }
    }

    private void everyTileReachable(Map m) {
        Location l = m.getRooms()[0].getLocation();
        boolean[][] reachable = new boolean[m.getW()][m.getH()];
        reachable = bfs(m, l, reachable);
        for (int i = 0; i < m.getW(); i++) {
            for (int j = 0; j < m.getH(); j++) {
                assertTrue(reachable[i][j] || m.getT()[i][j] == Terrain.WALL);
            }
        }
    }

    private boolean[][] bfs(Map m, Location l, boolean[][] reachable) {
        ArrayDeque<Location> q = new ArrayDeque<>();
        q.add(l);
        while (!q.isEmpty()) {
            l = q.poll();
            if (reachable[l.getX()][l.getY()]) {
                continue;
            }
            reachable[l.getX()][l.getY()] = true;
            for (Location a : l.getAdjacent()) {
                if (!outOfBounds(a, m.getW(), m.getH())) {
                    if (!reachable[a.getX()][a.getY()] && m.getT()[a.getX()][a.getY()] != Terrain.WALL) {
                        q.add(a);
                    }
                }
            }
        }
        return reachable;
    }

    private boolean outOfBounds(Location a, int w, int h) {
        int x = a.getX();
        int y = a.getY();

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

    private boolean deadEnd(Location l, Terrain[][] t) {
        if (this.outOfBounds(l, t.length, t[0].length)) {
            return false;
        }
        if (t[l.getX()][l.getY()] != Terrain.CORRIDOR) {
            return false;
        }
        int count = 0;

        Direction turn = Direction.DOWN;

        for (int k = 0; k < 4; k++) {
            if (this.notOutOfBoundsAndNotWall(l.locInDir(turn), t)) {
                count++;
            }
            turn = turn.getClockwiseTurn();
        }

        if (count == 1) {
            return true;
        }
        return false;

    }

    private boolean notOutOfBoundsAndNotWall(Location l, Terrain[][] t) {
        if (!this.outOfBounds(l, t.length, t[0].length)) {
            if (t[l.getX()][l.getY()] != Terrain.WALL) {
                return true;
            }
        }
        return false;
    }

    private void printMap(Map c) {
        for (int i = 0; i < c.getT().length; i++) {
            for (int j = 0; j < c.getT()[0].length; j++) {
                String k = "#";
                if (c.getT()[i][j] == Terrain.FLOOR) {
                    k = ".";
                } else if (c.getT()[i][j] == Terrain.CORRIDOR) {
                    k = "o";
                }
                System.out.print(k + " ");
            }
            System.out.println("");
        }
    }
}
