/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

/**
 *
 * @author Ahmed Nouh
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import org.json.simple.*;
public abstract class Figure implements Cloneable{

    private int x, y,cX,cY; // Coordinates
    private boolean filled, selected;
    private Color color;
    private Stroke[] strokes = new Stroke[]{new BasicStroke(1),new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
        BasicStroke.JOIN_MITER, 10.0f, new float[]{10.0f}, 0.0f)};
    public Figure(int x, int y, int cX,int cY,boolean filled, Color color) {
        this.x = x;
        this.y = y;
        this.cX = cX;
        this.cY = cY;
        this.filled = filled;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getcY() {
        return cY;
    }

    protected void setX(int x) {
        this.x = x;
    }

    protected void setY(int y) {
        this.y = y;
    }
    
    protected void setcY(int cY) {
        this.cY = cY;
    }

    protected void setcX(int cX) {
        this.cX = cX;
    }

    public int getcX() {
        return cX;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isFilled() {
        return filled;
    }

    public void fill() {
        filled = true;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    protected Stroke nullStroke(){
        return strokes[0];
    }
    protected Stroke dottedStroke(){
        return strokes[1];
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); //To change body of generated methods, choose Tools | Templates.
    }
    public  void move(int dx,int dy){
        // dx the new center pos  , dy the new center pos
        x+=dx-cX;
        y+=dy-cY;
        cX=dx;
        cY=dy;
        
    }

    
    public abstract void draw(Graphics2D graphics);
    public abstract boolean contains(Point point);
    public abstract JSONObject toJson();
    public  void resize(int x,int y){}
    public  void resize(int x,int y,int n){}
    public void resize(int x,int y,int u,int v){}
}