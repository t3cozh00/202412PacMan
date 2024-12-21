import javax.swing.JFrame; // import the JFrame class from Java's Swing library. Swing is a toolkit for building graphical user interfaces (GUIs) in Java.
// the width of the window going from left to right is 19 columns * 32 pixels and the height is going to be 21 rows * 32 pixels

// step1 create a window
// step2 need a j panel to draw the game
// step3 create a game
// step3.1 load images
// step4 iterate through the tile map, store wall block, food, and ghost in three different hash sets
// the reason we use hash set instead of array list is that it is easier to search in a hash set
// in order to draw each image, we need to specify where it is (x, y, width, height)
// the best way to store the information is to create a class in PacMan.java
// step4.1 create hash sets to represent wall, food, and ghost objects
// step4.2 map title (an array of strings)
// step4.3 go through the tile map and create the objects for wall, food, and ghost


public class App {
    public static void main(String[] args) throws Exception {
        int rowCount = 21;
        int columnCount = 19;
        int tileSize = 32;
        int boardWidth = columnCount * tileSize;
        int boardHeight = rowCount * tileSize;

        JFrame frame = new JFrame("Pac Man"); // create a new JFrame object
        //frame.setVisible(true);// make the window visible
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null); // center the window
        frame.setResizable(false); // unenables the user to resize the window
        frame.setDefaultCloseOperation(JFrame
        .EXIT_ON_CLOSE);// terminate the game if the player click the close button

        // step 2 draw on j panel to show the game (create another java file - PacMan.java to inherit JPanel)

        // create an instance of JPanel PacMan
        PacMan pacmanGame = new PacMan();
        frame.add(pacmanGame); // add the PacMan object to the JFrame
        frame.pack(); // resize the window to fit the preferred size of its contents
        frame.setVisible(true); // make the window visible
    }
}
