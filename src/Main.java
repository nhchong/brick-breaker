import java.awt.Dimension;
import javax.swing.JFrame;

public class Main {
    public Main() {}

    private static final Dimension MAX_DIMENSION = new Dimension(1600, 1200);
    private static final Dimension DEFAULT_DIMENSION = new Dimension(1200, 800);
    private static final Dimension MIN_DIMENSION = new Dimension(400, 300);

    public static void main(String[] var0) {
        JFrame frame = new JFrame("Breakout");
        frame.setPreferredSize(DEFAULT_DIMENSION);
        frame.setMaximumSize(MAX_DIMENSION);
        frame.setMinimumSize(MIN_DIMENSION);
        frame.setResizable(false);
        Engine engine;
        if (var0.length == 0) { engine = new Engine(); }
        else if (var0.length  == 2) { engine = new Engine(var0[0], var0[1]); }
        else { engine = new Engine(var0[0], var0[1], var0[2], var0[3]); }
        View view = new View(engine);
        engine.addObserver(view);
        engine.notifyObservers();
        frame.getContentPane().add(view);
        frame.pack();
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
    }
}
