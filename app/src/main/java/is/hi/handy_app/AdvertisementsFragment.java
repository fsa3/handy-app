package is.hi.handy_app;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class AdvertisementsFragment extends Fragment {
    private List<Ad> mAds;
    private TextView mTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_advertisements, container, false);
        mTextView = (TextView) view.findViewById(R.id.ad_title);

        NetworkManager networkManager = NetworkManager.getInstance(getActivity());
        networkManager.getAds(new NetworkCallback<List<Ad>>() {
            @Override
            public void onSuccess(List<Ad> result) {
                mAds = result;
                mTextView.setText(mAds.get(0).getTitle());
            }

            @Override
            public void onaFailure(String errorString) {
                Log.d(TAG, "Failed to get ads: " + errorString);
            }
        });

        return view;
    }
}
