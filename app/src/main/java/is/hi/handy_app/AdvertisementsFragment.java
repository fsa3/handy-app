package is.hi.handy_app;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.Objects;

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
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = AdvertisementsFragment.this.getActivity();
        mAdService = new AdService(mContext);

        View view =  inflater.inflate(R.layout.fragment_advertisements, container, false);
        mProgressBar = (ProgressBar) view.findViewById(R.id.ads_progressbar);
        mGridView = (GridView) view.findViewById(R.id.ads_gridview);
        mErrorText = (TextView) view.findViewById(R.id.ads_error);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.ads_swipe);

        getAds(null);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAds(null);
            }
        });

        ((MainActivity) mContext).setSearch("Search by Ad name or description", new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getAds(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    getAds(null);
                }
                return false;
            }
        });

        return view;
    }

    private void getAds(String searchQuery) {
        mAdService.findAll(searchQuery, new NetworkCallback<List<Ad>>() {
            @Override
            public void onSuccess(List<Ad> result) {
                mErrorText.setVisibility(View.GONE);
                mAds = result;
                AdsAdapter adapter = new AdsAdapter(AdvertisementsFragment.this.getActivity(), mAds);
                mGridView.setAdapter(adapter);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mGridView.setVisibility(View.VISIBLE);
                mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Fragment adFragment = new AdFragment(mAds.get(i));
                        FragmentManager fragmentManager = AdvertisementsFragment.this.getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, adFragment)
                                .addToBackStack(null)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
                    }
                });
                if (mAds.size() == 0) {
                    mErrorText.setText(getResources().getString(R.string.no_results));
                    mErrorText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onaFailure(String errorString) {
                mErrorText.setText(errorString);
                mProgressBar.setVisibility(View.GONE);
                mErrorText.setVisibility(View.VISIBLE);
            }
        });
    }
}
