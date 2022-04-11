package is.hi.handy_app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Base64;

import is.hi.handy_app.Entities.Ad;

public class AdActivity extends AppCompatActivity {
    private static final String EXTRA_AD = "is.hi.handy_app.ad";

    Ad mAd;

    TextView mAdTitle;
    ImageView mAdImage;
    Button mAdTrade;
    TextView mAdLocation;
    TextView mAdDate;
    TextView mAdDescription;
    TextView mAdAdvertiser;

    public static Intent newIntent(Context packageContext, Ad ad) {
        Intent i = new Intent(packageContext, AdActivity.class);
        i.putExtra(EXTRA_AD, ad);
        return i;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        mAd = (Ad) getIntent().getSerializableExtra(EXTRA_AD);
        mAdTitle = findViewById(R.id.ad_title);
        mAdImage = findViewById(R.id.ad_image);
        mAdTrade = findViewById(R.id.ad_trade);
        mAdLocation = findViewById(R.id.ad_location);
        mAdDate = findViewById(R.id.ad_date);
        mAdDescription = findViewById(R.id.ad_description);
        mAdAdvertiser = findViewById(R.id.ad_advertiser);
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
    }
}