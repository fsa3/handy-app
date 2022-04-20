package is.hi.handy_app;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Entities.Review;
import is.hi.handy_app.Entities.User;
import is.hi.handy_app.Library.AdsAdapter;
import is.hi.handy_app.Library.ReviewAdapter;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Services.AdService;
import is.hi.handy_app.Services.ReviewService;
import is.hi.handy_app.Services.UserService;

public class MyProfileFragment extends Fragment {
    private Context mContext;
    private User mUser;
    private List<Ad> mAds;
    private List<Review> mWrittenReviews;
    private UserService mUserService;
    private AdService mAdService;
    private ReviewService mReviewService;

    private EditText mNameInput;
    private EditText mEmailInput;
    private EditText mDescriptionInput;
    private GridView mActiveAds;
    private RecyclerView mReviewsWrittenList;

    private Button mSaveButton;
    private ProgressBar mSaveProgressBar;
    private Button mDeleteAccountButton;


    public MyProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MyProfileFragment.this.getActivity();
        mUserService = new UserService(mContext);
        mAdService = new AdService(mContext);
        mReviewService = new ReviewService(mContext);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_my_profile, container, false);

        ((MainActivity) mContext).mNavigationView.setCheckedItem(R.id.nav_my_profile);

        mNameInput = view.findViewById(R.id.edit_name_myProfile);
        mEmailInput = view.findViewById(R.id.edit_email_myProfile);
        mDescriptionInput = view.findViewById(R.id.About_me_myProfile);
        mActiveAds = view.findViewById(R.id.adsTV_myProfile);
        mReviewsWrittenList = view.findViewById(R.id.reviews_myProfile);
        mSaveButton = view.findViewById(R.id.save_myProfile);
        mSaveProgressBar = view.findViewById(R.id.myprofile_save_progressbar);
        mDeleteAccountButton = view.findViewById(R.id.deleteAcc_myProfile);

        long userId = mUserService.getLoggedInUserId();
        mUserService.getUser(userId, new NetworkCallback<User>() {
            @Override
            public void onSuccess(User result) {
                mUser = result;
                setUserInfo();
            }

            @Override
            public void onaFailure(String errorString) {

            }
        });

        getAds(userId);

        getWrittenReviews(userId);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSaveProgressBar.setVisibility(View.VISIBLE);
                mUser.setName(mNameInput.getText().toString());
                mUser.setEmail(mEmailInput.getText().toString());
                mUser.setInfo(mDescriptionInput.getText().toString());

                mUserService.saveUser(mUser, true, new NetworkCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        mUser = result;
                        setUserInfo();
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
            }
        });



        mDeleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserService.deleteUser(mUser, new NetworkCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        ((MainActivity) mContext).resetMenu();

                        Fragment handymenFragment = new HandymenFragment();
                        FragmentManager fragmentManager = MyProfileFragment.this.getActivity().getSupportFragmentManager();
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
                });
            }
        });

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

    private void setUserInfo() {
        mNameInput.setText(mUser.getName());
        mEmailInput.setText(mUser.getEmail());
        mDescriptionInput.setText(mUser.getInfo());
    }

    private void setUserActiveAds() {
        AdsAdapter adapter = new AdsAdapter(mContext, mAds);
        mActiveAds.setAdapter(adapter);
        AdsAdapter.setDynamicHeight(mActiveAds);
        mActiveAds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = AdActivity.newIntent(mContext, mAds.get(i));
                startActivityForResult(intent, AdvertisementsFragment.OPEN_AD_REQUEST_CODE);
            }
        });
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
                FragmentManager fragmentManager = MyProfileFragment.this.getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, handymenFragment)
                        .addToBackStack(null)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .commit();
            }
        }
    }
}