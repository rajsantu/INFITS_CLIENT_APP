package com.example.infits;

import android.annotation.SuppressLint;
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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Section2Q5#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Section2Q5 extends Fragment {

    ImageButton imgBack;
    Button nextbtn;
    TextView backbtn, diagtv;
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
    ArrayList<String> diagnosed;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Section2Q5() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Section2Q5.
     */
    // TODO: Rename and change types and number of parameters
    public static Section2Q5 newInstance(String param1, String param2) {
        Section2Q5 fragment = new Section2Q5();
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
        View view = inflater.inflate(R.layout.fragment_section2_q5, container, false);

        //dia,hyperthy,hypothy,hyperten,pcod,fattyl,lactose

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


        TextView gotomain = view.findViewById(R.id.gotomainsection);
        gotomain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(view).navigate(R.id.action_section2Q5_to_consultationFragment);

            }
        });

        diagnosed = new ArrayList<>();
        checkboxPrefs = requireContext().getSharedPreferences("checkbox_prefs", Context.MODE_PRIVATE);
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

        diagtv = view.findViewById(R.id.textView77);
//        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("STEP2Q3", Context.MODE_PRIVATE);
//        String json = sharedPreferences.getString("diagnosed", "");
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<String>>(){}.getType();
//        ArrayList<String> arrayList = gson.fromJson(json, type);
//        DataSectionTwo.diagnosed = arrayList;
//        diagnosed=arrayList;
        //Toast.makeText(requireContext(), String.valueOf(diagnosed), Toast.LENGTH_SHORT).show();
//        if(!json.isEmpty()) {
//
//        }
        dia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Diagnosed();
            }
        });

        hyperthy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Hyperthy();
            }
        });

        hypothy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Hypothy();
            }
        });

        hyperten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hyperten();
            }
        });

        pcod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pcod();
            }
        });

        fattyl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fattyl();
            }
        });

        lactose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Lactose();
            }
        });

        /*

        for(int i=0; i<diagnosed.size();i++) {
            Log.d(TAG,"Diagnosed with: " + diagnosed.get(i));
        }

         */


        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String other = oth.getText().toString();

                DataSectionTwo.s2q5 = diagtv.getText().toString();
                if ((!dia.isChecked()) && (!hyperten.isChecked()) && (!hyperthy.isChecked())
                && (!hypothy.isChecked()) && (!pcod.isChecked()) && (!fattyl.isChecked()) &&
                        (!lactose.isChecked())){
                    Toast.makeText(getContext(), "Select atleast one of the given options", Toast.LENGTH_SHORT).show();
                }
                else {
                    DataSectionTwo.diagnosed = diagnosed;
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
                    String json = gson.toJson(diagnosed);
                    SharedPreferences sharedPreferences2 = requireContext().getSharedPreferences("SEC2PROG", Context.MODE_PRIVATE);
                    int preval =       sharedPreferences2.getInt("progress2",0);
                    SectionPref.saveformsection2("diagnosed",json,4,preval,5,"STEP2Q5",requireContext());
                    Navigation.findNavController(v).navigate(R.id.action_section2Q5_to_section2Q6);
                }
                if (!other.isEmpty()){
                    diagnosed.add(other);
                    DataSectionTwo.diagnosed = diagnosed;
                    ConsultationFragment.psection2 += 1;
                    Navigation.findNavController(v).navigate(R.id.action_section2Q5_to_section2Q6);
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

    private void Lactose() {
        if(lactose.isChecked()) {
            diagnosed.add("Lactose intolerance");
            isLactose = lactose.isChecked();
        }
        else {
            diagnosed.remove("Lactose intolerance");
            isLactose = lactose.isChecked();
        }
    }

    private void Fattyl() {
        if(fattyl.isChecked()) {
            diagnosed.add("Fatty liver");
            isFattly = fattyl.isChecked();
        }
        else {
            diagnosed.remove("Fatty liver");
            isFattly = fattyl.isChecked();
        }
    }

    private void Pcod() {
        if(pcod.isChecked()) {
            diagnosed.add("PCOD/PCOS");
            isPcod = pcod.isChecked();
        }
        else {
            diagnosed.remove("PCOD/PCOS");
            isPcod = pcod.isChecked();
        }
    }

    private void Hyperten() {
        if(hyperten.isChecked()) {
            diagnosed.add("Hypertension");
            isHyperten = hyperten.isChecked();
        }
        else {
            diagnosed.remove("Hypertension");
            isHyperten = hyperten.isChecked();
        }
    }

    private void Hypothy() {
        if(hypothy.isChecked()) {
            diagnosed.add("Hypothyroidism");
            isHypothy = hypothy.isChecked();
        }
        else {
            diagnosed.remove("Hypothyroidism");
            isHypothy = hypothy.isChecked();
        }
    }

    private void Hyperthy() {
        if(hyperthy.isChecked()) {
            diagnosed.add("Hyperthyroidism");
            isHyperthy = hyperthy.isChecked();
        }
        else {
            diagnosed.remove("Hyperthyroidism");
            isHyperthy = hyperthy.isChecked();
        }
    }


    @SuppressLint("SuspiciousIndentation")
    private void Diagnosed() {
        if(dia.isChecked()) {
            diagnosed.add("Diabetes");
            isDiag = dia.isChecked();
//            editor.putBoolean("diabetes", dia.isChecked());
//            editor.apply();
        }
        else {
            diagnosed.remove("Diabetes");
            isDiag = dia.isChecked();
        }
//           editor.putBoolean("diabetes",dia.isChecked());
//           editor.apply();
    }
}