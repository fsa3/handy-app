package is.hi.handy_app;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.Networking.NetworkManager;

public class MainActivity extends AppCompatActivity {
    private List<Ad> mAds;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Eftirfarandi bara test sem sækir ads og sýnir titilinn á fyrsta ad-inu
        mTextView = (TextView) findViewById(R.id.first_text);

        NetworkManager networkManager = NetworkManager.getInstance(this);
        networkManager.getAds(new NetworkCallback<List<Ad>>() {
            @Override
            public void onSuccess(List<Ad> result) {
                mAds = result;
                mTextView.setText(mAds.get(0).getTitle());

                mTextView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onaFailure(String errorString) {
                Log.d(TAG, "Failed to get ads: " + errorString);
            }
        });
    }
}