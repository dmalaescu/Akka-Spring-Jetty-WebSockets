package com.examples.web;

/**
 * Created with IntelliJ IDEA.
 * User: mala
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Random;

import com.examples.akka.service.ImageService;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;      // JSON library from http://www.json.org/java/
import org.json.JSONObject;

public class GoogleQuery implements ImageService{

    // Put your website here
    private final String HTTP_REFERER = "http://www.example.com/";

    private String makeQuery(String query) {

        System.out.println("\nQuerying for " + query);

        try
        {
            // Convert spaces to +, etc. to make a valid URL
            query = URLEncoder.encode(query, "UTF-8");

            int startWith = (new Random()).nextInt(20);
            URL url = new URL("http://ajax.googleapis.com/ajax/services/search/images?start=" + startWith + "&rsz=small&v=1.0&q=" + query);
            URLConnection connection = url.openConnection();
            connection.addRequestProperty("Referer", HTTP_REFERER);

            // Get the JSON response
            String line;
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String response = builder.toString();
            JSONObject json = new JSONObject(response);

            System.out.println("Total results = " +
                    json.getJSONObject("responseData")
                            .getJSONObject("cursor")
                            .getString("estimatedResultCount"));

            JSONArray ja = json.getJSONObject("responseData")
                    .getJSONArray("results");

//            for (int i = 0; i < ja.length(); i++) {
//                System.out.print((i+1) + ". ");
//                JSONObject j = ja.getJSONObject(i);
//                System.out.println(j.getString("titleNoFormatting"));
//                System.out.println(j.getString("url"));
//            }

            return ja.getJSONObject((new Random()).nextInt(3)).getString("url");
        }
        catch (Exception e) {
            System.err.println("Something went wrong...");
            e.printStackTrace();
        }

        return "";
    }

    public static void main(String args[]) {
        GoogleQuery googleQuery = new GoogleQuery();
        System.out.println(googleQuery.makeQuery("cat"));
        byte[] donwload = googleQuery.downloadImage("cat");
        System.out.println("done");
    }

    @Override
    public byte[] downloadImage(String image) {
        try {
            URL url = new URL(makeQuery(image));
            return IOUtils.toByteArray(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
