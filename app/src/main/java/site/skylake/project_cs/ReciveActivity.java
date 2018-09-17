package site.skylake.project_cs;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ReciveActivity extends AppCompatActivity {

    ImageView ivCamerarev, ivGalleryrev, ivUploadrev, ivImagerev;
    CameraPhoto cameraPhotorev;
    GalleryPhoto galleryPhotorev;
    RatingBar ratingBarrev;

    ProgressDialog pdrev;

    public static final int RequestPermissionCode  = 1 ;

    final int CAMERA_REQUEST = 1100;
    final int GALLERY_REQUEST = 1200;

    String selectdPhoto;
    String encodeImage;
    String rating;
    String imageId;

    private static final String loginURL = "http://10.0.0.103/bookbiwproject/staff/api/process_revice.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recive);

        EnableRuntimePermissionToAccessCamera();

        final Intent intent = getIntent();
        imageId = intent.getStringExtra("id");

        cameraPhotorev = new CameraPhoto(getApplicationContext());
        galleryPhotorev = new GalleryPhoto(getApplicationContext());

        ivImagerev = findViewById(R.id.ivImage_rev);
        ivCamerarev = findViewById(R.id.ivCamera_rev);
        ivGalleryrev = findViewById(R.id.ivGallery_rev);
        ivUploadrev = findViewById(R.id.ivUpload_rev);
        ratingBarrev = findViewById(R.id.ratingBar_rev);
        pdrev = new ProgressDialog(this);
        pdrev.setMessage("Loading...");

        ratingBarrev.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = String.valueOf(v);
                switch ((int) ratingBar.getRating()){
                    case 1:
                        rating = "1";
                        break;
                    case 2:
                        rating = "2";
                        break;
                    case 3:
                        rating = "3";
                        break;
                    case 4:
                        rating = "4";
                        break;
                    case 5:
                        rating = "5";
                        break;
                    default:
                        rating = "0";
                        break;
                }
            }
        });

        ivCamerarev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startActivityForResult(cameraPhotorev.takePhotoIntent(),CAMERA_REQUEST);
                    cameraPhotorev.addToGallery();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Something Wrong while taking photos", Toast.LENGTH_SHORT).show();
                }
            }

        });

        ivGalleryrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(galleryPhotorev.openGalleryIntent(),GALLERY_REQUEST);
            }
        });

        ivUploadrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectdPhoto == null || selectdPhoto == ""){
                    Toast.makeText(getApplicationContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                    return;
                }else if (rating == null || rating == ""){
                    Toast.makeText(getApplicationContext(), "No Rating Selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    Bitmap bitmap = ImageLoader.init().from(selectdPhoto).requestSize(1024, 1024).getBitmap();
                    encodeImage = ImageBase64.encode(bitmap);
//                    Log.d(TAG, encodeImage);

                    ResponImage();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void ResponImage() {
        StringRequest request = new StringRequest(Request.Method.POST, loginURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.contains("success")){
                    pdrev.dismiss();
                    Toast.makeText(getApplicationContext(), "Upload Success", Toast.LENGTH_SHORT).show();
                    Intent inmain = new Intent(getApplicationContext(), UserMainActivity.class);
                    startActivity(inmain);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", imageId);
                params.put("data", encodeImage);
                params.put("rating", rating);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
        pdrev.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == CAMERA_REQUEST){
                String photoPath = cameraPhotorev.getPhotoPath();
                selectdPhoto = photoPath;
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).getBitmap();
                    ivImagerev.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(),
                            "Something Wrong while loading photos", Toast.LENGTH_SHORT).show();
                }
            }
            else if(requestCode == GALLERY_REQUEST){
                Uri uri = data.getData();

                galleryPhotorev.setPhotoUri(uri);
                String photoPath = galleryPhotorev.getPath();
                selectdPhoto = photoPath;
                try {
                    Bitmap bitmap = ImageLoader.init().from(photoPath).requestSize(512, 512).getBitmap();
                    ivImagerev.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    Toast.makeText(getApplicationContext(), "Something Wrong while choosing photos", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void EnableRuntimePermissionToAccessCamera() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA))
        {
            // Printing toast message after enabling runtime permission.
            Toast.makeText(this,"CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }
}
