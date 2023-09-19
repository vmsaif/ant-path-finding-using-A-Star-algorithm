/* -----------------------------------------------------------------------------
    Author: Saif Mahmud
    Date: 2023-22-07
    Description: Main class to run the game
*/

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        Game game = new Game();
        JFrame frame = new JFrame("Ant Path Finding with A* Algorithm");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);
        frame.add(game);
        frame.setVisible(true);
    }
}
