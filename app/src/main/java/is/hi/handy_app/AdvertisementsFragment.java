package is.hi.handy_app;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Library.AdsAdapter;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class AdvertisementsFragment extends Fragment {
    private List<Ad> mAds;
    private GridView mGridView;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_advertisements, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.ads_progressbar);
        mGridView = (GridView) view.findViewById(R.id.ads_gridview);

        NetworkManager networkManager = NetworkManager.getInstance(getActivity());
        networkManager.getAds(new NetworkCallback<List<Ad>>() {
            @Override
            public void onSuccess(List<Ad> result) {
                mAds = result;
                AdsAdapter adapter = new AdsAdapter(AdvertisementsFragment.this.getActivity(), mAds);
                mGridView.setAdapter(adapter);
                mProgressBar.setVisibility(View.GONE);
                mGridView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onaFailure(String errorString) {
                Log.d(TAG, "Failed to get ads: " + errorString);
            }
        });

        return view;
    }
}
