/* -----------------------------------------------------------------------------
    Author: Saif Mahmud
    Date: 2023-22-07
    Description: Main class to run the game
*/

import javax.swing.JFrame;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;

public class App {
    public static void main(String[] args) throws Exception {

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize theme. Using fallback.");
        }

        JFrame frame = new JFrame("Ant Path Finding with A* Algorithm");
        frame.setSize(1400, 800);

        Game game = new Game(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(game);

        frame.setVisible(true);
    }
}
