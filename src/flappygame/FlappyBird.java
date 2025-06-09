package flappygame;

import javax.swing.*;      // GUI elements ke liye (JFrame, JPanel, JOptionPane etc.)
import java.awt.*;         // Drawing aur colors ke liye (Graphics, Color, Font etc.)
import java.awt.event.*;   // Keyboard aur Timer events handle karne ke liye
import java.util.ArrayList; // Pipes ko list me store karne ke liye
import java.util.Random;   // Pipes ki random height banane ke liye

public class FlappyBird extends JPanel implements ActionListener, KeyListener {

    // --- Game board ka size ---
    int boardWidth = 360;    // Screen ki width
    int boardHeight = 640;   // Screen ki height

    // --- Game me use hone wali images (graphics) ---
    Image backgroundImg;     // Background image
    Image birdImg;           // Bird ki image
    Image topPipeImg;        // Upar wali pipe ki image
    Image bottomPipeImg;     // Neeche wali pipe ki image

    // --- Bird ki starting position aur size ---
    int birdX = boardWidth / 8;     // Bird ka X position
    int birdY = boardHeight / 2;    // Bird ka Y position
    int birdWidth = 34;             // Bird ki width
    int birdHeight = 24;            // Bird ki height

    // --- Bird class jo bird ki position, size, aur image store karti hai ---
    class Bird {
        int x = birdX;       // X position
        int y = birdY;       // Y position
        int width = birdWidth;   // Width
        int height = birdHeight; // Height
        Image img;               // Bird ki image

        Bird(Image img) {
            this.img = img;     // Constructor me image assign kar rahe hain
        }
    }

    // --- Pipe ki width aur height, aur opening gap ---
    int pipeWidth = 64;         // Pipe ki width
    int pipeHeight = 512;       // Pipe ki height
    final int openingSpace = boardHeight / 4; // Gap jo upar aur neeche pipe ke darmiyan hota hai

    // --- Pipe class har pipe ki properties define karti hai ---
    class Pipe {
        int x;                // Pipe ka X position
        int y;                // Pipe ka Y position
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;            // Pipe ki image
        boolean passed = false;  // Kya bird ne is pipe ko cross kiya?

        Pipe(Image img, int x, int y) {
            this.img = img;
            this.x = x;
            this.y = y;
        }
    }

    Bird bird;                 // Bird object

    int velocityX;             // Pipes ki speed (left move karte hain)
    int baseSpeed = -4;        // Default speed (difficulty ke hisaab se set hoti hai)
    int maxSpeed = -10;        // Max speed (hardest level)
    int velocityY = 0;         // Bird ki vertical speed
    int gravity = 1;           // Neeche girne wali force (gravity)

    ArrayList<Pipe> pipes;     // Pipes ka dynamic list
    Random random = new Random(); // Random height banane ke liye

    Timer gameLoop;            // Main game loop (60 times per second chalti hai)
    Timer placePipeTimer;      // Har 1.5 second me naye pipes add karta hai

    boolean gameOver = false;      // Game over flag
    boolean gameStarted = false;   // Game start hua ya nahi
    double score = 0;              // Player ka current score
    double highScore = 0;          // Sabse zyada banaya gaya score

    // --- Constructor: Game start hone par sab kuch initialize karta hai ---
    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight)); // Panel ka size set
        setFocusable(true);        // Keyboard events ka focus milta hai
        addKeyListener(this);      // Key events sunne ke liye

        // --- Images load karna (resources folder se) ---
        backgroundImg = new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("./flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();

        bird = new Bird(birdImg);  // Bird ka object banaya
        pipes = new ArrayList<>(); // Pipes ka arraylist banaya

        // --- Game loop set kiya (60 FPS ke liye) ---
        gameLoop = new Timer(1000 / 60, this);

        // --- Har 1500ms me naye pipes add karne wala timer ---
        placePipeTimer = new Timer(1500, e -> placePipes());

        // --- Game start hone se pehle difficulty puchhi jaati hai ---
        chooseDifficulty();
    }

    // --- Difficulty choose karne ka dialog box ---
    void chooseDifficulty() {
        String[] options = {"Easy", "Medium", "Hard"};
        int choice = JOptionPane.showOptionDialog(
                null,
                "Select Difficulty:",        // Dialog ka title
                "Flappy Bird",              // Window ka title
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.INFORMATION_MESSAGE,
                null,
                options,
                options[0]
        );

        // --- Speed set karte hain difficulty ke according ---
        switch (choice) {
            case 0: // Easy
                baseSpeed = -3;
                maxSpeed = -6;
                break;
            case 1: // Medium
                baseSpeed = -4;
                maxSpeed = -8;
                break;
            case 2: // Hard
                baseSpeed = -5;
                maxSpeed = -10;
                break;
            default:
                baseSpeed = -4;
                maxSpeed = -8;
                break;
        }

        velocityX = baseSpeed;  // Pipes ki speed set kar rahe hain
        gameLoop.start();       // Game loop start
        placePipeTimer.start(); // Pipe placement timer start
        gameStarted = true;
    }

    // --- Random height ke sath pipes ko screen pe laana ---
    void placePipes() {
        int minPipeTopY = -pipeHeight + 100;  // Upar ki pipe ki lowest position
        int maxPipeTopY = -100;               // Upar ki pipe ki highest position
        int randomPipeY = minPipeTopY + random.nextInt(maxPipeTopY - minPipeTopY + 1);

        // --- Dono pipes banate hain (upar aur neeche) ---
        Pipe topPipe = new Pipe(topPipeImg, boardWidth, randomPipeY);
        Pipe bottomPipe = new Pipe(bottomPipeImg, boardWidth, randomPipeY + pipeHeight + openingSpace);

        pipes.add(topPipe);
        pipes.add(bottomPipe);
    }

    // --- Screen draw karne ka method (automatically call hota hai) ---
    public void paintComponent(Graphics g) {
        super.paintComponent(g); // Background clear karta hai
        draw(g);                 // Sab cheezen draw karne ke liye
    }

    // --- Game ka main drawing function ---
    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, null); // Background
        g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null); // Bird

        for (Pipe pipe : pipes) {
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null); // Pipes
        }

        // --- Score aur high score text ---
        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.BOLD, 32));
        g.drawString("Score: " + (int) score, 10, 35);
        g.drawString("High: " + (int) highScore, 200, 35);

        // --- Agar game over ho jaye to message dikhana ---
        if (gameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString("Game Over", 100, boardHeight / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString("Press SPACE to Restart", 80, boardHeight / 2 + 40);
        }
    }

    // --- Har frame pe bird aur pipes ki position update karna ---
    public void move() {
        velocityY += gravity;  // Gravity lagti hai bird pe
        bird.y += velocityY;   // Bird neeche girta hai

        for (Pipe pipe : pipes) {
            pipe.x += velocityX; // Pipes left move karti hain

            // --- Agar bird ne pipe cross kar li to score badhao ---
            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
               
                score += 0.5;         // Har do pipes cross karne pe 1 point milta hai (0.5 har pipe ka)
                pipe.passed = true;  // Pipe ko mark karte hain ke ye pass ho chuki hai

                // --- Har 10 score pe game ki speed tez ho jati hai ---
                if ((int) score % 10 == 0 && velocityX > maxSpeed) {
                    velocityX -= 1;  // Speed aur tez karte hain (difficulty badhati hai)
                }
            }

            // --- Bird aur pipe ke darmiyan collision check karte hain ---
            if (collision(bird, pipe)) {
                gameOver = true;   // Agar takra gaya to game over ho jata hai
            }
        }

        // --- Agar bird neeche gir jaye ya screen ke bahar chali jaye to game over ---
        if (bird.y > boardHeight || bird.y < 0) {
            gameOver = true;
        }

        // --- Jo pipes screen ke bahar chale jayein, unhe hata do (memory save hoti hai) ---
        pipes.removeIf(pipe -> pipe.x + pipe.width < 0);
    }

    // --- Collision detection function (bird aur pipe takraye ya nahi) ---
    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&       // Bird ka right side pipe ke left side ke andar ho
               a.x + a.width > b.x &&       // Bird ka left side pipe ke right side ke andar ho
               a.y < b.y + b.height &&      // Bird ka bottom pipe ke top se takra raha ho
               a.y + a.height > b.y;        // Bird ka top pipe ke bottom se takra raha ho
    }

    // --- Ye method har frame me call hota hai (60 times/sec) ---
    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameStarted && !gameOver) {
            move();      // Game ki logic update karo
            repaint();   // Dubara draw karo
        }

        if (gameOver) {
            if (score > highScore) {
                highScore = score; // Naya high score set karna
            }
            gameLoop.stop();        // Game loop band karna
            placePipeTimer.stop();  // Pipe ka timer bhi band
        }
    }

    // --- Jab SPACE key press ho to bird jump kare ya game reset ho ---
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            velocityY = -9; // Bird jump karega (upar ki taraf move)

            if (gameOver) {
                // --- Game reset karte hain ---
                bird.x = birdX;
                bird.y = birdY;
                velocityY = 0;
                velocityX = baseSpeed;
                pipes.clear();      // Saari purani pipes hata do
                score = 0;
                gameOver = false;
                gameLoop.start();       // Game loop dobara start
                placePipeTimer.start(); // Pipe wala timer bhi start
            }
        }
    }

    // --- Ye 2 methods abhi use nahi ho rahi (required by interface) ---
    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    // --- Main function: Ye game start karta hai ---
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");     // Ek nayi window banao
        FlappyBird flappyBird = new FlappyBird();     // Game ka object banao
        frame.add(flappyBird);                        // Game panel ko window me add karo
        frame.setSize(flappyBird.boardWidth, flappyBird.boardHeight); // Window size set karo
        frame.setLocationRelativeTo(null);            // Window ko screen ke center me lao
        frame.setResizable(false);                    // Window resize na ho
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close pe program band ho
        frame.pack();                                 // Auto layout fix karo
        flappyBird.requestFocus();                    // Game input ka focus le
        frame.setVisible(true);                       // Window show karo
    }
}
