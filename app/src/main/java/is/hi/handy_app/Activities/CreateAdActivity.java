package is.hi.handy_app.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import is.hi.handy_app.Entities.Ad;
import is.hi.handy_app.Entities.Trade;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.R;
import is.hi.handy_app.Services.AdService;

public class CreateAdActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;

    public static String AD_SUCCESSFULLY_POSTED_EXTRA = "is.hi.handy_app.ad_successfully_posted";

    AdService mAdService;

    TextView mTitleTextView;
    Spinner mTradeSpinner;
    TextView mLocationTextView;
    TextView mDescriptionTextView;
    ImageView mImageView;
    Button mPostButton;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_ad_activity);

        mAdService = new AdService(this);

        mTitleTextView = findViewById(R.id.createAd_title);
        mTradeSpinner = findViewById(R.id.createAd_trade_spinner);
        mLocationTextView = findViewById(R.id.createAd_location);
        mDescriptionTextView = findViewById(R.id.createAd_description);
        mImageView = findViewById(R.id.createAd_image);
        mPostButton = findViewById(R.id.createAd_post);

        List<String> trades = Stream.of(Trade.values())
                .map(Trade::name)
                .collect(Collectors.toList());
        mTradeSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trades));

        mImageView.setOnClickListener(view -> selectImage());

        mPostButton.setOnClickListener(view -> postAd());
    }

    private void postAd() {

        byte[] imageInBytes = {};
        if (mImageView.getDrawable() != null) {
            Bitmap bitmap =((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageInBytes = baos.toByteArray();
        }

        Ad ad = new Ad();
        ad.setTitle(mTitleTextView.getText().toString());
        ad.setTrade(Trade.valueOf(mTradeSpinner.getSelectedItem().toString()));
        ad.setLocation(mLocationTextView.getText().toString());
        ad.setDescription(mDescriptionTextView.getText().toString());

        mAdService.saveAd(ad, imageInBytes, new NetworkCallback<Ad>() {
            @Override
            public void onSuccess(Ad result) {
                Intent data = new Intent();
                data.putExtra(AD_SUCCESSFULLY_POSTED_EXTRA, true);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onaFailure(String errorString) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.createAd_container),"Posting failed, error: " + errorString,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void selectImage() {
        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder bulder = new AlertDialog.Builder(CreateAdActivity.this);
        bulder.setTitle("Add Photo!");
        bulder.setItems(options, (dialogInterface, i) -> {
            if (i == 0) {
                askCameraPermission();
            }
            else if (i == 1) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
            else if (i == 2) {
                dialogInterface.dismiss();
            }
        });
        bulder.show();
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION_CODE);
        }
        else {
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to use camera", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();
            mImageView.setImageURI(mImageUri);
        }
        else if (requestCode == CAMERA_REQUEST_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(image);
        }
    }
}