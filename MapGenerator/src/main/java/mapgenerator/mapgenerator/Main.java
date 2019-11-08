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
        Terrain[][] c = m.createMap(50, 50, 6, 1);
        for (int i = 0; i < c.length; i++) {
            for (int j = 0; j < c[0].length; j++) {
                String k = "#";
                if (c[i][j] == Terrain.FLOOR) {
                    k = ".";
                } else if (c[i][j] == Terrain.CORRIDOR) {
                    k = "o";
                }
                System.out.print(k + " ");
            }
            System.out.println("");
        }
    }
}
