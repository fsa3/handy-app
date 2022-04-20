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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

import is.hi.handy_app.Entities.PortfolioItem;
import is.hi.handy_app.Networking.NetworkCallback;
import is.hi.handy_app.R;
import is.hi.handy_app.Services.PortfolioItemService;

public class CreatePortfolioItemActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    public static final int CAMERA_PERMISSION_CODE = 101;
    public static final int CAMERA_REQUEST_CODE = 102;

    public static String PORTFOLIO_ITEM_SUCCESSFULLY_POSTED_EXTRA = "is.hi.handy_app.portfolio_item_successfully_posted";

    PortfolioItemService mPortfolioItemService;

    TextView mTitleTextView;
    TextView mLocationTextView;
    TextView mDescriptionTextView;
    ImageView mImageView;
    Button mPostButton;

    private Uri mImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_portfolio_item);

        mPortfolioItemService = new PortfolioItemService(this);

        mTitleTextView = findViewById(R.id.createPortfolioItem_title);
        mLocationTextView = findViewById(R.id.createPortfolioItem_location);
        mDescriptionTextView = findViewById(R.id.createPortfolioItem_description);
        mImageView = findViewById(R.id.createPortfolioItem_image);
        mPostButton = findViewById(R.id.createPortfolioItem_post);

        mImageView.setOnClickListener(view -> selectImage());

        mPostButton.setOnClickListener(view -> postPortfolioItem());

    }

    private void postPortfolioItem() {

        byte[] imageInBytes = {};
        if (mImageView.getDrawable() != null) {
            Bitmap bitmap =((BitmapDrawable) mImageView.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imageInBytes = baos.toByteArray();
        }

        PortfolioItem item = new PortfolioItem();
        item.setTitle(mTitleTextView.getText().toString());
        item.setLocation(mLocationTextView.getText().toString());
        item.setDescription(mDescriptionTextView.getText().toString());

        mPortfolioItemService.savePortfolioItem(item, imageInBytes, new NetworkCallback<PortfolioItem>() {
            @Override
            public void onSuccess(PortfolioItem result) {
                Intent data = new Intent();
                data.putExtra(PORTFOLIO_ITEM_SUCCESSFULLY_POSTED_EXTRA, true);
                setResult(RESULT_OK, data);
                finish();
            }

            @Override
            public void onaFailure(String errorString) {
                Snackbar snackbar = Snackbar.make(findViewById(R.id.createPortfolioItem_container),"Posting failed, error: " + errorString,Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        });
    }

    private void selectImage() {
        CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder bulder = new AlertDialog.Builder(CreatePortfolioItemActivity.this);
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