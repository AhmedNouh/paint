/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import org.json.simple.JSONObject;

/**
 *
 * @author chicco
 */
public class Triangle extends Figure {

    private Polygon tri;

    public Triangle(int[] x, int[] y, boolean filled, Color color) {
        super(Math.min(x[0], Math.min(x[1], x[2])), Math.min(y[0], Math.min(y[1], y[2])),(x[0] + x[1] + x[2]) / 3,(y[0] + y[1] + y[2]) / 3, filled, color);
        tri = new Polygon(x, y, x.length);

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
        graphics.drawPolygon(tri); // draw outter shape
        //fill shape
        if (super.isFilled()) {
            graphics.setColor(super.getColor());
            graphics.fillPolygon(tri);
        }
    }

    @Override
    public boolean contains(Point point) {
        return tri.contains(point);
    }

    @Override
    public String toString() {
        return "Triangle";
    }

    @Override
    public JSONObject toJson() {
        JSONObject temp = new JSONObject();
        temp.put("class", "tri");
        temp.put("x", super.getX() + "");
        temp.put("y", super.getY() + "");
        temp.put("isFilled", super.isFilled() ? "1" : "0");
        temp.put("color", super.getColor().getRGB() + "");
        temp.put("u", this.axisToJ(tri.xpoints) + "");
        temp.put("v", this.axisToJ(tri.ypoints) + "");
        return temp;
    }

    private String axisToJ(int[] x) {
        String temp = "";
        for (int i : x) {
            temp += i + " ";
        }
        return temp;
    }

    public Polygon getPol() {
        return tri;
    }

    @Override
    public void move(int dx, int dy) {
        int xc=getcX(),yc=getcY();
        int[] x = tri.xpoints;
        int[] y = tri.ypoints;
        int[] nx = {dx + x[0]-xc, dx + x[1]-xc , dx + x[2]-xc};
        int[] ny = {dy + y[0]-yc, dy + y[1]-yc, dy + y[2]-yc};
        setcX(dx);
        setcY(dy);
        setX(Math.min(nx[0], Math.min(nx[1], nx[2])));
        setY(Math.min(ny[0], Math.min(ny[1], ny[2])));
        tri = new Polygon(nx, ny, 3);
    }

    public void resize(int x,int y,int n){
        int[] nx = tri.xpoints;
        int[] ny = tri.ypoints;
        nx[n]=x;
        ny[n]=y;
        setcX((nx[0]+nx[1]+nx[2])/3);
        setcY((ny[0]+ny[1]+ny[2])/3);
        setX(Math.min(nx[0], Math.min(nx[1], nx[2])));
        setY(Math.min(ny[0], Math.min(ny[1], ny[2])));
        tri=new Polygon(nx, ny, 3);
    }

}
