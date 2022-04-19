package is.hi.handy_app.Services;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.handy_app.Entities.Message;
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

    public void getMessagesBetweenUsers(long userId1, long userId2, NetworkCallback<List<Message>> callback) {
        mNetworkManager.sendRequest("/messages-between/" + userId1 + "/" + userId2, Request.Method.GET, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Message>>(){}.getType();
                List<Message> messages = gson.fromJson(result, listType);
                callback.onSuccess(messages);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Failed to get messages: " + errorString);
            }
        });
    }

    public void sendMessage(long senderId, long recipientId, String content, NetworkCallback<Message> callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("senderId", senderId);
            body.put("recipientId", recipientId);
            body.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mNetworkManager.sendRequestWithBody("/message", Request.Method.POST, body, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type messageType = new TypeToken<Message>(){}.getType();
                Message sentMessage = gson.fromJson(result, messageType);
                callback.onSuccess(sentMessage);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Failed to send message: " + errorString);
            }
        });
    }
}
