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

public class GetMethod extends AsyncTask<URL, Void, StringBuilder> {

    public interface GetMethodResponse {
        void processFinish(String output, boolean connectionError);
    }

    private boolean connectionError = false;
    private GetMethodResponse delegate = null;
    private MyApplication myApplication;

    public GetMethod(GetMethodResponse delegate, Context context) {
        this.delegate = delegate;
        this.myApplication = (MyApplication) context;
    }

    protected StringBuilder doInBackground(URL... urls) {
        URL url = urls[0];
        StringBuilder total = null;

        if (url != null) {
            HttpsURLConnection urlConnection = null;
            try {
                urlConnection = (HttpsURLConnection) url.openConnection();
            } catch (UnknownHostException e) {
                e.printStackTrace();
                connectionError = true;
            } catch (IOException e) {
                e.printStackTrace();
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
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    connectionError = true;
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
            }
        }

        return total;
    }

    @Override
    protected void onPostExecute(StringBuilder stringBuilder) {
        if (stringBuilder != null)
            delegate.processFinish(stringBuilder.toString(), connectionError);
        else
            delegate.processFinish(null, connectionError);
    }

}
