/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgenerator.mapgenerator;

import java.util.Random;
import java.util.Stack;

/**
 *
 * @author konstakallama
 */
public class MapGenerator {
    Random r = new Random();
    DynamicArray d;
    
    public Terrain[][] createMap(int w, int h, int roomN, int corridorN) {
        d = new DynamicArray(roomN);
        Terrain[][] t = initT(w, h);
        Room[] rooms = new Room[roomN];
        boolean[] connected = new boolean[roomN];
        
        connected[0] = true;
        
        for (int i = 0; i < roomN; i++) {
            Room room = this.createRoomIterator(t); //O(roomN * 100 * (w/5 * h/5)) ~ O(roomN * w * h)
            if (room != null) {
                rooms[i] = room;
            } else {
                connected[i] = true;
            }
        }
        
        boolean[][] corridors = addCorridors(roomN, corridorN, rooms); //O(roomN*corridorN)
        
        connected = dfs(corridors, connected); //O(roomN + min(roomN*corridorN, roomN^2))
        
        corridors = connectRooms(roomN, connected, corridors); //O(roomN)
        
        t = paintCorridors(t, rooms, corridors); //O(roomN^2 * (w+h))
        
        return t;
    }
    
    private Terrain[][] paintCorridors(Terrain[][] t, Room[] rooms, boolean[][] corridors) {
        for (int i = 0; i < corridors.length; i++) {
            for (int j = 0; j < corridors.length; j++) {
                if (i > j) {
                    if (rooms[i] != null && rooms[j] != null) {
                        t = paintCorridor(t, rooms[i], rooms[j]);
                    }
                }
            }
        }
        return t;
    }
    
    private boolean[][] connectRooms(int roomN, boolean[] connected, boolean[][] corridors) {
        for (int i = 0; i < roomN; i++) {
            if (!connected[i]) {
                int target = d.get(r.nextInt(d.getIndex()));
                corridors[i][target] = true;
                corridors[target][i] = true;
                connected[i] = true;
                d.add(i);
            }
        }
        return corridors;
    }
    
    private boolean[][] addCorridors(int roomN, int corridorN, Room[] rooms) {
        boolean[][] corridors = new boolean[roomN][roomN];
        for (int i = 0; i < roomN; i++) {
            if (rooms[i] != null) {
                for (int j = 0; j < corridorN; j++) {
                    int k = r.nextInt(roomN);
                    if (k != i && rooms[k] != null) {
                        corridors[i][k] = true;
                        corridors[k][i] = true;
                    }
                }
            }
        }
        return corridors;
    }
    
    private boolean[] dfs(boolean[][] corridors, boolean[] connected) {
        Stack<Integer> s = new Stack<>();
        s.push(0);
        
        while(!s.empty()) {
            int k = s.pop();
            connected[k] = true;
            d.add(k);
            for (int i = 0; i < corridors.length; i++) {
                if (corridors[k][i] && !connected[i]) {
                    s.push(i);
                }
            }
        }
        return connected;
    }
    
    private Terrain[][] initT(int w, int h) {
        Terrain[][] t = new Terrain[w][h];
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                t[i][j] = Terrain.WALL;
            }
        }
        return t;
    }
    
    private Room createRoomIterator(Terrain[][] t) {
        int i = 0;
        while (i < 1) {
            Room r = this.createRoom(t);
            if (r != null) {
                return r;
            }
            i++;
        }
        return null;
    }
    
    private Room createRoom(Terrain[][] t) {
        int w = 3 + r.nextInt(t.length / 5);
        int h = 3 + r.nextInt(t[0].length / 5);
        Location l = this.findRoomLocation(t, w, h);

        if (this.isValidRoomLocation(t, l.getX(), l.getY(), w, h)) {
            this.paintRoom(t, l, w, h);
            return new Room(l, w, h);
        }

        return null;
    }
    
    private Location findRoomLocation(Terrain[][] t, int w, int h) {
        int k = 0;
        int x = 0;
        int y = 0;

        while (k < 100) {
            x = r.nextInt(t.length - w);
            y = r.nextInt(t[0].length - h);
            if (this.isValidRoomLocation(t, x, y, w, h)) {
                break;
            }
            k++;
        }
        return new Location(x, y);
    }

    private boolean isValidRoomLocation(Terrain[][] t, int x, int y, int w, int h) {
        for (int i = Math.max(0, x - 2); i < Math.min(t.length, x + w + 2); i++) {
            for (int j = Math.max(0, y - 2); j < Math.min(t[0].length, y + h + 2); j++) {
                if (t[i][j] != Terrain.WALL) {
                    return false;
                }
            }
        }
        return true;
    }

    private void paintRoom(Terrain[][] t, Location l, int w, int h) {
        for (int i = l.getX(); i < l.getX() + w; i++) {
            for (int j = l.getY(); j < l.getY() + h; j++) {
                t[i][j] = Terrain.FLOOR;
            }
        }
    }
    
    private Terrain[][] paintCorridor(Terrain[][] t, Room from, Room to) {
        Direction d = getClosestDirForRooms(from, to);

        Location start = getCorridorStart(from, d, t);

        this.paintToDir(d, start, to, t);
        return t;
    }
    
    private Location getCorridorStart(Room from, Direction d, Terrain[][] t) {
        Location l = from.getMiddle();
        while (t[l.getX()][l.getY()] == Terrain.FLOOR) {
            l.move(d);
        }
        return l;
    }

    private Direction getClosestDirForRooms(Room from, Room to) {
        int fromX = from.getMiddle().getX();
        int fromY = from.getMiddle().getY();

        int toX = to.getMiddle().getX();
        int toY = to.getMiddle().getY();

        return this.getClosestDir(fromX, toX, fromY, toY);
    }

    private Direction getClosestDir(int fromX, int toX, int fromY, int toY) {
        if (Math.abs(fromX - toX) >= Math.abs(fromY - toY)) {
            if (fromX - toX < 0) {
                return Direction.RIGHT;
            } else {
                return Direction.LEFT;
            }
        } else {
            if (fromY - toY < 0) {
                return Direction.DOWN;
            } else if (fromY - toY > 0) {
                return Direction.UP;
            } else {
                return Direction.NONE;
            }
        }
    }
    
    private void paintToDir(Direction d, Location l, Room to, Terrain[][] t) {
        if (to.isNextTo(l)) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            return;
        }
        if (d == Direction.NONE) {
            return;
        }
        if (d == Direction.DOWN) {
            paintDown(l, to, t);
        } else if (d == Direction.UP) {
            paintUp(l, to, t);
        } else if (d == Direction.RIGHT) {
            paintRight(l, to, t);
        } else {
            paintLeft(l, to, t);
        }
    }
    
    private void paintDown(Location l, Room to, Terrain[][] t) {

        while (to.getLocation().getY() > l.getY() - 1) {

            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.DOWN);
        }
        if (to.isInside(l)) {
            l.move(Direction.UP);
            return;
        }
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX(), l.getY(), to.getLocation().getY()), l, to, t);

    }

    private void paintUp(Location l, Room to, Terrain[][] t) {
        while (to.getLocation().getY() + to.getH() <= l.getY() + 1) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.UP);
        }
        if (to.isInside(l)) {
            l.move(Direction.DOWN);
            return;
        }
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX(), l.getY(), to.getLocation().getY() + to.getH() - 1), l, to, t);

    }

    private void paintRight(Location l, Room to, Terrain[][] t) {
        while (to.getLocation().getX() > l.getX() - 1) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.RIGHT);
        }
        if (to.isInside(l)) {
            l.move(Direction.LEFT);
            return;
        }
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX(), l.getY(), to.getLocation().getY()), l, to, t);
    }

    private void paintLeft(Location l, Room to, Terrain[][] t) {
        while (to.getLocation().getX() + to.getW() <= l.getX() + 1) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            l.move(Direction.LEFT);
        }
        if (to.isInside(l)) {
            l.move(Direction.RIGHT);
            return;
        }
        this.paintToDir(this.getClosestDir(l.getX(), to.getLocation().getX() + to.getW() - 1, l.getY(), to.getLocation().getY()), l, to, t);
    }
}
