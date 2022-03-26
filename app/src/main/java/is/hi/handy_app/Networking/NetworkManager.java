package is.hi.handy_app.Networking;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Entities.HandyUser;

public class NetworkManager {

    private static final String BASE_URL = "http://10.0.2.2:8080/api";

    @SuppressLint("StaticFieldLeak")
    private static NetworkManager sInstance;
    private static RequestQueue sQueue;
    private final Context mContext;

    public static synchronized NetworkManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NetworkManager(context);
        }
        return sInstance;
    }

    private NetworkManager(Context context) {
        mContext = context;
        sQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (sQueue == null) {
            sQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return sQueue;
    }

    public void sendRequest(String url, int method, NetworkCallback<String> callback) {
        StringRequest request = new StringRequest(method, BASE_URL + url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                callback.onaFailure(error.toString());
            }
        });
        sQueue.add(request);
    }
}
