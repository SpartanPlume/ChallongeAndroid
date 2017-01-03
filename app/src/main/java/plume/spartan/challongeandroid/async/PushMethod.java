package plume.spartan.challongeandroid.async;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import plume.spartan.challongeandroid.global.MyApplication;

/**
 * Created by charpe_r on 15/11/16.
 */

public class PushMethod extends AsyncTask<URL, Void, String> {

    public interface PushMethodResponse {
        void processFinish(String output, int responseCode);
    }

    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    private MyApplication myApplication;
    private String requestMethod;
    private Map<String, String> parameters;
    private PushMethodResponse delegate;
    private int responseCode = 0;

    public PushMethod(Context context, String requestMethod) {
        this(context, requestMethod, null, null);
    }

    public PushMethod(Context context, String requestMethod, Map<String, String> parameters) {
        this(context, requestMethod, parameters, null);
    }

    public PushMethod(Context context, String requestMethod, Map<String, String> parameters, PushMethodResponse delegate) {
        this.myApplication = (MyApplication) context;
        this.requestMethod = requestMethod;
        this.parameters = parameters;
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
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestMethod(requestMethod);

                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
                    String toWrite = "api_key=" + myApplication.getApiKey();
                    if (parameters != null && parameters.size() > 0) {
                        for (Map.Entry<String, String> entry : parameters.entrySet()) {
                            toWrite += "&" + entry.getKey() + "=" + entry.getValue();
                        }
                    }
                    wr.write(toWrite);
                    wr.flush();

                    responseCode = urlConnection.getResponseCode();
                    if (responseCode != 422 && responseCode != 401) {
                        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                        BufferedReader r = new BufferedReader(new InputStreamReader(in));
                        total = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            total.append(line).append('\n');
                        }
                    } else {
                        InputStream in = new BufferedInputStream(urlConnection.getErrorStream());
                        BufferedReader r = new BufferedReader(new InputStreamReader(in));
                        total = new StringBuilder();
                        String line;
                        while ((line = r.readLine()) != null) {
                            total.append(line).append('\n');
                        }
                    }
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
        if (delegate != null) {
            delegate.processFinish(string, responseCode);
        } else if (string != null) {
            System.out.println(string);
        }
    }
}
