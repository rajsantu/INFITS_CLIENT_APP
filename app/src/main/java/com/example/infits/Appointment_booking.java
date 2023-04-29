package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Appointment_booking extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DataFromDatabase dataFromDatabase;

    public String base64File;

    String extension, fileName;

    FrameLayout nextBtn, nowBtn, anyTimeBtn;
    private List<FrameLayout> frameLayoutList;

    Spinner customSpinner;
    String spinnerItem, add_dietitian, appointmentTimeText;
    ArrayList<CustomItem> customList;

    EditText description;

    private static final int PICKFILE_RESULT_CODE = 1;

    private RecyclerView recyclerView;
    private List<Date> dateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_booking);

        customSpinner = findViewById(R.id.customIconSpinner);
        customList = getCustomList();
        CustomeAdapterSpinner adapter = new CustomeAdapterSpinner(this, customList);

        if (customSpinner != null) {
            customSpinner.setAdapter(adapter);
            customSpinner.setOnItemSelectedListener(this);
        }

        customSpinner.post(new Runnable() {
            @Override
            public void run() {
                int width = customSpinner.getWidth();
                customSpinner.setDropDownWidth(width);
            }
        });

        nextBtn = findViewById(R.id.nextbtn);

        nowBtn = findViewById(R.id.nowBtn);
        anyTimeBtn = findViewById(R.id.anyTimeBtn);

        add_dietitian = "Smith M.";
        appointmentTimeText = "";
//        base64File = "attached";

//        fileName = DataFromDatabase.clientuserID+"_"+DataFromDatabase.dietitianuserID;
        fileName = "smith12_faizan29";

        description = findViewById(R.id.user_description);

        customSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CustomItem selectedItem = (CustomItem) parent.getItemAtPosition(position);
                spinnerItem = selectedItem.getSpinnerItem();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNowButtonSelected = nowBtn.isSelected();
                boolean isAnytimeButtonSelected = anyTimeBtn.isSelected();

                if (isNowButtonSelected) {
                    Intent intent = new Intent(getApplicationContext(), Appointment_Booking2_Now.class);
                    startActivity(intent);
                } else if (isAnytimeButtonSelected) {
                    Intent intent = new Intent(getApplicationContext(), Appointment_Booking2.class);
                    intent.putExtra("event_name", spinnerItem);
                    intent.putExtra("add_dietitian", add_dietitian);
                    intent.putExtra("appointmentTime", appointmentTimeText);
                    intent.putExtra("description", description.getText().toString());
                    if(base64File != null){
                        intent.putExtra("attachment", base64File);
                    }
                    intent.putExtra("file_type", extension);
                    intent.putExtra("file_name", fileName);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(Appointment_booking.this, "Please select the appointment type", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Define appointmentTime OnClickListener
        View.OnClickListener appointmentTime = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout dayLayout = (FrameLayout) v;

                // Check if this is the now or anytime button
                boolean isNowButton = dayLayout.getId() == R.id.nowBtn;
                boolean isAnytimeButton = dayLayout.getId() == R.id.anyTimeBtn;

                FrameLayout nowButton, anyTimeButton;
                TextView nowText, anytimeText;

                nowButton = findViewById(R.id.nowBtn);
                anyTimeButton = findViewById(R.id.anyTimeBtn);

                anytimeText = anyTimeButton.findViewById(R.id.anyTimeText);
                nowText = nowButton.findViewById(R.id.nowText);

                // If the now button is selected, deselect the anytime button (and vice versa)
                if (isNowButton) {
                    anyTimeButton.setSelected(false);
                    anyTimeButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background_white));
                    anytimeText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.skyBlue));
                    nowButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background));
                    nowText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
                    appointmentTimeText = nowText.getText().toString();
                } else if (isAnytimeButton) {
                    nowButton.setSelected(false);
                    nowButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background_white));
                    nowText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.skyBlue));

                    anyTimeButton.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.rounded_background));
                    anytimeText.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));

                    appointmentTimeText = anytimeText.getText().toString();
                }

                // Toggle the selection state of the clicked button
                dayLayout.setSelected(!dayLayout.isSelected());
                dayLayout.setBackground(ContextCompat.getDrawable(getApplicationContext(), dayLayout.isSelected() ? R.drawable.rounded_background : R.drawable.rounded_background_white));

                // Check if the child views exist before accessing them
                TextView dayText = null;
                TextView dateText = null;
                if (dayLayout.getChildCount() > 0) {
                    View child0 = dayLayout.getChildAt(0);
                    if (child0 instanceof TextView) {
                        dayText = (TextView) child0;
                    }
                }
                if (dayLayout.getChildCount() > 1) {
                    View child1 = dayLayout.getChildAt(1);
                    if (child1 instanceof TextView) {
                        dateText = (TextView) child1;
                    }
                }

                if (dayText != null) {
                    dayText.setTextColor(ContextCompat.getColor(getApplicationContext(), dayLayout.isSelected() ? R.color.white : R.color.skyBlue));
                }
                if (dateText != null) {
                    dateText.setTextColor(ContextCompat.getColor(getApplicationContext(), dayLayout.isSelected() ? R.color.white : R.color.skyBlue));
                }

                // Launch the appropriate activity based on the button selection
            }
        };

        // Set appointmentTime OnClickListener to nowBtn and anyTimeBtn FrameLayouts
        findViewById(R.id.nowBtn).setOnClickListener(appointmentTime);
        findViewById(R.id.anyTimeBtn).setOnClickListener(appointmentTime);

        findViewById(R.id.upload_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start an activity to select a file
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_RESULT_CODE);
            }
        });
    }

    // Upload attachment
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String filename = getFileName(uri);

            byte[] fileData = null;
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                int fileSize = inputStream.available();
                fileData = new byte[fileSize];
                inputStream.read(fileData);
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            base64File = Base64.encodeToString(fileData, Base64.DEFAULT);

            ContentResolver contentResolver = getContentResolver(); // Get a reference to the ContentResolver
            // Get the MIME type of the file from the Uri
            String fileType = contentResolver.getType(uri);

//            Typeface nats = Typeface.createFromAsset(getAssets(), "font/nats_regular.ttf");
            Typeface nats = ResourcesCompat.getFont(this, R.font.nats_regular);

            String fileDispName;
            // triming extension
            if(filename.length() > 32)
                fileDispName  = filename.substring(0,32)+"...";
            else
                fileDispName = filename.substring(0,filename.indexOf('.'));

//            Toast.makeText(this, filename, Toast.LENGTH_SHORT).show();

            // Create a new RelativeLayout for the uploaded file
            RelativeLayout fileLayout = new RelativeLayout(this);
            fileLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            fileLayout.setId(View.generateViewId());

            RelativeLayout.LayoutParams iconParam = new RelativeLayout.LayoutParams(
//                    RelativeLayout.LayoutParams.WRAP_CONTENT,
//                    RelativeLayout.LayoutParams.WRAP_CONTENT
                    40,100
            );

            RelativeLayout.LayoutParams deleteParam = new RelativeLayout.LayoutParams(27,100);

            RelativeLayout.LayoutParams shareParam = new RelativeLayout.LayoutParams(27,100);

            RelativeLayout.LayoutParams nameParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            RelativeLayout.LayoutParams dateParam = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );

            iconParam.addRule(RelativeLayout.CENTER_VERTICAL);

            // Create a new ImageView for the file type icon
            ImageView fileTypeIcon = new ImageView(this);
//            fileTypeIcon.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            fileTypeIcon.setImageResource(getFileTypeIcon(filename));
            fileTypeIcon.setId(View.generateViewId());
            fileTypeIcon.setLayoutParams(iconParam);
            fileLayout.addView(fileTypeIcon);

            nameParam.addRule(RelativeLayout.RIGHT_OF, fileTypeIcon.getId());
            nameParam.setMargins(25, -20, 0, 0);

            // Create a new TextView for the file name
            TextView fileNameView = new TextView(this);
//            fileNameView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
//            fileNameView.setText(filename);
            fileNameView.setTypeface(nats);
            fileNameView.setText(fileDispName);
            fileNameView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
            fileNameView.setTextColor(Color.parseColor("#000000"));
//            fileNameView.setPadding(25, 0, 0, 0);
            fileNameView.setLayoutParams(nameParam);
            fileNameView.setId(View.generateViewId());
            fileLayout.addView(fileNameView);

            dateParam.addRule(RelativeLayout.BELOW, fileNameView.getId());
            dateParam.addRule(RelativeLayout.RIGHT_OF, fileTypeIcon.getId());
            dateParam.setMargins(25, -50, 0, 0); // left, top, right, bottom


            // Create a new TextView for the file date
            TextView fileDateView = new TextView(this);
//            fileDateView.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            fileDateView.setText(getCurrentDate());
            fileDateView.setTypeface(nats);
            fileDateView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            fileDateView.setTextColor(Color.parseColor("#8D8D8D"));
            fileDateView.setLayoutParams(dateParam);
//            fileDateView.setPadding(25, 12, 0, 0);
            fileLayout.addView(fileDateView);

            deleteParam.addRule(RelativeLayout.CENTER_VERTICAL);
            deleteParam.addRule(RelativeLayout.ALIGN_PARENT_END);
            deleteParam.setMargins(0, 0, 20, 0);

            //imageview for delete
            ImageView deleteIcon = new ImageView(this);
            deleteIcon.setId(View.generateViewId());
            deleteIcon.setImageResource(R.drawable.delete_icon);
            deleteIcon.setLayoutParams(deleteParam);
            fileLayout.addView(deleteIcon);

            shareParam.addRule(RelativeLayout.CENTER_VERTICAL);
            shareParam.addRule(RelativeLayout.LEFT_OF,deleteIcon.getId());
            shareParam.setMargins(0, 0, 20, 0);

            //imageview for share
            ImageView shareIcon = new ImageView(this);
            shareIcon.setImageResource(R.drawable.share_icon);
            shareIcon.setLayoutParams(shareParam);
            fileLayout.addView(shareIcon);

            shareIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(Appointment_booking.this, "Share Clicked", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW);

                    // Set the data and MIME type of the file to be opened
                    intent.setDataAndType(uri, fileType);

                    // Add the FLAG_GRANT_READ_URI_PERMISSION flag to the intent
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                    // Start the activity with the intent
                    startActivity(intent);
                }
            });

            // Add the new RelativeLayout to the parent LinearLayout or RecyclerView
            // For example, if you have a LinearLayout with id "file_list":
            LinearLayout fileList = findViewById(R.id.file_list);
            if (fileList != null && fileLayout != null) {
                fileList.addView(fileLayout);
            } else {
                // Handle the case where fileList or fileLayout is null
            }

            //delete button functionality
            deleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(Appointment_booking.this, "Deleted " + filename, Toast.LENGTH_SHORT).show();
                    fileList.removeView(findViewById(fileLayout.getId()));
                }
            });
        }
    }

    private String setFileType(String ans){
        return ans;
    }

    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    int displayNameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (displayNameIndex >= 0) {
                        result = cursor.getString(displayNameIndex);
                    }
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1)
                result = result.substring(cut + 1);
        }
        return result;
    }

    // Helper method to get the current date in a specific format
    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Helper method to get the appropriate file type icon for a given file name
    private int getFileTypeIcon(String fileName) {
        extension = getFileExtension(fileName);
        switch (extension) {
            case "pdf":
                return R.drawable.pdf;
            case "doc":
            case "docx":
                return R.drawable.ic_baseline_document_scanner_24;
            case "jpg":
            case "jpeg":
                return R.drawable.ic_baseline_image_24;
            default:
                return R.drawable.ic_baseline_upload_file_24;
        }
    }

    // Helper method to get the file extension from a given file name
    private String getFileExtension(String fileName) {
        String extension = "";

        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            extension = fileName.substring(i + 1);
        }

        return extension.toLowerCase();
    }

    private ArrayList<CustomItem> getCustomList() {
        customList = new ArrayList<>();

        customList.add(new CustomItem("Follow up"));
        customList.add(new CustomItem("Consultation"));
        customList.add(new CustomItem("Diet Plan"));
        customList.add(new CustomItem("Diet Chart"));

        return customList;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        CustomItem item = (CustomItem) parent.getSelectedItem();
//        Toast.makeText(this, item.getSpinnerItem(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}