/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;

import java.util.ArrayDeque;
import java.util.Random;
import mapgenerator.mapgenerator.Location;
import mapgenerator.mapgenerator.Map;
import mapgenerator.mapgenerator.MapGenerator;
import mapgenerator.mapgenerator.Terrain;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class Tests {

    private Random r = new Random();
    private MapGenerator mg = new MapGenerator();

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
    public void defaultParameters() {
        this.everyTileReachable(50, 50, 5, 1);
    }
    
    @Test
    public void largeRoomN() {
        this.everyTileReachable(50, 50, 25, 1);
    }
    
    @Test
    public void noRandomCorridors() {
        this.everyTileReachable(50, 50, 5, 0);
    }
    
    
    
    
    
    private void everyTileReachable(int w, int h, int roomN, int corridorN) {
        Map m = mg.createMap(w, h, roomN, corridorN);
        Location l = m.getRooms()[0].getMiddle();
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
        } else if (x > w) {
            return true;
        } else if (y < 0) {
            return true;
        } else if (y > h) {
            return true;
        }
        
        return false;
    }
}
