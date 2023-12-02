package com.example.infits;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.AppCompatButton;

public class PhotoDialogBox extends AppCompatDialogFragment {

    private AppCompatButton take_photo, gallery, remove_photo;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
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
                Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
        gallery.setOnClickListener(new View.OnClickListener() { // choose photo from gallery
            @Override
            public void onClick(View view) {
                dismiss();
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, 2);
            }
        });
        remove_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                EditProfile.profile_pic.setImageResource(R.drawable.profilepic);
                EditProfile.bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.profilepic);
            }
        });

        return builder.create();
    }
}
