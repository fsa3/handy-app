package is.hi.handy_app.Services;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
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
import is.hi.handy_app.MainActivity;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class UserService {
    public static final String USER_ID = "logged-in-user-id";
    public static final String USER_NAME = "logged-in-user-name";
    public static final String USER_EMAIL = "logged-in-user-email";
    public static final String HANDYUSER_LOGGEDIN = "handy-user-logged-in";

    private NetworkManager mNetworkManager;
    private Context mContext;
    private SharedPreferences mSharedPreferences;


    public UserService(Context context) {
        mContext = context;
        mNetworkManager = NetworkManager.getInstance(context);
        mSharedPreferences = mContext.getSharedPreferences(MainActivity.SHARED_PREFS, Context.MODE_PRIVATE);
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
                Type handyUserType = new TypeToken<HandyUser>(){}.getType();
                User loggedInUser = gson.fromJson(result, userType);
                Log.d("eh logged in", loggedInUser.getID() + "");
                HandyUser handyUser = gson.fromJson(result, handyUserType);
                saveLoggedInUser(loggedInUser, handyUser.getTrade() != null);
                callback.onSuccess(loggedInUser);
            }

            @Override
            public void onaFailure(String errorString) {
                callback.onaFailure("Login not successful: " + errorString);
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

    public void saveLoggedInUser(User user, boolean isHandyUser) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putLong(USER_ID, user.getID());
        Log.d("eh logged in", "user saved: " + user.getID());
        editor.putBoolean(HANDYUSER_LOGGEDIN, isHandyUser);
        editor.putString(USER_NAME, user.getName());
        editor.putString(USER_EMAIL, user.getEmail());
        editor.apply();
    }

    public boolean isUserLoggedIn() {
        long userId = mSharedPreferences.getLong(USER_ID, 0);
        return userId != 0;
    }

    public String getLoggedInUserName() {
        return mSharedPreferences.getString(USER_NAME, "");
    }

    public String getLoggedInUserEmail() {
        return mSharedPreferences.getString(USER_EMAIL, "");
    }
}
