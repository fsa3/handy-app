package is.hi.handy_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Entities.Review;
import is.hi.handy_app.Services.ReviewService;


public class ReviewActivity extends AppCompatActivity {
    private static final String EXTRA_HANDY = "is.hi.handy_app.handyuser";
    RecyclerView mRecyclerView;
    List<Review> mReviewList;
    //ReviewAdapter mReviewAdapter;
    ReviewService mReviewService;
    HandyUser mHandyUser;
    TextView mbanner;
    TextView mGiveUserRev;
    Button mReviewButton;
    EditText mReviewInput;

    public static Intent newIntent(Context packageContext, HandyUser handyUser) {
        Intent i = new Intent(packageContext, ReviewActivity.class);
        i.putExtra(EXTRA_HANDY, handyUser);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        setContentView(R.layout.activity_review);

        mReviewService = new ReviewService(this);
        mHandyUser = (HandyUser)getIntent().getSerializableExtra(EXTRA_HANDY);
        mbanner = findViewById(R.id.Review_Banner);
        mReviewButton = findViewById(R.id.review_button);
        mGiveUserRev = findViewById(R.id.review_about);
        mReviewInput = findViewById(R.id.ed_review);
        mbanner.setText(mHandyUser.getName() +" Reviews");
        mGiveUserRev.setText("Give " + mHandyUser.getName() + " a review!");
        displayItems();


        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = mReviewInput.getText().toString();
                //mReviewService.saveReview();
            }
        });

         */


    }

    private void displayItems(){
        /*
        mRecyclerView = findViewById(R.id.recycler_reviews);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,1));
        long id = mHandyUser.getID();
        mReviewService.getMyReviews(id, new NetworkCallback<List<Review>>() {
            @Override
            public void onSuccess(List<Review> result) {
                mReviewList = result;
                mReviewAdapter = new ReviewAdapter(ReviewActivity.this,mReviewList);
                mRecyclerView.setAdapter(mReviewAdapter);



            }

            @Override
            public void onaFailure(String errorString) {
                Toast.makeText(ReviewActivity.this, errorString, Toast.LENGTH_SHORT).show();
            }
        });
         */
    }
}