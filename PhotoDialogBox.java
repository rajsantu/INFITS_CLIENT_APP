package com.example.infits;

import static android.app.Activity.RESULT_OK;
import static com.example.infits.EditProfile.profile_pic;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.getCacheDir;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/** @noinspection ALL*/
public class PhotoDialogBox extends AppCompatDialogFragment {

    private AppCompatButton take_photo, gallery, remove_photo;
    SharedPrefManager spf;
    Uri uri_for_camera;
    private static final int pic_id = 1001;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        spf = new SharedPrefManager(getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_edit_camera, null); //Inflating custom dialog box on the fragment

        builder.setView(view)
                .setTitle("Choose Action");

        take_photo = view.findViewById(R.id.option_take_photo);
        gallery = view.findViewById(R.id.option_gallery);
        remove_photo = view.findViewById(R.id.option_remove);

        take_photo.setOnClickListener(new View.OnClickListener() { // take photo button
            @Override
            public void onClick(View view) {
                dismiss();
                startActivity(new Intent(getApplicationContext(), cameraStart.class));
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() { // choose photo from gallery
            @Override
            public void onClick(View view) {
                dismiss();
                startActivity(new Intent(getApplicationContext(), galleryStart.class));
            }
        });
        remove_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                profile_pic.setImageResource(R.drawable.profilepic);
                EditProfile.bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profilepic);
                Bitmap bm = ((BitmapDrawable)profile_pic.getDrawable()).getBitmap();
                spf.createSession(bm);

            }
        });
        return builder.create();
    }
}
