package chupalika.pleasepickaplace;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by ipb on 11/12/2016.
 */
public class Requester {
    private static Requester instance;
    private RequestQueue queue;
    private static Context context;

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

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}
