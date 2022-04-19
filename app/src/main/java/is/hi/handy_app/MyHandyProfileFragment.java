package is.hi.handy_app;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Entities.Trade;
import is.hi.handy_app.Entities.User;
import is.hi.handy_app.Library.AdsAdapter;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Services.AdService;
import is.hi.handy_app.Services.UserService;

public class MyHandyProfileFragment extends Fragment {
    private Context mContext;
    private HandyUser mUser;
    private List<Ad> mAds;
    private UserService mUserService;
    private AdService mAdService;

    private EditText mNameInput;
    private EditText mEmailInput;
    private EditText mDescriptionInput;
    private Spinner mTradeInput;
    private EditText mHourlyRateInput;
    private GridView mActiveAds;

    private Button mSaveButton;
    private ProgressBar mSaveProgressBar;
    private Button mDeleteAccountButton;

    private List<String> mTrades = Stream.of(Trade.values())
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_handy_profile, container, false);

        mNameInput = view.findViewById(R.id.edit_name_myhProfile);
        mEmailInput = view.findViewById(R.id.edit_email_myhProfile);
        mDescriptionInput = view.findViewById(R.id.About_me_myhProfile);
        mTradeInput = view.findViewById(R.id.trade_myhProfileS);
        mHourlyRateInput = view.findViewById(R.id.edit_rate_myhProfile);
        mActiveAds = view.findViewById(R.id.adsTV_myhProfile);
        mSaveButton = view.findViewById(R.id.save_myhProfile);
        mSaveProgressBar = view.findViewById(R.id.myhprofile_save_progressbar);
        mDeleteAccountButton = view.findViewById(R.id.deleteAcc_myhProfile);

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

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        FragmentManager fragmentManager = MyHandyProfileFragment.this.getActivity().getSupportFragmentManager();
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
    }
}