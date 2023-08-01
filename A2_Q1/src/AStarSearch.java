

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import java.util.PriorityQueue;

import javax.swing.JPanel;


public class AStarSearch extends JPanel {

    private Tile[][] tiles;
    private Tile start;
    private Tile goal;
    private Ant ant;
    private ArrayList<ArrayList<Tile>> allPath2D = new ArrayList<ArrayList<Tile>>();
    
    public AStarSearch(Tile[][] tiles, Ant ant, Tile start, Tile goal) {
        this.tiles = tiles;
        this.start = start;
        this.goal = goal;
        this.ant = ant;
    }
    
    public ArrayList<Tile> search() {

        ArrayList<Tile> output = new ArrayList<>();
        ArrayList<Tile> closedSet = new ArrayList<>();
        
        // create openSet and add start to it 
        PriorityQueue<Tile> openSet = new PriorityQueue<Tile>();

        // add start to openSet
        openSet.add(start);

        boolean foundGoal = false;
        boolean noPath = false;
        
      
        // while openSet is not empty 
        while(openSet.size() > 0 && foundGoal == false && noPath == false){
            
            // check if first element in openSet is goal
            Tile current = openSet.poll();
            // System.out.println("current: " + current);
            ArrayList<Tile> row = new ArrayList<>();
            allPath2D.add(row);
            row.add(current);
            // System.out.println();
            // System.out.println("Removing Main Grid ["+current.getX()+"]["+current.getY()+"] F:" + current.getF() + " G:" + current.getG() + " H:" + current.getH());
            
            if(current.equals(goal) && noPath == false){
                // reconstruct path
                output = reconstructPath(current);
                foundGoal = true;
                // ant.stopSearch();

            } else {
                // remove current from openSet and add to closedSet
                // openSet.remove(current);
                closedSet.add(current);

                // get neighbors of current/ outgoing connections
                PriorityQueue<Tile> neighbors = getNeighbors(current);

                // printQueue(neighbors);

                // for each neighbor of current, evaluate cost 
                while(neighbors.size() > 0){
                    Tile currNeighbor = neighbors.poll();
                    
                    // System.out.println("--Polled Grid ["+currNeighbor.getX()+"]["+currNeighbor.getY()+"] F:" + currNeighbor.getF() + " G:" + currNeighbor.getG() + " H:" + currNeighbor.getH());
                    
                    // if neighbor is not in closedSet and not obstacle, then evaluate cost
                    if(!closedSet.contains(currNeighbor) && !currNeighbor.isObstacle()){
                        row.add(currNeighbor);
                        double tempCost = current.getG() + currNeighbor.getCost();
                        
                        // System.out.println("tempCost:  " + tempCost + " gCost: " + current.getG() + " current: " + current.getCost());

                        boolean betterPath = false;

                        // check for a shorter route
                        if(!openSet.contains(currNeighbor)){
                            // if neighbor is not in openSet, add to openSet and add g cost
                            currNeighbor.setG(tempCost);
                            betterPath = true;
                            
                            // System.out.println("ADDING Grid ["+currNeighbor.getX()+"]["+currNeighbor.getY()+"] F:" + currNeighbor.getF() + " G:" + currNeighbor.getG() + " H:" + currNeighbor.getH());

                        } else { 
                            // openSet contains neighbor, check if shorter route
                            
                            if (tempCost < currNeighbor.getG()){
                                // found a shorter route, update g cost
                                openSet.remove(currNeighbor);
                                currNeighbor.setG(tempCost);
                                openSet.add(currNeighbor);
                                betterPath = true;
                                
                            }
                        }

                        // set neighbor's heuristic estimated cost to goal
                        if(betterPath){
                            currNeighbor.setH(getHeuristic(currNeighbor, goal));

                            // set neighbor's f
                            currNeighbor.setF(currNeighbor.getG() + currNeighbor.getH());

                            // record previous node
                            currNeighbor.setCameFrom(current);
                            
                        }
                        

                        // add neighbor to openSet if not already in it
                        if(!openSet.contains(currNeighbor)){
                            // System.out.println("ADDING Grid ["+currNeighbor.getX()+"]["+currNeighbor.getY()+"] F:" + currNeighbor.getF() + " G:" + currNeighbor.getG() + " H:" + currNeighbor.getH());
                            openSet.add(currNeighbor);
                            
                        }
                        

                        // System.out.println("--Polled Grid ["+currNeighbor.getX()+"]["+currNeighbor.getY()+"] F:" + currNeighbor.getF() + " G:" + currNeighbor.getG() + " H:" + currNeighbor.getH());

    
                        // update GUI after each iteration of the search algorithm

                    } // end if statement
                    // System.out.println(" Grid ["+currNeighbor.getX()+"]["+currNeighbor.getY()+"] F:" + currNeighbor.getF() + " G:" + currNeighbor.getG() + " H:" + currNeighbor.getH());

                } // end while loop

                

                
            } // end else(not goal)

            // if openSet is empty, then no path
            if(openSet.size() == 0){
                System.out.println("No path found");
                noPath = true;
            }
            
            
        
        } // end main while loop

        ant.setAllPath2D(allPath2D);
        // System.out.println("Grid [1][0] F:" + tiles[1][0].getF() + " G:" + tiles[1][0].getG() + " H:" + tiles[1][0].getH());
        // System.out.println("Grid [1][1] F:" + tiles[1][1].getF() + " G:" + tiles[1][1].getG() + " H:" + tiles[1][1].getH());    

        return output;
    }


    private double getHeuristic(Tile currNeighbor, Tile goal) {

        // manhattan distance between current neighbor and goal
        int dx = Math.abs(currNeighbor.getX() - goal.getX());
        int dy = Math.abs(currNeighbor.getY() - goal.getY());
        return dx + dy;
    }

    private PriorityQueue<Tile> getNeighbors(Tile tile) {

        PriorityQueue<Tile> neighbors = new PriorityQueue<>();

        // get the indexes of the tile from the neighbors 2d array
        
        int x = tile.getX();
        int y = tile.getY();

        // add neighbours including diagonals
        // top left
        if(x > 0 && y > 0){
            neighbors.add(tiles[x-1][y-1]);
        }
        // top
        if(y > 0){
            neighbors.add(tiles[x][y-1]);
        }
        // top right
        if(x < tiles.length-1 && y > 0){
            neighbors.add(tiles[x+1][y-1]);
        }
        // right
        if(x < tiles.length-1){
            neighbors.add(tiles[x+1][y]);
        }
        // bottom right
        if(x < tiles.length-1 && y < tiles[0].length-1){
            neighbors.add(tiles[x+1][y+1]);
        }
        // bottom
        if(y < tiles[0].length-1){
            neighbors.add(tiles[x][y+1]);
        }
        // bottom left
        if(x > 0 && y < tiles[0].length-1){
            neighbors.add(tiles[x-1][y+1]);
        }
        // left
        if(x > 0){
            neighbors.add(tiles[x-1][y]);
        }

        return neighbors;
    }
    
    public ArrayList<Tile> reconstructPath(Tile current) {
        ArrayList<Tile> path = new ArrayList<>();

        while (current != null) {
            path.add(current);

            // move to the previous node
            current = current.getCameFrom();
            // System.out.println("Path: (" + current.getX() + ", " + current.getY()+")");
        }

        return path;
    }
    
}