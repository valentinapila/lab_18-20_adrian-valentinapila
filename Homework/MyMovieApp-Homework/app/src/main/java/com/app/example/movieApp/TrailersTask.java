package com.app.example.movieApp;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TrailersTask extends AsyncTask<Long, Void, List<Trailers>> {

    public static String LOG_TAG = TrailersTask.class.getSimpleName();

    private final Listener mListener;

    public TrailersTask(Listener mListener) {
        this.mListener = mListener;
    }

    interface Listener {
        void onLoadFinished(List<Trailers> trailers);
    }

    @Override
    protected List<Trailers> doInBackground(Long... params) {
        List<Trailers> trailers = new ArrayList<>();
        if (params.length == 0) {
            return null;
        }
        long movieId = params[0];
            try {

                URL url;
                url = new URL("http://api.themoviedb.org/3/movie/" + movieId + "/videos?api_key=" + BuildConfig.API_KEY);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                String results = IOUtils.toString(inputStream);
                parseJson(results,trailers);
                inputStream.close();
                return trailers;
            } catch (IOException e) {
                e.printStackTrace();
            }


        return null;
    }

    public static void parseJson(String data, List<Trailers> list){
        Trailers trailers = new Trailers();

        try {
            JSONObject mainObject = new JSONObject(data);
            JSONArray resArray = mainObject.getJSONArray("results");
            for (int i = 0; i < resArray.length(); i++) {
                JSONObject jsonObject = resArray.getJSONObject(i);
                trailers.setmId(jsonObject.getString("id"));
                trailers.setmKey(jsonObject.getString("key"));
                trailers.setmName(jsonObject.getString("name"));
                trailers.setmSite(jsonObject.getString("site"));
                trailers.setmSize(jsonObject.getString("size"));
                list.add(trailers);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Error occurred during JSON Parsing");
        }

    }

    @Override
    protected void onPostExecute(List<Trailers> trailers) {
        if (trailers != null) {
            mListener.onLoadFinished(trailers);
        } else {
            mListener.onLoadFinished(new ArrayList<Trailers>());
        }
    }


    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }
}
