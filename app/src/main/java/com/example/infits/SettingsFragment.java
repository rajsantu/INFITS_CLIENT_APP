package com.example.infits;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {

    ImageView profilepic;

    ImageButton imgbtnAccount, imgbtnDevice, imgbtnNotif, imgbtnRef, imgbtnAbout, imgbtnHelp, imgbtnKnowDt, backButton;

    TextView tvName;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private NavController navController;

    public SettingsFragment() {

    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
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

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                requireActivity().finish();
                startActivity(new Intent(getActivity(),DashBoardMain.class));
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        // Initialize NavController
        NavHostFragment navHostFragment = (NavHostFragment) requireActivity().getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView3);
        navController = navHostFragment.getNavController();

        profilepic = view.findViewById(R.id.profilepic);

        imgbtnAbout = view.findViewById(R.id.imgbtnAbout);
        imgbtnAccount = view.findViewById(R.id.imgbtnAccount);
        imgbtnDevice = view.findViewById(R.id.imgbtnDevice);
        imgbtnNotif = view.findViewById(R.id.imgbtnNotif);
        imgbtnRef = view.findViewById(R.id.imgbtnRef);
        imgbtnHelp = view.findViewById(R.id.imgbtnHelp);
        imgbtnKnowDt = view.findViewById(R.id.know_diet_btn);
        tvName = view.findViewById(R.id.tvName);
        backButton = view.findViewById(R.id.settings_back_bt);

        profilepic.setImageBitmap(DataFromDatabase.profile);

        tvName.setText(DataFromDatabase.name);

        imgbtnRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*final Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);
                dialog.setContentView(R.layout.referralcodedialog);

                final EditText goal = view.findViewById(R.id.goal);
                final EditText referralCode = dialog.findViewById(R.id.referralcode);
                final ImageView checkBtn = dialog.findViewById(R.id.checkReferral);

                checkBtn.setOnClickListener(v -> {
                    dialog.dismiss();

                    String code = getReferralCode();
                    String enteredCode = referralCode.getText().toString();

                    if(enteredCode.equals(code)) {
                        // correct code
                    } else {
                        Dialog wrongRefDialog = new Dialog(requireContext());
                        wrongRefDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        wrongRefDialog.setCancelable(true);
                        wrongRefDialog.setContentView(R.layout.wrong_referral_dialog);

                        Button tryAgain = wrongRefDialog.findViewById(R.id.try_again);
                        tryAgain.setOnClickListener(it -> wrongRefDialog.dismiss());

                        wrongRefDialog.show();
                    }
                });

                dialog.show();*/
                Navigation.findNavController(view).navigate(R.id.action_settingsFragment_to_referralFragment);
            }
        });

        imgbtnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_helpFragment);
            }
        });

        imgbtnAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_account);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleBackButtonClick();
            }
        });

//        imgbtnAccount.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), Account.class);
//                startActivity(intent);
//            }
//        });

        imgbtnNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_notification);
            }
        });

        imgbtnDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(),DeviceSettings.class);
                startActivity(intent);
            }
        });

        imgbtnAbout.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_aboutUsFragment);
        });

        imgbtnKnowDt.setOnClickListener(v->{
            Navigation.findNavController(v).navigate(R.id.action_settingsFragment_to_profile2);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ImageView imageView = view.findViewById(R.id.profilepic);
        imageView.setImageResource(R.drawable.profile);
    }

    private String getReferralCode() {
        return "test123";
    }
    private void handleBackButtonClick() {
        getActivity().onBackPressed();
    }
}