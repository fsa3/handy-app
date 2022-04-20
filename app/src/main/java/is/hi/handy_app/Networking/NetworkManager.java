package is.hi.handy_app.Networking;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class NetworkManager {

    private static final String BASE_URL = "http://10.0.2.2:8080/api";
    //private static final String BASE_URL = "http://handlaginn.herokuapp.com/api";

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
        StringRequest request = new StringRequest(method, BASE_URL + url, callback::onSuccess, error -> {
            Log.d(TAG, error.toString());
            callback.onaFailure(error.toString());
        });
        sQueue.add(request);
    }

    public void sendRequestWithBody(String url, int method, JSONObject body, NetworkCallback<String> callback) {
        JsonObjectRequest request = new JsonObjectRequest(method, BASE_URL + url, body,
                response -> callback.onSuccess(response.toString()), error -> {
                    VolleyLog.d("JSONPost", "Error: " + error.getMessage());
                    callback.onaFailure(error.getMessage());
                });
        sQueue.add(request);
    }
}
