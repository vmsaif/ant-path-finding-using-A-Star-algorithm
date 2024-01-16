/* -----------------------------------------------------------------------------
    Author: Saif Mahmud
    Date: 2023-22-07
*/

import java.awt.Color;
import java.awt.Graphics;

public class Tile implements Comparable<Tile> {
    public static final double COST_OPEN_TERRAIN = 1;
    public static final double COST_GRASSLAND = 3;
    public static final double COST_SWAMPLAND = 4;
    public static final double COST_OBSTACLE = Integer.MAX_VALUE;
    private static int xOffset;
    private static int yOffset;

    private int x;
    private int y;

    private double cost;
    private double f; // total cost
    private double g; // cost so far
    private double h; // heuristic
    private boolean isObstacle;
    private boolean isStart;
    private boolean isGoal;
    private boolean isOpenTerrain;
    private boolean isGrassland;
    private boolean isSwampland;
    private Tile cameFrom;
    private boolean removeAntImg;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;

        this.cost = COST_OPEN_TERRAIN;
        isOpenTerrain = true;
        this.cameFrom = null;
    }

    public void draw(Graphics g, Color openTerrainColor, Color openTerrainBoarderColor) {

        g.setColor(openTerrainColor);
        g.fillRect(getXpixel(), getYpixel(), Game.getTilesize(), Game.getTilesize()); // draw the tile background

        if (isObstacle) {
            drawObstacleImage(g);
        } else if (isStart) {
            if (removeAntImg) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(getXpixel(), getYpixel(), Game.getTilesize(), Game.getTilesize());
            } else {
                drawAntImg(g);
            }
        } else if (isGoal) {
            drawFoodImg(g);
        } else if (isGrassland) {
            drawGrassland(g);
        } else if (isSwampland) {
            drawSwampland(g);
        } else {
            // open terrain
            g.setColor(openTerrainColor);
            g.fillRect(getXpixel(), getYpixel(), Game.getTilesize(), Game.getTilesize());
        }

        g.setColor(openTerrainBoarderColor);
        g.drawRect(getXpixel(), getYpixel(), Game.getTilesize(), Game.getTilesize());

    }

    private void drawObstacleImage(Graphics g) {
        if (Game.getObstacleImg() != null) {
            g.drawImage(Game.getObstacleImg(), getXpixel(), getYpixel(), Game.getTilesize(), Game.getTilesize(), null);
        } else {
            System.out.println("Obstacle image is null");
        }
    }

    private void drawSwampland(Graphics g) {
        if (Game.getSwamplandImg() != null) {
            g.drawImage(Game.getSwamplandImg(), getXpixel(), getYpixel(), Game.getTilesize(), Game.getTilesize(), null);
        } else {
            System.out.println("Swampland image is null");
        }
    }

    private void drawGrassland(Graphics g) {
        if (Game.getGrasslandImg() != null) {
            g.drawImage(Game.getGrasslandImg(), getXpixel(), getYpixel(), Game.getTilesize(), Game.getTilesize(), null);
        } else {
            System.out.println("Grassland image is null");
        }
    }

    private void drawFoodImg(Graphics g) {
        if (Game.getFoodImg() != null) {
            g.drawImage(Game.getFoodImg(), getXpixel(), getYpixel(), Game.getTilesize(), Game.getTilesize(), null);
        } else {
            System.out.println("Food image is null");
        }
    }

    private void drawAntImg(Graphics g) {
        if (Game.getAntImg() != null) {
            g.drawImage(Game.getAntImg(), getXpixel(), getYpixel(), Game.getTilesize(), Game.getTilesize(), null);
        } else {
            System.out.println("Ant image is null");
        }
    }

    public void removeAntImage(Boolean val) {
        removeAntImg = true;
    }

    public static void setxOffset(int offset) {
        xOffset = offset;
    }

    public static void setyOffset(int offset) {
        yOffset = offset;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getXpixel() {
        return (x * Game.getTilesize()) + xOffset;
    }

    public int getYpixel() {
        return (y * Game.getTilesize()) + yOffset;
    }

    public double getCost() {
        if (isObstacle)
            cost = COST_OBSTACLE;
        else if (isGrassland)
            cost = COST_GRASSLAND;
        else if (isSwampland)
            cost = COST_SWAMPLAND;
        else if (isOpenTerrain)
            cost = COST_OPEN_TERRAIN;
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void resetStart() {
        isStart = false;
    }

    public void resetGoal() {
        isGoal = false;
    }

    public boolean isStart() {
        return isStart;
    }

    public void setStart() {
        this.isStart = true;
    }

    public boolean isGoal() {
        return isGoal;
    }

    public void setGoal() {
        this.isGoal = true;
    }

    public boolean isOpenTerrain() {
        return isOpenTerrain;
    }

    public void setOpenTerrain() {
        cost = COST_OPEN_TERRAIN;
        this.isOpenTerrain = true;
        this.isGrassland = false;
        this.isSwampland = false;
        this.isObstacle = false;
    }

    public boolean isGrassland() {
        return isGrassland;
    }

    public void setGrassland() {
        cost = COST_GRASSLAND;
        this.isGrassland = true;
        this.isObstacle = false;
        this.isOpenTerrain = false;
        this.isSwampland = false;
    }

    public boolean isSwampland() {
        return isSwampland;
    }

    public void setSwampland() {
        cost = COST_SWAMPLAND;
        this.isSwampland = true;
        this.isObstacle = false;
        this.isOpenTerrain = false;
        this.isGrassland = false;
    }

    public boolean isObstacle() {
        return isObstacle;
    }

    public void setObstacle() {
        cost = COST_OBSTACLE;
        this.isObstacle = true;
        this.isOpenTerrain = false;
        this.isGrassland = false;
        this.isSwampland = false;
    }

    public double getF() {
        return f;
    }

    public void setF(double f) {
        this.f = f;
    }

    // g is cost so far
    public double getG() {
        return g;
    }

    public void setG(double g) {
        this.g = g;
    }

    // h is heuristic
    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public void setCameFrom(Tile current) {
        cameFrom = current;
    }

    public void resetCameFrom() {
        cameFrom = null;
    }

    public Tile getCameFrom() {
        return cameFrom;
    }

    @Override
    public int compareTo(Tile other) {
        int result = Double.compare(this.f, other.f);
        // if f is the same, compare h
        if (result == 0) {
            result = Double.compare(this.h, other.h);

            if (result == 0) {
                result = Double.compare(this.g, other.g);
            }
        }
        return result;
    }

    public Boolean isWithinTile(int mouseX, int mouseY) {
        int pixelXmin = getXpixel();
        int pixelYmin = getYpixel();

        int pixelXmax = pixelXmin + Game.getTilesize();
        int pixelYmax = pixelYmin + Game.getTilesize();

        return (mouseX >= pixelXmin && mouseX <= pixelXmax && mouseY >= pixelYmin && mouseY <= pixelYmax);
    }

    // for debugging
    // to string method
    public String toString() {
        return "Tile: " + x + ", " + y + " Cost: " + cost;
    }

    public static int getxOffset() {
        return xOffset;
    }

    public static int getyOffset() {
        return yOffset;
    }
}
