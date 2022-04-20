package is.hi.handy_app.Services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Entities.Trade;
import is.hi.handy_app.Entities.User;
import is.hi.handy_app.MainActivity;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class UserService {
    public static final String USER_ID = "logged-in-user-id";
    public static final String USER_NAME = "logged-in-user-name";
    public static final String USER_EMAIL = "logged-in-user-email";
    public static final String USER_TRADE = "logged-in-user-trade";
    public static final String HANDYUSER_LOGGEDIN = "handy-user-logged-in";

    private final NetworkManager mNetworkManager;
    private final SharedPreferences mSharedPreferences;


    public UserService(Context context) {
        mNetworkManager = NetworkManager.getInstance(context);
        mSharedPreferences = context.getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
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
        JSONObject body = new JSONObject();
        try {
            body.put("email", email);
            body.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, body.toString());
        mNetworkManager.sendRequestWithBody("/login", Request.Method.POST, body, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type userType = new TypeToken<User>(){}.getType();
                Type handyUserType = new TypeToken<HandyUser>(){}.getType();
                User loggedInUser = gson.fromJson(result, userType);
                Log.d("eh logged in", loggedInUser.getID() + "");
                HandyUser handyUser = gson.fromJson(result, handyUserType);
                saveLoggedInUser(loggedInUser, handyUser.getTrade() != null, handyUser.getTrade());
                callback.onSuccess(loggedInUser);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Login not successful: " + errorString);
            }
        });
    }

    public void getUser(long id, NetworkCallback<User> callback) {
        mNetworkManager.sendRequest("/user/" + id, Request.Method.GET, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type userType = new TypeToken<User>(){}.getType();
                User user = gson.fromJson(result, userType);
                callback.onSuccess(user);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Unable to get user: " + errorString);
            }
        });
    }

    public void getHandyUser(long id, NetworkCallback<HandyUser> callback) {
        mNetworkManager.sendRequest("/handyUser/" + id, Request.Method.GET, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type userType = new TypeToken<HandyUser>(){}.getType();
                HandyUser user = gson.fromJson(result, userType);
                callback.onSuccess(user);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Unable to get user: " + errorString);
            }
        });
    }

    public void logout() {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.remove(USER_ID);
        editor.remove(HANDYUSER_LOGGEDIN);
        editor.remove(USER_NAME);
        editor.remove(USER_EMAIL);
        editor.apply();
    }

    public void saveLoggedInUser(User user, boolean isHandyUser, Trade trade) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(USER_ID, user.getID());
        Log.d("eh logged in", "user saved: " + user.getID());
        editor.putBoolean(HANDYUSER_LOGGEDIN, isHandyUser);
        editor.putString(USER_NAME, user.getName());
        editor.putString(USER_EMAIL, user.getEmail());
        if (isHandyUser) {
            editor.putString(USER_TRADE, trade.toString());
        }
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        long userId = mSharedPreferences.getLong(USER_ID, 0);
        return userId != 0;
    }

    public void saveUser(User user, boolean update, NetworkCallback<User> callback) {
        JSONObject body = new JSONObject();
        try {
            if (update) {
                body.put("id", user.getID());
            }
            body.put("name", user.getName());
            body.put("email", user.getEmail());
            body.put("password", user.getPassword());
            body.put("info", user.getInfo());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "/createuser";
        int method = Request.Method.POST;
        if (update) {
            url = "/user";
            method = Request.Method.PATCH;
        }
        mNetworkManager.sendRequestWithBody(url, method, body, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type userType = new TypeToken<User>(){}.getType();
                User savedUser = gson.fromJson(result, userType);
                callback.onSuccess(savedUser);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("User not saved: " + errorString);
            }
        });

    }

    public void saveHandyUser(HandyUser handyUser, boolean update, NetworkCallback<HandyUser> callback) {
        JSONObject body = new JSONObject();
        try {
            if (update) {
                body.put("id", handyUser.getID());
            }
            body.put("name", handyUser.getName());
            body.put("email", handyUser.getEmail());
            body.put("password", handyUser.getPassword());
            body.put("info", handyUser.getInfo());
            body.put("trade", handyUser.getTrade());
            body.put("hourlyRate", handyUser.getHourlyRate());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        String url = "/createhandyuser";
        int method = Request.Method.POST;
        if (update) {
            url = "/handyuser";
            method = Request.Method.PATCH;
        }
        mNetworkManager.sendRequestWithBody(url, method, body, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                Type handyUserType = new TypeToken<HandyUser>(){}.getType();
                HandyUser savedHandyUser = gson.fromJson(result, handyUserType);
                callback.onSuccess(savedHandyUser);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("HandyUser not saved: " + errorString);
            }
        });
    }

    public void deleteUser(User user, NetworkCallback<User> callback) {
        mNetworkManager.sendRequest("/user/" + user.getID(), Request.Method.DELETE, new NetworkCallback<String>() {
            @Override
            public void onSuccess(String result) {
                logout();
                callback.onSuccess(null);
            }

            @Override
            public void onaFailure(String errorString) {
                logout();
                callback.onSuccess(null);
            }
        });
    }

    public Long getLoggedInUserId() {
        return mSharedPreferences.getLong(USER_ID, 0);
    }

    public String getLoggedInUserName() {
        return mSharedPreferences.getString(USER_NAME, "");
    }

    public String getLoggedInUserEmail() {
        return mSharedPreferences.getString(USER_EMAIL, "");
    }

    public String getLoggedInUserTrade() {
        return mSharedPreferences.getString(USER_TRADE, "empty");
    }

    public boolean getIsHandyUserLoggedIn() {
        return mSharedPreferences.getBoolean(HANDYUSER_LOGGEDIN, false);
    }
}
