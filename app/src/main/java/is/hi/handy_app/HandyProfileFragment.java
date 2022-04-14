package is.hi.handy_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
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
     Button mButtonSubmit;
     RatingBar mRatingBar;
     TextView mHandyInfo;
     TextView mHandyName;
     TextView mHandyTrade;
     TextView mHandyHourlyRate;
     TextView mAverageRating;


    public HandyProfileFragment(HandyUser handyUser) {
        this.mHandyUser= handyUser;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_handyprofile, container,false);
        ((MainActivity)HandyProfileFragment.this.requireActivity()).hideSearch();
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
                //TODO Útfæra rating. tengja við rating bar
            }
        });
        mButtonMessage = (Button) view.findViewById(R.id.send_message);
        mButtonMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Útfæra Message

            }
        });



        return view;
    }
}
