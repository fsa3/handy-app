package is.hi.handy_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import is.hi.handy_app.Entities.HandyUser;
import is.hi.handy_app.Entities.Trade;
import is.hi.handy_app.Services.UserService;

public class RegisterHandyUserActivity extends AppCompatActivity implements View.OnClickListener{

    private UserService mUserService;

    private TextView mBanner, mRegisterUser;
    private EditText mNameText, mEmailText, mPasswordText;
    private Spinner mSpinner;
    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_handy_user);

        mUserService = new UserService(this);

        mBanner = (TextView) findViewById(R.id.bannerDescription);
        mBanner.setOnClickListener(this);

        mRegisterUser = (Button) findViewById(R.id.registerUser);
        mRegisterUser.setOnClickListener(this);

        mNameText = (EditText) findViewById(R.id.fullName);
        mEmailText = (EditText) findViewById(R.id.email);
        mPasswordText = (EditText) findViewById(R.id.password);

        mSpinner = (Spinner) findViewById(R.id.spinner);
        mSpinner.setAdapter(new ArrayAdapter<Trade>(this, android.R.layout.simple_spinner_item, Trade.values()));

        mProgressBar = (ProgressBar) findViewById(R.id.loading);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bannerDescription:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.registerUser:
                registerHandyUser();
        }
    }

        private void registerHandyUser() {
            String name = mNameText.getText().toString().trim();
            String email = mEmailText.getText().toString().trim();
            String password = mPasswordText.getText().toString().trim();
            //Trade trade = mSpinner; //TODO:

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

            if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                mEmailText.setError("Please provide valid email");
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

            HandyUser handyUser = new HandyUser();
            handyUser.setName(name);
            handyUser.setEmail(email);
            handyUser.setPassword(password);
            //handyUser.setTrade(trade);


            // saveHandyUser...
        }
    }
}