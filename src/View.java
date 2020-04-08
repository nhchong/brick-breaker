import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observer;
import java.util.Observable;

class View extends JPanel implements Observer {

    // Engine
    private Engine engine;

    // Dimensions
    private static final Dimension MAX_CANVAS_DIMENSION = new Dimension(1600, 1200);
    private static final Dimension DEFAULT_CANVAS_DIMENSION = new Dimension(1200, 800);
    private static final Dimension MIN_CANVAS_DIMENSION = new Dimension(400, 100);

    // Splash Screen
    JPanel splashScreen = new JPanel();
    JLabel endGameHeader = new JLabel();
    JLabel endGameBody = new JLabel();

    // Splash Screen
    JPanel endGameScreen = new JPanel();

    // score JLabel
    JLabel scoreAndLives = new JLabel();


    View(Engine e) {
        engine = e;

        // set sizes of game board
        setPreferredSize(DEFAULT_CANVAS_DIMENSION);
        setSize(DEFAULT_CANVAS_DIMENSION);
        setMaximumSize(MAX_CANVAS_DIMENSION);
        setMinimumSize(MIN_CANVAS_DIMENSION);
        setLayout(new GridBagLayout());

        // Mouse motion listener to control paddle
        addMouseMotionListener(new mouseMoveAdapter());

        // Build splash screen
        buildSplashScreen();

        // Build end game screen
        buildendGameScreen();

        // Add Score and Lives to Screen
        addScoreAndLives();

    }

    class mouseMoveAdapter extends MouseMotionAdapter {
        public void mouseMoved(MouseEvent e) { engine.setPaddleX(e.getX()); }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  // antialiasing look nicer
                RenderingHints.VALUE_ANTIALIAS_ON);

        if (engine.inSplashScreen) {
            hideScoreAndLives();
            showSplashScreen();
        } else if (engine.inEndGameScreen) {
            hideScoreAndLives();
            hideEndGameScreen();
            updateEndGameScreen();
            showEndGameScreen();
        } else {
            hideSplashScreen();
            hideEndGameScreen();
            drawBlocks(g2);
            drawBall(g2);
            drawPaddle(g2);
            drawScoreAndLives();
        }
    }

    void buildendGameScreen() {
        Font headerFont = new Font("Courier New", Font.BOLD, 50);
        Font bodyFont = new Font("Courier New", Font.PLAIN, 30);
        Font buttonFont = new Font("Courier New", Font.PLAIN, 20);

        JButton playAgainBtn = new JButton("Play Again");

        endGameHeader.setAlignmentY(Component.CENTER_ALIGNMENT);
        endGameBody.setAlignmentY(Component.CENTER_ALIGNMENT);

        endGameHeader.setAlignmentX(Component.CENTER_ALIGNMENT);
        endGameBody.setAlignmentX(Component.CENTER_ALIGNMENT);
        playAgainBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        endGameHeader.setFont(headerFont);
        endGameBody.setFont(bodyFont);
        playAgainBtn.setFont(buttonFont);

        playAgainBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.startGame();
            }
        });

        endGameScreen.setLayout(new BoxLayout(endGameScreen, BoxLayout.Y_AXIS));
        endGameScreen.add(endGameHeader);
        endGameScreen.add(endGameBody);
        endGameScreen.add(playAgainBtn);
        endGameScreen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        endGameScreen.setVisible(false);
        add(endGameScreen);
    }

    void updateEndGameScreen() {
        String headerText = engine.previousScore == engine.totalBlocks ? "CONGRATULATIONS, YOU WON! :)" : "Yikes, you lost :(";
        String livesText = engine.previousLives == 1 ? " life" : " lives";
        String bodyText = "You scored " + engine.previousScore + " with " + engine.previousLives + livesText + " left";
        endGameHeader.setText(headerText);
        endGameBody.setText(bodyText);
    }

    void buildSplashScreen() {

        Font headerFont = new Font("Courier New", Font.BOLD, 50);
        Font bodyFont = new Font("Courier New", Font.PLAIN, 30);
        Font instructionFont = new Font("Courier New", Font.PLAIN, 20);
        Font buttonFont = new Font("Courier New", Font.PLAIN, 20);

        JLabel header = new JLabel("BREAKOUT");
        JLabel name = new JLabel("Nicholas Chong");
        JLabel userId = new JLabel("205331133");
        JLabel instructions1 = new JLabel("Use cursor or left/right keys to control paddel", SwingConstants.CENTER);
        JLabel instructions2 = new JLabel("Use the space bar to pause/start game", SwingConstants.CENTER);
        JButton startGameBtn = new JButton("Let's Get This Party Started");

        header.setAlignmentY(Component.CENTER_ALIGNMENT);
        name.setAlignmentY(Component.CENTER_ALIGNMENT);
        userId.setAlignmentY(Component.CENTER_ALIGNMENT);
        instructions1.setAlignmentY(Component.CENTER_ALIGNMENT);
        instructions2.setAlignmentY(Component.CENTER_ALIGNMENT);

        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        userId.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions1.setAlignmentX(Component.CENTER_ALIGNMENT);
        instructions2.setAlignmentX(Component.CENTER_ALIGNMENT);
        startGameBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.setFont(headerFont);
        name.setFont(bodyFont);
        userId.setFont(bodyFont);
        instructions1.setFont(instructionFont);
        instructions2.setFont(instructionFont);
        startGameBtn.setFont(buttonFont);

        startGameBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                engine.startGame();
            }
        });

        splashScreen.setLayout(new BoxLayout(splashScreen, BoxLayout.Y_AXIS));
        splashScreen.add(header);
        splashScreen.add(name);
        splashScreen.add(userId);
        splashScreen.add(instructions1);
        splashScreen.add(instructions2);
        splashScreen.add(startGameBtn);
        splashScreen.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        add(splashScreen);
    }

    void drawPaddle(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(3));
        g2.draw(engine.paddle);
        g2.setColor(Color.YELLOW);
        g2.fill(engine.paddle);
    }

    void drawBall(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.fill(engine.ball);
    }

    void drawBlocks(Graphics2D g2) {
        for (Block block : engine.blocks) {
            if (!block.getHit()) {
                g2.setColor(block.color);
                g2.fill(block.container);
            }
        }
    }

    void addScoreAndLives() {
        add(scoreAndLives);
        scoreAndLives.setVisible(false);
    }

    void hideScoreAndLives() {
        scoreAndLives.setVisible(false);
    }

    void drawScoreAndLives() {
        scoreAndLives.setVisible(true);
    }

    void showSplashScreen() {
        splashScreen.setVisible(true);
    }

    void hideSplashScreen() {
        splashScreen.setVisible(false);
    }

    void showEndGameScreen() {
        endGameScreen.setVisible(true);
    }

    void hideEndGameScreen() {
        endGameScreen.setVisible(false);
    }

    public void update(Observable var1, Object var2) {
        scoreAndLives.setText("Score: " + engine.getScore() + "  Lives: " + engine.getLives());
        repaint();
    }
}
