package de.uniba.georacer.parsing;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

import de.uniba.georacer.model.Landmark;

public class LandmarkParser {
    public void parseLandmarks(Context context) {
        String fileContent = loadJSONFromAsset(context);

        Type listType = new TypeToken<List<Landmark>>() {
        }.getType();
        List<Landmark> landmarks = new Gson().fromJson(fileContent, listType);

        Collections.shuffle(landmarks);

        for(Landmark landmark : landmarks) {
            System.out.println("landmarks> " + landmark.getName() + " latitude " + landmark.getPosition().getLatitude());
        }


    }


    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("Landmarks.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }
    }




