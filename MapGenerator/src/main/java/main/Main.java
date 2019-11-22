/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.logging.Level;
import java.util.logging.Logger;
import mapgenerators.MapGenerator;
import mapgenerators.MapGenerator2;
import org.apache.commons.lang.time.StopWatch;
import support.generic.MapGenerator2Parameters;
import support.map.Map;
import support.map.Terrain;

/**
 *
 * @author konstakallama
 */
public class Main {

    public static void main(String[] args) {
        MapGenerator m = new MapGenerator();
        MapGenerator2 m2 = new MapGenerator2();
        StopWatch sw = new StopWatch();
        //maxW, maxH, steps, minRoomW, minRoomH, maxRoomW, maxRoomH, minCorridorLen, maxCorridorLen, roomChance, connectDistance
        MapGenerator2Parameters par = new MapGenerator2Parameters(50, 50, 5000, 3, 3, 10, 10, 4, 10, 0.9, 1);

        sw.start();
        //Map c = m.createMap(50, 50, 5, 1);
        Map c = m2.createMap(par);
        sw.stop();

        long time = sw.getTime();

        printMap(c);

        System.out.println("Time elapsed: " + time + " ms");
    }

    private static void printMap(Map c) {
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