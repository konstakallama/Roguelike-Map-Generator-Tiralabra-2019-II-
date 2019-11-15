/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mapgenerator.mapgenerator;

import java.util.Random;

/**
 *
 * @author konstakallama
 */
public class MapGenerator2 {

    Random r = new Random();
    int maxW;
    int maxH;

    public Terrain[][] createMap(int maxW, int maxH) {
        this.maxW = maxW;
        this.maxH = maxH;
        Terrain[][] t = initT(maxW, maxH);
        int w = 3 + r.nextInt(maxW / 5);
        int h = 3 + r.nextInt(maxH / 5);
        Room r0 = new Room(new Location(maxW / 2, maxH / 2), w, h);
        paintRoom(t, r0.getLocation(), r0.getW(), r0.getH());

        for (int i = 0; i < 1000; i++) {
            LocDir ld = selectRandomWall(t);

            if (ld == null) {
                continue;
            }

            double rn = r.nextDouble();

            if (rn < 0.7) {
                tryRoom(ld.getL(), ld.getD(), t);
            } else {
                //tryCorridor(ld.getL(), ld.getD(), t);
            }
        }

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

    private LocDir selectRandomWall(Terrain[][] t) {
        for (int i = 0; i < 1000; i++) {
            Location l = new Location(r.nextInt(maxW), r.nextInt(maxH));
            Direction d = wallNextToFloor(l, t);
            if (d != Direction.NONE) {
                return new LocDir(l, d);
            }
        }
        return null;
    }

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

    private void tryRoom(Location l, Direction d, Terrain[][] t) {
        int w = 3 + r.nextInt(maxW / 5);
        int h = 3 + r.nextInt(maxH / 5);

        if (validRoomLocation(l, d, t, w, h)) {
            t[l.getX()][l.getY()] = Terrain.CORRIDOR;
            paintRoom(t, getRoomNw(l, d, w, h), w, h);
        }
    }

    private void tryCorridor(Location l, Direction d, Terrain[][] t) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    private boolean validRoomLocation(Location l, Direction d, Terrain[][] t, int w, int h) {
        return this.checkRoomLocation(this.getRoomNw(l, d, w, h), this.getRoomSe(l, d, w, h), t);
    }

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

    private boolean checkRoomLocation(Location nw, Location se, Terrain[][] t) {
        for (int i = nw.getX() - 1; i < se.getX() + 1; i++) {
            for (int j = nw.getY() - 1; j < se.getY() + 1; j++) {
                if (!outOfBounds(new Location(i, j))) {
                    if (t[i][j] != Terrain.WALL) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

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

}
