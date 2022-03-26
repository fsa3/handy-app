package is.hi.handy_app;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext =  HandymenFragment.this.getActivity();
        mUserService = new UserService(mContext);

        View view = inflater.inflate(R.layout.fragment_handymen, container, false);
        mListView = (ListView) view.findViewById(R.id.handymen_listview);
        mProgressBar = (ProgressBar) view.findViewById(R.id.handymen_progressbar);
        mErrorText = (TextView) view.findViewById(R.id.handymen_error);

        mUserService.findAllHandyUsers(new NetworkCallback<List<HandyUser>>() {
            @Override
            public void onSuccess(List<HandyUser> result) {
                mHandyUsers = result;
                HandyUserAdapter adapter = new HandyUserAdapter(mContext, mHandyUsers);
                mListView.setAdapter(adapter);
                mProgressBar.setVisibility(View.GONE);
                mListView.setVisibility(View.VISIBLE);
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(HandymenFragment.this.getActivity() , mHandyUsers.get(i).getName(), Toast.LENGTH_SHORT).show();
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
