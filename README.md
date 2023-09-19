# Ant Path Finding using A* Algorithm

[![Hits](https://hits.sh/github.com/vmsaif/ant-path-finding-using-A-Star-algorithm.svg?label=Visits&color=100b75)](https://hits.sh/github.com/vmsaif/ant-path-finding-using-A-Star-algorithm/)

## Logic and Design of Program

The program uses the A* algorithm to find the shortest path from the ant to the food. The ant is the start and the food is the goal. The ant can move in 8 directions. 

When the game starts, the user is asked to select 

-the start and the goal cells
-the obstacle cells
-the nature of the terrain for each cell between
    - Open Terrain	
    - Grassland
    - Swampland
    - Obstacles

After the user has selected the cells, the program calculates the shortest path from the ant to the food. The program uses the A* algorithm to find the shortest path. The A* algorithm uses a heuristic function to find the shortest path. The heuristic function used in this program is the Manhattan distance. The program shows the search evaluation of the A* algorithm. When the path is found, the ant starts moving from the start cell to the goal cell.

## Compiling and Running

### Compiling
The game uses a simple state machine to manage the different states of the game. 

- Run the command `javac -d bin src/*.java` to compile the Java files in the `src` directory to `bin/` directory.
- Run the command `java -cp bin/ App` to run the main game.

## Resources

### Images
- [App Icon](https://www.flaticon.com/free-icon/ant_1779584)
- [Ant Image](https://www.pngegg.com/en/png-zblks)
- [Food Image](https://www.pngegg.com/en/png-medpx)

### Bugs:
I have not found any bugs in the program yet. If you find any, please let me know.



