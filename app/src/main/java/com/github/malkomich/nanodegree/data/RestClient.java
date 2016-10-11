package com.github.malkomich.nanodegree.data;

import android.net.Uri;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Generic REST client to retrieve data from web services.
 */
public abstract class RestClient {

    private String baseUri;
    protected JSONObject data;

    protected RestClient(String baseUri) {
        this.baseUri = baseUri;
    }

    /**
     * Check if domain objects has been set.
     *
     * @return Success status
     */
    protected boolean apiSuccess() {
        return data != null;
    };

    /**
     * Joins the base url of the web service with the required params.
     *
     * @param resource
     *              Resource of the web service which is being requested
     * @param paramNames
     *              Names of the params sent to build the URL
     * @param paramValues
     *              Values of the params sent to build the URL
     * @return Service URL
     */
    protected URL buildURL(String resource, String[] paramNames, String[] paramValues, int numParams) {

        String baseUri = resource != null ? this.baseUri + "/" + resource : this.baseUri;
        baseUri += numParams > 0 ? "?" : "";

        Uri.Builder uriBuilder = Uri.parse(baseUri)
            .buildUpon();
        for(int i=0; i < numParams; i++) {
            uriBuilder.appendQueryParameter(paramNames[i], paramValues[i]);
        }

        try {
            return new URL(uriBuilder.build()
                .toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Calls the REST API to retrieve the String data, and then calls to the
     * parser.
     *
     * @param url
     *            Service URL
     */
    protected void doRequest(URL url) {

        StringBuilder output = new StringBuilder();

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");

            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            String line;
            while ((line = br.readLine()) != null) {
                output.append(line);
            }

            conn.disconnect();

        } catch (IOException e) {

            e.printStackTrace();

        }

        parseOutput(output.toString());
    }

    /**
     * Parse a String well JSON-formed to a JSON object.
     *
     * @param output
     *            Service Data result
     */
    private void parseOutput(String output) {

        try {
            data = new JSONObject(output);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
