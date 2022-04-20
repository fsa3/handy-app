package is.hi.handy_app.Services;

import android.content.Context;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Entities.Review;
import is.hi.handy_app.Entities.User;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class ReviewService {
    private final NetworkManager mNetworkManager;

    public ReviewService(Context context){
        mNetworkManager = NetworkManager.getInstance(context);
    }

    public void getMyReviews(long userId, NetworkCallback <List<Review>> callback){
        mNetworkManager.sendRequest("/reviews/" + userId, Request.Method.GET, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Review>>(){}.getType();
                List<Review> reviews = gson.fromJson(result, listType);
                callback.onSuccess(reviews);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Failed to get Reviews: " + errorString);
            }
        });
    }

    public void getMyWrittenReviews(long userId, NetworkCallback <List<Review>> callback){
        mNetworkManager.sendRequest("/reviews-written/" + userId, Request.Method.GET, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<Review>>(){}.getType();
                List<Review> reviews = gson.fromJson(result, listType);
                callback.onSuccess(reviews);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Failed to get Reviews: " + errorString);
            }
        });
    }

    public void saveReview (long authorID, Long handyUserID, int rating, String review, NetworkCallback<Review> callback){
        JSONObject body = new JSONObject();
        try {
            body.put("text", review );
            body.put("rating",rating);
            body.put("author", authorID);
            body.put("handyman",handyUserID);
        }catch (JSONException e){
            e.printStackTrace();
        }
        mNetworkManager.sendRequestWithBody("/reviews", Request.Method.POST, body, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type reviewType = new TypeToken<Review>(){}.getType();
                Review saveReview = gson.fromJson(result,reviewType);
                callback.onSuccess(saveReview);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Review not saved" + errorString);
            }
        });
    }

    public void deleteReview(Review review, NetworkCallback<Review> callback){
        mNetworkManager.sendRequest("/reviews/" + review.getID(), Request.Method.DELETE, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                callback.onSuccess(null);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Error deleting review" + errorString);
            }
        });

    }




}
