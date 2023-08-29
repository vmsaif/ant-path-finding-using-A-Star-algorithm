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


    private int x;
    private int y;
    private double cost;
    private double f; //total cost
    private double g; //cost so far
    private double h; //heuristic
    private boolean isObstacle;
    private boolean isStart;
    private boolean isGoal;
    private boolean isOpenTerrain;
    private boolean isGrassland;
    private boolean isSwampland;
    private Tile cameFrom;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
        this.cost = COST_OPEN_TERRAIN;
        isOpenTerrain = true;
        this.cameFrom = null;
    }

    public void draw(Graphics g, int tileSize) {
        if (isObstacle) {
            g.setColor(Color.BLACK);
        } else if (isStart) {
            g.setColor(Color.GREEN);
        } else if (isGoal) {
            g.setColor(Color.WHITE);
        } else if (isGrassland) {
            g.setColor(Color.YELLOW);
        } else if (isSwampland) {
            g.setColor(Color.BLUE);
        } else {
            // open terrain
            g.setColor(Color.WHITE);
        }
        g.fillRect((int)x * tileSize, (int)y * tileSize, tileSize, tileSize);
        g.setColor(Color.BLACK);
        g.drawRect((int)x * tileSize, (int)y * tileSize, tileSize, tileSize);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public double getCost() {
        if(isObstacle)
            cost = COST_OBSTACLE;
        else if(isGrassland)
            cost = COST_GRASSLAND;
        else if(isSwampland)
            cost =  COST_SWAMPLAND;
        else if(isOpenTerrain)
            cost =  COST_OPEN_TERRAIN;
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public void resetStart(){
        isStart = false;
    }

    public void resetGoal(){
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
        
            if(result == 0){
                result = Double.compare(this.g, other.g);
            }
        } 
        return result;
    }

    public Boolean getTile(int mouseX, int mouseY, int tileSize){
        int pixelXmin = x * tileSize;
        int pixelYmin = y * tileSize;

        int pixelXmax = pixelXmin + tileSize;
        int pixelYmax = pixelYmin + tileSize;

        
        Boolean result = false;
        if(mouseX >= pixelXmin && mouseX < pixelXmax && mouseY >= pixelYmin && mouseY < pixelYmax){
            result = true;
        }
        return result;
    }

    // for debugging
    //to string method
    public String toString() {
        return "Tile: " + x + ", " + y + " Cost: " + cost;
    }
}
