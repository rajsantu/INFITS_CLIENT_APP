package com.example.infits;

import static android.content.Context.CLIPBOARD_SERVICE;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ReferFragment extends Fragment {

    ImageButton copy;
    Button share, gotReferred;
    TextView referralTV;

    String referralCode = "";

    public ReferFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refer, container, false);

        hooks(view);
        getReferralCode();

        copy.setOnClickListener(v -> {
            String textToCopy = referralCode;

            if(!textToCopy.isEmpty()) {
                ClipboardManager clipboard = (ClipboardManager) requireActivity().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", textToCopy);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(requireContext(), "Copied to clipboard", Toast.LENGTH_LONG).show();
            }
        });

        share.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, referralCode);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        });

        gotReferred.setOnClickListener(this::showDialog);

        return view;
    }

    private void showDialog(View view) {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.referral_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText referralET = dialog.findViewById(R.id.referralET);
        Button submit = dialog.findViewById(R.id.submit);
        Button cancel = dialog.findViewById(R.id.cancel);

        submit.setOnClickListener(v -> {
            String referralCode = referralET.getText().toString();

            if(!referralCode.isEmpty()) {
                checkReferralTable(referralCode);
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void checkReferralTable(String referralCode) {
        String referralUrl = String.format("%scheckReferralTable.php",DataFromDatabase.ipConfig);

        StringRequest referralRequest = new StringRequest(
                Request.Method.POST, referralUrl,
                response -> {
                    Log.d("ReferralFragment", "checkReferralTable: " + response);

                    if(response.equals("found")) {
                        updateReferralTable(referralCode);
                        showSuccessDialog();
                    } else {
                        showFailureDialog();
                    }
                },
                error -> Log.e("ReferralFragment", "checkReferralTable: " + error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("referralCode", referralCode);

                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(referralRequest);
    }

    private void showFailureDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.referral_try_again);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView btn = dialog.findViewById(R.id.btn);

        btn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void showSuccessDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(R.layout.referral_congratulation);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        ImageView btn = dialog.findViewById(R.id.btn);

        btn.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void updateReferralTable(String referralCode) {
        String referralUrl = String.format("%supdateReferralTable.php",DataFromDatabase.ipConfig);

        StringRequest referralRequest = new StringRequest(
                Request.Method.POST, referralUrl,
                response -> {
                    Log.d("ReferralFragment", "updateReferralTable: " + response);

                },
                error -> Log.e("ReferralFragment", "updateReferralTable: " + error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("referralCode", referralCode);
                data.put("activeUsers", DataFromDatabase.clientuserID);
                data.put("clientID", DataFromDatabase.clientuserID); /* no actual need for this but cannot leave null as well */

                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(referralRequest);
    }

    private void getReferralCode() {
        // fetch referral from database
        String referralUrl = String.format("%sgetReferralCode.php",DataFromDatabase.ipConfig);

        StringRequest referralRequest = new StringRequest(
                Request.Method.POST, referralUrl,
                response -> {
                    Log.d("ReferralFragment", "getReferralCode: " + response);
                    referralCode = response;
                    referralTV.setText(response);

                },
                error -> Log.e("ReferralFragment", "getReferralCode: " + error.toString())
        ) {
            @NotNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();

                data.put("clientID", DataFromDatabase.clientuserID);

                return data;
            }
        };
        Volley.newRequestQueue(requireContext()).add(referralRequest);
    }

    private void hooks(View view) {
        copy = view.findViewById(R.id.copy);
        share = view.findViewById(R.id.share);
        gotReferred = view.findViewById(R.id.got_referred);
        referralTV = view.findViewById(R.id.referralTV);
    }
}