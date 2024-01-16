# Ant Path Finding using A* Algorithm

[![Hits](https://hits.sh/github.com/vmsaif/ant-path-finding-using-A-Star-algorithm.svg?label=Visits&color=100b75)](https://hits.sh/github.com/vmsaif/ant-path-finding-using-A-Star-algorithm/)

# About the Game

Dive into a world where you're not just a player, but a creator. In this interactive game, **you** design the maze, but here's the twist: Can you design a maze swiftly, using the least time possible, yet make our AI take the maximum time to find its way? The challenge? Make the AI ponder over your crafty creation. While our AI boasts of solving most mazes in a mere moment, we've added a graphic representation of the ant's journey to allow you to savor the AI's decision-making, one step at a time. The stakes? Your creativity versus AI's intelligence. Get your creator's cap, and let the games begin!

## Your Journey as a Creator

At the start of the game, you're presented with a blank canvas. Here, you get to wear the hat of a creator:

1. **Designate Start and Finish:** Decide where the ant starts its journey and where its food (goal) lies.
2. **Craft the Terrain:** Mold the landscape by placing obstacles, open terrains, grasslands, or swamplands. Each terrain type presents a unique challenge to the AI. Grasslands make the ant take longer to cross than open terrains, and swamplands prolong its journey even more. Obstacles? They're the ultimate roadblock, making paths impassable.
3. **The Ant's Quest:** Once you've crafted your masterpiece, sit back and watch the ant embark on its quest to find the goal. Behind the scenes is the formidable A* algorithm, aided by the Manhattan distance heuristic function. But you don't need to dive into these complexities; your main course is the visual spectacle of the ant navigating its way through the challenges you've set.

Note: You can even make it further challenging by limiting the count of each terrain type. For example, you can limit yourself from using the number of obstacles, grassland, and swamplands you can use. This will make the mage creation more challenging.

## Logic and Design of Program

The program uses the A* algorithm to find the shortest path from the ant to the food. The ant is the start and the food is the goal. The ant can move in 8 directions. 

When the game starts, the user is asked to select:
- The start and the goal cells.
- The obstacle cells.
- The nature of the terrain for each cell between:
    - Open Terrain
    - Grassland
    - Swampland
    - Obstacles

After the user has selected the cells, the program calculates the shortest path from the ant to the food. The program uses the A* algorithm to find the shortest path. The A* algorithm uses a heuristic function to find the shortest path. The heuristic function used in this program is the Manhattan distance. The program shows the search evaluation of the A* algorithm. When the path is found, the ant starts moving from the start cell to the goal cell.

## About This Repository

This repository uses CI/CD to automatically build and release the program for Windows, macOS, and Linux. The program is packaged as a JAR file. The JAR file is then packaged into an MSI file for Windows, a DMG file for macOS, and a DEB file for Linux. The program is built using GitHub Actions and is released using GitHub Releases. The program is built for the following platforms:

- Windows (64-bit)
- macOS (64-bit)
- Linux (64-bit)
- JAR File

You can find them here in the [Releases](https://github.com/vmsaif/ant-path-finding-using-A-Star-algorithm/releases) section.

## Running the Program

- Make sure you have JRE installed on your computer. You can download JRE [here](https://adoptium.net/) or [here](https://www.oracle.com/java/technologies/javase-jre8-downloads.html).

Downloading the jar file is preferred. You can download the release version of the Game from the latest release [here](https://github.com/vmsaif/ant-path-finding-using-A-Star-algorithm/releases). The program is packaged as a JAR file. You can run the program by double-clicking on the JAR file.

## **Ubuntu**

### Option 1: Downloading the DEB File (simpler)

1. Open the terminal and navigate to the directory where you downloaded the .deb file.

```bash
cd ~/Downloads
```

2. Run the following command to install the package:

```bash
sudo dpkg -i ./ant-path-finding-v1.0.0-linux_amd64.deb # Replace the filename/version with the name of the DEB file you downloaded
```
3. If you get an error, run the following command to install the dependencies:

```bash
sudo apt-get install -f
```

4. Run the following command to start the game:

You can start the game by searching for it in the applications menu named `ant-path-finding` or by running the following command in the terminal:
```bash
/opt/ant-path-finding/bin/ant-path-finding
```

### Option 2: Downloading the JAR File

1. **Installing Java** (if it's not already installed):
   ```bash
   sudo apt update
   sudo apt install default-jre
   ```

2. **Navigate to the Download Location**:
   ```bash
   cd ~/Downloads
   ```

3. **Provide Execute Permissions** (Optional, but useful if you want to execute it directly):
   ```bash
   chmod +x ant-path-finding-v1.0.0.jar # Replace the filename with the name of the JAR file you downloaded
   ```

4. **Run the JAR File**:
   ```bash
   java -jar ant-path-finding-v1.0.0.jar # Replace the filename with the name of the JAR file you downloaded
   ```

## **macOS**
There are 2 methods. Downloading the dmg file or downloading the jar file. 
### Option 1: Downloading the DMG File (simpler)
I am not a registered Apple developer, That is why you will get a warning when you try to open the app. Thus, you will have to bypass the security settings to run the app.
Here are the steps:

1. Click on the .dmg file you have downloaded. 

2. Drag the app to the Applications folder.

3. In the Finder, locate the Ant Path Finding app.

   (Don’t use Launchpad to do this. Launchpad doesn’t allow you to access the shortcut menu.)

4. Press and hold Control then click the app icon.

5. Click Open.

   The app is saved as an exception to your security settings, and you can open it in the future by double-clicking it just as you can any registered app.

### Option 2: Downloading the JAR File
Make sure you have JRE installed on your computer. You can download JRE [here](https://adoptium.net/) or [here](https://www.oracle.com/java/technologies/javase-jre8-downloads.html).

1. **Navigate to the Download Location** (using Terminal):
   ```bash
   cd ~/Downloads
   ```

3. **Provide Execute Permissions** (Optional):
   ```bash
   chmod +x ant-path-finding-v1.0.0.jar # Replace the filename with the name of the JAR file you downloaded
   ```

4. **Run the JAR File**:
   ```bash
   java -jar ant-path-finding-v1.0.0.jar # Replace the filename with the name of the JAR file you downloaded
   ```

## **Windows**

### Option 1: Downloading the MSI File (simpler)

1. Open the folder where you downloaded the MSI file.
2. Double-click on the MSI file to run it.

At this point, the Windows smart screen will block the app from running. As I am not a signed developer by Microsoft, this warning will appear. 

3. If you get the warning, click on "More Info" and then click on "Run Anyway".
4. Wait for the installation to complete.
5. Run the game from the Desktop shortcut.

### Option 2: Downloading the JAR File

1. **Confirming Java**:

   Make sure you have JRE installed on your computer. You can download JRE [here](https://adoptium.net/) or [here](https://www.oracle.com/java/technologies/javase-jre8-downloads.html).

   Ensure that Java is added to your `PATH` so that it can be accessed from the command prompt. During the JRE installation, You will have the option to add to your path. It is unchecked by default. Make sure to enable it.  To check if Java is installed, run the following command in Command Prompt or PowerShell:
   ```bash
   java -version
   ```
   If Java is installed, you should see the version number. If you see an error, you need to install Java. See above for instructions on installing Java.

2. **Navigate to the Download Location** (using Command Prompt or PowerShell):
   ```bash
   cd c:\Users\username\Downloads # Replace username with your username
   ```

3. **Run the JAR File**:
   ```bash
   java -jar ant-path-finding-v1.0.0.jar
   ```
In all cases, once the commands are followed, the Java application packaged inside the JAR file should start running.

## Screenshots

![Screenshot 1](assets\screenshots\antPathFinding.gif "Computer Playing Black")

## UNINSTALLING THE PROGRAM

### Windows

1. Open the Control Panel.
2. Click on "Programs and Features".
3. Select "Ant Path Finding" from the list.
4. Click on "Uninstall".

### macOS

1. Open Finder.
2. Click on "Applications".
3. Right-click on "Ant Path Finding".
4. Click on "Move to Bin".

### Ubuntu

1. Open the terminal.
2. Run the following command:
   ```bash
   sudo apt-get remove ant-path-finding
   ```
3. Enter your password when prompted.

## Team
- Tahsin Ridwan Pulok [@tahsinpulok](https://github.com/tahsinpulok) - collaboration, providing assets and new feature/UI ideas.
- Saif Mahmud [@vmsaif](https://github.com/vmsaif) - programming and documentation.