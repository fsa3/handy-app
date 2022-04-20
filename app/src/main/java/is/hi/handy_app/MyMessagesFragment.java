package is.hi.handy_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import is.hi.handy_app.Entities.User;
import is.hi.handy_app.Adapters.MessageUserAdapter;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Services.MessageService;
import is.hi.handy_app.Services.UserService;


public class MyMessagesFragment extends Fragment {
    private Context mContext;
    private MessageService mMessageService;
    private UserService mUserService;
    private List<User> mMessageUsers;
    
    private LinearLayout mUsersContainer;
    private ListView mListView;
    private ProgressBar mProgressBar;
    private TextView mErrorText;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public MyMessagesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        mMessageService = new MessageService(mContext);
        mUserService = new UserService(mContext);
        ((MainActivity) MyMessagesFragment.this.requireActivity()).hideSearch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_messages, container, false);
        
        mUsersContainer = view.findViewById(R.id.myMessages_container);
        mListView = view.findViewById(R.id.myMessages_listview);
        mProgressBar = view.findViewById(R.id.myMessages_progressbar);
        mErrorText = view.findViewById(R.id.myMessages_error);
        mSwipeRefreshLayout = view.findViewById(R.id.myMessages_swipe);
        
        getMessageUsers();

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMessageUsers();
            }
        });
        
        return view;
    }

    private void getMessageUsers() {
        long userId = mUserService.getLoggedInUserId();
        mMessageService.getMyMessagedUsers(userId, new NetworkCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> result) {
                mErrorText.setVisibility(View.GONE);
                mMessageUsers = result;
                MessageUserAdapter adapter = new MessageUserAdapter(mContext, mMessageUsers);
                mListView.setAdapter(adapter);
                mProgressBar.setVisibility(View.GONE);
                mSwipeRefreshLayout.setRefreshing(false);
                mUsersContainer.setVisibility(View.VISIBLE);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = MessagesActivity.newIntent(mContext, mMessageUsers.get(i).getID(), mMessageUsers.get(i).getName());
                        startActivity(intent);
                    }
                });
                if (mMessageUsers.size() == 0) {
                    mErrorText.setText(R.string.no_messages);
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