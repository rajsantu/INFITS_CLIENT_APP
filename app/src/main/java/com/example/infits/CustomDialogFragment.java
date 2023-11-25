package com.example.infits;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.AlarmClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.infits.R;
import com.shawnlin.numberpicker.NumberPicker;

public class CustomDialogFragment extends DialogFragment {
    private ImageView cancel;
    private Button setAlarm,cancelAlarmBtn;
    private EditText label;
    private Context context;
    private int hrsVal,minutesVal,ampmVal;
    private ImageButton uparrow1, uparrow2,uparrow3, downarrow1, downarrow2,downarrow3;
    private NumberPicker numberPicker1, numberPicker2,numberPicker3;
    private  String[] numberPicker3List = {"AM","PM"};
    private  String[] numberPicker2List =
            {"00","01","02","03","04","05","06","07","08","09","10",
                    "11","12","13","14","15","16","17","18","19","20",
                    "21","22","23","24","25","26","27","28","29","30",
                    "31","32","33","34","35","36","37","38","39","40",
                    "41","42","43","44","45","46","47","48","49","50",
                    "51","52","53","54","55","56","57","58","59"};

    private TextView selectedTime,min,ampm;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private  AlarmManager alarmManager;
    private int hoursIn24Format;
    @SuppressLint("MissingInflatedId")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the custom layout
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.set_an_alarm, null);
        setAlarm = view.findViewById(R.id.setAlarm);
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hoursIn24Format = convertTo24HourFormat(hrsVal,ampmVal );
                checkValidation(label.getText().toString());
                // All permissions are granted, proceed with your alarm-related functionality
            }
        });

        // Override onRequestPermissionsResult to handle the result of the permission request
        label = view.findViewById(R.id.label);
        cancel = view.findViewById(R.id.cancelAlarm);
        uparrow1 = view.findViewById(R.id.uparrow1);
        uparrow2 = view.findViewById(R.id.uparrow2);
        uparrow3 = view.findViewById(R.id.uparrow3);
        downarrow1 = view.findViewById(R.id.downarrow1);
        downarrow2 = view.findViewById(R.id.downarrow2);
        downarrow3 = view.findViewById(R.id.downarrow3);
        numberPicker1 = view.findViewById(R.id.numberPicker1);
        numberPicker2 = view.findViewById(R.id.numberPicker2);
        numberPicker3 = view.findViewById(R.id.numberPicker3);
        numberPicker2.setDisplayedValues(numberPicker2List);
        numberPicker2.setMaxValue(numberPicker2List.length-1);
        numberPicker3.setDisplayedValues(numberPicker3List);
        numberPicker3.setMaxValue(numberPicker3List.length );
        selectedTime = view.findViewById(R.id.selectedTime);
        min = view.findViewById(R.id.min);
        ampm = view.findViewById(R.id.ampm);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });


        selectedTime.setText(String.format("%s",numberPicker1.getValue()));
        minutesVal = (numberPicker2.getValue());
        min.setText(String.format("%s",numberPicker2List[numberPicker2.getValue()]));

        hrsVal = numberPicker1.getValue();
        ampmVal = numberPicker3.getValue();
        ampm.setText(String.format("%s",numberPicker3List[numberPicker3.getValue()-1]));

//         Find and set up dialog elements
        NumberPicker.OnValueChangeListener numberPickerListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (picker == numberPicker1) {
                    hrsVal = newVal;
                    selectedTime.setText(String.format("%s", newVal));
                } else if (picker == numberPicker2) {
                    minutesVal = newVal;
                    min.setText(String.format("%s", numberPicker2List[newVal]));
                } else if (picker == numberPicker3) {
                    ampmVal = newVal - 1;
                    ampm.setText(String.format("%s", numberPicker3List[newVal - 1]));
                }
            }
        };
//jhhjbmnb
// Set the common listener for your NumberPickers
        numberPicker1.setOnValueChangedListener(numberPickerListener);
        numberPicker2.setOnValueChangedListener(numberPickerListener);
        numberPicker3.setOnValueChangedListener(numberPickerListener);

// Create a common listener for arrow buttons
        View.OnClickListener arrowListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NumberPicker picker = null;
                if (v == uparrow1) {
                    picker = numberPicker1;
                } else if (v == downarrow1) {
                    picker = numberPicker1;
                } else if (v == uparrow2) {
                    picker = numberPicker2;
                } else if (v == downarrow2) {
                    picker = numberPicker2;
                } else if (v == uparrow3) {
                    picker = numberPicker3;
                } else if (v == downarrow3) {
                    picker = numberPicker3;
                }

                if (picker != null) {
                    if (v == uparrow1 || v == downarrow1) {
                        hrsVal = picker.getValue();
                        selectedTime.setText(String.format("%s", hrsVal));
                    } else if (v == uparrow2 || v == downarrow2) {
                        minutesVal = picker.getValue();
                        //  min.setText(String.format("%s", minutesVal));
                        min.setText(String.format("%s", numberPicker2List[numberPicker2.getValue()-1]));
                    } else if (v == uparrow3 || v == downarrow3) {
                        ampmVal = picker.getValue() - 1;
                        ampm.setText(String.format("%s", numberPicker3List[picker.getValue() - 1]));
                    }
                }

                if (v == uparrow1 || v == uparrow2 || v == uparrow3) {
                    picker.setValue(picker.getValue() + 1);
                } else if (v == downarrow1 || v == downarrow2 || v == downarrow3) {
                    picker.setValue(picker.getValue() - 1);
                }
            }
        };

// Set the common listener for your arrow buttons
        uparrow1.setOnClickListener(arrowListener);
        downarrow1.setOnClickListener(arrowListener);
        uparrow2.setOnClickListener(arrowListener);
        downarrow2.setOnClickListener(arrowListener);
        uparrow3.setOnClickListener(arrowListener);
        downarrow3.setOnClickListener(arrowListener);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();
    }

    private void checkValidation(String labelString) {
        if(labelString.isEmpty()){
            label.setError("please enter label");
        }
        else {
            setAlarm(labelString);
        }
    }

    private int convertTo24HourFormat ( int selectedHours, int amPmValue){
        if (amPmValue == 1 && selectedHours < 12) {
            selectedHours += 12;
        } else if (amPmValue == 0 && selectedHours == 12) {
            selectedHours = 0;
        }
        return selectedHours;
    }
    private void setAlarm(String labelString) {
        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM);
        intent.putExtra(AlarmClock.EXTRA_HOUR,hoursIn24Format);
        intent.putExtra(AlarmClock.EXTRA_MINUTES,minutesVal);
        intent.putExtra(AlarmClock.EXTRA_MESSAGE, labelString); // Set the alarm message
        intent.putExtra(AlarmClock.EXTRA_SKIP_UI, true);
        Toast.makeText(getActivity().getApplicationContext(), "Alarm Set", Toast.LENGTH_SHORT).show();
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivity(intent);
            dismiss();
        }
    }


    // Create and return the dialog

    private void setupNumberPicker(NumberPicker numberPicker, ImageButton upButton, ImageButton downButton) {


        if (upButton != null) {
            upButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberPicker.setValue(numberPicker.getValue() + 1);

                    Log.d("TAG", "up arrow clicked.New value" + numberPicker.getValue());
                }
            });
        }

        if (downButton != null) {
            downButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    numberPicker.setValue(numberPicker.getValue() - 1);
                    Log.d("TAG", "down arrow clicked.New value" + numberPicker.getValue());
                }
            });
        }

        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.d("TAG", "numberPicker scrolled");
                numberPicker.setValue(newVal);
            }
        });
    }


}
