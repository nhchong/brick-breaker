import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Random;

public class Engine extends Observable {

    // On/Off switch
    boolean gameOn = false;

    // Screen
    private final double defaultScreenWidth = 1200;
    private final double defaultScreenHeight = 800;

    // Timers
    Timer repaintTimer = new Timer();
    Timer ballMoveTimer = new Timer();

    // Ball
    private double ballX;
    private double ballY;
    private int dx = 5;
    private int dy = 5;
    private final double defaultBallWidth = 20;
    public Ellipse2D ball;
    private int ballMoveTimeFrame = 50;

    // Paddle
    private final double defaultPaddleWidth = 120;
    private final double defaultPaddleHeight = 20;
    private final double paddleToBottomOffset = 50;
    private double paddleX;
    private double paddleY = defaultScreenHeight-paddleToBottomOffset-(defaultPaddleHeight/2);
    public Rectangle2D paddle;

    //Blocks
    ArrayList<Block> blocks = new ArrayList<Block>();
    boolean setOwnBlocks = false;
    private int numBlocksPerRow;
    private int numBlockRows;
    public int totalBlocks;
    private int spaceBetweenBlocks = 5;
    private final double blockHeightOffset = 50;
    private final double blockWidthOffset = 70;
    private double blockWidth;
    final private double blockHeight = 20;
    final private int defaultNumBlockRows = 12;
    final private int defaultNumBlockColumns = 11;

    // Score
    private int score = 0;
    public int previousScore = 0;

    // Number of lives
    private final int defaultLives = 3;
    private int lives = defaultLives;
    public int previousLives = 0;


    // If the splash screen is being shown
    boolean inSplashScreen = true;

    // If the end game screen is being shown
    boolean inEndGameScreen = false;

    // Repaint FPS
    static private int repaintFPS = 50;


    Engine(String frameRate, String ballSpeed) {
        repaintFPS = Integer.parseInt(frameRate);
        setBallSpeed(Integer.parseInt(ballSpeed));
        setBlocks(true, defaultNumBlockRows, defaultNumBlockColumns);
        this.setChanged();
        setKeyListener();
    }

    Engine(String frameRate, String ballSpeed, String numRows, String numColumns) {
        repaintFPS = Integer.parseInt(frameRate);
        setBallSpeed(Integer.parseInt(ballSpeed));
        setBlocks(false, Integer.parseInt(numRows), Integer.parseInt(numColumns));
        this.setChanged();
        setKeyListener();
    }

    Engine() {
        setKeyListener();
        setBlocks(true, defaultNumBlockRows, defaultNumBlockColumns);
    }

    void setBlocks(boolean isDefault, int rows, int columns) {
        if (isDefault) {
            setOwnBlocks = false;
            numBlocksPerRow = 11;
            numBlockRows = 12;
        } else {
            setOwnBlocks = true;
            numBlocksPerRow = columns;
            numBlockRows = rows;
        }
        totalBlocks = numBlocksPerRow*numBlockRows;
        setBlockWidth();
    }

    void setBlockWidth() {
        blockWidth = (defaultScreenWidth-(blockWidthOffset*2)-
                ((numBlocksPerRow-1)*spaceBetweenBlocks))/numBlocksPerRow;
    }

    void setKeyListener() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_SPACE && !inSplashScreen && !inEndGameScreen) {
                if (!gameOn) {
                    gameOn = true;
                    startRepaintTimer();
                    startBallMoveTimer();
                } else {
                    gameOn = false;
                    stopRepaintTimer();
                    stopBallMoveTimer();
                }
            }
            else if ((e.getID() == KeyEvent.KEY_RELEASED && e.getKeyCode() == KeyEvent.VK_ESCAPE && !inSplashScreen && !inEndGameScreen)) {
                endGame();
            }
            else if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_LEFT && gameOn) {
                setPaddleX(paddleX-20);
            }
            else if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_RIGHT && gameOn) {
                setPaddleX(paddleX+20);
            }
            return false;
        });
    }

    void startGame() {
        inSplashScreen = false;
        inEndGameScreen = false;
        gameOn = false;
        stopRepaintTimer();
        stopBallMoveTimer();
        initiatePaddle();
        initiateBall();
        if (lives == defaultLives) {
            // only initiate blocks if it's a new game
            initiateBlocks();
        }
        setChanged();
        notifyObservers();
    }

    void initiateBall() {
        ballX = defaultScreenWidth/2;
        ballY = paddleY-defaultBallWidth;
        updateBall();
    }
    void initiatePaddle() {
        paddleX = defaultScreenWidth/2;
        updatePaddle();
    }

    void initiateBlocks() {
        for (int row = 0; row < numBlockRows; ++row) {
            for (int column = 0; column < numBlocksPerRow; ++column) {
                double x = blockWidthOffset+(blockWidth*column)+(spaceBetweenBlocks*(column));
                double	y = blockHeightOffset + row*blockHeight + row*spaceBetweenBlocks;
                Block block = new Block(x, y, blockWidth, blockHeight);
                if (setOwnBlocks) {
                    Random rand = new Random();
                    int r = rand.nextInt(6);
                    block.setColor(generateRandomColor(r));
                } else {
                    if (isBlackBlock(row, column)) block.setColor(Color.BLACK);
                    else block.setColor(Color.YELLOW);
                }
                blocks.add(block);
            }
        }
    }

    Color generateRandomColor(int r) {
        if (r == 0) return Color.CYAN;
        if (r == 1) return Color.GREEN;
        if (r == 2) return Color.PINK;
        if (r == 3) return Color.ORANGE;
        if (r == 4) return Color.MAGENTA;
        else return Color.RED;
    }

    boolean isBlackBlock(int row, int column) {
        if (((row == 0) || (row == 1)) && ((column == 0) || (column == 1) || (column == 2))) return true;
        if (((row == 5) || (row == 5)) && ((column == 1) || (column == 2))) return true;
        if (((row == 10) || (row == 11)) && ((column == 0) || (column == 1) || (column == 2))) return true;
        if (((row >= 0) && (row <= 11))  && (column == 2)) return true;
        if ((row == 6)  && (column == 1)) return true;
        if (((row >= 0) && (row <= 6)) && (column == 4)) return true;
        if (((row >= 0) && (row <= 11)) && (column == 6)) return true;
        if (((row >= 5) && (row <= 6)) && (column == 5)) return true;
        if (((row >= 0) && (row <= 6)) && (column == 8)) return true;
        if (((row >= 0) && (row <= 11)) && (column == 10)) return true;
        if (((row == 0) || (row == 1) || (row == 5) || (row == 6)) && (column == 9)) return true;
        else return false;
    }

    void setBallSpeed(int bs) {
        dx = bs*5;
        dy = bs*5;
    }

    void setPaddleX(double x) {
        if (gameOn) {
            paddleX = x;
            if (paddleX > (defaultScreenWidth - (defaultPaddleWidth / 2))) {
                paddleX = defaultScreenWidth - (defaultPaddleWidth / 2);
            } else if (paddleX < (defaultPaddleWidth / 2)) {
                paddleX = defaultPaddleWidth / 2;
            }
            updatePaddle();
        }
    }

    void updatePaddle() {
        paddle = new Rectangle2D.Double(paddleX-(defaultPaddleWidth/2), paddleY-(defaultPaddleHeight/2),
                defaultPaddleWidth, defaultPaddleHeight);
    }

    void updateBall() {
        ball = new Ellipse2D.Double(ballX-(defaultBallWidth/2), ballY-(defaultBallWidth/2),
                defaultBallWidth, defaultBallWidth);
    }

    void clearBlocks() {
        blocks.clear();
    }

    void startRepaintTimer() {
        repaintTimer = new Timer();
        TimerTask task = new TimerTask()  {
            @Override
            public void run() {
                setChanged();
                notifyObservers();
            }
        };
        repaintTimer.schedule(task, 0, (1000/repaintFPS));
    }

    void startBallMoveTimer() {
        ballMoveTimer = new Timer();
        TimerTask task = new TimerTask()  {
            @Override
            public void run() {
                moveBall();
            }
        };
        ballMoveTimer.schedule(task, 0, (1000/ballMoveTimeFrame));
    }

    void stopRepaintTimer() {
        repaintTimer.cancel();
    }

    void stopBallMoveTimer() {
        ballMoveTimer.cancel();
    }

    public void moveBall() {
        checkBlockCollision();
        checkPaddleCollision();
        checkWallCollision();
        ballX += dx;
        ballY += dy;
        updateBall();
        checkBallDeath();
    }

    void checkPaddleCollision() {
        if (ball.intersects(paddle)) { dy *= -1; }
    }

    void checkWallCollision() {
        if (ballX < defaultBallWidth/2 || ballX > (defaultScreenWidth - defaultBallWidth/2)) { dx *= -1; }
        if (ballY < defaultBallWidth/2 || ballY > (defaultScreenHeight - defaultBallWidth/2)) { dy *= -1; }
    }

    void checkBlockCollision() {
        for (Block block : blocks) {
            if (!block.getHit() && ball.intersects(block.container)) {
                // if the ball hit the left-side of the block
                if (ballX < block.getX()) {
                    dx *= -1;
                    block.setHit();
                }
                // if the block hit the right side of the block
                else if (ballX > (block.getX()+block.getWidth())) {
                    dx *= -1;
                    block.setHit();
                }
                // if the block hit the top of the block
                else if (ballY > block.getY()) {
                    dy *= -1;
                    block.setHit();
                }
                // if the block hit the bottom of the block
                else if (ballY < (block.getY()+block.getHeight())) {
                    dy *= -1;
                    block.setHit();
                }
                score++;
                if (score == totalBlocks) { endGame(); }
                break;
            }
        }
    }

    void checkBallDeath() {
        System.out.print(paddleY);
        System.out.print("      ");
        System.out.print(defaultScreenWidth-(defaultBallWidth));
        System.out.print("      ");
        if (ballY > (paddleY+paddleToBottomOffset)) {
            lives -= 1;
            if (lives == 0) { endGame(); }
            else { startGame(); }
        }
    }

    void endGame() {
        gameOn = false;
        inEndGameScreen = true;
        previousLives = lives;
        previousScore = score;
        resetLives();
        resetScore();
        clearBlocks();
        stopBallMoveTimer();
    }

    String getScore() {
        return Integer.toString(score);
    }


    String getLives() {
        return Integer.toString(lives);
    }

    void resetScore() {
        score = 0;
    }

    void resetLives() {
        lives = defaultLives;
    }

}

