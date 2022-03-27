package is.hi.handy_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Services.UserService;



public class HandyProfileFragment extends Fragment {
     UserService mUserService;
     HandyUser mHandyUser;
     Button mButtonMessage;
     Button mButtonReview;
     TextView mHandyName;
     TextView mHandyTrade;
     TextView mHandyHourlyRate;

    public HandyProfileFragment(HandyUser handyUser) {
        this.mHandyUser= handyUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handyprofile, container,false);
        ((MainActivity)HandyProfileFragment.this.requireActivity()).hideSearch();
       /* mHandyName = view.findViewById(R.id.handy_name);*/
       /* mHandyTrade = view.findViewById(R.id.handy_trade);*/
      /*  mHandyHourlyRate = view.findViewById(R.id.handy_hourly_rate);*/

        /*mHandyName.setText(mHandyUser.getName());*/
        /*mHandyTrade.setText(mHandyUser.getTrade());*/
      /*  mHandyHourlyRate.setText((int) mHandyUser.getHourlyRate());*/

        return view;
    }
}
