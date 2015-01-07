package json;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import shapes.*;
import org.json.simple.*;

public class JsonWriter {

    private int RGB;
    private ArrayList<Figure> figures;
    private JSONObject obj;
    private ArrayList<JSONObject> objects;

    public JsonWriter(int RGB, ArrayList<Figure> figures, String filePath) {
        this.RGB = RGB;
        this.figures = figures;
        obj=new JSONObject();
        objects = new ArrayList<>();
        obj.put("Background Color", RGB+"");
        JSONArray fig = new JSONArray();
        for (Figure f : figures) {
            objects.add(f.toJson());
        }
        obj.put("figures", objects);
        try {
            FileWriter file = new FileWriter(filePath+".json");
            file.write(obj.toJSONString());
            file.flush();
            file.close();

        } catch (IOException e) {

        }
    }

}
