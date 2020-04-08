import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Block {
    double x;
    double y;
    double width;
    double height;
    boolean hit = false;
    Color color;
    Rectangle2D container;

    Block(double var1, double var2, double var3, double var4) {
        x = var1;
        y = var2;
        width = var3;
        height = var4;
        container = new Rectangle2D.Double(x, y, width, height);
        hit = false;
    }

    void setColor(Color c) {
        color = c;
    }

    void setHit() {
        hit = true;
    }

    boolean getHit() { return hit; }

    double getX() { return x; }

    double getY() { return y; }

    double getWidth() { return width; }

    double getHeight() { return height; }
}
