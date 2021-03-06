package is.hi.handy_app.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import is.hi.handy_app.Activities.AdActivity;
import is.hi.handy_app.Activities.CreateAdActivity;
import is.hi.handy_app.Adapters.AdsAdapter;
import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.MainActivity;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.R;
import is.hi.handy_app.Services.AdService;
import is.hi.handy_app.Services.UserService;

public class AdvertisementsFragment extends Fragment {
    public static int NEW_AD_REQUEST_CODE = 201;
    public static int OPEN_AD_REQUEST_CODE = 202;

    private Context mContext;
    private UserService mUserService;
    private AdService mAdService;
    private List<Ad> mAds;

    private GridView mGridView;
    private ProgressBar mProgressBar;
    private TextView mErrorText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton mCreateAdButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = AdvertisementsFragment.this.getActivity();
        mAdService = new AdService(mContext);
        mUserService = new UserService(mContext);

        ((MainActivity) mContext).mNavigationView.setCheckedItem(R.id.nav_advertisements);

        View view =  inflater.inflate(R.layout.fragment_advertisements, container, false);
        mProgressBar = view.findViewById(R.id.ads_progressbar);
        mGridView = view.findViewById(R.id.ads_gridview);
        mErrorText = view.findViewById(R.id.ads_error);
        mSwipeRefreshLayout = view.findViewById(R.id.ads_swipe);
        mCreateAdButton = view.findViewById(R.id.ads_create_new);

        if (!mUserService.isUserLoggedIn()) {
            mCreateAdButton.setVisibility(View.GONE);
        }

        getAds(null);

        mSwipeRefreshLayout.setOnRefreshListener(() -> getAds(null));

        mCreateAdButton.setOnClickListener(view1 -> startActivityForResult(new Intent(mContext, CreateAdActivity.class), NEW_AD_REQUEST_CODE));

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_AD_REQUEST_CODE && resultCode == RESULT_OK) {
            assert data != null;
            if (data.getBooleanExtra(CreateAdActivity.AD_SUCCESSFULLY_POSTED_EXTRA, false)) {
                getAds(null);
                Snackbar snackbar = Snackbar.make(mGridView, "Ad successfully posted!", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
        else if (requestCode == OPEN_AD_REQUEST_CODE && resultCode == RESULT_OK) {
            getAds(null);
            assert data != null;
            if (data.getBooleanExtra(AdActivity.AD_SUCCESSFULLY_DELETED_EXTRA, false)) {
                Snackbar snackbar = Snackbar.make(mGridView, "Ad successfully deleted", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (data.getStringExtra(AdActivity.SHOW_TRADE) != null) {
                ((MainActivity) mContext).mNavigationView.setCheckedItem(R.id.nav_handymen);
                Fragment handymenFragment = new HandymenFragment(data.getStringExtra(AdActivity.SHOW_TRADE));
                FragmentManager fragmentManager = AdvertisementsFragment.this.requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, handymenFragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        }
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
                mGridView.setOnItemClickListener((adapterView, view, i, l) -> {
                    Intent intent = AdActivity.newIntent(mContext, mAds.get(i));
                    startActivityForResult(intent, OPEN_AD_REQUEST_CODE);
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
