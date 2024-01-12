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

import java.util.LinkedHashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SectionOneQFive#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SectionOneQFive extends Fragment {

    ImageButton imgBack;
    Button nextbtn;
    TextView backbtn, gendertv;
    RadioButton male, female, other;
    String uGender ="";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SectionOneQFive() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SectionOneQFive.
     */
    // TODO: Rename and change types and number of parameters
    public static SectionOneQFive newInstance(String param1, String param2) {
        SectionOneQFive fragment = new SectionOneQFive();
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
        View view = inflater.inflate(R.layout.fragment_section_one_q_five, container, false);

        imgBack = view.findViewById(R.id.imgback);
        nextbtn = view.findViewById(R.id.nextbtn);
        backbtn = view.findViewById(R.id.backbtn);
        male = view.findViewById(R.id.male);
        female = view.findViewById(R.id.female);
        other = view.findViewById(R.id.other);
        RadioGroup radio=view.findViewById(R.id.radioGroup);
        gendertv = view.findViewById(R.id.textView77);

        TextView gotomain = view.findViewById(R.id.gotomainsection);
        gotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_sectionOneQFive_to_consultationFragment);

            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("STEP1Q5", Context.MODE_PRIVATE);
        String storedvalue = sharedPreferences.getString("uGender", "");
        if(!storedvalue.isEmpty()) {
            switch (storedvalue) {
                case "male":
                    male.setBackgroundResource(R.drawable.radiobtn_on);
                    female.setBackgroundResource(R.drawable.radiobtn_off);
                    other.setBackgroundResource(R.drawable.radiobtn_off);

                    male.setTextColor(Color.WHITE);
                    female.setTextColor(Color.BLACK);
                    other.setTextColor(Color.BLACK);
                    uGender="male";
                    break;
                case "female":
                    female.setBackgroundResource(R.drawable.radiobtn_on);
                    male.setBackgroundResource(R.drawable.radiobtn_off);
                    other.setBackgroundResource(R.drawable.radiobtn_off);

                    female.setTextColor(Color.WHITE);
                    male.setTextColor(Color.BLACK);
                    other.setTextColor(Color.BLACK);
                    uGender="female";
                    break;
                case "other":
                    other.setBackgroundResource(R.drawable.radiobtn_on);
                    female.setBackgroundResource(R.drawable.radiobtn_off);
                    male.setBackgroundResource(R.drawable.radiobtn_off);

                    other.setTextColor(Color.WHITE);
                    male.setTextColor(Color.BLACK);
                    female.setTextColor(Color.BLACK);
                    uGender="other";
                    break;
                default:

            }
            DataSectionOne.gender = storedvalue;
        }


        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                male.setBackgroundResource(R.drawable.radiobtn_on);
                female.setBackgroundResource(R.drawable.radiobtn_off);
                other.setBackgroundResource(R.drawable.radiobtn_off);

                male.setTextColor(Color.WHITE);
                female.setTextColor(Color.BLACK);
                other.setTextColor(Color.BLACK);
                uGender="male";
            }
        });

        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                female.setBackgroundResource(R.drawable.radiobtn_on);
                male.setBackgroundResource(R.drawable.radiobtn_off);
                other.setBackgroundResource(R.drawable.radiobtn_off);

                female.setTextColor(Color.WHITE);
                male.setTextColor(Color.BLACK);
                other.setTextColor(Color.BLACK);
                uGender="female";
            }
        });

        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                other.setBackgroundResource(R.drawable.radiobtn_on);
                female.setBackgroundResource(R.drawable.radiobtn_off);
                male.setBackgroundResource(R.drawable.radiobtn_off);

                other.setTextColor(Color.WHITE);
                male.setTextColor(Color.BLACK);
                female.setTextColor(Color.BLACK);
                uGender="other";
            }
        });


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(),uGender, Toast.LENGTH_SHORT).show();

                DataSectionOne.gender = uGender;
                DataSectionOne.s1q5 = gendertv.getText().toString();

                if(uGender.isEmpty())
                    Toast.makeText(getContext(),"Select a gender",Toast.LENGTH_SHORT).show();
                else{
                    ConsultationFragment.psection1+=1;
                    SharedPreferences sharedPreferences2 = requireContext().getSharedPreferences("SEC1PROG", Context.MODE_PRIVATE);
                    int preval =       sharedPreferences2.getInt("progress",0);
                    SectionPref.saveform("uGender",uGender,4,preval,5,"STEP1Q5",requireContext());
//                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("STEP1Q5", Context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = sharedPreferences.edit();
//                    editor.putString("uGender", uGender);
//                    editor.apply();
//                    String sharedage = sharedPreferences.getString("uGender", "");
//
//                    if (!(sharedage.isEmpty()) && preval==4){
//                        SharedPreferences sharedPreferences1 = requireContext().getSharedPreferences("SEC1PROG", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
//                        editor1.putInt("progress", 5);
//                        editor1.apply();
//                    }
                Navigation.findNavController(v).navigate(R.id.action_sectionOneQFive_to_sectionOneQSix);
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