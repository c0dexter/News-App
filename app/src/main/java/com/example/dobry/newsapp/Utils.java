package com.example.dobry.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by dobry on 04.07.17.
 */

public class Utils {
    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = Utils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link Utils} object.
     */
    private Utils() {

    }

    /**
     * Return an {@link News} object by parsing out information
     * about the first news from the input newsJSON string.
     */
    private static List<News> extractNewsFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding newses to
        List<News> newses = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            //TODO: zrobić poprawne parsowanie
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            Log.println(Log.INFO, LOG_TAG, newsJSON);

            // Extract the JSONArray associated with the key called "items",
            // which represents a list of newses.
            JSONArray newsesArray = baseJsonResponse.getJSONArray("items");
            Log.println(Log.INFO, LOG_TAG, String.valueOf(newsesArray));

            // For each news in the newsesArray, create an {@link News} object
            for (int i = 0; i < newsesArray.length(); i++) {

                // Get a single news at position i within the list of items (newses)
                JSONObject currentNews = newsesArray.getJSONObject(i);
                Log.println(Log.INFO, LOG_TAG, String.valueOf(currentNews));

                // For a given news, extract the JSONObject associated with the
                // key called "volumeInfo", which represents a list of all properties
                // for that news. + [authors] list
                JSONObject volumeInfo = currentNews.getJSONObject("volumeInfo");

                // Extract the value for the key called "author"
                String author;

                // Check if JSONArray exist
                if (volumeInfo.has("authors")) {
                    JSONArray authors = volumeInfo.getJSONArray("authors");
                    Log.println(Log.INFO, LOG_TAG, String.valueOf(authors));

                    // Check JSONArray Returns true if this object has no mapping for name or if it has a mapping whose value is NULL
                    if (!volumeInfo.isNull("authors")) {
                        // Get 1st element
                        author = (String) authors.get(0);
                    } else {
                        // assign info about missing info about author
                        author = "*** unknown author ***";
                    }
                } else {
                    // assign info about missing info about author
                    author = "*** missing info of authors ***";
                }

                // For a given news, extract the JSONObject associated with the
                // key called "imageLinks", which represents a list of all cover
                // images in a different size
                JSONObject imageLinks = volumeInfo.getJSONObject("imageLinks");
                Log.println(Log.INFO, LOG_TAG, String.valueOf(imageLinks));

                // For a given news, extract the JSONObject associated with the
                // key called "saleInfo", which represents a list of region and object RetailPrice{amount, currency}
                JSONObject saleInfo = currentNews.getJSONObject("saleInfo");
                JSONObject retailPrice = saleInfo.getJSONObject("retailPrice");


                // Extract the value for the key called "title"
                String title = volumeInfo.getString("title");

                // Extract the value for the key called "language"
                String language = volumeInfo.getString("language");

                // Extract String URL of specific cover
                String coverImageUrl = imageLinks.getString("smallThumbnail");

                // Extract the value for the key called "smallThumbnail"
                // Using REGEX and StringBuilder
                StringBuilder stringBuilder = new StringBuilder();

                Pattern p = Pattern.compile("id=(.*?)&");
                Matcher m = p.matcher(coverImageUrl);
                if (m.matches()) {
                    String id = m.group(1);
                    coverImageUrl = String.valueOf(stringBuilder.append("https://books.google.com/books/content/images/frontcover/").append(id).append("?fife=w300"));
                } else {
                    Log.i(LOG_TAG, "Issue with cover");
                }

                // Extract the value for the key called "amount"
                double amount = retailPrice.getDouble("amount");

                // Extract the value for the key called "currencyCode"
                String currency = retailPrice.getString("currencyCode");


                // Extract the value for the key called "buyLink"
                String buyLink = (String) saleInfo.get("buyLink");

                // Create a new {@link News} object with the title, author, coverImageUrl, price, currency and language
                // and url from the JSON response.
                // TODO: zmienić mapowanie, dodać poprawną konstrukcję obiektu
                News newsItem = new News(title, POOOOOOOOOOOOOPRAWWWWWWWWWWWWWWWWWWWWWWWWWWWWWWW TOOOOOOOOOO);

                // Add the new {@link News} to the list of newsesList.
                newses.add(newsItem);

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e(LOG_TAG, "Problem parsing the news JSON results", e);
        }

        // Return the list of newses (newsesList)
        return newses;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {

        // To avoid "magic numbers" in code, all numeric values mustn't been used directly in a code
        final int READ_TIMEOUT = 10000;
        final int CONNECT_TIMEOUT = 15000;
        final int CORRECT_RESPONSE_CODE = 200;

        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT /* milliseconds */);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == CORRECT_RESPONSE_CODE) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Query the USGS dataset and return a list of {@link News} objects.
     */
    static List<News> fetchNewsData(String requestUrl) {

        final int SLEEP_TIME_MILLIS = 2000;

        // This action with sleeping is required for displaying circle progress bar
        try {
            Thread.sleep(SLEEP_TIME_MILLIS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
            Log.i(LOG_TAG, "HTTP request: OK");
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extract relevant fields from the JSON response and create a list of {@link News}s
        List<News> listNewses = extractNewsFromJson(jsonResponse);

        // Return the list of {@link News}s
        return listNewses;
    }

}