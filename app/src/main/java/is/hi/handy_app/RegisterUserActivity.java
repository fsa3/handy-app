package is.hi.handy_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import is.hi.handy_app.Entities.User;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Services.UserService;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener{

    public static String USER_SUCCESSFULLY_POSTED_EXTRA = "is.hi.handy_app.user_successfully_posted";
    public static String CREATE_HANDYMAN_ACCOUNT = "is.hi.handy_app.user_create_handyman_account";

    private UserService mUserService;

    private TextView mBanner, mRegisterUser;
    private EditText mNameText, mEmailText, mPasswordText;
    private ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mUserService = new UserService(this);

        mBanner = (TextView) findViewById(R.id.bannerDescription);
        mBanner.setOnClickListener(this);

        mRegisterUser = (Button) findViewById(R.id.registerUser);
        mRegisterUser.setOnClickListener(this);

        mNameText = (EditText) findViewById(R.id.fullName);
        mEmailText = (EditText) findViewById(R.id.email);
        mPasswordText = (EditText) findViewById(R.id.password);

        mProgressBar = (ProgressBar) findViewById(R.id.loading);

        findViewById(R.id.noHandyAccLink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent data = new Intent();
                data.putExtra(CREATE_HANDYMAN_ACCOUNT, true);
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.bannerDescription:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerUser();
                break;
        }
    }

    private void registerUser() {
        Log.d("clicked", "register user clicked!");
        String name = mNameText.getText().toString().trim();
        String email = mEmailText.getText().toString().trim();
        String password = mPasswordText.getText().toString().trim();

        if(name.isEmpty()){
            mNameText.setError("Name is required");
            mNameText.requestFocus();
            return;
        }

        if(email.isEmpty()){
            mEmailText.setError("Email is required");
            mEmailText.requestFocus();
            return;
        }

        if(password.isEmpty()){
            mPasswordText.setError("Password is required");
            mPasswordText.requestFocus();
            return;
        }

        if(password.length() < 6){
            mPasswordText.setError("Min password length should be 6 characters");
            mPasswordText.requestFocus();
            return;
        }

        mProgressBar.setVisibility(View.VISIBLE);

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        Log.d("user to create", user.getName());
        mUserService.saveUser(user, false, new NetworkCallback<User>() {
            @Override
            public void onSuccess(User result) {
                Intent data = new Intent();
                data.putExtra(USER_SUCCESSFULLY_POSTED_EXTRA, true);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onaFailure(String errorString) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.registerUser_container), "Registration failed, error: " + errorString,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });

    }
}