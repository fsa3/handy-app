package is.hi.handy_app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import is.hi.handy_app.Entities.User;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Services.UserService;

public class LoginFragment extends Fragment {
    private Context mContext;
    private UserService mUserService;

    private EditText mEmailText;
    private EditText mPasswordText;
    private Button mLoginButton;
    private ProgressBar mProgressBar;
    private TextView mLoginMessage;

    private TextView mCreateAccLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mContext = LoginFragment.this.getActivity();
        mUserService = new UserService(mContext);

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ((MainActivity) LoginFragment.this.requireActivity()).hideSearch();

        mCreateAccLink = (TextView) view.findViewById(R.id.noAccLink);

        mEmailText = view.findViewById(R.id.email);
        mPasswordText = view.findViewById(R.id.password);
        mLoginButton = view.findViewById(R.id.login);
        mProgressBar = view.findViewById(R.id.loading);
        mLoginMessage = view.findViewById(R.id.login_message);
        
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                mLoginMessage.setVisibility(View.GONE);
                mUserService.login(mEmailText.getText().toString(), mPasswordText.getText().toString(), new NetworkCallback<User>() {
                    @Override
                    public void onSuccess(User result) {
                        mProgressBar.setVisibility(View.GONE);
                        ((MainActivity) mContext).resetMenu();

                        Fragment handymenFragment = new HandymenFragment();
                        FragmentManager fragmentManager = LoginFragment.this.getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, handymenFragment)
                                .addToBackStack(null)
                                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                .commit();

                        Snackbar snackbar = Snackbar.make(view, "Logged in as " + result.getName(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }

                    @Override
                    public void onaFailure(String errorString) {
                        Toast.makeText(mContext, "Not logged in", Toast.LENGTH_SHORT).show();
                        mProgressBar.setVisibility(View.GONE);
                        mLoginMessage.setText(getResources().getString(R.string.login_failed));
                        mLoginMessage.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        mCreateAccLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, RegisterUserActivity.class));
            }
        });


        return view;
    }
}