package plume.spartan.challongeandroid.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

import plume.spartan.challongeandroid.global.MyApplication;

/**
 * Created by charpe_r on 29/10/16.
 */

public class GetMethod extends AsyncTask<URL, Void, String> {

    public interface GetMethodResponse {
        void processFinish(String output, int responseCode);
    }

    private MyApplication myApplication;
    private GetMethodResponse delegate = null;
    private int responseCode = 0;

    public GetMethod(Context context, GetMethodResponse delegate) {
        this.myApplication = (MyApplication) context;
        this.delegate = delegate;
    }

    protected String doInBackground(URL... urls) {
        URL url = urls[0];
        StringBuilder total = null;

        if (url != null) {
            HttpsURLConnection urlConnection = null;
            try {
                urlConnection = (HttpsURLConnection) url.openConnection();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                responseCode = 404;
            } catch (IOException e) {
                e.printStackTrace();
                responseCode = 401;
            }
            if (urlConnection != null) {
                try {
                    urlConnection = (HttpsURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((myApplication.getUsername() + ":" + myApplication.getApiKey()).getBytes("UTF-8"), Base64.DEFAULT));
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    BufferedReader r = new BufferedReader(new InputStreamReader(in));
                    total = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        total.append(line).append('\n');
                    }
                    responseCode = urlConnection.getResponseCode();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    responseCode = 404;
                } catch (IOException e) {
                    e.printStackTrace();
                    responseCode = 401;
                } finally {
                    urlConnection.disconnect();
                }
            }
        }

        if (total != null)
            return (total.toString());
        return (null);
    }

    @Override
    protected void onPostExecute(String string) {
        delegate.processFinish(string, responseCode);
    }

}
