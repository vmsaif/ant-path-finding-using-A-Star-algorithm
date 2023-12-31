/* -----------------------------------------------------------------------------
    Author: Saif Mahmud
    Date: 2023-22-07
*/

import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;


public class Ant {

    private int antX;
    private int antY;
    private Tile start;
    private Tile goal;
    private Tile[][] tiles;
    private Image antImage;
    private LinkedList<Tile> path;

    private AStarSearch aStarSearch;
    private LinkedList<LinkedList<Tile>> allPath2D = new LinkedList<LinkedList<Tile>>();

    public Ant(Tile start, Tile goal, int tileSize, Tile[][] tiles, Image antImage) {
        this.start = start;
        this.goal = goal;
        this.tiles = tiles;
        this.antImage = antImage;
        antX = start.getXpixel();
        antY = start.getYpixel();
    }

    public void draw(Graphics g, int tileSize) {
        g.drawImage(antImage, antX, antY, tileSize, tileSize, null);
    }
   
    // search for the path from start to goal using A* search
    public void search() {
        aStarSearch = new AStarSearch(tiles, this, start, goal);
        path = aStarSearch.search();
    }

    public LinkedList<Tile> getPath() {
        
        // find the path and store it in path LinkedList
        if(path == null){
            this.search();
        }
        return path;
    }


    public void setAllPath2D(LinkedList<LinkedList<Tile>> allPath2D) {
        this.allPath2D = allPath2D;
    }

    public LinkedList<LinkedList<Tile>> getAllPath2D() {
        return allPath2D;
    }

    public int getX() {
        return antX;
    }

    public int getY() {
        return antY;
    }
    public void setX(int x) {
        antX = x;
    }

    public void setY(int y) {
        antY = y;
    }

}// end class Ant
