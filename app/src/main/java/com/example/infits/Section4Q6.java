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
import android.widget.TextView;
import android.widget.Toast;

import com.example.infits.customDialog.SectionPref;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Section4Q6#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Section4Q6 extends Fragment {

    ImageButton imgBack;
    Button nextbtn;
    TextView backbtn, textView77;
    RadioButton no,daily,oneWeek,twWeek,thrWeek,monthly;
    String skipping="";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Section4Q6() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Section4Q6.
     */
    // TODO: Rename and change types and number of parameters
    public static Section4Q6 newInstance(String param1, String param2) {
        Section4Q6 fragment = new Section4Q6();
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
        View view = inflater.inflate(R.layout.fragment_section4_q6, container, false);

        imgBack = view.findViewById(R.id.imgback);
        nextbtn = view.findViewById(R.id.nextbtn);
        backbtn = view.findViewById(R.id.backbtn);
        no = view.findViewById(R.id.no);
        daily = view.findViewById(R.id.daily);
        oneWeek = view.findViewById(R.id.oneWeek);
        twWeek = view.findViewById(R.id.twWeek);
        thrWeek = view.findViewById(R.id.thrWeek);
        monthly = view.findViewById(R.id.monthly);
        textView77 = view.findViewById(R.id.textView77);

        TextView gotomain = view.findViewById(R.id.gotomainsection);
        gotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_section4Q6_to_consultationFragment);

            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("STEP4Q6", Context.MODE_PRIVATE);
        String storedvalue = sharedPreferences.getString("skipping", "");
        if(!storedvalue.isEmpty()) {
            switch (storedvalue) {
                case "No":
                    No();
                    break;
                case "Daily":
                    Daily();
                    break;
                case "Once a week":
                    OneWeek();
                    break;
                case "Twice a week":
                    TwoWeek();
                    break;
                case "3-5 times a week":
                    ThrWeek();
                    break;
                case "Monthly":
                    Monthly();
                    break;
                default:

            }
            DataSectionFour.skipping = storedvalue;
        }
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                No();
            }
        });

        daily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Daily();
            }
        });

        oneWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneWeek();
            }
        });


        twWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwoWeek();
            }
        });

        thrWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThrWeek();
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Monthly();
            }
        });
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getContext(),employment, Toast.LENGTH_SHORT).show();

                DataSectionFour.skipping = skipping;
                DataSectionFour.s4q6 = textView77.getText().toString();
                if (skipping.equals(""))
                    Toast.makeText(getContext(), "Select atleast one of the given options", Toast.LENGTH_SHORT).show();
                else {
                    ConsultationFragment.psection4 += 1;
                    SharedPreferences sharedPreferences2 = requireContext().getSharedPreferences("SEC4PROG", Context.MODE_PRIVATE);
                    int preval =       sharedPreferences2.getInt("progress4",0);
                    SectionPref.saveformsection4("skipping",skipping,5,preval,6,"STEP4Q6",requireContext());
                    Navigation.findNavController(v).navigate(R.id.action_section4Q6_to_section4Q7);
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConsultationFragment.psection4>0)
                    ConsultationFragment.psection4-=1;
                requireActivity().onBackPressed();
            }
        });

        imgBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
    private void No() {
        no.setBackgroundResource(R.drawable.radiobtn_on);
        daily.setBackgroundResource(R.drawable.radiobtn_off);
        oneWeek.setBackgroundResource(R.drawable.radiobtn_off);
        twWeek.setBackgroundResource(R.drawable.radiobtn_off);
        thrWeek.setBackgroundResource(R.drawable.radiobtn_off);
        monthly.setBackgroundResource(R.drawable.radiobtn_off);

        no.setTextColor(Color.WHITE);
        daily.setTextColor(Color.BLACK);
        oneWeek.setTextColor(Color.BLACK);
        twWeek.setTextColor(Color.BLACK);
        thrWeek.setTextColor(Color.BLACK);
        monthly.setTextColor(Color.BLACK);

        skipping="No";
    }

    private void Daily() {
        daily.setBackgroundResource(R.drawable.radiobtn_on);
        no.setBackgroundResource(R.drawable.radiobtn_off);
        oneWeek.setBackgroundResource(R.drawable.radiobtn_off);
        twWeek.setBackgroundResource(R.drawable.radiobtn_off);
        thrWeek.setBackgroundResource(R.drawable.radiobtn_off);
        monthly.setBackgroundResource(R.drawable.radiobtn_off);

        daily.setTextColor(Color.WHITE);
        no.setTextColor(Color.BLACK);
        oneWeek.setTextColor(Color.BLACK);
        twWeek.setTextColor(Color.BLACK);
        thrWeek.setTextColor(Color.BLACK);
        monthly.setTextColor(Color.BLACK);

        skipping="Daily";
    }

    private void OneWeek() {
        oneWeek.setBackgroundResource(R.drawable.radiobtn_on);
        daily.setBackgroundResource(R.drawable.radiobtn_off);
        no.setBackgroundResource(R.drawable.radiobtn_off);
        twWeek.setBackgroundResource(R.drawable.radiobtn_off);
        thrWeek.setBackgroundResource(R.drawable.radiobtn_off);
        monthly.setBackgroundResource(R.drawable.radiobtn_off);

        oneWeek.setTextColor(Color.WHITE);
        daily.setTextColor(Color.BLACK);
        no.setTextColor(Color.BLACK);
        twWeek.setTextColor(Color.BLACK);
        thrWeek.setTextColor(Color.BLACK);
        monthly.setTextColor(Color.BLACK);

        skipping="Once a week";
    }

    private void TwoWeek() {
        twWeek.setBackgroundResource(R.drawable.radiobtn_on);
        daily.setBackgroundResource(R.drawable.radiobtn_off);
        oneWeek.setBackgroundResource(R.drawable.radiobtn_off);
        no.setBackgroundResource(R.drawable.radiobtn_off);
        thrWeek.setBackgroundResource(R.drawable.radiobtn_off);
        monthly.setBackgroundResource(R.drawable.radiobtn_off);

        twWeek.setTextColor(Color.WHITE);
        daily.setTextColor(Color.BLACK);
        oneWeek.setTextColor(Color.BLACK);
        no.setTextColor(Color.BLACK);
        thrWeek.setTextColor(Color.BLACK);
        monthly.setTextColor(Color.BLACK);

        skipping="Twice a week";
    }

    private void ThrWeek() {
        thrWeek.setBackgroundResource(R.drawable.radiobtn_on);
        daily.setBackgroundResource(R.drawable.radiobtn_off);
        oneWeek.setBackgroundResource(R.drawable.radiobtn_off);
        twWeek.setBackgroundResource(R.drawable.radiobtn_off);
        no.setBackgroundResource(R.drawable.radiobtn_off);
        monthly.setBackgroundResource(R.drawable.radiobtn_off);

        thrWeek.setTextColor(Color.WHITE);
        daily.setTextColor(Color.BLACK);
        oneWeek.setTextColor(Color.BLACK);
        twWeek.setTextColor(Color.BLACK);
        no.setTextColor(Color.BLACK);
        monthly.setTextColor(Color.BLACK);

        skipping="3-5 times a week";
    }

    private void Monthly() {
        monthly.setBackgroundResource(R.drawable.radiobtn_on);
        daily.setBackgroundResource(R.drawable.radiobtn_off);
        oneWeek.setBackgroundResource(R.drawable.radiobtn_off);
        twWeek.setBackgroundResource(R.drawable.radiobtn_off);
        thrWeek.setBackgroundResource(R.drawable.radiobtn_off);
        no.setBackgroundResource(R.drawable.radiobtn_off);

        monthly.setTextColor(Color.WHITE);
        daily.setTextColor(Color.BLACK);
        oneWeek.setTextColor(Color.BLACK);
        twWeek.setTextColor(Color.BLACK);
        thrWeek.setTextColor(Color.BLACK);
        no.setTextColor(Color.BLACK);

        skipping="Monthly";
    }
}