package is.hi.handy_app;

import android.view.View;

import androidx.fragment.app.Fragment;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Services.UserService;


public class HandyProfileFragment extends Fragment {
    private UserService mUserService;
    private HandyUser mHandyUser;


    public HandyProfileFragment(HandyUser handyUser) {
        this.mHandyUser= handyUser;
    }
}
