package com.example.infits;

import static android.app.PendingIntent.getActivity;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class galleryStart extends AppCompatActivity {

    SharedPrefManager spf;
    Bitmap photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_start);

        ActivityResultLauncher<Intent> activityResultLauncher_pick_photo = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    spf = new SharedPrefManager(getApplicationContext());

                    EditProfile.profile_pic.setImageURI(result.getData().getData());
                    photo=getBitmapFromURI(result.getData().getData());
                    DataFromDatabase.profile = photo;
                    spf.createSession(photo);
                    finish();
                }
        );
        Intent pickPhoto = new Intent(Intent.ACTION_PICK);
        pickPhoto.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncher_pick_photo.launch(pickPhoto);
    }

    public Bitmap getBitmapFromURI(Uri uri){
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, filePathColumn, null, null, null);
        if(cursor!=null){
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            File f_image = new File(cursor.getString(columnIndex));
            cursor.close();
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            return BitmapFactory.decodeFile(f_image.getAbsolutePath(), o2);
        }
        return null;
    }

    @Override
    public void onBackPressed()
    {
        finish();
    }
}