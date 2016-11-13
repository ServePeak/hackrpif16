package chupalika.pleasepickaplace;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by ipb on 11/12/2016.
 */
public class Requester {
    private static Requester instance;
    private RequestQueue queue;
    private static Context context;

    public static final String SERVERURL = "http://762ffcaf.ngrok.io";
    private String lastMessage = "";

    private Requester(Context c) {
        context = c;
        queue = getRequestQueue();
    }

    public static synchronized Requester getInstance(Context context) {
        if (instance == null) {
            instance = new Requester(context);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            queue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return queue;
    }

    public void addRequest(String url,final Callback callback) {
        //Request a String response the url
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            //called when a response is received
            @Override
            public void onResponse(String response) {
                System.out.println(response);
                setLastMessage(response);
                callback.callback(instance);
            }
        }, new Response.ErrorListener() {
            //called when there is an error on the request or no response
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Error on Request!");
                setLastMessage("Error on Request!");
                callback.callback(instance);
            }
        });

        //Add the request to RequestQueue
        queue.add(request);

    }

    //retrieves the last message, returns it, and resets it
    public String getLastMessage() {
        String ans = lastMessage;
        lastMessage = "";
        return ans;
    }

    private void setLastMessage(String s) {
        lastMessage = s;
    }
}
