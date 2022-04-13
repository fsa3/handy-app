package is.hi.handy_app;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.hi.handy_app.Entities.User;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Services.UserService;

public class MyProfileFragment extends Fragment {
    private Context mContext;
    private User mUser;
    private UserService mUserService;

    public MyProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MyProfileFragment.this.getActivity();
        mUserService = new UserService(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);
        
        long userId = mUserService.getLoggedInUserId();
        mUserService.getUser(userId, new NetworkCallback<User>() {
            @Override
            public void onSuccess(User result) {
                mUser = result;
                setUserInfo();
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });

        return view;
    }

    private void setUserInfo() {
        Log.d("Profile for user:", mUser.getName());
    }
}