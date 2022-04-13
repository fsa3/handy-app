package is.hi.handy_app.Services;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.handy_app.Entities.User;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class MessageService {
    private NetworkManager mNetworkManager;
    private Context mContext;

    public MessageService(Context context) {
        mContext = context;
        mNetworkManager = NetworkManager.getInstance(context);
    }

    public void getMyMessagedUsers(long userId, NetworkCallback<List<User>> callback) {
        mNetworkManager.sendRequest("/myMessages/" + userId, Request.Method.GET, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<User>>(){}.getType();
                List<User> resultUsers = gson.fromJson(result, listType);
                callback.onSuccess(resultUsers);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Failed to get my messages: " + errorString);
            }
        });
    }
}
