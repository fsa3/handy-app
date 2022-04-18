package is.hi.handy_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import is.hi.handy_app.Entities.HandyUser;



public class HandyProfileFragment extends Fragment {
     HandyUser mHandyUser;
     Button mButtonMessage;
     Button mButtonReview;
     Button mButtonSubmit;
     RatingBar mRatingBar;
     TextView mHandyInfo;
     TextView mHandyName;
     TextView mHandyTrade;
     TextView mHandyHourlyRate;
     TextView mAverageRating;
     Context mContext;


    public HandyProfileFragment(HandyUser handyUser) {
        this.mHandyUser= handyUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       mContext = HandyProfileFragment.this.getActivity();

        View view = inflater.inflate(R.layout.fragment_handyprofile, container,false);
        ((MainActivity)HandyProfileFragment.this.requireActivity()).hideSearch();

        if (savedInstanceState != null) {
            String json = savedInstanceState.getString("Handyman");
            if (!json.isEmpty()) {
                Gson gson = new Gson();
                mHandyUser = gson.fromJson(json, HandyUser.class);
            }
        }


       mHandyName = view.findViewById(R.id.handy_name);
       mHandyTrade = view.findViewById(R.id.handy_trade);
       mHandyHourlyRate = view.findViewById(R.id.handy_hourly_rate);
       mAverageRating = view.findViewById(R.id.my_rating);
       mHandyInfo = view.findViewById(R.id.handy_info);
       mRatingBar = (RatingBar) view.findViewById(R.id.handy_rating_bar);


       mHandyName.setText(mHandyUser.getName());

       String resultEnum = mHandyUser.getTrade().toString();
      mHandyTrade.setText(resultEnum);

       double result = mHandyUser.getHourlyRate();
       String finalResult = new Double(result).toString();
       mHandyHourlyRate.setText("My Hourly Rate :" + finalResult);

       double resultRating = mHandyUser.getAverageRating();
       String finalResultRating = new Double(resultRating).toString();
       mAverageRating.setText("My Rating: " + finalResultRating);

       mHandyInfo.setText("Ég er rosa handy handy handy");

       mButtonSubmit = (Button) view.findViewById(R.id.submit_button);
       mButtonSubmit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               float ratingNumber = mRatingBar.getRating();
           }
       });

        mButtonReview = (Button) view.findViewById(R.id.write_a_review);

        mButtonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ReviewActivity.newIntent(mContext, mHandyUser);
                startActivityForResult(intent,101);
            }
        });

        mButtonMessage = (Button) view.findViewById(R.id.send_message);

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Gson gson = new Gson();
        String json = gson.toJson(mHandyUser);
        savedInstanceState.putString("Handyman",json);
    }
}
