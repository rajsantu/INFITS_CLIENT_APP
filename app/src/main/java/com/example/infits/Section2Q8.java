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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.infits.customDialog.SectionPref;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Section2Q8#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Section2Q8 extends Fragment {

    ImageButton imgBack;
    Button nextbtn;
    TextView backbtn, famtv;
    CheckBox dia,hyperthy,hypothy,hyperten,pcod,fattyl,lactose;
    EditText oth;
    private SharedPreferences checkboxPrefs;
    private SharedPreferences.Editor editor;
    private boolean isDiag;
    private boolean isHyperthy;
    private boolean isHypothy;
    private boolean isHyperten;
    private boolean isPcod;
    private boolean isFattly;
    private boolean isLactose;
    ArrayList<String> fam;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Section2Q8() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Section2Q8.
     */
    // TODO: Rename and change types and number of parameters
    public static Section2Q8 newInstance(String param1, String param2) {
        Section2Q8 fragment = new Section2Q8();
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
        View view = inflater.inflate(R.layout.fragment_section2_q8, container, false);

        imgBack = view.findViewById(R.id.imgback);
        nextbtn = view.findViewById(R.id.nextbtn);
        backbtn = view.findViewById(R.id.backbtn);
        dia = view.findViewById(R.id.dia);
        hyperthy = view.findViewById(R.id.hyperthy);
        hypothy = view.findViewById(R.id.hypothy);
        hyperten = view.findViewById(R.id.hyperten);
        pcod = view.findViewById(R.id.pcod);
        fattyl = view.findViewById(R.id.fattyl);
        lactose = view.findViewById(R.id.lactose);
        oth = view.findViewById(R.id.oth);

        fam = new ArrayList<>();

        famtv = view.findViewById(R.id.textView77);

        TextView gotomain = view.findViewById(R.id.gotomainsection);
        gotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_section2Q8_to_consultationFragment);

            }
        });


        checkboxPrefs = requireContext().getSharedPreferences("checkbox_pref", Context.MODE_PRIVATE);
        editor = checkboxPrefs.edit();

        boolean isChecked1 = checkboxPrefs.getBoolean("diabetes", false);
        dia.setChecked(isChecked1);
        isDiag = isChecked1;


        boolean isChecked2 = checkboxPrefs.getBoolean("hyperthy", false);
        hyperthy.setChecked(isChecked2);
        isHyperthy = isChecked2;

        boolean isChecked3 = checkboxPrefs.getBoolean("hypothy", false);
        hypothy.setChecked(isChecked3);
        isHypothy = isChecked3;

        boolean isChecked4 = checkboxPrefs.getBoolean("hyperten", false);
        hyperten.setChecked(isChecked4);
        isHyperten = isChecked4;

        boolean isChecked5 = checkboxPrefs.getBoolean("pcod", false);
        pcod.setChecked(isChecked5);
        isPcod = isChecked5;

        boolean isChecked6 = checkboxPrefs.getBoolean("fattly", false);
        fattyl.setChecked(isChecked6);
        isFattly = isChecked6;

        boolean isChecked7 = checkboxPrefs.getBoolean("lactose", false);
        lactose.setChecked(isChecked7);
        isLactose = isChecked7;

        dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dia.isChecked()) {
                    fam.add("Diabetes");
                    isDiag = dia.isChecked();
                }
                else {
                    fam.remove("Diabetes");
                    isDiag = dia.isChecked();
                }
            }
        });

        hyperthy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hyperthy.isChecked()) {
                    fam.add("Hyperthyroidism");
                    isHyperthy = hyperthy.isChecked();
                }
                else {
                    fam.remove("Hyperthyroidism");
                    isHyperthy = hyperthy.isChecked();
                }
            }
        });

        hypothy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hypothy.isChecked()) {
                    fam.add("Hypothyroidism");
                    isHypothy = hypothy.isChecked();
                }
                else {
                    fam.remove("Hypothyroidism");
                    isHypothy = hypothy.isChecked();
                }
            }
        });

        hyperten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hyperten.isChecked()) {
                    fam.add("Hypertension");
                    isHyperten = hyperten.isChecked();
                }
                else {
                    fam.remove("Hypertension");
                    isHyperten = hyperten.isChecked();
                }
            }
        });

        pcod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pcod.isChecked()) {
                    fam.add("PCOD/PCOS");
                    isPcod = pcod.isChecked();
                }
                else {
                    fam.remove("PCOD/PCOS");
                    isPcod = pcod.isChecked();
                }
            }
        });

        fattyl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fattyl.isChecked()) {
                    fam.add("Fatty liver");
                    isFattly = fattyl.isChecked();
                }
                else {
                    fam.remove("Fatty liver");
                    isFattly = fattyl.isChecked();
                }
            }
        });

        lactose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lactose.isChecked()) {
                    fam.add("Fatty liver");
                    isLactose = lactose.isChecked();
                }
                else {
                    fam.remove("Fatty liver");
                    isLactose = lactose.isChecked();
                }
            }
        });

        String other = oth.getText().toString();

        if(other!=null)
            fam.add(other);


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataSectionTwo.familyHistory = fam;
                DataSectionTwo.s2q8 = famtv.getText().toString();


                //Toast.makeText(getContext(), "Data:" + DataSectionTwo.familyHistory, Toast.LENGTH_SHORT).show();
                if ((!dia.isChecked()) && (!hyperten.isChecked()) && (!hyperthy.isChecked())
                        && (!hypothy.isChecked()) && (!pcod.isChecked()) && (!fattyl.isChecked()) &&
                        (other.equals("") || other.equals(" ")))
                    Toast.makeText(getContext(), "Select atleast one of the given options", Toast.LENGTH_SHORT).show();
                else {
                    ConsultationFragment.psection2 += 1;
                    editor.putBoolean("diabetes", isDiag);
                    // editor.apply();
                    editor.putBoolean("hyperthy", isHyperthy);
                    // editor.apply();
                    editor.putBoolean("hypothy", isHypothy);
                    // editor.apply();
                    editor.putBoolean("hyperten", isHyperten);
                    // editor.apply();
                    editor.putBoolean("pcod", isPcod);
                    // editor.apply();
                    editor.putBoolean("fattly", isFattly);
                    // editor.apply();
                    editor.putBoolean("lactose", isLactose);
                    editor.apply();

                    Gson gson = new Gson();
                    String json = gson.toJson(fam);
                    SharedPreferences sharedPreferences2 = requireContext().getSharedPreferences("SEC2PROG", Context.MODE_PRIVATE);
                    int preval =       sharedPreferences2.getInt("progress2",0);
                    SectionPref.saveformsection2("fam",json,7,preval,8,"STEP2Q8",requireContext());

                    Navigation.findNavController(v).navigate(R.id.action_section2Q8_to_consultationFragment);
                }
            }
        });

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ConsultationFragment.psection2>0)
                    ConsultationFragment.psection2-=1;
                requireActivity().onBackPressed();
            }
        });

        imgBack.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
}