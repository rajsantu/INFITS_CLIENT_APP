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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infits.customDialog.SectionPref;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Section5Q1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Section5Q1 extends Fragment {

    ImageButton imgBack;
    Button nextbtn;
    TextView backbtn, textView77;
    RadioButton veg,nonveg,ovo,vegan,gluten;
    EditText oth;
    String preference="";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Section5Q1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Section5Q1.
     */
    // TODO: Rename and change types and number of parameters
    public static Section5Q1 newInstance(String param1, String param2) {
        Section5Q1 fragment = new Section5Q1();
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
        View view = inflater.inflate(R.layout.fragment_section5_q1, container, false);

        //veg,nonveg,ovo,vegan,gluten

        imgBack = view.findViewById(R.id.imgback);
        nextbtn = view.findViewById(R.id.nextbtn);
        backbtn = view.findViewById(R.id.backbtn);
        veg = view.findViewById(R.id.veg);
        nonveg = view.findViewById(R.id.nonveg);
        ovo = view.findViewById(R.id.ovo);
        vegan = view.findViewById(R.id.vegan);
        gluten = view.findViewById(R.id.gluten);
        oth = view.findViewById(R.id.oth);
        textView77 = view.findViewById(R.id.textView77);

        TextView gotomain = view.findViewById(R.id.gotomainsection);
        gotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_section5Q1_to_consultationFragment);

            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("STEP5Q1", Context.MODE_PRIVATE);
        String storedvalue = sharedPreferences.getString("preference", "");
        String othVal = oth.getText().toString();
        if(!storedvalue.isEmpty()) {
            switch (storedvalue) {
                case "Vegetarian":
                   Veg();
                    break;
                case "Non-vegetarian":
                    NonVeg();
                    break;
                case "Ovo-vegetarian":
                    Ovo();
                    break;
                case "Vegan":
                    Vegan();
                    break;
                case "Gluten-free":
                   Gluten();
                    break;
                default:

            }
            if(storedvalue==othVal){
                Other();
                oth.setText(storedvalue);
            }
            DataSectionFive.preference = storedvalue;
        }

        veg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Veg();
            }
        });

        nonveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NonVeg();
            }
        });

        ovo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Ovo();
            }
        });

        vegan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vegan();
            }
        });

        gluten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gluten();
            }
        });

        oth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Other();
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(),employment, Toast.LENGTH_SHORT).show();

                DataSectionFive.preference = preference;
                DataSectionFive.s5q1 = textView77.getText().toString();
                if (preference.equals(""))
                    Toast.makeText(getContext(), "Select atleast one of the given options", Toast.LENGTH_SHORT).show();
                else {
                    ConsultationFragment.psection5 += 1;
//                    SharedPreferences sharedPreferences2 = requireContext().getSharedPreferences("SEC5PROG", Context.MODE_PRIVATE);
//                    int preval =       sharedPreferences2.getInt("progress4",0);
                    SectionPref.saveformsection5("preference",preference,0,0,1,"STEP5Q1",requireContext());
                    Navigation.findNavController(v).navigate(R.id.action_section5Q1_to_section5Q2);
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConsultationFragment.psection5>0)
                    ConsultationFragment.psection5-=1;
                requireActivity().onBackPressed();
            }
        });

        imgBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
    private void Veg() {
        veg.setBackgroundResource(R.drawable.radiobtn_on);
        nonveg.setBackgroundResource(R.drawable.radiobtn_off);
        ovo.setBackgroundResource(R.drawable.radiobtn_off);
        vegan.setBackgroundResource(R.drawable.radiobtn_off);
        gluten.setBackgroundResource(R.drawable.radiobtn_off);
        oth.setBackgroundResource(R.drawable.radiobtn_off);

        veg.setTextColor(Color.WHITE);
        nonveg.setTextColor(Color.BLACK);
        ovo.setTextColor(Color.BLACK);
        vegan.setTextColor(Color.BLACK);
        gluten.setTextColor(Color.BLACK);
        oth.setTextColor(Color.BLACK);

        preference="Vegetarian";
    }
    private void NonVeg() {
        nonveg.setBackgroundResource(R.drawable.radiobtn_on);
        veg.setBackgroundResource(R.drawable.radiobtn_off);
        ovo.setBackgroundResource(R.drawable.radiobtn_off);
        vegan.setBackgroundResource(R.drawable.radiobtn_off);
        gluten.setBackgroundResource(R.drawable.radiobtn_off);
        oth.setBackgroundResource(R.drawable.radiobtn_off);

        nonveg.setTextColor(Color.WHITE);
        veg.setTextColor(Color.BLACK);
        ovo.setTextColor(Color.BLACK);
        vegan.setTextColor(Color.BLACK);
        gluten.setTextColor(Color.BLACK);
        oth.setTextColor(Color.BLACK);

        preference="Non-vegetarian";
    }

    private void Ovo() {
        ovo.setBackgroundResource(R.drawable.radiobtn_on);
        nonveg.setBackgroundResource(R.drawable.radiobtn_off);
        veg.setBackgroundResource(R.drawable.radiobtn_off);
        vegan.setBackgroundResource(R.drawable.radiobtn_off);
        gluten.setBackgroundResource(R.drawable.radiobtn_off);
        oth.setBackgroundResource(R.drawable.radiobtn_off);

        ovo.setTextColor(Color.WHITE);
        nonveg.setTextColor(Color.BLACK);
        veg.setTextColor(Color.BLACK);
        vegan.setTextColor(Color.BLACK);
        gluten.setTextColor(Color.BLACK);
        oth.setTextColor(Color.BLACK);

        preference="Ovo-vegetarian";
    }

    private void Vegan() {
        vegan.setBackgroundResource(R.drawable.radiobtn_on);
        nonveg.setBackgroundResource(R.drawable.radiobtn_off);
        ovo.setBackgroundResource(R.drawable.radiobtn_off);
        veg.setBackgroundResource(R.drawable.radiobtn_off);
        gluten.setBackgroundResource(R.drawable.radiobtn_off);
        oth.setBackgroundResource(R.drawable.radiobtn_off);

        vegan.setTextColor(Color.WHITE);
        nonveg.setTextColor(Color.BLACK);
        ovo.setTextColor(Color.BLACK);
        veg.setTextColor(Color.BLACK);
        gluten.setTextColor(Color.BLACK);
        oth.setTextColor(Color.BLACK);

        preference="Vegan";
    }

    private void Gluten() {
        gluten.setBackgroundResource(R.drawable.radiobtn_on);
        nonveg.setBackgroundResource(R.drawable.radiobtn_off);
        ovo.setBackgroundResource(R.drawable.radiobtn_off);
        vegan.setBackgroundResource(R.drawable.radiobtn_off);
        veg.setBackgroundResource(R.drawable.radiobtn_off);
        oth.setBackgroundResource(R.drawable.radiobtn_off);

        gluten.setTextColor(Color.WHITE);
        nonveg.setTextColor(Color.BLACK);
        ovo.setTextColor(Color.BLACK);
        vegan.setTextColor(Color.BLACK);
        veg.setTextColor(Color.BLACK);
        oth.setTextColor(Color.BLACK);

        preference="Gluten-free";
    }

    private void Other() {
        oth.setBackgroundResource(R.drawable.radiobtn_on);
        nonveg.setBackgroundResource(R.drawable.radiobtn_off);
        ovo.setBackgroundResource(R.drawable.radiobtn_off);
        vegan.setBackgroundResource(R.drawable.radiobtn_off);
        gluten.setBackgroundResource(R.drawable.radiobtn_off);
        veg.setBackgroundResource(R.drawable.radiobtn_off);

        oth.setTextColor(Color.WHITE);
        nonveg.setTextColor(Color.BLACK);
        ovo.setTextColor(Color.BLACK);
        vegan.setTextColor(Color.BLACK);
        veg.setTextColor(Color.BLACK);
        gluten.setTextColor(Color.BLACK);

        preference=oth.getText().toString();
    }


}