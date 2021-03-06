package is.hi.handy_app.Fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.hi.handy_app.Activities.HandyProfileActivity;
import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Entities.Trade;
import is.hi.handy_app.Adapters.HandyUserAdapter;
import is.hi.handy_app.MainActivity;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.R;
import is.hi.handy_app.Services.UserService;

public class HandymenFragment extends Fragment {
    private Context mContext;
    private UserService mUserService;
    private List<HandyUser> mHandyUsers;

    private LinearLayout mHandymenContainer;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private TextView mErrorText;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Spinner mTradeSpinner;

    private String mNameSearch = null;
    private String mTradeSearch = null;

    public HandymenFragment() {
    }

    public HandymenFragment(String trade) {
        mTradeSearch = trade;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext =  HandymenFragment.this.getActivity();
        mUserService = new UserService(mContext);

        View view = inflater.inflate(R.layout.fragment_handymen, container, false);
        mHandymenContainer = view.findViewById(R.id.handymen_container);
        mListView = view.findViewById(R.id.handymen_listview);
        mProgressBar = view.findViewById(R.id.handymen_progressbar);
        mErrorText = view.findViewById(R.id.handymen_error);
        mSwipeRefreshLayout = view.findViewById(R.id.handymen_swipe);
        mTradeSpinner = view.findViewById(R.id.handymen_trade_spinner);
        List<String> trades = Stream.of(Trade.values())
                    .map(Trade::name)
                    .collect(Collectors.toList());
        trades.add(0, "All trades");
        mTradeSpinner.setAdapter(new ArrayAdapter<>(mContext, android.R.layout.simple_list_item_1, trades));
        if (mTradeSearch != null) {
            int tradeIndex = trades.indexOf(mTradeSearch);
            mTradeSpinner.setSelection(tradeIndex);
        }

        getHandymen();

        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            mNameSearch = null;
            getHandymen();
        });

        ((MainActivity) mContext).setSearch("Search for a Handyman", new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mNameSearch = query;
                getHandymen();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    mNameSearch = null;
                    getHandymen();
                }
                return false;
            }
        });

        mTradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    mTradeSearch = null;
                } else {
                    mTradeSearch = trades.get(i);
                }
                getHandymen();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        return view;
    }

    private void getHandymen() {
        mUserService.findAllHandyUsers(mNameSearch, mTradeSearch, new NetworkCallback<List<HandyUser>>() {
            @Override
            public void onSuccess(List<HandyUser> result) {
                mErrorText.setVisibility(View.GONE);
                mHandyUsers = result;
                HandyUserAdapter adapter = new HandyUserAdapter(mContext, mHandyUsers);
                mListView.setAdapter(adapter);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mHandymenContainer.setVisibility(View.VISIBLE);
                mListView.setOnItemClickListener((adapterView, view, i, l) -> {
                    Intent intent = HandyProfileActivity.newIntent(mContext, mHandyUsers.get(i));
                    startActivity(intent);
                });
                if (mHandyUsers.size() == 0) {
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
