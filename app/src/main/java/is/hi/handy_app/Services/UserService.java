package is.hi.handy_app.Services;

import android.content.Context;
import android.net.Uri;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class UserService {
    private NetworkManager mNetworkManager;

    public UserService(Context context) {
        mNetworkManager = NetworkManager.getInstance(context);
    }

    public void findAllHandyUsers(String name, String trade, NetworkCallback<List<HandyUser>> callback) {
        Uri.Builder uri = Uri.parse("/handymen").buildUpon();
        if (name != null) {
            uri.appendQueryParameter("name", name);
        }
        if (trade != null) {
            uri.appendQueryParameter("trade", trade);
        }
        mNetworkManager.sendRequest(uri.build().toString(), Request.Method.GET, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<HandyUser>>(){}.getType();
                List<HandyUser> handyUsers = gson.fromJson(result, listType);
                callback.onSuccess(handyUsers);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Failed to get Handymen: " + errorString);
            }
        });
    }
}
