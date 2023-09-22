/* -----------------------------------------------------------------------------
    Author: Saif Mahmud
    Date: 2023-22-07
    
    Description: 
    
    The objective of this program is to implement a game with a tiled search area that has obstacles. 
    The search area is a 16 × 16 grid with some obstacles, a start point and a goal point. 
    The robot character (an ant) should find its way from the start point (home) to the goal point (food) with minimal costs. 
    The search area has different types of terrains with different cost values, as specified in the following table:

        Open Terrain Cost = 1
        Grassland Cost = 3
        Swampland Cost = 4
        Obstacles Not applicable
        The program allows the user to specify the grid configuration at the beginning of each run. The user will have to specify the following elements:

        the start and the goal cells
        the obstacle cells
        the nature of the terrain for each cell
        The program also allows the user to see visually the path search evolution from the start point to the goal point.
*/

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

public class Game extends JPanel implements MouseListener {

    private final int TILE_SIZE = 40;
    private final int NUM_ROWS = 16;
    private final int NUM_COLS = 16;

    private Ant ant;
    private Tile[][] tiles;
    private LinkedList<Tile> tobeDrawn;
    private boolean startMovingAnt;

    private Tile startTile;
    private Tile goalTile;

    private JPanel buttonPanel;
    protected boolean obstacleSelectionMode;
    protected boolean startLocationSelectionMode;
    private boolean goalLocationSelectionMode;
    private boolean openTerrainSelectionMode;
    private boolean swamplandSelectionMode;
    private boolean grasslandSelectionMode;
    private Image foodImg;
    private int buttonCount;
    private boolean noPath;

    public Game() {

        buttonCount = 8; // number of buttons
        // Set the layout manager to a BorderLayout
        setLayout(new BorderLayout());

        // Create a panel to hold the buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(buttonCount, 1, 10, 10));

        createTiles();
        addMouseListener(this);

        // Create the buttons

        selectStartLocation();
        selectGoalLocation();
        selectObstacle();
        selectGrassland();
        selectSwampland();
        selectOpenTerrain();
        JButton searchButton = searchButton(); // returning the button to enable the button after search
        resetButton(searchButton); // it will enable the search button after reseting the game


        // load food image
        loadFoodImg();

        // Add the button panel
        add(buttonPanel, BorderLayout.EAST);
        setPreferredSize(new Dimension(getPreferredSize().width + 200, getPreferredSize().height));

        startTile = null;
        goalTile = null;
        startMovingAnt = false;
        noPath = false;
        tobeDrawn = new LinkedList<Tile>();
    }

    private void resetButton(JButton searchButton) {
        JButton result = new JButton("Reset");
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTile = null;
                goalTile = null;
                ant = null;
                noPath = false;
                startMovingAnt = false;
                tobeDrawn = new LinkedList<Tile>();
                searchButton.setEnabled(true);
                createTiles();
                repaint();
            }
        });
        buttonPanel.add(result);
    }

    private void loadFoodImg() {
        try {
            foodImg = ImageIO.read(getClass().getResource("images/food.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // the selection mode of available buttons
    private void selectOpenTerrain() {
        // Create the reset button
        JButton result = new JButton("<html>Select Open Terrain<br/><center><font size='2'>Cost: 0</font></center></html>");
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openTerrainSelectionMode = true;
                startLocationSelectionMode = false;
                goalLocationSelectionMode = false;
                obstacleSelectionMode = false;
                swamplandSelectionMode = false;
                grasslandSelectionMode = false;
            }
        });
        buttonPanel.add(result);
    }

    private void selectSwampland() {
        JButton result = new JButton("<html>Select Swampland<br/><center><font size='2'>Cost: 4</font></center></html>");
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                swamplandSelectionMode = true;
                startLocationSelectionMode = false;
                goalLocationSelectionMode = false;
                obstacleSelectionMode = false;
                openTerrainSelectionMode = false;
                grasslandSelectionMode = false;
            }
        });
        buttonPanel.add(result);
    }

    private void selectGrassland() {
        JButton result = new JButton("<html>Select Grassland<br/><center><font size='2'>Cost: 3</font></center></html>");
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grasslandSelectionMode = true;
                startLocationSelectionMode = false;
                goalLocationSelectionMode = false;
                obstacleSelectionMode = false;
                openTerrainSelectionMode = false;
                swamplandSelectionMode = false;

            }
        });
        buttonPanel.add(result);
    }

    // search button to start the search algorithm
    private JButton searchButton() {
        JButton result = new JButton("A* Search");
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                grasslandSelectionMode = false;
                startLocationSelectionMode = false;
                goalLocationSelectionMode = false;
                obstacleSelectionMode = false;
                openTerrainSelectionMode = false;
                swamplandSelectionMode = false;
                if (startTile == null) {
                    System.out.println("Please select start location");
                }
                if (goalTile == null) {
                    System.out.println("Please select goal location");
                }

                if (startTile != null && goalTile != null) {
                    ant = new Ant(startTile, goalTile, TILE_SIZE, tiles);
                    ant.search();
                    result.setEnabled(false); // Disable the button
                    delayPaint();
                }
                

            }
        });
        buttonPanel.add(result);
        return result;
    }

    private void selectGoalLocation() {
        JButton result = new JButton("Select Goal Location");
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goalLocationSelectionMode = true;
                startLocationSelectionMode = false;
                obstacleSelectionMode = false;
                openTerrainSelectionMode = false;
                swamplandSelectionMode = false;
                grasslandSelectionMode = false;
            }
        });
        buttonPanel.add(result);
    }

    public void selectStartLocation() {
        JButton result = new JButton("Select Start Location");
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startLocationSelectionMode = true;
                goalLocationSelectionMode = false;
                obstacleSelectionMode = false;
                openTerrainSelectionMode = false;
                swamplandSelectionMode = false;
                grasslandSelectionMode = false;
            }
        });
        buttonPanel.add(result);
    }

    public void selectObstacle() {
        JButton result = new JButton("<html>Select Obstacle<br/><center><font size='2'>Cost: Impossible</font></center></html>");
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                obstacleSelectionMode = true;
                startLocationSelectionMode = false;
                goalLocationSelectionMode = false;
                openTerrainSelectionMode = false;
                swamplandSelectionMode = false;
                grasslandSelectionMode = false;
            }
        });
        buttonPanel.add(result);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Handle mouse clicks on the game panel
        int row = e.getX();
        int col = e.getY();

        if (row >= 0 && row < NUM_ROWS * TILE_SIZE && col >= 0 && col < NUM_COLS * TILE_SIZE) {

            Tile clickedTile = fetchTile(row, col);

            if (clickedTile != null) {
                if (startLocationSelectionMode) {
                    handleStartLocationSelection(clickedTile);
                } else if (goalLocationSelectionMode) {
                    handleGoalLocationSelection(clickedTile);
                } else if (obstacleSelectionMode) {
                    clickedTile.setObstacle();
                } else if (openTerrainSelectionMode) {
                    clickedTile.setOpenTerrain();
                } else if (swamplandSelectionMode) {
                    clickedTile.setSwampland();
                } else if (grasslandSelectionMode) {
                    clickedTile.setGrassland();
                }
                repaint();
            } // else the user clicked outside the game board, so do nothing
        } // else the user clicked outside the canvas, so do nothing
    }

    private void handleGoalLocationSelection(Tile clickedTile) {
        goalLocationSelectionMode = false;

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (tiles[i][j].isGoal()) {
                    tiles[i][j].resetGoal();
                }
            }
        }
        // Set the clicked tile as the start location
        clickedTile.setGoal();
        goalTile = clickedTile;

    }

    private void handleStartLocationSelection(Tile clickedTile) {
        startLocationSelectionMode = false;

        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                if (tiles[i][j].isStart()) {
                    tiles[i][j].resetStart();
                }
            }
        }
        // Set the clicked tile as the start location
        clickedTile.setStart();
        startTile = clickedTile;
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    private void createTiles() {
        tiles = new Tile[NUM_ROWS][NUM_COLS];
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                // all open terrain by default.
                tiles[i][j] = new Tile(i, j);
            }
        }
    }

    private Tile fetchTile(int mouseX, int mouseY) {
        Tile result = null;
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                Tile tile = tiles[i][j];
                if (tile.getTile(mouseX, mouseY, TILE_SIZE)) {
                    result = tile;
                }
            }
        }
        return result;
    }

    // delayed timer to animate the ant and making it smoother movement.
    public void delayPaint() {
        int delay = 50; 
        Timer timer = new Timer(delay, new ActionListener() {
            LinkedList<LinkedList<Tile>> allPath2D = ant.getAllPath2D();
            Tile subNodeToBeDrawn = null;
            Tile mainNode;

            boolean addMainNode = true;

            @Override
            public void actionPerformed(ActionEvent e) {

                if (allPath2D.isEmpty()) {
                    startMovingAnt = true;
                    tobeDrawn = ant.getPath();
                    ((Timer) e.getSource()).stop();
                    animateAnt();
                } else {

                    // first remove all subnodes/all neighbor nodes from the main drawing array.
                    if (subNodeToBeDrawn != null) {
                        tobeDrawn.remove(subNodeToBeDrawn);
                        subNodeToBeDrawn = null;
                    }

                    // add main node which will stay there parmanently to be drawn.
                    LinkedList<Tile> oneIterationArray = allPath2D.get(0);
                    if (oneIterationArray.size() > 0 && addMainNode) {
                        mainNode = oneIterationArray.get(0);
                        tobeDrawn.add(mainNode);
                        addMainNode = false;

                        // remove main node from the allnodes array
                        oneIterationArray.remove(mainNode);
                    }

                    // split subnodes
                    if (oneIterationArray.size() > 1) {
                        subNodeToBeDrawn = oneIterationArray.get(0);
                        if (!tobeDrawn.contains(subNodeToBeDrawn)) { // checking if subnode is already in the list
                            tobeDrawn.add(subNodeToBeDrawn);
                            oneIterationArray.remove(subNodeToBeDrawn);
                        }

                    }

                    // if last subnode is left, remove it anlong with the main row.
                    if (oneIterationArray.size() == 1) {
                        allPath2D.remove(0);
                        addMainNode = true;
                    } else if (oneIterationArray.size() == 0 && allPath2D.size() == 1) {
                        allPath2D.remove(0);
                    }
                    if (oneIterationArray.size() == 0 && allPath2D.size() > 0) {
                        allPath2D.remove(0); // Remove the empty list from allPath2D
                    }
                }
                repaint();
            } // end of action performed
        });
        timer.start();
    }

    // animate the ant from start to goal
    public void animateAnt() {
        if (startMovingAnt) {
            double speed = 3.0;
            int delay = 10; // 0.1 second delay
            LinkedList<Tile> path = ant.getPath();

            if(path.size() == 0){
               // draw no path found on the screen
                noPath = true;
                
            } else {
                // set the ant to the start location
                ant.setX(path.get(path.size() - 1).getX() * TILE_SIZE);
                ant.setY(path.get(path.size() - 1).getY() * TILE_SIZE);

                Timer timer = new Timer(delay, new ActionListener() {
                    int i;

                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (!path.isEmpty()) {

                            i = path.size() - 1;

                            Tile nextTile = path.get(i);

                            double dx = nextTile.getX() * TILE_SIZE - ant.getX();
                            double dy = nextTile.getY() * TILE_SIZE - ant.getY();

                            // angle from current tile to next tile
                            double angle = Math.atan2(dy, dx);
                            double distance = Math.sqrt((dx * dx) + (dy * dy));

                            double newAntX = ant.getX() + speed * Math.cos(angle);
                            double newAntY = ant.getY() + speed * Math.sin(angle);

                            // if the ant is close to the next tile, remove the tile from the path
                            if (distance < speed) {
                                path.remove(i);
                            } else {
                                // move the ant
                                ant.setX((int) newAntX);
                                ant.setY((int) newAntY);
                            }

                            repaint();
                        } else {
                            ((Timer) e.getSource()).stop();
                        }

                    }
                });
                timer.start();
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // draw tiles
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                tiles[i][j].draw(g, TILE_SIZE);
            }
        }

        // draw food
        if (goalTile != null && goalTile.isGoal()) {
            g.drawImage(foodImg, goalTile.getX() * TILE_SIZE, goalTile.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE, null);
        }

        // draw ant
        if (ant != null) {
            ant.draw(g, TILE_SIZE);
        }

        

        // draw path
        drawPath(g, TILE_SIZE, tobeDrawn);

        if(noPath){
            // write no path found on the screen at the center with increased font size
            g.setFont(g.getFont().deriveFont(50f));
            g.setColor(Color.RED);
            g.drawString("No Path Found", 300, 300);
        }

    } // end paintComponent

    public void drawPath(Graphics g, int tileSize, LinkedList<Tile> LinkedList) {

        if (LinkedList != null && LinkedList.size() > 1) {
            g.setColor(Color.RED);

            for (int i = 0; i < LinkedList.size() - 1; i++) {

                Tile current = LinkedList.get(i);

                Tile next = LinkedList.get(i + 1);

                int x1 = (int) current.getX() * tileSize + tileSize / 2;
                int y1 = (int) current.getY() * tileSize + tileSize / 2;
                int x2 = (int) next.getX() * tileSize + tileSize / 2;
                int y2 = (int) next.getY() * tileSize + tileSize / 2;

                g.drawLine(x1, y1, x2, y2);
                // repaint();
            }
        }
    }

    public int getTileSize() {
        return TILE_SIZE;
    }

}// end class
