/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sau.hw;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static sau.hw.ai.agents.DatabaseAgent.DATABASE_FILE;

/**
 *
 * @author Danish Kumar
 */
public class JsonTests {

    public static final String DATABASE_FILE = "./rsc/data/activities.json";
    private JSONObject database;

    @Test
    public void testReadJson() {
        try {
            String string = FileUtils.readFileToString(new File(DATABASE_FILE), "utf-8");
//            System.out.println(string);

            database = new JSONObject(string);
            System.out.println(database);
//            JSONArray arrayWeather = database.getJSONArray("weather");
//            for(int i = 0; i < arrayWeather.length(); i++) {
//                JSONObject item = arrayWeather.getJSONObject(i);
////                item.get
//            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
