package com.example.infits;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class ReferralActiveUsersFragment extends Fragment {

    RecyclerView recView;

    public ReferralActiveUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_referral_active_users, container, false);

        recView = view.findViewById(R.id.recView);

        fetchActiveUsers();

        return view;
    }

    private void fetchActiveUsers() {
        String referralUrl = String.format("%sgetReferralActiveUsers.php",DataFromDatabase.ipConfig);

        StringRequest referralRequest = new StringRequest(
                Request.Method.POST, referralUrl,
                response -> {
                    Log.d("ReferralActiveUsersFrag", "fetchActiveUsers: " + response);

                    String[] activeUsers = response.split(", ");
                    getProfiles(activeUsers);
                },
                error -> Log.e("ReferralActiveUsersFrag", "fetchActiveUsers: " + error.toString())
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

    private void getProfiles(String[] activeUsers) {
        ArrayList<String> profiles = new ArrayList<>();
        String profileUrl = String.format("%sgetProfileOfUsers.php",DataFromDatabase.ipConfig);

        AtomicInteger cnt = new AtomicInteger();

        for(String user: activeUsers) {
            StringRequest profileRequest = new StringRequest(
                    Request.Method.POST, profileUrl,
                    response -> {
                        Log.d("ReferralActiveUsersFrag", "getProfiles: " + response);

                        profiles.add(response);
                        cnt.getAndIncrement();

                        if(Integer.valueOf(String.valueOf(cnt)).equals(activeUsers.length)) {
                            setupAdapter(activeUsers, profiles);
                        }

                    },
                    error -> Log.e("ReferralActiveUsersFrag", "getProfiles: " + error.toString())
            ) {
                @NotNull
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> data = new HashMap<>();

                    data.put("clientID", user);

                    return data;
                }
            };
            Volley.newRequestQueue(requireContext()).add(profileRequest);
        }
    }

    private void setupAdapter(String[] activeUsers, ArrayList<String> profiles) {
        ReferralActiveUsersAdapter adapter = new ReferralActiveUsersAdapter(activeUsers, profiles, requireContext());
        recView.setAdapter(adapter);
        recView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }
}