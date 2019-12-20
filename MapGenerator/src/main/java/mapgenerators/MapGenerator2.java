/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgenerators;

import support.generic.LocDir;
import support.map.Direction;
import support.map.Location;
import support.map.Map;
import support.map.Terrain;
import support.map.Room;
import java.util.Random;
import support.generic.MapGenerator2Parameters;

/**
 *
 * @author konstakallama
 */
public class MapGenerator2 {
    

    Random r = new Random();
    int maxW;
    int maxH;
    MapGenerator2Parameters par;
    Map m;
    Room[] rooms;
    int ri;
    
    public Map createMap(int maxW, int maxH) {
        MapGenerator2Parameters par = new MapGenerator2Parameters(maxW, maxH, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return this.createMap(par);
    }
    
    public Map createMap(int maxW, int maxH, long randomSeed) {
        r.setSeed(randomSeed);
        MapGenerator2Parameters par = new MapGenerator2Parameters(maxW, maxH, 0, 0, 0, 0, 0, 0, 0, 0, 0);
        return this.createMap(par);
    }
    
    public Map createMap(MapGenerator2Parameters par, long randomSeed) {
        r.setSeed(randomSeed);
        return this.createMap(par);
    }

    public Map createMap(MapGenerator2Parameters par) {
        this.par = par;
        this.maxW = par.maxW;
        this.maxH = par.maxH;
        Terrain[][] t = initT(maxW, maxH);
        m = new Map(maxW, maxH);
        m.setT(t);
        
        //Create first room of random size and location
        int w = par.minRoomW + r.nextInt(par.maxRoomW);
        int h = par.minRoomW + r.nextInt(par.maxRoomW);
        Room r0 = new Room(new Location(r.nextInt(maxW - par.maxRoomW) + 1, r.nextInt(maxH - par.maxRoomH + 1)), w, h);
        paintRoom(t, r0.getLocation(), r0.getW(), r0.getH());
        rooms = new Room[50];
        rooms[0] = r0;
        ri = 1;
        
        
              
        m.recordHistory();
        
        //Attempt to add something steps amount of times
        for (int i = 0; i < par.steps; i++) {
            
            //Select random wall next to a room or corridor, also returns the direction in which to build
            LocDir ld = selectRandomWall(t);

            //Selection is random atm so it's possible that it fails
            if (ld == null) {
                continue;
            }

            double rn = r.nextDouble();
            Location behind = ld.getL().locInDir(ld.getD().getOpposite());
            //If behind is a room, try a corridor. Otherwise random besed on roomChance.
            if (rn < par.roomChance && t[behind.getX()][behind.getY()] != Terrain.FLOOR) {
                tryRoom(ld.getL(), ld.getD(), t);
            } else {
                tryCorridorWithConnect(ld.getL(), ld.getD(), t);
            }
        }

        removeDeadEnds(t);
        
        
        m.setRooms(rooms);

        return m;
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
     * Selects random tiles until it finds and returns one that is wall next to just one floor/corridor tile.
     */
    private LocDir selectRandomWall(Terrain[][] t) {
        for (int i = 0; i < 100; i++) {
            Location l = new Location(r.nextInt(maxW), r.nextInt(maxH));
            Direction d = wallNextToFloor(l, t);
            if (d != Direction.NONE) {
                return new LocDir(l, d);
            }
        }
        return null;
    }

    /**
     * Checks if tile is wall with exactly 1 adjacent floor/corridor.
     */
    private Direction wallNextToFloor(Location l, Terrain[][] t) {
        if (t[l.getX()][l.getY()] != Terrain.WALL) {
            return Direction.NONE;
        }

        int floors = 0;
        Direction d = Direction.NONE;
        for (Location a : l.getAdjacent()) {
            if (!this.outOfBounds(a)) {
                if (t[a.getX()][a.getY()] != Terrain.WALL) {
                    floors++;
                    d = a.getClosestDir(l);
                }
            }
        }
        if (floors == 1) {
            return d;
        }
        return Direction.NONE;
    }
    
    /**
     * Tries to add a room of random size to d from l. Does nothing if the terrain there is not wall.
     */
    private void tryRoom(Location l, Direction d, Terrain[][] t) {
        int w = par.minRoomW + r.nextInt(par.maxRoomW);
        int h = par.minRoomW + r.nextInt(par.maxRoomW);

        if (validRoomLocation(l, d, t, w, h)) {
            t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            Location nw = getRoomNw(l, d, w, h);
            paintRoom(t, nw, w, h);
            
            if (ri >= rooms.length) {
                Room[] nr = new Room[rooms.length * 2];
                for (int i = 0; i < ri; i++) {
                    nr[i] = rooms[i];
                }
                rooms = nr;
            }
            rooms[ri] = new Room(nw, w, h);
            ri++;
            
            m.recordHistory();
        }
    }

    /**
     * Dumb corridor build which fails if terrain is not wall. Unused.
     */
    private void tryCorridorNoConnect(Location l, Direction d, Terrain[][] t) {
        int len = par.minCorridorLen + r.nextInt(par.maxCorridorLen - par.minCorridorLen);
        if (this.validRoomLocation(l, d, t, 3, len)) {
            paintCorridor(l, d, len, t);
        }
    }

    /**
     * Paint rectangular room (w, h) to l.
     */
    private void paintRoom(Terrain[][] t, Location l, int w, int h) {
        for (int i = l.getX(); i < l.getX() + w; i++) {
            for (int j = l.getY(); j < l.getY() + h; j++) {
                if (!this.outOfBounds(new Location(i, j))) {
                    t[i][j] = Terrain.FLOOR;
                }
            }
        }
    }

    /**
     * True if terrain in w x h space in direction d from location l is only wall.
     */
    private boolean validRoomLocation(Location l, Direction d, Terrain[][] t, int w, int h) {
        return this.checkRoomLocation(this.getRoomNw(l, d, w, h), this.getRoomSe(l, d, w, h), t);
    }

    /**
     * Get the northwest corner of the room of width and height w, h if it was painted in direction d from location l.
     */
    private Location getRoomNw(Location l, Direction d, int w, int h) {
        if (d == Direction.DOWN) {
            return new Location(l.getX() - (w / 2), l.getY() + 1);
        } else if (d == Direction.LEFT) {
            return new Location(l.getX() - w, l.getY() - (h / 2));
        } else if (d == Direction.RIGHT) {
            return new Location(l.getX() + 1, l.getY() - (h / 2));
        } else if (d == Direction.UP) {
            return new Location(l.getX() - (w / 2), l.getY() - h);
        }
        return null;
    }

    /**
     * Get the southeast corner of the room of width and height w, h if it was painted in direction d from location l.
     */
    private Location getRoomSe(Location l, Direction d, int w, int h) {
        if (d == Direction.DOWN) {
            return new Location(l.getX() + (w / 2), l.getY() + h);
        } else if (d == Direction.LEFT) {
            return new Location(l.getX() - 1, l.getY() + (h / 2));
        } else if (d == Direction.RIGHT) {
            return new Location(l.getX() + w, l.getY() + (h / 2));
        } else if (d == Direction.UP) {
            return new Location(l.getX() + (w / 2), l.getY() - 1);
        }
        return null;
    }

    /**
     * Check if room with given nw and se corners fits, ie if that area contains only wall.
     */
    private boolean checkRoomLocation(Location nw, Location se, Terrain[][] t) {
        for (int i = nw.getX() - 1; i < se.getX() + 2; i++) {
            for (int j = nw.getY() - 1; j < se.getY() + 2; j++) {
                if (this.outOfBounds(new Location(i, j))) {
                    return false;
                } else if (t[i][j] != Terrain.WALL) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Check if location is out of bounds for the map.
     */
    private boolean outOfBounds(Location a) {
        int x = a.getX();
        int y = a.getY();

        if (x < 0) {
            return true;
        } else if (x >= maxW) {
            return true;
        } else if (y < 0) {
            return true;
        } else if (y >= maxH) {
            return true;
        }

        return false;
    }

    /**
     * Paint a corridor of len tiles in direction d from location l. Stops if encounters something other than wall.
     */
    private void paintCorridor(Location l, Direction d, int len, Terrain[][] t) {
        for (int i = 0; i < len; i++) {
            if (this.outOfBounds(l)) {
                break;
            } else if (t[l.getX()][l.getY()] != Terrain.WALL) {
                break;
            }
            t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            l.move(d);
        }
    }

    /**
     * Paint corridor of random length from l to d. Extends by a maximum of connectDistance if there is a floor/corridor tile to connect to within that distance.
     */
    private void tryCorridorWithConnect(Location l, Direction d, Terrain[][] t) {
        int len = par.minCorridorLen + r.nextInt(par.maxCorridorLen - par.minCorridorLen);
        Location oldL = new Location(l.getX(), l.getY());
        if (scanCorridor(l, d, len, t)) {
            l.move(d, len);
            if (this.scanCorridor(l, d, par.connectDistance, t) && canConnect(l, d, par.connectDistance, t)) {
                len += par.connectDistance;
            }
            this.paintCorridor(oldL, d, len, t);
            m.recordHistory();
        }
    }

    /**
     * True if terrain surrounding specified corridor is only wall. Does not check tiles the corridor will actually be painted on.
     */
    private boolean scanCorridor(Location l, Direction d, int len, Terrain[][] t) {
        for (int i = 0; i < len; i++) {
            Location l0 = l.locInDir(d.getClockwiseTurn());
            if (this.outOfBoundsOrNotWall(l0, t)) {
                return false;
            }
            l0 = l.locInDir(d.getCounterclockwiseTurn());
            if (this.outOfBoundsOrNotWall(l0, t)) {
                return false;
            }
            l = l.locInDir(d);
        }
        return true;
    }

    private boolean outOfBoundsOrNotWall(Location l, Terrain[][] t) {
        if (this.outOfBounds(l)) {
            return true;
        } else if (t[l.getX()][l.getY()] != Terrain.WALL) {
            return true;
        }
        return false;
    }

    private boolean notOutOfBoundsAndNotWall(Location l, Terrain[][] t) {
        if (!this.outOfBounds(l)) {
            if (t[l.getX()][l.getY()] != Terrain.WALL) {
                return true;
            }
        }
        return false;
    }

    /**
     * True if there is a floor/corridor tile in specified corridor's path.
     */
    private boolean canConnect(Location l, Direction d, int len, Terrain[][] t) {
        for (int i = 0; i < len; i++) {
            if (this.notOutOfBoundsAndNotWall(l, t)) {
                return true;
            }
            l = l.locInDir(d);
        }
        return false;
    }

    /**
     * Runs removeDeadEndCorridor on every tile on the map.
     */
    private void removeDeadEnds(Terrain[][] t) {
        for (int i = 0; i < this.maxW; i++) {
            for (int j = 0; j < this.maxH; j++) {
                boolean de = deadEnd(new Location(i, j), t) != Direction.NONE;
                this.removeDeadEndCorridor(new Location(i, j), t);
                if (de) {
                    m.recordHistory();
                }
            }
        }
    }

    /**
     * Checks if tile is a dead end corridor and recursively removes that corridor until the head no longer is a dead end (ie room or intersection).
     */
    private void removeDeadEndCorridor(Location l, Terrain[][] t) {
        Direction d = deadEnd(l, t);
        if (d != Direction.NONE) {
            t[l.getX()][l.getY()] = Terrain.WALL;
            l = l.locInDir(d);
            this.removeDeadEndCorridor(l, t);
        }
    }

    /**
     * If tile is a dead end corridor (surrounded by 3 walls and 1 floor/corridor), returns the direction of the 1 floor/corridor. Otherwise returns DIrection.NONE.
     */
    private Direction deadEnd(Location l, Terrain[][] t) {
        if (t[l.getX()][l.getY()] != Terrain.CORRIDOR) {
            return Direction.NONE;
        }
        int count = 0;
        Direction d = Direction.NONE;

        Direction turn = Direction.DOWN;

        for (int k = 0; k < 4; k++) {
            if (this.notOutOfBoundsAndNotWall(l.locInDir(turn), t)) {
                count++;
                d = turn;
            }
            turn = turn.getClockwiseTurn();
        }

        if (count == 1) {
            return d;
        }
        return Direction.NONE;

    }

}
