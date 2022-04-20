package is.hi.handy_app.Activities;

import static is.hi.handy_app.Adapters.HandyUserAdapter.round;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.R;
import is.hi.handy_app.Services.ReviewService;


public class ReviewActivity extends AppCompatActivity {
    private static final String EXTRA_HANDY = "is.hi.handy_app.handyuser";
    ReviewService mReviewService;
    HandyUser mHandyUser;
    TextView mGiveUserRev;
    Button mReviewButton;
    EditText mReviewInput;
    RatingBar mRatingBar;
    TextView mCurrentRatingTextView;

    public static Intent newIntent(Context packageContext, HandyUser handyUser) {
        Intent i = new Intent(packageContext, ReviewActivity.class);
        i.putExtra(EXTRA_HANDY, handyUser);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_review);

        mReviewService = new ReviewService(this);
        mHandyUser = (HandyUser)getIntent().getSerializableExtra(EXTRA_HANDY);
        mReviewButton = findViewById(R.id.review_button);
        mGiveUserRev = findViewById(R.id.review_about);
        mReviewInput = findViewById(R.id.ed_review);
        mRatingBar = findViewById(R.id.handy_rating_bar);
        mCurrentRatingTextView = findViewById(R.id.handy_current_average_rating);

        mGiveUserRev.setText(String.format(getResources().getString(R.string.give_user_a_review), mHandyUser.getName()));
        mCurrentRatingTextView.setText(String.valueOf(round(mHandyUser.getAverageRating(), 1)));

        mReviewButton.setOnClickListener(view -> {
            String result = mReviewInput.getText().toString();
            //mReviewService.saveReview();
        });
    }
}