package is.hi.handy_app;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.List;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Library.AdsAdapter;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;
import is.hi.handy_app.Services.AdService;

public class AdvertisementsFragment extends Fragment {
    private Context mContext;
    private AdService mAdService;
    private List<Ad> mAds;

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private TextView mErrorText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = AdvertisementsFragment.this.getActivity();
        mAdService = new AdService(mContext);

        View view =  inflater.inflate(R.layout.fragment_advertisements, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.ads_progressbar);
        mGridView = (GridView) view.findViewById(R.id.ads_gridview);
        mErrorText = (TextView) view.findViewById(R.id.ads_error);

        mAdService.findAll(new NetworkCallback<List<Ad>>() {
            @Override
            public void onSuccess(List<Ad> result) {
                mAds = result;
                AdsAdapter adapter = new AdsAdapter(AdvertisementsFragment.this.getActivity(), mAds);
                mGridView.setAdapter(adapter);
                mProgressBar.setVisibility(View.GONE);
                mGridView.setVisibility(View.VISIBLE);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(mContext, mAds.get(i).getDescription(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onaFailure(String errorString) {
                mErrorText.setText(errorString);
                mProgressBar.setVisibility(View.GONE);
                mErrorText.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }
}
