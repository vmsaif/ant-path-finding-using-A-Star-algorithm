/* -----------------------------------------------------------------------------
    Author: Saif Mahmud
    Date: 2023-22-07
    Course: COMP 452
    Student ID: 3433058
    Assignment: 2
    Part: 1
*/

import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Ant {

    private int antX;
    private int antY;
    private Tile start;
    private Tile goal;
    private Tile[][] tiles;
    private Image antImage;
    private ArrayList<Tile> path;
    private Graphics g;

    private AStarSearch aStarSearch;
    private ArrayList<ArrayList<Tile>> allPath2D = new ArrayList<ArrayList<Tile>>();

    public Ant(Tile start, Tile goal, int tileSize, Tile[][] tiles) {
        this.start = start;
        this.goal = goal;
        this.tiles = tiles;
        loadAntImg();
        antX = start.getX()*tileSize;
        antY = start.getY()*tileSize;
    }

    private void loadAntImg() {
        try {
            antImage = ImageIO.read(new File("ant.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics g, int tileSize) {
        g.drawImage(antImage, antX, antY, tileSize, tileSize, null);
    }
   
    public void search() {
        aStarSearch = new AStarSearch(tiles, this, start, goal);
        path = aStarSearch.search();
    }

    public ArrayList<Tile> getPath() {
        // find the path and store it in path arraylist
        
        if(path == null){
            this.search();
        }
        return path;
    }


    public void setAllPath2D(ArrayList<ArrayList<Tile>> allPath2D) {
        this.allPath2D = allPath2D;
    }

    public ArrayList<ArrayList<Tile>> getAllPath2D() {
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
