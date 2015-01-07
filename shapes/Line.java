/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import org.json.simple.JSONObject;

/**
 *
 * @author chicco
 */
public class Line extends Figure {

    private int x2, y2;

    public Line(int x1, int y1, int x2, int y2, Color color) {
        super(x1, y1, x2, y2, false, color);
        this.x2 = x2;
        this.y2 = y2;
    }

    @Override
    public void draw(Graphics2D graphics) {
        if (super.isSelected()) {
            graphics.setStroke(super.dottedStroke());
            graphics.setColor((super.getColor() == Color.BLACK) ? Color.YELLOW : Color.BLACK);
        } else {
            graphics.setStroke(super.nullStroke());
            graphics.setColor(super.getColor());
        }
        graphics.drawLine(super.getX(), super.getY(), x2, y2);
    }

    @Override
    public boolean contains(Point point) {
        Line2D line = new Line2D.Double(super.getX(), super.getY(), x2, y2);
        return line.ptSegDistSq(point) <= 10;

    }

    @Override
    public JSONObject toJson() {
        JSONObject temp = new JSONObject();
        temp.put("class", "line");
        temp.put("x", super.getX() + "");
        temp.put("y", super.getY() + "");
        temp.put("isFilled", super.isFilled() ? "1" : "0");
        temp.put("color", super.getColor().getRGB() + "");
        temp.put("u", x2 + "");
        temp.put("v", y2 + "");
        return temp;
    }

    @Override
    public void move(int dx, int dy) {
        super.move(dx, dy); //To change body of generated methods, choose Tools | Templates.
        x2=dx;
        y2=dy;
    }

    public void resize(int x, int y, int n) {
        if (n == 1) {
            setX(x);
            setY(y);
        } else {
            setcX(x);
            setcY(y);
            x2=x;
            y2=y;
        }

    }

    @Override
    public String toString() {
        return "Line";
    }

}
