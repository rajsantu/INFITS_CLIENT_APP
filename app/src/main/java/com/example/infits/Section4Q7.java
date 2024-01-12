package com.example.infits;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infits.customDialog.SectionPref;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Section4Q7#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Section4Q7 extends Fragment {

    ImageButton imgBack;
    Button nextbtn;
    TextView backbtn, textView80;
    EditText eTextHeight;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Section4Q7() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Section4Q7.
     */
    // TODO: Rename and change types and number of parameters
    public static Section4Q7 newInstance(String param1, String param2) {
        Section4Q7 fragment = new Section4Q7();
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
        View view = inflater.inflate(R.layout.fragment_section4_q7, container, false);

        imgBack = view.findViewById(R.id.imgback);
        nextbtn = view.findViewById(R.id.nextbtn);
        backbtn = view.findViewById(R.id.backbtn);
        eTextHeight = view.findViewById(R.id.eTextHeight);
        textView80 = view.findViewById(R.id.textView80);

        TextView gotomain = view.findViewById(R.id.gotomainsection);
        gotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_section4Q7_to_consultationFragment);

            }
        });

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("STEP4Q7", Context.MODE_PRIVATE);
        String storedvalue = sharedPreferences.getString("activity_duration", "");
        if(!storedvalue.isEmpty()) {
            eTextHeight.setText(storedvalue);
            DataSectionFour.activity_duration = storedvalue;
        }
        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String activity_duration = null;
                activity_duration = eTextHeight.getText().toString();
                //Toast.makeText(getContext(),user_height, Toast.LENGTH_SHORT).show();

                DataSectionFour.activity_duration = activity_duration;
                DataSectionFour.s4q7 = textView80.getText().toString();
                if (activity_duration.equals(""))
                    Toast.makeText(getContext(), "Select atleast one of the given options", Toast.LENGTH_SHORT).show();
                else {
                    ConsultationFragment.psection4 += 1;
                    SharedPreferences sharedPreferences2 = requireContext().getSharedPreferences("SEC4PROG", Context.MODE_PRIVATE);
                    int preval =       sharedPreferences2.getInt("progress4",0);
                    SectionPref.saveformsection4("activity_duration",activity_duration,6,preval,7,"STEP4Q7",requireContext());
                    Navigation.findNavController(v).navigate(R.id.action_section4Q7_to_consultationFragment);
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

}