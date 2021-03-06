package is.hi.handy_app.Fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.hi.handy_app.Activities.AdActivity;
import is.hi.handy_app.Activities.CreatePortfolioItemActivity;
import is.hi.handy_app.Adapters.AdsAdapter;
import is.hi.handy_app.Adapters.PortfolioItemAdapter;
import is.hi.handy_app.Adapters.ReviewAdapter;
import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Entities.PortfolioItem;
import is.hi.handy_app.Entities.Review;
import is.hi.handy_app.Entities.Trade;
import is.hi.handy_app.Entities.User;
import is.hi.handy_app.MainActivity;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.R;
import is.hi.handy_app.Services.AdService;
import is.hi.handy_app.Services.PortfolioItemService;
import is.hi.handy_app.Services.ReviewService;
import is.hi.handy_app.Services.UserService;

public class MyHandyProfileFragment extends Fragment {
    public static int OPEN_PORTFOLIOITEM_REQUEST_CODE = 302;

    private Context mContext;
    private HandyUser mUser;
    private List<Ad> mAds;
    private List<PortfolioItem> mPortfolioItems;
    private List<Review> mWrittenReviews;
    private UserService mUserService;
    private AdService mAdService;
    private PortfolioItemService mPortfolioItemService;
    private ReviewService mReviewService;

    private EditText mNameInput;
    private EditText mEmailInput;
    private EditText mDescriptionInput;
    private Spinner mTradeInput;
    private EditText mHourlyRateInput;
    private GridView mActiveAds;
    private ListView mPortfolioItemsList;
    private RecyclerView mReviewsWrittenList;

    private Button mSaveButton;
    private ProgressBar mSaveProgressBar;
    private Button mDeleteAccountButton;
    private Button mCreatePortfolioItemButton;

    private final List<String> mTrades = Stream.of(Trade.values())
            .map(Trade::name)
            .collect(Collectors.toList());

    public MyHandyProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MyHandyProfileFragment.this.getActivity();
        mUserService = new UserService(mContext);
        mAdService = new AdService(mContext);
        mPortfolioItemService = new PortfolioItemService(mContext);
        mReviewService = new ReviewService(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_handy_profile, container, false);

        ((MainActivity) mContext).mNavigationView.setCheckedItem(R.id.nav_my_profile);

        mNameInput = view.findViewById(R.id.edit_name_myhProfile);
        mEmailInput = view.findViewById(R.id.edit_email_myhProfile);
        mDescriptionInput = view.findViewById(R.id.About_me_myhProfile);
        mTradeInput = view.findViewById(R.id.trade_myhProfileS);
        mHourlyRateInput = view.findViewById(R.id.edit_rate_myhProfile);
        mActiveAds = view.findViewById(R.id.adsTV_myhProfile);
        mPortfolioItemsList = view.findViewById(R.id.portfolioItems_myhProfile);
        mReviewsWrittenList = view.findViewById(R.id.reviews_myhProfile);
        mSaveButton = view.findViewById(R.id.save_myhProfile);
        mSaveProgressBar = view.findViewById(R.id.myhprofile_save_progressbar);
        mDeleteAccountButton = view.findViewById(R.id.deleteAcc_myhProfile);
        mCreatePortfolioItemButton = view.findViewById(R.id.addToPortfolio_myhProfile);

        mTradeInput.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, mTrades));

        long userId = mUserService.getLoggedInUserId();
        mUserService.getHandyUser(userId, new NetworkCallback<HandyUser>() {
            @Override
            public void onSuccess(HandyUser result) {
                mUser = result;
                setHandyUserInfo();
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });

        getAds(userId);

        getPortfolioItems(userId);

        getWrittenReviews(userId);

        mSaveButton.setOnClickListener(v -> {
            mSaveProgressBar.setVisibility(View.VISIBLE);
            mUser.setName(mNameInput.getText().toString());
            mUser.setEmail(mEmailInput.getText().toString());
            mUser.setInfo(mDescriptionInput.getText().toString());
            mUser.setTrade(Trade.valueOf(mTradeInput.getSelectedItem().toString()));
            mUser.setHourlyRate(Double.parseDouble(mHourlyRateInput.getText().toString()));

            mUserService.saveHandyUser(mUser, true, new NetworkCallback<HandyUser>() {
                @Override
                public void onSuccess(HandyUser result) {
                    mUser = result;
                    setHandyUserInfo();
                    Snackbar snackbar = Snackbar.make(view, "Profile updated", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    mUserService.login(mUser.getEmail(), mUser.getPassword(), new NetworkCallback<User>() {
                        @Override
                        public void onSuccess(User result) {
                            ((MainActivity) mContext).resetMenu();
                            mSaveProgressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onaFailure(String errorString) {

                        }
                    });
                }

                @Override
                public void onaFailure(String errorString) {

                }
            });
        });

        mCreatePortfolioItemButton.setOnClickListener(view1 -> startActivityForResult(new Intent(mContext, CreatePortfolioItemActivity.class), OPEN_PORTFOLIOITEM_REQUEST_CODE));

        mDeleteAccountButton.setOnClickListener(v -> mUserService.deleteUser(mUser, new NetworkCallback<User>() {
            @Override
            public void onSuccess(User result) {
                ((MainActivity) mContext).resetMenu();

                Fragment handymenFragment = new HandymenFragment();
                FragmentManager fragmentManager = MyHandyProfileFragment.this.requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, handymenFragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();

                Snackbar snackbar = Snackbar.make(view, "Account successfully deleted", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onaFailure(String errorString) {
                Snackbar snackbar = Snackbar.make(view, "Failed to delete account " + errorString, Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }));

        return view;
    }

    private void getAds(long userId) {
        mAdService.findByUser(userId, new NetworkCallback<List<Ad>>() {
            @Override
            public void onSuccess(List<Ad> result) {
                mAds = result;
                if (mAds.size() > 0) {
                    setUserActiveAds();
                }
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });
    }

    private void getPortfolioItems(long userId) {
        mPortfolioItemService.getUserPortfolioItems(userId, new NetworkCallback<List<PortfolioItem>>() {
            @Override
            public void onSuccess(List<PortfolioItem> result) {
                mPortfolioItems = result;
                if (mPortfolioItems.size() > 0) {
                    setPortfolioItemsList();
                }
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });
    }

    private void getWrittenReviews(long userId) {
        mReviewService.getMyWrittenReviews(userId, new NetworkCallback<List<Review>>() {
            @Override
            public void onSuccess(List<Review> result) {
                mWrittenReviews = result;
                if (mWrittenReviews.size() > 0) {
                    setWrittenReviewsList();
                }
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });
    }

    private void setHandyUserInfo() {
        mNameInput.setText(mUser.getName());
        mEmailInput.setText(mUser.getEmail());
        mDescriptionInput.setText(mUser.getInfo());
        mTradeInput.setSelection(mTrades.indexOf(mUser.getTrade().toString()));
        mHourlyRateInput.setText(String.valueOf(mUser.getHourlyRate()));
    }

    private void setUserActiveAds() {
        AdsAdapter adapter = new AdsAdapter(mContext, mAds);
        mActiveAds.setAdapter(adapter);
        AdsAdapter.setDynamicHeight(mActiveAds);
        mActiveAds.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent intent = AdActivity.newIntent(mContext, mAds.get(i));
            startActivityForResult(intent, AdvertisementsFragment.OPEN_AD_REQUEST_CODE);
        });
    }

    private void setPortfolioItemsList() {
        PortfolioItemAdapter adapter = new PortfolioItemAdapter(mContext, mPortfolioItems);
        mPortfolioItemsList.setAdapter(adapter);
        PortfolioItemAdapter.setDynamicHeight(mPortfolioItemsList);
    }

    private void setWrittenReviewsList() {
        mReviewsWrittenList.setLayoutManager(new LinearLayoutManager(mContext));
        ReviewAdapter adapter = new ReviewAdapter(mContext, mWrittenReviews, true);
        mReviewsWrittenList.setAdapter(adapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AdvertisementsFragment.OPEN_AD_REQUEST_CODE && resultCode == RESULT_OK) {
            getAds(mUser.getID());
            assert data != null;
            if (data.getBooleanExtra(AdActivity.AD_SUCCESSFULLY_DELETED_EXTRA, false)) {
                Snackbar snackbar = Snackbar.make(mActiveAds, "Ad successfully deleted", Snackbar.LENGTH_LONG);
                snackbar.show();
            } else if (data.getStringExtra(AdActivity.SHOW_TRADE) != null) {
                ((MainActivity) mContext).mNavigationView.setCheckedItem(R.id.nav_handymen);
                Fragment handymenFragment = new HandymenFragment(data.getStringExtra(AdActivity.SHOW_TRADE));
                FragmentManager fragmentManager = MyHandyProfileFragment.this.requireActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, handymenFragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        } else if (requestCode == OPEN_PORTFOLIOITEM_REQUEST_CODE && resultCode == RESULT_OK) {
            getPortfolioItems(mUser.getID());
            assert data != null;
            if (data.getBooleanExtra(CreatePortfolioItemActivity.PORTFOLIO_ITEM_SUCCESSFULLY_POSTED_EXTRA, false)) {
                Snackbar snackbar = Snackbar.make(mPortfolioItemsList, "Successfully added to your portfolio", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }
    }
}