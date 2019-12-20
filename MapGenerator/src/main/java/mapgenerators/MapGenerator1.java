/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgenerators;

import java.util.Random;
import support.generic.DirectionPair;
import support.generic.DynamicArray;
import support.generic.IntegerStack;
import support.map.Direction;
import support.map.Location;
import support.map.Map;
import support.map.Room;
import support.map.Terrain;

/**
 *
 * @author konstakallama
 */
public class MapGenerator1 {

    Random r = new Random();
    DynamicArray d;
    Map m;

    /**
     * Create a Map with the old otm-algorithm.
     *
     * @param w Map width
     * @param h Map Height
     * @param roomN Maximum number of rooms. The algorithm will try to create
     * this many rooms but drops the number if it can't find space.
     * @param corridorN Number of random-destination corridors from each room.
     * @return The created Map.
     */
    public Map createMap(int w, int h, int roomN, int corridorN) {
        d = new DynamicArray(roomN);
        Terrain[][] t = initT(w, h);
        Room[] rooms = new Room[roomN];
        boolean[] connected = new boolean[roomN];

        connected[0] = true;
        
        m = new Map(w, h);
        m.setT(t);

        for (int i = 0; i < roomN; i++) {
            Room room = this.createRoom(t); //O(roomN * 100 * (w/5 * h/5)) ~ O(roomN * w * h)
            m.recordHistory();
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

        //Map m = new Map(t, rooms, w, h);
        m.setRooms(rooms);

        return m;
    }
    
    /**
     * Create Map with a given random seed. Otherwise identical to the other createMap.
     */
    public Map createMap(int w, int h, int roomN, int corridorN, long randomSeed) {
        r.setSeed(randomSeed);
        return this.createMap(w, h, roomN, corridorN);
    }

    /**
     * Paint the corridors in the corridors array into t.
     */
    private Terrain[][] paintCorridors(Terrain[][] t, Room[] rooms, boolean[][] corridors) {
        for (int i = 0; i < corridors.length; i++) {
            for (int j = 0; j < corridors.length; j++) {
                if (i > j) {
                    if (rooms[i] != null && rooms[j] != null) {
                        t = paintCorridor(t, rooms[i], rooms[j]);
                        m.recordHistory();
                    }
                }
            }
        }
        return t;
    }

    /**
     * Add corridors from every unconnected room to a random connected room.
     */
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

    /**
     * Add corridorN corridors from each room to some random other room.
     */
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

    /**
     * Find which rooms are connected to the first room via dfs.
     */
    private boolean[] dfs(boolean[][] corridors, boolean[] connected) {
        IntegerStack s = new IntegerStack();
        s.push(0);

        while (!s.empty()) {
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

    /**
     * Paint a room of random rectangular size to a random location. Returns
     * null if it can't find a valid location.
     */
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

    /**
     * Create random room locations until finding a valid one or failing a set
     * amount of times.
     */
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

    /**
     * Check if given room fits into given location.
     */
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

    /**
     * Paint rectangular room (w, h) to l.
     */
    private void paintRoom(Terrain[][] t, Location l, int w, int h) {
        for (int i = l.getX(); i < l.getX() + w; i++) {
            for (int j = l.getY(); j < l.getY() + h; j++) {
                t[i][j] = Terrain.FLOOR;
            }
        }
    }

    /**
     * Paint a corridor from the middle of room from to the middle of room to. Always makes a simple corridor with just one turn, first traveling to the direction with more distance to traverse in.
     */
    private Terrain[][] paintCorridor(Terrain[][] t, Room from, Room to) {
        DirectionPair dp = getDirectionsForRooms(from, to);
        Location start = getCorridorStart(from, dp.getD1(), t);   
        Location turn = paintToDirection(dp.getD1(), start, to, t);
        paintToDirection(dp.getD2(), turn, to, t);
        return t;
    }
    
    /**
     * Returns the horizontal direction you have to travel in to get to room to from room from.
     */
    private Direction getHorizontalForRooms(Room from, Room to) {
        if (from.getMiddle().getX() < to.getMiddle().getX()) {
            return Direction.RIGHT;
        } else if (from.getMiddle().getX() > to.getMiddle().getX()) {
            return Direction.LEFT;
        } else {
            return Direction.NONE;
        }
    }
    
    /**
     * Returns the vertical direction you have to travel in to get to room to from room from.
     */
    private Direction getVerticalForRooms(Room from, Room to) {
        if (from.getMiddle().getY() < to.getMiddle().getY()) {
            return Direction.DOWN;
        } else if (from.getMiddle().getY() > to.getMiddle().getY()) {
            return Direction.UP;
        } else {
            return Direction.NONE;
        }
    }

    /**
     * Returns the directions you have to travel in to get to room to from room from. The direction with more distance to traverse in will be first. If one of the directions is NONE, it will be second.
     */
    private DirectionPair getDirectionsForRooms(Room from, Room to) {
        Direction d1 = this.getHorizontalForRooms(from, to);
        Direction d2 = this.getVerticalForRooms(from, to);
        if (d1 == Direction.NONE) {
            return new DirectionPair(d2, d1);
        } else if (d2 == Direction.NONE) {
            return new DirectionPair(d1, d2);
        } else {
            if (Math.abs(from.getMiddle().getX() - to.getMiddle().getX()) > Math.abs(from.getMiddle().getY() - to.getMiddle().getY())) {
                return new DirectionPair(d1, d2);
            } else {
                return new DirectionPair(d2, d1);
            }
        }
    }

    /**
     * Returns the location on the edge of the room in direction d from the middle.
     */
    private Location getCorridorStart(Room from, Direction d, Terrain[][] t) {
        Location l = from.getMiddle();
        while (t[l.getX()][l.getY()] == Terrain.FLOOR) {
            l.move(d);
        }
        return l;
    }

    /**
     * Paint a corridor down until you can't get any closer in this direction.
     */
    private Location paintDown(Location l, Room to, Terrain[][] t) {
        while (to.getMiddle().getY() > l.getY()) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            if (to.isNextTo(l)) {
                break;
            }
            l.move(Direction.DOWN);
        }
        return l;
    }

    /**
     * Paint a corridor up until you can't get any closer in this direction.
     */
    private Location paintUp(Location l, Room to, Terrain[][] t) {
        while (to.getMiddle().getY() < l.getY()) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            if (to.isNextTo(l)) {
                break;
            }
            l.move(Direction.UP);
        }
        return l;
    }

    /**
     * Paint a corridor right until you can't get any closer in this direction.
     */
    private Location paintRight(Location l, Room to, Terrain[][] t) {
        while (to.getMiddle().getX() > l.getX()) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            if (to.isNextTo(l)) {
                break;
            }
            l.move(Direction.RIGHT);
        }
        return l;
    }

    /**
     * Paint a corridor left until you can't get any closer in this direction.
     */
    private Location paintLeft(Location l, Room to, Terrain[][] t) {
        while (to.getMiddle().getX() < l.getX()) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            if (to.isNextTo(l)) {
                break;
            }
            l.move(Direction.LEFT);
        }
        return l;
    }

    /**
     * Call the appropriate paint[Direction] method for d.
     */
    private Location paintToDirection(Direction d, Location l, Room to, Terrain[][] t) {
        if (to.isNextTo(l) || d == Direction.NONE) {
            if (t[l.getX()][l.getY()] == Terrain.WALL) {
                t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            }
            return l;
        }
        if (d == Direction.DOWN) {
            return paintDown(l, to, t);
        } else if (d == Direction.UP) {
            return paintUp(l, to, t);
        } else if (d == Direction.RIGHT) {
            return paintRight(l, to, t);
        } else {
            return paintLeft(l, to, t);
        }
    }
}
