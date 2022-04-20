package is.hi.handy_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.List;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Entities.PortfolioItem;
import is.hi.handy_app.Entities.Review;
import is.hi.handy_app.Adapters.PortfolioItemAdapter;
import is.hi.handy_app.Adapters.ReviewAdapter;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Services.PortfolioItemService;
import is.hi.handy_app.Services.ReviewService;
import is.hi.handy_app.Services.UserService;


public class HandyProfileActivity extends AppCompatActivity {
    private static final String EXTRA_HANDYUSER = "is.hi.handy_app.handyuser";

    UserService mUserService;
    ReviewService mReviewService;
    PortfolioItemService mPortfolioItemService;

    HandyUser mHandyUser;
    List<PortfolioItem> mPortfolioItems;
    List<Review> mReviews;

    Button mButtonMessage;
    Button mButtonReview;
    TextView mHandyInfo;
    TextView mHandyName;
    TextView mHandyTrade;
    TextView mHandyHourlyRate;
    TextView mAverageRating;
    ListView mPortfolioItemsList;
    RecyclerView mReviewsRecyclerView;

    public static Intent newIntent(Context packageContext, HandyUser handyUser) {
        Intent i = new Intent(packageContext, HandyProfileActivity.class);
        i.putExtra(EXTRA_HANDYUSER, handyUser);
        return i;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handyprofile);
        mUserService = new UserService(this);
        mReviewService = new ReviewService(this);
        mPortfolioItemService = new PortfolioItemService(this);

        mHandyUser = (HandyUser) getIntent().getSerializableExtra(EXTRA_HANDYUSER);

        if (savedInstanceState != null) {
            String json = savedInstanceState.getString("Handyman");
            if (!json.isEmpty()) {
                Gson gson = new Gson();
                mHandyUser = gson.fromJson(json, HandyUser.class);
            }
        }

        mHandyName = findViewById(R.id.handy_name);
        mHandyTrade = findViewById(R.id.handy_trade);
        mHandyHourlyRate = findViewById(R.id.handy_hourly_rate);
        mAverageRating = findViewById(R.id.my_rating);
        mHandyInfo = findViewById(R.id.handy_info);
        mPortfolioItemsList = findViewById(R.id.handy_portfolio);
        mReviewsRecyclerView = findViewById(R.id.handy_reviews);


        mHandyName.setText(mHandyUser.getName());

        String resultEnum = mHandyUser.getTrade().toString();
        mHandyTrade.setText(resultEnum);

        double result = mHandyUser.getHourlyRate();
        String finalResult = Double.valueOf(result).toString();
        mHandyHourlyRate.setText(new StringBuilder().append(getString(R.string.hourly_rate)).append(finalResult).append(" kr.").toString());

        double resultRating = mHandyUser.getAverageRating();
        String finalResultRating = Double.valueOf(resultRating).toString();
        mAverageRating.setText(new StringBuilder().append(getString(R.string.av_rating)).append(finalResultRating).toString());

        mHandyInfo.setText(mHandyUser.getInfo());

        mButtonReview = findViewById(R.id.write_a_review);


        mButtonReview.setOnClickListener(view -> {
            Intent intent = ReviewActivity.newIntent(HandyProfileActivity.this, mHandyUser);
            startActivityForResult(intent, 101);
        });

        mButtonMessage = findViewById(R.id.send_message);

        if (mHandyUser.getID() == mUserService.getLoggedInUserId()) {
            mButtonMessage.setEnabled(false);
            mButtonReview.setEnabled(false);
        }
        else if (!mUserService.isUserLoggedIn()) {
            mButtonMessage.setEnabled(false);
            mButtonMessage.setText(R.string.sing_in_to_message);
            mButtonReview.setEnabled(false);
            mButtonReview.setText(R.string.sign_in_to_rate);
        }

        mButtonMessage.setOnClickListener(view -> {
            Intent i = MessagesActivity.newIntent(HandyProfileActivity.this, mHandyUser.getID(), mHandyUser.getName());
            startActivity(i);
        });

        mPortfolioItemService.getUserPortfolioItems(mHandyUser.getID(), new NetworkCallback<List<PortfolioItem>>() {
            @Override
            public void onSuccess(List<PortfolioItem> result) {
                mPortfolioItems = result;
                if (mPortfolioItems.size() > 0) {
                    PortfolioItemAdapter adapter = new PortfolioItemAdapter(HandyProfileActivity.this, mPortfolioItems);
                    mPortfolioItemsList.setAdapter(adapter);
                    PortfolioItemAdapter.setDynamicHeight(mPortfolioItemsList);
                }
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });

        mReviewService.getMyReviews(mHandyUser.getID(), new NetworkCallback<List<Review>>() {
            @Override
            public void onSuccess(List<Review> result) {
                mReviews = result;
                if (mReviews.size() > 0) {
                    mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(HandyProfileActivity.this));
                    ReviewAdapter adapter = new ReviewAdapter(HandyProfileActivity.this, mReviews, false);
                    mReviewsRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Gson gson = new Gson();
        String json = gson.toJson(mHandyUser);
        savedInstanceState.putString("Handyman", json);
    }
}
