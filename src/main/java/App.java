/* -----------------------------------------------------------------------------
    Author: Saif Mahmud
    Date: 2023-22-07
    Description: Main class to run the game
*/

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;
import com.formdev.flatlaf.FlatLightLaf;

public class App {
    public static void main(String[] args) throws Exception {

        int height = 800;
        int width = 1000;

        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Failed to initialize theme. Using fallback.");
            ex.printStackTrace();
        }

        JFrame frame = new JFrame("Ant Path Finding with A* Algorithm");
        frame.setSize(width, height);
        frame.setMinimumSize(new Dimension(width, height));

        Game game = new Game(frame);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout()); 

        frame.add(game, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);  // Center the window on the screen

        frame.setVisible(true);
    }
}
