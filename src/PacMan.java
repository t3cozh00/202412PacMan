import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.HashSet;
import java.util.Random;

public class PacMan extends JPanel implements ActionListener, KeyListener
{
    // create a class to store the information of block
    class Block {
        int x;
        int y;
        int width;
        int height;
        Image image;

        // save the starting position for player to restart the game
        int startX;
        int startY;

        // specifiy volocities for each object in x and y directions
        char direction = 'U'; // U = up, D = down, L = left, R = right
        int velocityX = 0;
        int velocityY = 0;

        // create a constructor Block
        Block(Image image, int x, int y, int width, int height) {
            this.image = image;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.startX = x;
            this.startY = y;
        }

        // when press the arrow key, update the direction and velocity
        void updateDirection(char direction) {
            this.direction = direction;
            updateVelocity();
        }

        void updateVelocity() {
            if (direction == 'U') {
                velocityX = 0;
                velocityY = -tileSize/4;
            }
            else if (direction == 'D') {
                velocityX = 0;
                velocityY = tileSize/4;
            }
            else if (direction == 'L') {
                velocityX = -tileSize/4;
                velocityY = 0;
            }
            else if (direction == 'R') {
                velocityX = tileSize/4;
                velocityY = 0;
            }
        }
    }

    private int rowCount = 21;
    private int columnCount = 19;
    private int tileSize = 32;
    private int boardWidth = columnCount * tileSize;
    private int boardHeight = rowCount * tileSize;

    private Image wallImage;
    private Image blueGhostImage;
    private Image orangeGhostImage;
    private Image pinkGhostImage;
    private Image redGhostImage;

    private Image pacmanRightImage;
    private Image pacmanLeftImage;
    private Image pacmanUpImage;
    private Image pacmanDownImage;

    //X = wall, O = skip, P = pac man, ' ' = food
    //Ghosts: b = blue, o = orange, p = pink, r = red
    private String[] tileMap = {
        "XXXXXXXXXXXXXXXXXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X                 X",
        "X XX X XXXXX X XX X",
        "X    X       X    X",
        "XXXX XXXX XXXX XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXrXX X XXXX",
        "O       bpo       O",
        "XXXX X XXXXX X XXXX",
        "OOOX X       X XOOO",
        "XXXX X XXXXX X XXXX",
        "X        X        X",
        "X XX XXX X XXX XX X",
        "X  X     P     X  X",
        "XX X X XXXXX X X XX",
        "X    X   X   X    X",
        "X XXXXXX X XXXXXX X",
        "X                 X",
        "XXXXXXXXXXXXXXXXXXX" 
    };

    // create hash sets
    HashSet<Block> walls;
    HashSet<Block> foods;
    HashSet<Block> ghosts;
    Block pacman; // only have one block to represent pacman
    Timer gameLoop;

    // create a constructor
    PacMan () {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setBackground(Color.BLACK);
        addKeyListener(this);
        setFocusable(true); // make sure the JPanel is the one listening to key presses 

        // load images
        wallImage = new ImageIcon(getClass().getResource("./wall.png")).getImage();
        blueGhostImage = new ImageIcon(getClass().getResource("./blueGhost.png")).getImage();
        orangeGhostImage = new ImageIcon(getClass().getResource("./orangeGhost.png")).getImage();
        pinkGhostImage = new ImageIcon(getClass().getResource("./pinkGhost.png")).getImage();
        redGhostImage = new ImageIcon(getClass().getResource("./redGhost.png")).getImage();

        pacmanDownImage = new ImageIcon(getClass().getResource("./pacmanDown.png")).getImage();
        pacmanUpImage = new ImageIcon(getClass().getResource("./pacmanUp.png")).getImage();
        pacmanLeftImage = new ImageIcon(getClass().getResource("./pacmanLeft.png")).getImage();
        pacmanRightImage = new ImageIcon(getClass().getResource("./pacmanRight.png")).getImage();

        loadMap(); 
        // System.out.println(walls.size());
        // System.out.println(foods.size());
        // System.out.println(ghosts.size());
        gameLoop = new Timer(50, this); // 50 is the delay, this is the PacMan object
        gameLoop.start(); 
    }

    public void loadMap(){
        // initialize hash sets
        walls = new HashSet<Block>();
        foods = new HashSet<Block>();
        ghosts = new HashSet<Block>();

        // iterate through the tile map, r = row, c = column
        for (int r = 0; r < rowCount; r++) {
            for (int c = 0; c < columnCount; c++) {
                String row = tileMap[r]; // get the current row
                char tileMapChar = row.charAt(c); // get the character at the position

                int x = c * tileSize;
                int y = r * tileSize;
                // figure out where the tile is, the x and y position

                if (tileMapChar == 'X') {
                    Block wall = new Block(wallImage, x, y, tileSize, tileSize); 
                    walls.add(wall);
                } 
                else if (tileMapChar == ' ') {
                    Block food = new Block(null, x + 14, y + 14, 14, 14);
                    foods.add(food);
                }
                else if (tileMapChar == 'b') {
                    Block ghost = new Block(blueGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'o') {
                    Block ghost = new Block(orangeGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'p') {
                    Block ghost = new Block(pinkGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'r') {
                    Block ghost = new Block(redGhostImage, x, y, tileSize, tileSize);
                    ghosts.add(ghost);
                }
                else if (tileMapChar == 'P') {
                    pacman = new Block(pacmanRightImage, x, y, tileSize, tileSize);
                }

            }
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g); // invoke the same function of the same name from JPanel   
        draw(g);
    }

    public void draw (Graphics g) {
        // draw a rectangle
        // draw PacMan image as a block object
        g.drawImage(pacman.image, pacman.x, pacman.y, pacman.width, pacman.height, null);

        // draw gohsts
        for (Block ghost : ghosts) {
            g.drawImage(ghost.image, ghost.x, ghost.y, ghost.width, ghost.height, null);
        }

        for (Block wall : walls) {
            g.drawImage(wall.image, wall.x, wall.y, wall.width, wall.height, null);
        }

        g.setColor(Color.WHITE);
        for (Block food : foods) {
                g.fillRect(food.x, food.y, food.width, food.height);
        }
    }

    public void move() {
        pacman.x += pacman.velocityX;
        pacman.y += pacman.velocityY;
    }

    // used for game loop
    @Override
    public void actionPerformed(ActionEvent e) {
        move(); // update the position of the objects, then repaint
        repaint(); // to call paintComponent
    }

    @Override
    // keyTyped: when you type on a key that has a corresponding character
    // keyPressed: you press a key including arrow keys to trigger this function
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        //System.out.println("KeyEvent: " + e.getKeyCode());
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            pacman.updateDirection('U');
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            pacman.updateDirection('D');
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            pacman.updateDirection('L');
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            pacman.updateDirection('R');
        }
    }
}
