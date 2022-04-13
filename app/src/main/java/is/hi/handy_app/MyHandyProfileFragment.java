package is.hi.handy_app;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Services.UserService;

public class MyHandyProfileFragment extends Fragment {
    private Context mContext;
    private HandyUser mUser;
    private UserService mUserService;

    public MyHandyProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MyHandyProfileFragment.this.getActivity();
        mUserService = new UserService(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_handy_profile, container, false);

        long userId = mUserService.getLoggedInUserId();
        mUserService.getHandyUser(userId, new NetworkCallback<HandyUser>() {
            @Override
            public void onSuccess(HandyUser result) {
                mUser = result;
                setHandyUserInfo();
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });

        return view;
    }

    private void setHandyUserInfo() {
        Log.d("Profile for handyuser:", mUser.getName());
    }
}