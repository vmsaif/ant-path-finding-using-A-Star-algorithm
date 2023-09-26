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
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.Timer;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Game extends JPanel implements MouseListener, MouseMotionListener {

    private JFrame frame;
    private final static int TILE_SIZE = 40;
    private final int NUM_ROWS = 16;
    private final int NUM_COLS = 16;
    private int obstacleCount;
    private int swamplandCount;
    private int grasslandCount;
    private int openTerrainCount;

    private Ant ant;
    private Tile[][] tiles;
    private LinkedList<Tile> tobeDrawn;
    private boolean startClicked;
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

    private int buttonCount;
    private boolean noPath;
    private Tile lastDraggedTile = null;

    private long startTimeBeforeSearch;
    private long elapsedTimeAfterSearch;
    private String elapsedTimeStringBeforeSearch;
    
    private long startTimeBeforeAnimation;
    private long elapsedTimeAfterAnimation;
    private String elapsedTimeStringAfterAnimation;
    private boolean antReached;
    protected boolean startTimer;

    // colors
    private Color bgColor;
    private Color openTerrainColor;
    private Color footerColor;
    private Color timerFontColor;
    private Color openTerrainBoarderColor;
    private Color counterDigitColor;
    private Font timerFont;
    private Font buttonFont;

    
    // images 
    private static Image antImage;
    private static Image foodImg;
    private static Image grasslandImg;
    private static Image swamplandImg;
    private static Image obstacleImg;

    public Game(JFrame frame) {

        // change the color if needed

        bgColor = new Color(234, 242, 255, (int) (0.7 * 255)); // 0.7 is the opacity
        openTerrainColor = new Color(255, 255, 255, (int) (0.5 * 255));
        openTerrainBoarderColor = new Color(0, 0, 0, (int) (0.5 * 255));

        counterDigitColor = new Color(255, 0, 0, (int) (1.0 * 255));

        footerColor = new Color(0, 0, 255, (int) (0.1 * 255)); 
        timerFontColor = new Color(0, 0, 0, (int) (1.0 * 255)); 

        timerFont = new Font("Arial", Font.BOLD, 18);
        buttonFont = new Font("Arial", Font.PLAIN, 12);
        
        // -------------------------

        this.frame = frame;
        buttonCount = 8; // number of buttons
        // Set the layout manager to a BorderLayout
        setLayout(new BorderLayout());

        // Create a panel to hold the buttons
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, buttonCount, 10, 10));

        //load all images
        loadImg();

        // create board
        createTiles();
        addMouseListener(this);
        addMouseMotionListener(this);

        // Create the buttons

        selectStartLocation();
        selectGoalLocation();
        selectObstacle();
        selectGrassland();
        selectSwampland();
        selectOpenTerrain();
        searchButton(); // returning the button to enable the button after search
        resetButton(); // it will enable the search button after reseting the game

        // Add the button panel
        add(buttonPanel, BorderLayout.NORTH);
        setPreferredSize(new Dimension(getPreferredSize().width + 200, getPreferredSize().height));

        
    
        tobeDrawn = new LinkedList<Tile>();

        // set timer
        elapsedTimeStringBeforeSearch = "0:00:000";
        elapsedTimeStringAfterAnimation = "0:00:000";
        
        setTimerMageCreation();
        setTimerSolving();

        // resizeble windows
        resizeWindow();
        
    }

    private void resizeWindow() {
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {

                // the top bar location

                // set the grid offset/start point
                int xOffset = (int) ((frame.getWidth() - TILE_SIZE * NUM_COLS) / 2);
                int yOffset = (int) ((frame.getHeight() - TILE_SIZE * NUM_ROWS) / 1.8);

                Tile.setxOffset(xOffset);
                Tile.setyOffset(yOffset);

                repaint();
            }
        });
    }

    private void loadImg() {
        try {
            obstacleImg = ImageIO.read(getClass().getResource("/images/obstacle.png"));
            swamplandImg = ImageIO.read(getClass().getResource("/images/swampland.png"));
            grasslandImg = ImageIO.read(getClass().getResource("/images/grassland.png"));
            foodImg = ImageIO.read(getClass().getResource("/images/food.png"));
            antImage = ImageIO.read(getClass().getResource("/images/ant.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void resetButton() {
        JButton result = new JButton("Reset");
        result.setFont(buttonFont);
        result.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startTimer = false;
                startTile = null;
                goalTile = null;
                ant = null;
                noPath = false;
                startMovingAnt = false;
                tobeDrawn = new LinkedList<Tile>();
                obstacleCount = 0;
                swamplandCount = 0;
                grasslandCount = 0;
                openTerrainCount = 0;

                startClicked = false;
                startTimeBeforeSearch = System.currentTimeMillis();
                elapsedTimeAfterSearch = 0;
                elapsedTimeStringBeforeSearch = "0:00:000";
                
                startTimeBeforeAnimation = 0;
                elapsedTimeAfterAnimation = 0;
                elapsedTimeStringAfterAnimation = "0:00:000";
                antReached = false;
                createTiles();
                setTimerMageCreation();
                setTimerSolving();
                repaint();
            }
        });
        buttonPanel.add(result);
    }


    // the selection mode of available buttons
    private void selectOpenTerrain() {
        // Create the reset button
        JButton result = new JButton("<html>Open Terrain<br/><center><font size='2'>Cost: 1</font></center></html>");
        result.setFont(buttonFont);
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
        JButton result = new JButton("<html>Swampland<br/><center><font size='2'>Cost: 4</font></center></html>");
        result.setFont(buttonFont);
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
        JButton result = new JButton("<html>Grassland<br/><center><font size='2'>Cost: 3</font></center></html>");
        result.setFont(buttonFont);

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
        result.setFont(buttonFont);

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
                    resetCameFrom(tiles); // useful if the user wants to search for the second time or more
                    ant = new Ant(startTile, goalTile, TILE_SIZE, tiles);
                    startClicked = true;
                    startTimeBeforeAnimation = System.currentTimeMillis();
                    ant.search();
                    delayPaint();
                }
            }
        });
        buttonPanel.add(result);
        return result;
    }

    private void resetCameFrom(Tile[][] tiles) {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j].resetCameFrom();
            }
        }
    }

    private void selectGoalLocation() {
        JButton result = new JButton("Goal");
        result.setFont(buttonFont);
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
        JButton result = new JButton("Start");
        result.setFont(buttonFont);
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
        JButton result = new JButton("<html><center>Obstacle<br/><font size='2'>Cost: Impossible</font></center></html>");
        result.setFont(buttonFont);
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

    private void handleGoalLocationSelection(Tile clickedTile) {
        goalLocationSelectionMode = false;

        if(goalTile != null){
            goalTile.resetGoal();
        }
        // Set the clicked tile as the start location
        clickedTile.setGoal();
        goalTile = clickedTile;

    }

    private void handleStartLocationSelection(Tile clickedTile) {
        startLocationSelectionMode = false;

        if(startTile != null){
            startTile.resetStart();
        }

        startClicked = false;
        startMovingAnt = false;
        noPath = false;
        ant = null;
        tobeDrawn = new LinkedList<Tile>();

        obstacleCount = 0;
        swamplandCount = 0;
        grasslandCount = 0;
        openTerrainCount = 0;

        // Set the clicked tile as the start location
        clickedTile.setStart();
        startTile = clickedTile;
        repaint();
    }

    private void changeTileState(MouseEvent e) {
        int row = e.getX();
        int col = e.getY();
        
        Tile clickedTile = fetchTile(row, col);

        if (clickedTile != null && clickedTile != lastDraggedTile) {
            lastDraggedTile = clickedTile;

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
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        changeTileState(e);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        changeTileState(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        lastDraggedTile = null;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(!startTimer){
            startTimeBeforeSearch = System.currentTimeMillis();
            startTimer = true;
        }
    }    
    
    @Override
    public void mouseMoved(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

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
                if (tile.isWithinTile(mouseX, mouseY)) {
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
            
            int delay = 10; // 0.1 second delay
            LinkedList<Tile> path = ant.getPath();

            if(path.size() == 0){
               // draw no path found on the screen
                noPath = true;
                
            } else {
                // first remove the start tile static image
                path.get(path.size() - 1).removeAntImage(true);

                // set the ant to the start location
                ant.setX(path.get(path.size() - 1).getXpixel());
                ant.setY(path.get(path.size() - 1).getYpixel());

                // start the timer
                Timer timer = new Timer(delay, new ActionListener() {
                    int i;
                    double speed;
                    
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        if (!path.isEmpty()) {

                            i = path.size() - 1;

                            Tile nextTile = path.get(i);

                            speed = setSpeed(nextTile);
                            

                            double dx = nextTile.getXpixel() - ant.getX();
                            double dy = nextTile.getYpixel() - ant.getY();

                            // angle from current tile to next tile
                            double angle = Math.atan2(dy, dx);
                            double distance = Math.sqrt((dx * dx) + (dy * dy));

                            double newAntX = ant.getX() + speed * Math.cos(angle);
                            double newAntY = ant.getY() + speed * Math.sin(angle);

                            // if the ant is close to the next tile, remove the tile from the path
                            if (distance < speed) {
                                ant.setX(nextTile.getXpixel());
                                ant.setY(nextTile.getYpixel());
                                path.remove(i);

                                // if the ant reached the goal, stop the timer
                                if (path.isEmpty()) {
                                    antReached = true;
                                }
                            } else {
                                // move the ant
                                ant.setX((int) newAntX);
                                ant.setY((int) newAntY);
                            }

                            repaint();
                        } else {
                            ((Timer) e.getSource()).stop();
                            startMovingAnt = false;
                            antReached = true;
                        }
                    }

                    private double setSpeed(Tile nextTile) {
                        double output = Tile.COST_SWAMPLAND; // highest cost 4 for open terrain

                        if(nextTile.isSwampland()){
                            output = (output/Tile.COST_SWAMPLAND) + 1.5; // 1.25
                        } else if(nextTile.isGrassland()){
                            output = output*2/Tile.COST_GRASSLAND + 0.5; // 2.5
                        } else {
                            // open terrain 
                            output = output + 1;
                        }

                        return output;
                    }
                });
                timer.start();
            }
        }
    }
   
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set the background color using a hex value
        g.setColor(bgColor);  // This is for white color. Change the hex value for different colors.
        g.fillRect(0, 0, getWidth(), getHeight());  // Fill the entire component's area

        printBoardAndCount(g);

        // draw ant
        if (ant != null) {
            ant.draw(g, TILE_SIZE);
        }

        // draw path
        drawPath(g, TILE_SIZE, tobeDrawn);

        printIfNoPath(g);

        // Draw the elapsed time

        printTimer(g);

        drawTileCount(g);

    } // end paintComponent

    private void drawTileCount(Graphics g) {
        
        // Draw the icons with their counts
        try {
            int iconSize = 20; // Define the size of your icons
            int iconTextSpacing = 5; // Spacing between icon and text
            int wordSpacing = 45; // Spacing between each icon-count pair
            double iconScaling = 2.5;
            int totalIconWidth = 4 * iconSize + 3 * wordSpacing; // 4 icons
            int totalWidth = totalIconWidth + 3 * iconTextSpacing; // The space after each icon for text

            // Calculate the starting X-coordinate to make the icons and text centered
            int terrainCountX = (frame.getWidth() - totalWidth) / 2;

            int terrainCountY = (int) (frame.getHeight() / 15);

            g.setColor(counterDigitColor);

            g.drawImage(obstacleImg, terrainCountX, terrainCountY, iconSize, iconSize, null);
            g.drawString("" + obstacleCount, terrainCountX + iconSize + iconTextSpacing, terrainCountY + iconSize - 3);

            terrainCountX += iconSize + iconTextSpacing + wordSpacing;

            g.drawImage(grasslandImg, terrainCountX, terrainCountY, iconSize, iconSize, null);
            g.drawString("" + grasslandCount, terrainCountX + iconSize + iconTextSpacing, terrainCountY + iconSize - 3);

            terrainCountX += iconSize + iconTextSpacing + wordSpacing;

            g.drawImage(swamplandImg, terrainCountX, terrainCountY, iconSize, iconSize, null);
            g.drawString("" + swamplandCount, terrainCountX + iconSize + iconTextSpacing, terrainCountY + iconSize - 3);

            terrainCountX += iconSize + iconTextSpacing + wordSpacing;

            g.setColor(openTerrainColor);
            g.fillRect(terrainCountX, terrainCountY + 3, (int) (Game.getTileSize() / iconScaling), (int) (Game.getTileSize() / iconScaling));
            g.setColor(openTerrainBoarderColor);
            g.drawRect(terrainCountX, terrainCountY + 3, (int) (Game.getTileSize() / iconScaling), (int) (Game.getTileSize() / iconScaling));
            g.setColor(counterDigitColor);
            g.drawString("" + openTerrainCount, terrainCountX + iconSize + iconTextSpacing, terrainCountY + iconSize - 2);

        } catch (NullPointerException e) {
            System.out.println("Image not found");
        }
    }

    private void printTimer(Graphics g) {
        // draw a footer for the timer
        g.setColor(footerColor);
        g.fillRect(0, frame.getHeight() - 70, frame.getWidth(), 100);

        g.setColor(timerFontColor); // Sets the color to red.
        g.setFont(timerFont); 

        String timerText = "Creation Time: " + elapsedTimeStringBeforeSearch + "   |   " + "AI Solving Time: " + elapsedTimeStringAfterAnimation;
        int stringWidth = g.getFontMetrics().stringWidth(timerText);
        int centeredX = (frame.getWidth() - stringWidth) / 2;
        int centerY = frame.getHeight() - 47;
        g.drawString(timerText, centeredX, centerY);
    }

    private void printIfNoPath(Graphics g) {
        if(noPath){
            // write no path found on the screen at the center with increased font size
            Font font = g.getFont().deriveFont(50f);
            g.setFont(font);
            g.setColor(Color.RED);
            
            // Calculate the size of the string
            FontMetrics metrics = g.getFontMetrics(font);
            int stringWidth = metrics.stringWidth("No Path Found");
            int stringHeight = metrics.getAscent() - metrics.getDescent();
        
            // Calculate x and y for the string to be centered
            int x = (frame.getWidth() - stringWidth) / 2;
            int y = (frame.getHeight() - 75 + stringHeight) / 2;
        
            g.drawString("No Path Found", x, y);
        }
        
    }

    private void printBoardAndCount(Graphics g) {
        // draw tiles and add counter for each terrain

        int obstacle = 0;
        int swampland = 0;
        int grassland = 0;
        int openTerrain = 0;
        
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                tiles[i][j].draw(g, openTerrainColor, openTerrainBoarderColor);
                if(tiles[i][j].isObstacle()){
                    obstacle++;
                } else if(tiles[i][j].isSwampland()){
                    swampland++;
                } else if(tiles[i][j].isGrassland()){
                    grassland++;
                } else if(tiles[i][j].isOpenTerrain()){
                    openTerrain++;
                }
            }
        }

        if(startTile != null){
            openTerrain--;
        }

        if(goalTile != null){
            openTerrain--;
        }

        obstacleCount = obstacle;
        swamplandCount = swampland;
        grasslandCount = grassland;
        openTerrainCount = openTerrain;
    }

    private void setTimerMageCreation() {
        int delay = 50; 
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(startTimer){
                    elapsedTimeAfterSearch = System.currentTimeMillis() - startTimeBeforeSearch;
                    long minutes = (elapsedTimeAfterSearch / 1000) / 60;
                    long seconds = (elapsedTimeAfterSearch / 1000) % 60;
                    long milliseconds = elapsedTimeAfterSearch % 1000;

                    elapsedTimeStringBeforeSearch = String.format("%d:%02d:%03d", minutes, seconds, milliseconds);
                    repaint();

                    if(startClicked || noPath){
                        ((Timer) e.getSource()).stop();
                    }
                }
                
            }
        });
        timer.start();
    }

    private void setTimerSolving() {
        int delay = 50; 
        Timer timer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(startClicked){
                    elapsedTimeAfterAnimation = System.currentTimeMillis() - startTimeBeforeAnimation;
                    long minutes = (elapsedTimeAfterAnimation / 1000) / 60;
                    long seconds = (elapsedTimeAfterAnimation / 1000) % 60;
                    long milliseconds = elapsedTimeAfterAnimation % 1000;

                    elapsedTimeStringAfterAnimation = String.format("%d:%02d:%03d", minutes, seconds, milliseconds);
                    repaint();
                }
                
                if(antReached || noPath){
                    ((Timer) e.getSource()).stop();
                }
            }
        });
        timer.start();
    }

    public void drawPath(Graphics g, int tileSize, LinkedList<Tile> LinkedList) {

        if (LinkedList != null && LinkedList.size() > 1) {
            g.setColor(Color.RED);

            for (int i = 0; i < LinkedList.size() - 1; i++) {

                Tile current = LinkedList.get(i);

                Tile next = LinkedList.get(i + 1);
                
                int x1 = (int) current.getXpixel() + tileSize / 2;
                int y1 = (int) current.getYpixel() + tileSize / 2;
                int x2 = (int) next.getXpixel() + tileSize / 2;
                int y2 = (int) next.getYpixel() + tileSize / 2;

                g.drawLine(x1, y1, x2, y2);
                // repaint();
            }
        }
    }

    public static int getTileSize() {
        return TILE_SIZE;
    }

    public static Image getObstacleImg() {
        return obstacleImg;
    }

    public static Image getSwamplandImg() {
        return swamplandImg;
    }

    public static Image getGrasslandImg() {
        return grasslandImg;
    }

    public static Image getFoodImg() {
        return foodImg;
    }

    public static Image getAntImg() {
        return antImage;
    }



}// end class
