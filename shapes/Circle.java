/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package shapes;

import java.awt.Color;

/**
 *
 * @author chicco
 */
public class Circle extends Ellipse{
    public Circle(int x,int y,int diameter,boolean filled,Color fillColor){
        super(x,y,diameter,diameter,filled,fillColor);
    }

    @Override
    public String toString() {
        return "circle";
    }

    public void resize(int x, int y) {
        super.resize(x, x);
    }

    @Override
    public void resize(int x, int y, int u, int v) {
        super.resize(x, y, u, u); //To change body of generated methods, choose Tools | Templates.
    }
    
}
