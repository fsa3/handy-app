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
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import is.hi.handy_app.Entities.HandyUser;


public class HandyProfileActivity extends AppCompatActivity {
    private static final String EXTRA_HANDYUSER = "is.hi.handy_app.handyuser";

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

    public static Intent newIntent(Context packageContext, HandyUser handyUser) {
        Intent i = new Intent(packageContext, HandyProfileActivity.class);
        i.putExtra(EXTRA_HANDYUSER, handyUser);
        return i;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handyprofile);

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
        mRatingBar = findViewById(R.id.handy_rating_bar);


        mHandyName.setText(mHandyUser.getName());

        String resultEnum = mHandyUser.getTrade().toString();
        mHandyTrade.setText(resultEnum);

        double result = mHandyUser.getHourlyRate();
        String finalResult = new Double(result).toString();
        mHandyHourlyRate.setText("My Hourly Rate :" + finalResult);

        double resultRating = mHandyUser.getAverageRating();
        String finalResultRating = new Double(resultRating).toString();
        mAverageRating.setText("My Rating: " + finalResultRating);

        mHandyInfo.setText(mHandyUser.getInfo());

        mButtonSubmit = findViewById(R.id.submit_button);
        mButtonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float ratingNumber = mRatingBar.getRating();
            }
        });

        mButtonReview = findViewById(R.id.write_a_review);

        mButtonReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = ReviewActivity.newIntent(HandyProfileActivity.this, mHandyUser);
                startActivityForResult(intent, 101);
            }
        });

        mButtonMessage = findViewById(R.id.send_message);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Gson gson = new Gson();
        String json = gson.toJson(mHandyUser);
        savedInstanceState.putString("Handyman", json);
    }
}
