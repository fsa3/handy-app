package is.hi.handy_app.Services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Entities.User;
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

    public void login(String email, String password, NetworkCallback<User> callback) {
        Map<String, String> body = new HashMap<String, String>();
        body.put("email", email);
        body.put("password", password);
        Log.d(TAG, body.toString());
        mNetworkManager.sendRequestWithBody("/login", Request.Method.POST, body, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type userType = new TypeToken<User>(){}.getType();
                User loggedInUser = gson.fromJson(result, userType);
                callback.onSuccess(loggedInUser);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Login not successful: " + errorString);
            }
        });
    }
}
