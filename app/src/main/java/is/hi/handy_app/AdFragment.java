package is.hi.handy_app;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Base64;
import java.util.Objects;

import is.hi.handy_app.Entities.Ad;

public class AdFragment extends Fragment {
    Ad mAd;

    TextView mAdTitle;
    ImageView mAdImage;
    Button mAdTrade;
    TextView mAdLocation;
    TextView mAdDate;
    TextView mAdDescription;
    TextView mAdAdvertiser;

    public AdFragment(Ad ad) {
        this.mAd = ad;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad, container, false);
        ((MainActivity) AdFragment.this.requireActivity()).hideSearch();
        mAdTitle = view.findViewById(R.id.ad_title);
        mAdImage = view.findViewById(R.id.ad_image);
        mAdTrade = view.findViewById(R.id.ad_trade);
        mAdLocation = view.findViewById(R.id.ad_location);
        mAdDate = view.findViewById(R.id.ad_date);
        mAdDescription = view.findViewById(R.id.ad_description);
        mAdAdvertiser = view.findViewById(R.id.ad_advertiser);
        mAdTitle.setText(mAd.getTitle());
        byte[] decodedImage = Base64.getDecoder().decode(mAd.getStringImage());
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedImage, 0, decodedImage.length);
        mAdImage.setImageBitmap(decodedByte);
        mAdTrade.setText(mAd.getTrade().toString());
        mAdLocation.setText(mAd.getLocation());
        mAdDate.setText(mAd.getFormattedDate());
        mAdDescription.setText(mAd.getDescription());
        mAdAdvertiser.setText(String.format(getResources().getString(R.string.posted_by), mAd.getUser().getName()));
        return view;
    }
}