/* -----------------------------------------------------------------------------
    Author: Saif Mahmud
    Date: 2023-22-07
*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;

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
    private static Color boardDefaultColor = Color.WHITE;
    private Image antImage;
    private Image foodImg;
    private Image grasslandImg;
    private Image swamplandImg;
    private Image obstacleImg;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        
        this.cost = COST_OPEN_TERRAIN;
        isOpenTerrain = true;
        this.cameFrom = null;
    }

    public void draw(Graphics g) {
        if (isObstacle) {
            drawObstacleImage(g);
        } else if (isStart) {
            if (removeAntImg) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize());
            } else {
                drawAntImage(g);
            }
        } else if (isGoal) {
            drawFoodImage(g);
        } else if (isGrassland) {
            drawGrassland(g);
        } else if (isSwampland) {
            drawSwampland(g);
        } else {
            // open terrain
            g.setColor(boardDefaultColor);
            g.fillRect(getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize());
        }

        // if((!isStart || removeAntImg) && !isGoal){
            
        // }
        
        g.setColor(Color.BLACK);
        g.drawRect(getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize());

    }

    private void drawObstacleImage(Graphics g) {
        try {
            if(obstacleImg == null){
                obstacleImg = ImageIO.read(getClass().getResource("/assets/images/obstacle.png"));
            }
            g.setColor(boardDefaultColor);
            g.fillRect(getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize());
            g.drawImage(obstacleImg, getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawSwampland(Graphics g) {
        try {
            if(swamplandImg == null){
                swamplandImg = ImageIO.read(getClass().getResource("/assets/images/swampland.png"));
            }
            g.setColor(boardDefaultColor);
            g.fillRect(getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize());
            g.drawImage(swamplandImg, getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawGrassland(Graphics g) {
        try {
            if(grasslandImg == null){
                grasslandImg = ImageIO.read(getClass().getResource("/assets/images/grassland.png"));
            }
            g.setColor(boardDefaultColor);
            g.fillRect(getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize());
            g.drawImage(grasslandImg, getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void drawFoodImage(Graphics g) {
        try {
            if(foodImg == null){
                foodImg = ImageIO.read(getClass().getResource("/assets/images/food.png"));
            }
            g.setColor(boardDefaultColor);
            g.fillRect(getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize());
            g.drawImage(foodImg, getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize(), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawAntImage(Graphics g) {
        try {
            if(antImage == null){
                antImage = ImageIO.read(getClass().getResource("/assets/images/ant.png"));
            }
            g.setColor(boardDefaultColor);
            g.fillRect(getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize());
            g.drawImage(antImage, getXpixel(), getYpixel(), Game.getTileSize(), Game.getTileSize(), null);
        } catch (IOException e) {
            e.printStackTrace();
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
        return (x * Game.getTileSize()) + xOffset;
    }

    public int getYpixel() {
        return (y * Game.getTileSize()) + yOffset;
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

        int pixelXmax = pixelXmin + Game.getTileSize();
        int pixelYmax = pixelYmin + Game.getTileSize();

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
