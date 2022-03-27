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
import android.widget.ListView;
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

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Library.HandyUserAdapter;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;
import is.hi.handy_app.Services.UserService;

public class HandymenFragment extends Fragment {
    private Context mContext;
    private UserService mUserService;
    private List<HandyUser> mHandyUsers;

    private ListView mListView;
    private ProgressBar mProgressBar;
    private TextView mErrorText;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext =  HandymenFragment.this.getActivity();
        mUserService = new UserService(mContext);

        View view = inflater.inflate(R.layout.fragment_handymen, container, false);
        mListView = (ListView) view.findViewById(R.id.handymen_listview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.handymen_progressbar);
        mErrorText = (TextView) view.findViewById(R.id.handymen_error);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.handymen_swipe);

        getHandymen(null);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHandymen(null);
            }
        });

        ((MainActivity) mContext).setSearch("Search for a Handyman", new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getHandymen(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() == 0) {
                    getHandymen(null);
                }
                return false;
            }
        });

        return view;
    }

    private void getHandymen(String name) {
        mUserService.findAllHandyUsers(name, new NetworkCallback<List<HandyUser>>() {
            @Override
            public void onSuccess(List<HandyUser> result) {
                mHandyUsers = result;
                HandyUserAdapter adapter = new HandyUserAdapter(mContext, mHandyUsers);
                mListView.setAdapter(adapter);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mListView.setVisibility(View.VISIBLE);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Fragment handyProfileFragment = new HandyProfileFragment(mHandyUsers.get(i));
                        FragmentManager fragmentManager = HandymenFragment.this.getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, handyProfileFragment)
                                .addToBackStack(null)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();
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
    }
}
