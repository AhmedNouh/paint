/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import java.awt.Color;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import shapes.*;

/**
 *
 * @author Ahmed Nouh
 */
public class JsonReader {

    private ArrayList<Figure> figures;
    private JSONParser parser;
    private int rgb;
    private String temp;

    public JsonReader(ArrayList<Figure> figures, String filePath) {
        this.figures = figures;
        figures.clear();
        parser = new JSONParser();

        try {

            Object obj = parser.parse(new FileReader(filePath));

            JSONObject object = (JSONObject) obj;

            temp = (String) object.get("Background Color");
            rgb = Integer.parseInt(temp);
            // loop array
            JSONArray objects = (JSONArray) object.get("figures");
            Iterator<JSONObject> iter = objects.iterator();
            while (iter.hasNext()) {
                JSONObject j = iter.next();
                String theClass,x,y,isFilled,color,u,v;
                theClass=(String) j.get("class");
                x=(String) j.get("x");
                y=(String) j.get("y");
                isFilled=(String) j.get("isFilled");
                color=(String) j.get("color");
                u=(String) j.get("u");
                v=(String) j.get("v");
                boolean filled= isFilled.equals("1");
                Figure f;
                if(theClass.equals("ellipse")){
                    f=new Ellipse(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(u), Integer.parseInt(v), filled, new Color(Integer.parseInt(color)));
                }
                else if(theClass.equals("rect")){
                    f=new Rect(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(u), Integer.parseInt(v), filled, new Color(Integer.parseInt(color)));
                }
                else if(theClass.equals("tri")){
                    String[] t1= u.split("\\s+");
                    String[] t2=v.split("\\s+");
                    int n = t1.length;
                    int[] a= new int[n];
                    int [] b =new int[n];
                    for(int i=0;i<n;i++){
                        a[i]=Integer.parseInt(t1[i]);
                        b[i]=Integer.parseInt(t2[i]);
                    }
                    // edit if i add more polyg
                    f=new Triangle(a, b, filled, new Color(Integer.parseInt(color))) ;
                }
                else {
                    f=new Line(Integer.parseInt(x), Integer.parseInt(y), Integer.parseInt(u), Integer.parseInt(v), new Color(Integer.parseInt(color)));
                }
                figures.add(f);
            }

        } catch (FileNotFoundException e) {
            System.out.println("error");
        } catch (IOException | ParseException e) {

            System.out.println(e.getClass());
        }

    }
    public int getRgb() {
        return rgb;
    }

}
