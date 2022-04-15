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

import com.google.android.material.textfield.TextInputEditText;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Entities.Review;
import is.hi.handy_app.Services.ReviewService;

public class ReviewFragment extends Fragment {
    //ReviewService mReviewService;
    HandyUser mHandyUser;
   // Review mReview;
    Button mButtonSubmitReview;
    TextView mUserReviewBanner;
    TextView mWriteAboutHeader;
    TextInputEditText mReviewInput;

    public ReviewFragment(HandyUser handyUser){this.mHandyUser = handyUser;}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container,false);
        ((MainActivity)ReviewFragment.this.requireActivity()).hideSearch();
        mUserReviewBanner = view.findViewById(R.id.user_review);
        mWriteAboutHeader = view.findViewById(R.id.write_about_user);
        mReviewInput = view.findViewById(R.id.review_Input);

        mUserReviewBanner.setText(mHandyUser.getName() + " reviews!");
        mWriteAboutHeader.setText("Like to review " + mHandyUser.getName() + " ?");

        //TODO útfæra review Lista sem er birtur í View, kallar á aðferð úr ReviewService


        mButtonSubmitReview = (Button) view.findViewById(R.id.review_button);
        mButtonSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO útfæra Submit virkni. get aðferð úr view editText og post aðferð með review Service
            }
        });


        return view;
    }
}
