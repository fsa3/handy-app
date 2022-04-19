package is.hi.handy_app.Services;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;


import is.hi.handy_app.Entities.PortfolioItem;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class PortfolioItemService {
    UserService mUserService;
    NetworkManager mNetworkManager;

    public PortfolioItemService(Context context) {
        mNetworkManager = NetworkManager.getInstance(context);
        mUserService = new UserService(context);
    }

    public void getUserPortfolioItems(long userId, NetworkCallback<List<PortfolioItem>> callback) {
        mNetworkManager.sendRequest("/portfolioItem/" + userId, Request.Method.GET, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<PortfolioItem>>(){}.getType();
                List<PortfolioItem> portfolioItems = gson.fromJson(result, listType);
                callback.onSuccess(portfolioItems);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Failed to get portfolio: " + errorString);
            }
        });
    }

    public void savePortfolioItem(PortfolioItem item, byte[] image, NetworkCallback<PortfolioItem> callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("title", item.getTitle());
            body.put("location", item.getLocation());
            body.put("description", item.getDescription());
            body.put("user", mUserService.getLoggedInUserId());
            JSONArray imageBytes = new JSONArray(image);
            body.put("imageBytes", imageBytes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mNetworkManager.sendRequestWithBody("/createPortfolioItem", Request.Method.POST, body, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type portfolioItemType = new TypeToken<PortfolioItem>(){}.getType();
                PortfolioItem savedItem = gson.fromJson(result, portfolioItemType);
                callback.onSuccess(savedItem);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Portfolio item not saved: " + errorString);
            }
        });

    }

    public void deletePortfolioItem(PortfolioItem item, NetworkCallback<PortfolioItem> callback) {
        mNetworkManager.sendRequest("portfolioItem" + item.getID(), Request.Method.DELETE, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(null);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Error deleting Portfolio item: " + errorString);
            }
        });
    }
}
