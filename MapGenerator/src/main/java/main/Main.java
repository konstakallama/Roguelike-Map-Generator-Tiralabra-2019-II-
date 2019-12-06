/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import mapgenerators.MapGenerator1;
import mapgenerators.MapGenerator2;
import mapgenerators.MapGenerator3;
import org.apache.commons.lang.time.StopWatch;
import support.generic.MapGenerator2Parameters;
import support.map.Map;
import support.map.Terrain;
import java.util.concurrent.TimeUnit;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;

/**
 *
 * @author konstakallama
 */
public class Main extends Application {

    int pixelSize = 12;
    Canvas mapCanvas;
    GraphicsContext drawer;
    BorderPane framework = new BorderPane();
    Scene historyScene;
    Scene menuScene;
    Stage stage;
    long time;
    int historyIndex;

    MapGenerator1 m1 = new MapGenerator1();
    MapGenerator2 m2 = new MapGenerator2();
    MapGenerator3 m3 = new MapGenerator3();

    public static void main(String[] args) {
        launch(Main.class);

    }

    public void drawMap(GraphicsContext drawer, Map map) {
        for (int i = 0; i < map.getT().length; i++) {
            for (int j = 0; j < map.getT()[0].length; j++) {
                if (map.getT(i, j) == Terrain.WALL) {
                    paintTile(i * pixelSize, j * pixelSize, "Black", drawer);
                } else if (map.getT(i, j) == Terrain.FLOOR) {
                    paintTile(i * pixelSize, j * pixelSize, "White", drawer);
                } else if (map.getT(i, j) == Terrain.CORRIDOR) {
                    paintTile(i * pixelSize, j * pixelSize, "White", drawer);
                } else if (map.getT(i, j) == Terrain.STAIRS) {
                    paintTile(i * pixelSize, j * pixelSize, "Yellow", drawer);
                }
            }
        }
    }

    private void paintTile(int x, int y, String color, GraphicsContext drawer) {
        drawer.setFill(Paint.valueOf(color));
        drawer.fillRect(x, y, pixelSize, pixelSize);
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.stage = primaryStage;
        menu();

//        printMap(m);
//        System.out.println("Time elapsed: " + time + " ms");
    }

    private void historyAnimation(Map m) {
        this.framework = new BorderPane();
        framework.setMinSize(50 * pixelSize, 60 * pixelSize);

        mapCanvas = new Canvas(m.getW() * this.pixelSize, m.getH() * this.pixelSize);
        drawer = mapCanvas.getGraphicsContext2D();

        framework.setCenter(mapCanvas);
        historyIndex = 0;
        drawMap(drawer, new Map(m.getTerrainHistory()[0], null, 0, 0));

        historyScene = new Scene(framework);
        stage.setScene(historyScene);
        stage.show();

        historyScene.setOnKeyPressed((event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                historyIndex++;
                if (historyIndex < m.getHi()) {
                    drawMap(drawer, new Map(m.getTerrainHistory()[historyIndex], null, 0, 0));
                } else {
                    drawMap(drawer, m);
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                menu();
            }
        });

//        for (int i = 0; i < m.getHi(); i++) {
//            drawMap(drawer, new Map(m.getTerrainHistory()[i], null, 0, 0));
//            try {
//                TimeUnit.MILLISECONDS.sleep(500);
//            } catch (Exception e) {
//                
//            }
//            
//        }
    }

    private Map createMap() {

        StopWatch sw = new StopWatch();
        //maxW, maxH, steps, minRoomW, minRoomH, maxRoomW, maxRoomH, minCorridorLen, maxCorridorLen, roomChance, connectDistance
        MapGenerator2Parameters par = new MapGenerator2Parameters(50, 50, 500, 3, 3, 10, 10, 4, 10, 0.9, 1);

        sw.start();
        Map c = m1.createMap(50, 50, 5, 1);
        //Map c = m2.createMap(par);
        //Map c = m3.GenerateMap(50, 50, 0.65, 0.15, 4, 2);
        sw.stop();

        time = sw.getTime();
        return c;
    }

    private void menu() {
        //Map m = createMap();

        HBox menuButtons = new HBox();

        VBox b1box = createB1Box();
        VBox b2box = createB2Box();
        VBox b3box = createB3Box();

        menuButtons.getChildren().add(b1box);
        menuButtons.getChildren().add(b2box);
        menuButtons.getChildren().add(b3box);

        menuButtons.setAlignment(Pos.CENTER);
        menuButtons.setMinSize(50 * pixelSize, 60 * pixelSize);

        menuScene = new Scene(menuButtons);

        stage.setScene(menuScene);
        stage.show();
    }

    private void mg1ClickedEvent(TextField mw1, TextField mh1, TextField rn, TextField cn) {
        int w;
        int h;
        int roomN;
        int corrN;

        try {
            w = Integer.parseInt(mw1.getText());
        } catch (Exception e) {
            w = 50;
        }

        try {
            h = Integer.parseInt(mh1.getText());
        } catch (Exception e) {
            h = 50;
        }

        try {
            roomN = Integer.parseInt(rn.getText());
        } catch (Exception e) {
            roomN = 5;
        }

        try {
            corrN = Integer.parseInt(cn.getText());
        } catch (Exception e) {
            corrN = 1;
        }

        Map c1 = m1.createMap(w, h, roomN, corrN);
        this.historyAnimation(c1);
    }

    private VBox createB1Box() {
        VBox b1box = new VBox();
        TextField mw1 = new TextField("Map Width");
        TextField mh1 = new TextField("Map Height");
        TextField rn = new TextField("Room Number");
        TextField cn = new TextField("Corridor Number");
        Button b1 = new Button("Mg1");
        b1.setOnMouseClicked((event) -> {
            mg1ClickedEvent(mw1, mh1, rn, cn);

        });
        b1box.getChildren().addAll(mw1, mh1, rn, cn, b1);
        return b1box;
    }

    private VBox createB2Box() {
        //maxW, maxH, steps, minRoomW, minRoomH, maxRoomW, maxRoomH, minCorridorLen, maxCorridorLen, roomChance, connectDistance
        VBox b2box = new VBox();
        TextField mw2 = new TextField("Map Width");
        TextField mh2 = new TextField("Map Height");
        TextField steps = new TextField("Steps");
        TextField minrw = new TextField("Min Room Width");
        TextField minrh = new TextField("Min Room Height");
        TextField maxrw = new TextField("Max Room Width");
        TextField maxrh = new TextField("Max Room Height");
        TextField mincl = new TextField("Min Corr Length");
        TextField maxcl = new TextField("Max Corr Length");
        TextField rc = new TextField("Room Chance");
        TextField cd = new TextField("Connect Distance");
        Button b2 = new Button("Mg2");
        b2.setOnMouseClicked((event) -> {
            mg2ClickedEvent(mw2, mh2, steps, minrw, minrh, maxrw, maxrh, mincl, maxcl, rc, cd);
        });
        b2box.getChildren().addAll(mw2, mh2, steps, minrw, minrh, maxrw, maxrh, mincl, maxcl, rc, cd, b2);
        return b2box;
    }

    private VBox createB3Box() {
        VBox b3box = new VBox();
        TextField mw3 = new TextField("Map Width");
        TextField mh3 = new TextField("Map Height");
        TextField fc = new TextField("Floor Chance");
        TextField fr = new TextField("Min Floor Ratio");
        TextField wai = new TextField("Iterations With Wall Add");
        TextField nwai = new TextField("Iterations W/o Wall Add");
        Button b3 = new Button("Mg3");
        b3.setOnMouseClicked((event) -> {
            mg3ClickedEvent(mw3, mh3, fc, fr, wai, nwai);
        });
        b3box.getChildren().addAll(mw3, mh3, fc, fr, wai, nwai, b3);
        return b3box;
    }

    private void mg2ClickedEvent(TextField mw2, TextField mh2, TextField steps, TextField minrw, TextField minrh, TextField maxrw, TextField maxrh, TextField mincl, TextField maxcl, TextField rc, TextField cd) {
        int w;
        int h;
        int s;
        int minRoomW;
        int maxRoomW;
        int minRoomH;
        int maxRoomH;
        int minCorrL;
        int maxCorrL;
        double roomChance;
        int connectDistance;

        try {
            w = Integer.parseInt(mw2.getText());
        } catch (Exception e) {
            w = 50;
        }

        try {
            h = Integer.parseInt(mh2.getText());
        } catch (Exception e) {
            h = 50;
        }

        try {
            s = Integer.parseInt(steps.getText());
        } catch (Exception e) {
            s = 500;
        }

        try {
            minRoomW = Integer.parseInt(minrw.getText());
        } catch (Exception e) {
            minRoomW = 3;
        }

        try {
            maxRoomW = Integer.parseInt(maxrw.getText());
        } catch (Exception e) {
            maxRoomW = 10;
        }

        try {
            minRoomH = Integer.parseInt(minrh.getText());
        } catch (Exception e) {
            minRoomH = 3;
        }

        try {
            maxRoomH = Integer.parseInt(maxrh.getText());
        } catch (Exception e) {
            maxRoomH = 10;
        }

        try {
            minCorrL = Integer.parseInt(mincl.getText());
        } catch (Exception e) {
            minCorrL = 4;
        }

        try {
            maxCorrL = Integer.parseInt(maxcl.getText());
        } catch (Exception e) {
            maxCorrL = 10;
        }

        try {
            roomChance = Double.parseDouble(rc.getText());
        } catch (Exception e) {
            roomChance = 0.9;
        }

        try {
            connectDistance = Integer.parseInt(minrw.getText());
        } catch (Exception e) {
            connectDistance = 3;
        }

        MapGenerator2Parameters par = new MapGenerator2Parameters(w, h, s, minRoomW, maxRoomW, minRoomH, maxRoomH, minCorrL, maxCorrL, roomChance, connectDistance);
        Map c2 = m2.createMap(par);
        this.historyAnimation(c2);
    }

    private void mg3ClickedEvent(TextField mw3, TextField mh3, TextField fc, TextField fr, TextField wai, TextField nwai) {
        int w;
        int h;
        double floorChance;
        double floorRatio;
        int waits;
        int nwaits;

        try {
            w = Integer.parseInt(mw3.getText());
        } catch (Exception e) {
            w = 50;
        }

        try {
            h = Integer.parseInt(mh3.getText());
        } catch (Exception e) {
            h = 50;
        }

        try {
            floorChance = Double.parseDouble(fc.getText());
        } catch (Exception e) {
            floorChance = 0.65;
        }
        
        try {
            floorRatio = Double.parseDouble(fr.getText());
        } catch (Exception e) {
            floorRatio = 0.15;
        }

        try {
            waits = Integer.parseInt(wai.getText());
        } catch (Exception e) {
            waits = 5;
        }
        
        try {
            nwaits = Integer.parseInt(nwai.getText());
        } catch (Exception e) {
            nwaits = 1;
        }

        Map c3 = m3.createMap(w, h, floorChance, floorRatio, waits, nwaits, false);
        this.historyAnimation(c3);
    }
}
