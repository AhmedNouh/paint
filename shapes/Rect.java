/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import org.json.simple.JSONObject;

/**
 *
 * @author chicco
 */
public class Rect extends Figure {

    private  int width, height;

    public Rect(int x, int y, int width, int height, boolean filled, Color fillColor) {
        super(x, y,x+width/2,y+height/2, filled, fillColor);
        this.width = width;
        this.height = height;
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
        graphics.drawRect(super.getX(), super.getY(), width, height); // draw outter shape
        //fill shape
        if (super.isFilled()) {
            graphics.setColor(super.getColor());
            graphics.fillRect(super.getX(), super.getY(), width, height);
        }

    }

    @Override
    public boolean contains(Point point) {
        return super.getX() <= point.x && point.x - super.getX() <= width && super.getY() <= point.y && point.y - super.getY() <= height;
    }

    @Override
    public JSONObject toJson() {
        JSONObject temp = new JSONObject();
        temp.put("class","rect");
        temp.put("x", super.getX() + "");
        temp.put("y", super.getY() + "");
        temp.put("isFilled", super.isFilled()?"1":"0");
        temp.put("color", super.getColor().getRGB() + "");
        temp.put("u", width+"");
        temp.put("v", height+"");
        return temp;
    }

    public void resize(int x, int y) {
        width=x;
        height=y;
        setcX(getX()+x/2);
        setcY(getY()+y/2);
    }

    public void resize(int x, int y, int u, int v) {
        resize(u, v);
        setX(x);
        setY(y);
        
    }
    
    
}
