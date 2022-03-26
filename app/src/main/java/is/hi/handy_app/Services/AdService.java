package is.hi.handy_app.Services;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class AdService {
    NetworkManager mNetworkManager;

    public AdService(Context context) {
        mNetworkManager = NetworkManager.getInstance(context);
    }

    public void findAll(NetworkCallback<List<Ad>> callback) {
        mNetworkManager.sendRequest("/ads", Request.Method.GET, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Ad>>(){}.getType();
                List<Ad> ads = gson.fromJson(result, listType);
                callback.onSuccess(ads);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Failed to get advertisements: " + errorString);
            }
        });
    }
}
