/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import org.json.simple.JSONObject;

/**
 *
 * @author chicco
 */
public class Ellipse extends Figure {

    private  int xDiameter, yDiameter;

    public Ellipse(int x, int y, int diameter1, int diameter2, boolean filled, Color fillColor) {
        super(x, y,x+diameter1/2,y+diameter2/2,filled, fillColor);
        this.xDiameter = diameter1;
        this.yDiameter = diameter2;
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
        graphics.drawOval(super.getX(), super.getY(), xDiameter, yDiameter); // draw outter shape
        //fill shape
        if (super.isFilled()) {
            graphics.setColor(super.getColor());
            graphics.fillOval(super.getX(), super.getY(), xDiameter, yDiameter);
        }
    }

    @Override
    public boolean contains(Point point) {
        Ellipse2D temp = new Ellipse2D.Double(super.getX(), super.getY(), xDiameter, yDiameter);
        return temp.contains(point);
    }

    @Override
    public JSONObject toJson() {
        JSONObject temp = new JSONObject();
        temp.put("class","ellipse");
        temp.put("x", super.getX() + "");
        temp.put("y", super.getY() + "");
        temp.put("isFilled", super.isFilled()?"1":"0");
        temp.put("color", super.getColor().getRGB() + "");
        temp.put("u", xDiameter+"");
        temp.put("v", yDiameter+"");
        return temp;
    }

    
    
    public void resize(int x, int y) {
        xDiameter=x;
        yDiameter=y;
        setcX(getX()+x/2);
        setcY(getY()+y/2);
    }

    public void resize(int x, int y, int u, int v) {
        resize(u, v);
        setX(x);
        setY(y);
    }
}
