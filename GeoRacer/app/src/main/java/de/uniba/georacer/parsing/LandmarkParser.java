package de.uniba.georacer.parsing;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.List;

import de.uniba.georacer.model.json.Landmark;

public class LandmarkParser {
    private List<Landmark> landmarks;

    public LandmarkParser(Context context) {
        landmarks = parseLandmarks(context);
    }

    public List<Landmark> getLandmarks() {
        return landmarks;
    }

    private List<Landmark> parseLandmarks(Context context) {
        String fileContent = loadJSONFromAsset(context);

        Type listType = new TypeToken<List<Landmark>>() {
        }.getType();
        List<Landmark> landmarks = new Gson().fromJson(fileContent, listType);

        for (Landmark landmark : landmarks) {
            System.out.println("landmarks> " + landmark.getName() + " latitude " + landmark.getPosition().getLatitude());
        }

        return landmarks;
    }

    private String loadJSONFromAsset(Context context) {
        String json;

        try {
            InputStream is = context.getAssets().open("Landmarks.json");
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }
}




