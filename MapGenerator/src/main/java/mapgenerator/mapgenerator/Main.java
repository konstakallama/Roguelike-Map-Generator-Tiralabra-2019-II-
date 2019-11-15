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
public class Main {

    public static void main(String[] args) {
        MapGenerator m = new MapGenerator();
        MapGenerator2 m2 = new MapGenerator2();
        Map c = new Map(m2.createMap(50, 50), null, 50, 50);
        
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
