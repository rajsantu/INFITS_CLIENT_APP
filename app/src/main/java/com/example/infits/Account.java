package com.example.infits;

import static android.content.Context.MODE_PRIVATE;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Account#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Account extends Fragment {

    DataFromDatabase dataFromDatabase;
    ImageView male, female,profile_pic, backBtn;

    RequestQueue queue;
    Button logout,save, editProfile;
    ImageButton yesLogout, noLogout;
    String client_gender, cleint_name, client_age, client_email,client_phoneno,client_userID;

    String client_weight,client_height,client_about;

    private Bitmap bitmap = DataFromDatabase.profile;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    String url = String.format("%ssave.php",DataFromDatabase.ipConfig);

    ActivityResultLauncher<String> photo;

    File file;

    String fileName, path;

    String gen = "M";

    Bitmap photoBit;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Account() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Account.
     */
    // TODO: Rename and change types and number of parameters
    public static Account newInstance(String param1, String param2) {
        Account fragment = new Account();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    //The code of infits


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

//        male = view.findViewById(R.id.gender_male_icon);
//        female=view.findViewById(R.id.gender_female_icon);
        EditText name=view.findViewById(R.id.name_edt);
        name.setText(DataFromDatabase.name);
        EditText age=view.findViewById(R.id.age_edt);
        age.setText(DataFromDatabase.age);
        EditText email=view.findViewById(R.id.email_edt);
        email.setText(DataFromDatabase.email);
        EditText phone=view.findViewById(R.id.phone_edt);
        EditText location=view.findViewById(R.id.location_edt);
        location.setText(DataFromDatabase.location);
        EditText weight=view.findViewById(R.id.weight_edt);
        weight.setText(DataFromDatabase.weight);
        EditText height=view.findViewById(R.id.height_edt);
        height.setText(DataFromDatabase.height);
        EditText plan = view.findViewById(R.id.plan_edt);
        if (DataFromDatabase.gender==("true")) {
            plan.setText("Pro");
        } else if (DataFromDatabase.gender==("false")){
            plan.setText("Basic");
        } else {
            plan.setText("None");
        }
        EditText gender = view.findViewById(R.id.gender_edt);
        if (DataFromDatabase.gender==("M")) {
            gender.setText("Male");
        } else if (DataFromDatabase.gender==("F")){
            gender.setText("Female");
        } else {
            gender.setText("None");
        }
        gender.setText(DataFromDatabase.gender);
        phone.setText(DataFromDatabase.mobile);
        profile_pic=view.findViewById(R.id.dp);
        logout=view.findViewById(R.id.button_logout);
        profile_pic.setImageBitmap(DataFromDatabase.profile);
        backBtn = view.findViewById(R.id.imgBack);
        editProfile = view.findViewById(R.id.button_editProfile);

        backBtn.setOnClickListener(v -> requireActivity().onBackPressed());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(requireContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.logoutdialog);

                yesLogout = dialog.findViewById(R.id.yes_log_out);
                noLogout = dialog.findViewById(R.id.no_log_out);

                yesLogout.setOnClickListener(it -> {
                    dialog.dismiss();

                    SharedPreferences loginDetails = requireActivity().getSharedPreferences("loginDetails",MODE_PRIVATE);
                    SharedPreferences.Editor editor = loginDetails.edit();
                    editor.clear();
                    editor.apply();

                    Intent i= new Intent(getActivity(),Login.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    requireActivity().finish();
                });

                noLogout.setOnClickListener(it -> dialog.dismiss());

                dialog.show();
            }
        });

        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfile.class);
            startActivity(intent);
        });

        queue = Volley.newRequestQueue(requireContext());

        return view;

    }

    private String getEncodedImg(Bitmap bitmap) {
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bo);
        byte[] imgByte = bo.toByteArray();

        return Base64.encodeToString(imgByte, Base64.DEFAULT);
    }

    private void updateDataLocally(String nameStr, String ageStr, String emailStr, String mobile) {
        DataFromDatabase.profile = bitmap;
        DataFromDatabase.name = nameStr;
        DataFromDatabase.age = ageStr;
        DataFromDatabase.email = emailStr;
        DataFromDatabase.mobile = mobile;
        DataFromDatabase.gender = client_gender;

        SharedPreferences prefs = requireActivity().getSharedPreferences("loginDetails",MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("name",nameStr);
        editor.putString("email",emailStr);
        editor.putString("mobile",mobile);
        editor.putString("age",ageStr);
        editor.putString("gender",client_gender);
        editor.putString("profilePhotoBase",getEncodedImg(bitmap));
        editor.putString("profilePhoto",getEncodedImg(bitmap));

        editor.apply();
    }

    public String getStringOfImage(Bitmap bm){
        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG,100,bo);
        byte[] imageByte = bo.toByteArray();
        String imageEncode = Base64.encodeToString(imageByte, Base64.DEFAULT);
        return imageEncode;
    }

    private void selectImage() {
        try {
            PackageManager pm = requireActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, requireActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                //final CharSequence[] options = {"Take Photo", "Choose From Gallery","Remove picture","Cancel"};
                final CharSequence[] options = { "Choose From Gallery","Remove picture","Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                      /*  if (options[item].equals("Take Photo")) {
                            dialog.cancel();
                            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else*/
                        if (options[item].equals("Choose From Gallery")) {
                            dialog.cancel();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        } else if (options[item].equals("Cancel")) {
                            dialog.cancel();
                        }
                        else if(options[item].equals("Remove picture")){
                            dialog.dismiss();
                            profile_pic.setImageResource(R.drawable.profilepic);
                            bitmap = BitmapFactory.decodeResource(requireActivity().getResources(), R.drawable.profilepic);
                        }
                        else
                            dialog.cancel();
                    }
                });
                builder.show();
            } else {
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Change app permission in your device settings", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Change app permission in your device settings", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_GALLERY) {
            try {
                if( data.getData() != null) {
                    Uri selectedImage = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                    //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    // bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                    profile_pic.setImageBitmap(bitmap);
                  /*  imgPath = getRealPathFromURI(selectedImage);
                    destination = new File(imgPath.toString());*/
                }
            } catch (Exception e) {
                Toast.makeText(getActivity(),"No picture selected",Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = getActivity().managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
}