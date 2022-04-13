package is.hi.handy_app.Services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Entities.User;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class AdService {
    UserService mUserService;
    NetworkManager mNetworkManager;

    public AdService(Context context) {
        mNetworkManager = NetworkManager.getInstance(context);
        mUserService = new UserService(context);
    }

    public void findAll(String searchQuery, NetworkCallback<List<Ad>> callback) {
        Uri.Builder uri = Uri.parse("/ads").buildUpon();
        if (searchQuery != null) {
            uri.appendQueryParameter("search", searchQuery);
        }
        mNetworkManager.sendRequest(uri.build().toString(), Request.Method.GET, new NetworkCallback<String>() {
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

    public void saveAd(Ad ad, byte[] image, NetworkCallback<Ad> callback) {
        JSONObject body = new JSONObject();
        try {
            body.put("title", ad.getTitle());
            body.put("description", ad.getDescription());
            body.put("location", ad.getLocation());
            body.put("trade", ad.getTrade().toString());
            body.put("user", mUserService.getLoggedInUserId());
            JSONArray imageBytes = new JSONArray(image);
            body.put("imageBytes", imageBytes);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mNetworkManager.sendRequestWithBody("/createad", Request.Method.POST, body, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type adType = new TypeToken<Ad>(){}.getType();
                Ad savedAd = gson.fromJson(result, adType);
                callback.onSuccess(savedAd);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Ad not saved: " + errorString);
            }
        });
    }

    public void deleteAd(Ad ad, NetworkCallback<Ad> callback) {
        mNetworkManager.sendRequest("/ads/" + ad.getID(), Request.Method.DELETE, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(null);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Error deleting Ad: " + errorString);
            }
        });
    }
}
