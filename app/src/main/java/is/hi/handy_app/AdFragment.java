package is.hi.handy_app;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import is.hi.handy_app.Entities.Ad;

public class AdFragment extends Fragment {
    Ad mAd;

    TextView mAdTitle;

    public AdFragment(Ad ad) {
        this.mAd = ad;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad, container, false);
        mAdTitle = (TextView) view.findViewById(R.id.ad_title);
        mAdTitle.setText(mAd.getTitle());
        return view;
    }
}