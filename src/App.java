/* -----------------------------------------------------------------------------
    Author: Saif Mahmud
    Date: 2023-22-07
    Description: Main class to run the game
*/

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        
        JFrame frame = new JFrame("Ant Path Finding with A* Algorithm");
        frame.setSize(1400, 900);

        Game game = new Game(frame);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);
        
        frame.setVisible(true);
    }
}
