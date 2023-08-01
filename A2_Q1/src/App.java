/* -----------------------------------------------------------------------------
    Author: Saif Mahmud
    Date: 2023-22-07
    Course: COMP 452
    Student ID: 3433058
    Assignment: 2
    Part: 1
    Description: Main class to run the game
*/

import javax.swing.JFrame;

public class App {
    public static void main(String[] args) throws Exception {
        Game game = new Game();
        JFrame frame = new JFrame("A2_P1");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 700);

        frame.add(game);
        // frame.pack();
        frame.setVisible(true);
    }
}
