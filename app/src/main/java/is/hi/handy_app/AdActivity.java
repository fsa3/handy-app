package is.hi.handy_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Base64;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Services.AdService;
import is.hi.handy_app.Services.UserService;

public class AdActivity extends AppCompatActivity {
    private static final String EXTRA_AD = "is.hi.handy_app.ad";

    private UserService mUserService;
    private AdService mAdService;

    private Ad mAd;

    private TextView mAdTitle;
    private ImageView mAdImage;
    private Button mAdTrade;
    private TextView mAdLocation;
    private TextView mAdDate;
    private TextView mAdDescription;
    private TextView mAdAdvertiser;
    private Button mAdMessageDeleteButton;

    public static Intent newIntent(Context packageContext, Ad ad) {
        Intent i = new Intent(packageContext, AdActivity.class);
        i.putExtra(EXTRA_AD, ad);
        return i;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        mUserService = new UserService(this);
        mAdService = new AdService(this);

        mAd = (Ad) getIntent().getSerializableExtra(EXTRA_AD);
        mAdTitle = findViewById(R.id.ad_title);
        mAdImage = findViewById(R.id.ad_image);
        mAdTrade = findViewById(R.id.ad_trade);
        mAdLocation = findViewById(R.id.ad_location);
        mAdDate = findViewById(R.id.ad_date);
        mAdDescription = findViewById(R.id.ad_description);
        mAdAdvertiser = findViewById(R.id.ad_advertiser);
        mAdMessageDeleteButton = findViewById(R.id.ad_message_delete_button);
        mAdTitle.setText(mAd.getTitle());
        byte[] decodedImage = Base64.getDecoder().decode(mAd.getStringImage());
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
        mAdImage.setImageBitmap(decodedByte);
        mAdTrade.setText(mAd.getTrade().toString());
        mAdLocation.setText(mAd.getLocation());
        mAdDate.setText(mAd.getFormattedDate());
        mAdDescription.setText(mAd.getDescription());
        mAdAdvertiser.setText(String.format(getResources().getString(R.string.posted_by), mAd.getUser().getName()));

        mAdTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                Fragment handymenFragment = new HandymenFragment(mAd.getTrade().toString());
                FragmentManager fragmentManager = AdFragment.this.getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, handymenFragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
                 */
            }
        });

        if (mUserService.getLoggedInUserId() == mAd.getUser().getID()) {
            mAdMessageDeleteButton.setText(R.string.delete_ad);
        }
        mAdMessageDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mUserService.getLoggedInUserId() == mAd.getUser().getID()) {
                    mAdService.deleteAd(mAd, new NetworkCallback<Ad>() {
                        @Override
                        public void onSuccess(Ad result) {
                            Intent data = new Intent();
                            setResult(RESULT_OK, data);
                            finish();
                        }

                        @Override
                        public void onaFailure(String errorString) {
                            Toast.makeText(AdActivity.this, errorString, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}