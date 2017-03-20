package com.example.labdesenvolvimento.integracaoomdb;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Util {


    public static String webToString(InputStream inputStream) {
        InputStream localStream = inputStream;
        String localString = "";
        Writer writer = new StringWriter();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(localStream, "UTF-8"));
            String line = reader.readLine();
            while (line != null) {
                writer.write(line);
                line = reader.readLine();
            }
            localString = writer.toString();
            writer.close();
            reader.close();
            localStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return localString;
    }

    public static boolean isValidResponse(String response) throws JSONException {
        try{
            return !new JSONObject(response).getString("Response").equals("True");
        } catch (Exception e){
            throw e;
        }
    }

    public static Filme convertJSONtoFilme(String jsonFile){
        Filme filme = null;
        try {
            filme = new Filme();

            JSONObject mainObject = new JSONObject(jsonFile);
            filme.setTitulo(mainObject.getString("Title"));
            filme.setAno(mainObject.getString("Year"));
            filme.setDiretor(mainObject.getString("Director"));
            filme.setGenero(mainObject.getString("Genre"));
            filme.setDuracao(mainObject.getString("Runtime"));
            filme.setNota(mainObject.getString("imdbRating"));
            filme.setLinkPoster(mainObject.getString("Poster"));
            filme.setSinopse(mainObject.getString("Plot"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return filme;
    }

    public static Bitmap downloadImage(URL url) throws IOException {
        InputStream inputStream;
        Bitmap image;

        image = BitmapFactory.decodeStream(url.openStream());

        url.openStream().close();

        return image;
    }
}
