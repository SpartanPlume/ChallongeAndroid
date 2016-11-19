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

public class PushMethod extends AsyncTask<URL, Void, StringBuilder> {

    public interface PushMethodResponse {
        void processFinish(String output, boolean connectionError);
    }

    public static String POST = "POST";
    public static String PUT = "PUT";
    public static String DELETE = "DELETE";

    private PushMethodResponse delegate;
    private MyApplication myApplication;
    private String requestMethod;
    private Map<String, String> parameters;
    private boolean connectionError;

    public PushMethod(Context context, String requestMethod) {
        this(null, context, requestMethod, null);
    }

    public PushMethod(Context context, String requestMethod, Map<String, String> parameters) {
        this(null, context, requestMethod, parameters);
    }

    public PushMethod(PushMethodResponse delegate, Context context, String requestMethod, Map<String, String> parameters) {
        this.delegate = delegate;
        this.myApplication = (MyApplication) context;
        this.requestMethod = requestMethod;
        this.parameters = parameters;
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

                    int resp = urlConnection.getResponseCode();
                    if (resp != 422 && resp != 401) {
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
        if (stringBuilder != null && delegate != null) {
            delegate.processFinish(stringBuilder.toString(), connectionError);
        } else if (delegate != null) {
            delegate.processFinish(null, connectionError);
        } else if (stringBuilder != null) {
            System.out.println(stringBuilder.toString());
        }
    }
}
