package com.example.infits;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infits.customDialog.SectionPref;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SectionOneQEight#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionOneQEight extends Fragment {

    ImageButton imgBack;
    Button nextbtn;
    TextView backbtn, shifttv;
    RadioButton sGeneral,sMorning, sEvening, sChange, sNA;
    String shift ="";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SectionOneQEight() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SectionOneQEight.
     */
    // TODO: Rename and change types and number of parameters
    public static SectionOneQEight newInstance(String param1, String param2) {
        SectionOneQEight fragment = new SectionOneQEight();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_section_one_q_eight, container, false);

        //sGeneral,sMorning, sEvening, sChange, sNA;

        imgBack = view.findViewById(R.id.imgback);
        nextbtn = view.findViewById(R.id.nextbtn);
        backbtn = view.findViewById(R.id.backbtn);
        sGeneral = view.findViewById(R.id.sGeneral);
        sMorning = view.findViewById(R.id.sMorning);
        sEvening = view.findViewById(R.id.sEvening);
        sChange = view.findViewById(R.id.sChange);
        sNA = view.findViewById(R.id.sNA);
        RadioGroup r=view.findViewById(R.id.radioGroup);
        shifttv = view.findViewById(R.id.textView77);

        TextView gotomain = view.findViewById(R.id.gotomainsection);
        gotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_sectionOneQTwo_to_consultationFragment);

            }
        });


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("STEP1Q8", Context.MODE_PRIVATE);
        String storedvalue = sharedPreferences.getString("shift", "");
        if(!storedvalue.isEmpty()) {
            switch (storedvalue) {
                case "General Shift":
                    sGeneral.setBackgroundResource(R.drawable.radiobtn_on);
                    sMorning.setBackgroundResource(R.drawable.radiobtn_off);
                    sEvening.setBackgroundResource(R.drawable.radiobtn_off);
                    sChange.setBackgroundResource(R.drawable.radiobtn_off);
                    sNA.setBackgroundResource(R.drawable.radiobtn_off);

                    sGeneral.setTextColor(Color.WHITE);
                    sMorning.setTextColor(Color.BLACK);
                    sEvening.setTextColor(Color.BLACK);
                    sChange.setTextColor(Color.BLACK);
                    sNA.setTextColor(Color.BLACK);
                    shift="General Shift";
                    break;
                case "Morning Shift":
                    sGeneral.setBackgroundResource(R.drawable.radiobtn_off);
                    sMorning.setBackgroundResource(R.drawable.radiobtn_on);
                    sEvening.setBackgroundResource(R.drawable.radiobtn_off);
                    sChange.setBackgroundResource(R.drawable.radiobtn_off);
                    sNA.setBackgroundResource(R.drawable.radiobtn_off);

                    sMorning.setTextColor(Color.WHITE);
                    sGeneral.setTextColor(Color.BLACK);
                    sEvening.setTextColor(Color.BLACK);
                    sChange.setTextColor(Color.BLACK);
                    sNA.setTextColor(Color.BLACK);

                    shift="Morning Shift";
                    break;
                case "Evening Shift":
                    sGeneral.setBackgroundResource(R.drawable.radiobtn_off);
                    sMorning.setBackgroundResource(R.drawable.radiobtn_off);
                    sEvening.setBackgroundResource(R.drawable.radiobtn_on);
                    sChange.setBackgroundResource(R.drawable.radiobtn_off);
                    sNA.setBackgroundResource(R.drawable.radiobtn_off);

                    sEvening.setTextColor(Color.WHITE);
                    sMorning.setTextColor(Color.BLACK);
                    sGeneral.setTextColor(Color.BLACK);
                    sChange.setTextColor(Color.BLACK);
                    sNA.setTextColor(Color.BLACK);

                    shift="Evening Shift";
                    break;
                case "Shift changes every week or every 2 weeks or monthly":
                    sGeneral.setBackgroundResource(R.drawable.radiobtn_off);
                    sMorning.setBackgroundResource(R.drawable.radiobtn_off);
                    sEvening.setBackgroundResource(R.drawable.radiobtn_off);
                    sChange.setBackgroundResource(R.drawable.radiobtn_on);
                    sNA.setBackgroundResource(R.drawable.radiobtn_off);

                    sChange.setTextColor(Color.WHITE);
                    sMorning.setTextColor(Color.BLACK);
                    sEvening.setTextColor(Color.BLACK);
                    sGeneral.setTextColor(Color.BLACK);
                    sNA.setTextColor(Color.BLACK);

                    shift="Shift changes every week or every 2 weeks or monthly";
                    break;
                case "NA":
                    sGeneral.setBackgroundResource(R.drawable.radiobtn_off);
                    sMorning.setBackgroundResource(R.drawable.radiobtn_off);
                    sEvening.setBackgroundResource(R.drawable.radiobtn_off);
                    sChange.setBackgroundResource(R.drawable.radiobtn_off);
                    sNA.setBackgroundResource(R.drawable.radiobtn_on);

                    sNA.setTextColor(Color.WHITE);
                    sMorning.setTextColor(Color.BLACK);
                    sEvening.setTextColor(Color.BLACK);
                    sChange.setTextColor(Color.BLACK);
                    sGeneral.setTextColor(Color.BLACK);

                    shift="NA";
                    break;
                default:
            }
            DataSectionOne.shift = storedvalue;
        }


        sGeneral.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sGeneral.setBackgroundResource(R.drawable.radiobtn_on);
                sMorning.setBackgroundResource(R.drawable.radiobtn_off);
                sEvening.setBackgroundResource(R.drawable.radiobtn_off);
                sChange.setBackgroundResource(R.drawable.radiobtn_off);
                sNA.setBackgroundResource(R.drawable.radiobtn_off);

                sGeneral.setTextColor(Color.WHITE);
                sMorning.setTextColor(Color.BLACK);
                sEvening.setTextColor(Color.BLACK);
                sChange.setTextColor(Color.BLACK);
                sNA.setTextColor(Color.BLACK);

                shift="General Shift";
            }
        });

        sMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sGeneral.setBackgroundResource(R.drawable.radiobtn_off);
                sMorning.setBackgroundResource(R.drawable.radiobtn_on);
                sEvening.setBackgroundResource(R.drawable.radiobtn_off);
                sChange.setBackgroundResource(R.drawable.radiobtn_off);
                sNA.setBackgroundResource(R.drawable.radiobtn_off);

                sMorning.setTextColor(Color.WHITE);
                sGeneral.setTextColor(Color.BLACK);
                sEvening.setTextColor(Color.BLACK);
                sChange.setTextColor(Color.BLACK);
                sNA.setTextColor(Color.BLACK);

                shift="Morning Shift";
            }
        });

        sEvening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sGeneral.setBackgroundResource(R.drawable.radiobtn_off);
                sMorning.setBackgroundResource(R.drawable.radiobtn_off);
                sEvening.setBackgroundResource(R.drawable.radiobtn_on);
                sChange.setBackgroundResource(R.drawable.radiobtn_off);
                sNA.setBackgroundResource(R.drawable.radiobtn_off);

                sEvening.setTextColor(Color.WHITE);
                sMorning.setTextColor(Color.BLACK);
                sGeneral.setTextColor(Color.BLACK);
                sChange.setTextColor(Color.BLACK);
                sNA.setTextColor(Color.BLACK);

                shift="Evening Shift";
            }
        });

        sChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sGeneral.setBackgroundResource(R.drawable.radiobtn_off);
                sMorning.setBackgroundResource(R.drawable.radiobtn_off);
                sEvening.setBackgroundResource(R.drawable.radiobtn_off);
                sChange.setBackgroundResource(R.drawable.radiobtn_on);
                sNA.setBackgroundResource(R.drawable.radiobtn_off);

                sChange.setTextColor(Color.WHITE);
                sMorning.setTextColor(Color.BLACK);
                sEvening.setTextColor(Color.BLACK);
                sGeneral.setTextColor(Color.BLACK);
                sNA.setTextColor(Color.BLACK);

                shift="Shift changes every week or every 2 weeks or monthly";
            }
        });

        sNA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sGeneral.setBackgroundResource(R.drawable.radiobtn_off);
                sMorning.setBackgroundResource(R.drawable.radiobtn_off);
                sEvening.setBackgroundResource(R.drawable.radiobtn_off);
                sChange.setBackgroundResource(R.drawable.radiobtn_off);
                sNA.setBackgroundResource(R.drawable.radiobtn_on);

                sNA.setTextColor(Color.WHITE);
                sMorning.setTextColor(Color.BLACK);
                sEvening.setTextColor(Color.BLACK);
                sChange.setTextColor(Color.BLACK);
                sGeneral.setTextColor(Color.BLACK);

                shift="NA";
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(),DataSectionOne.gender, Toast.LENGTH_SHORT).show();

                DataSectionOne.shift = shift;
                DataSectionOne.s1q8 = shifttv.getText().toString();
               // if (r.getCheckedRadioButtonId()==-1)
               if (r.getCheckedRadioButtonId()==-1 && shift.equals(""))
                    Toast.makeText(getContext(), "Select the shift", Toast.LENGTH_SHORT).show();
                else {
                    ConsultationFragment.psection1 += 1;
                      SharedPreferences sharedPreferences2 = requireContext().getSharedPreferences("SEC1PROG", Context.MODE_PRIVATE);
                      int preval =       sharedPreferences2.getInt("progress",0);
                      SectionPref.saveform("shift",shift,7,preval,8,"STEP1Q8",requireContext());
//                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("STEP1Q8", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("shift", shift);
//                    editor.apply();
//                    String sharedshift = sharedPreferences.getString("shift", "");
//                    if (!(sharedshift.isEmpty()) && preval==7) {
//                        SharedPreferences sharedPreferences1 = requireContext().getSharedPreferences("SEC1PROG", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
//                        editor1.putInt("progress", 8);
//                        editor1.apply();
//                    }
                    Navigation.findNavController(v).navigate(R.id.action_sectionOneQEight_to_consultationFragment);
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConsultationFragment.psection1>0)
                    ConsultationFragment.psection1-=1;
                requireActivity().onBackPressed();
            }
        });

        imgBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
}