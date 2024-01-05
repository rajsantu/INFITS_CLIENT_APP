package com.example.infits;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EmailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EmailFragment extends Fragment {
    EditText editTextSubject, editTextContent,editTextToEmail;
    Button button;
    public static EmailFragment newInstance() {
        EmailFragment fragment = new EmailFragment();
        Bundle args = new Bundle();
        // If you need to pass arguments to the fragment, add them to the bundle here
        // args.putString("key", "value");
        fragment.setArguments(args);
        return fragment;
    }

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public EmailFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment EmailFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static EmailFragment newInstance(String param1, String param2) {
//        EmailFragment fragment = new EmailFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_email, container, false);

        // Initialize views by finding their IDs in the inflated layout
        button = view.findViewById(R.id.btnSend);
        editTextSubject = view.findViewById(R.id.subject);
        editTextContent = view.findViewById(R.id.content);
        editTextToEmail = view.findViewById(R.id.to_email);

        // Set OnClickListener for the button
        button.setOnClickListener(v -> {
            // Retrieve data from EditText fields
            String subject = editTextSubject.getText().toString();
            String content = editTextContent.getText().toString();
            String to_email = editTextToEmail.getText().toString();

            if (subject.isEmpty() || content.isEmpty() || to_email.isEmpty()) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            } else {
                sendEmail(subject, content, to_email);
            }
        });

        return view;
    }

    // Method to send an email
    private void sendEmail(String subject, String content, String to_email) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{to_email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.setType("message/rfc822");
        startActivity(Intent.createChooser(intent, "Choose email client:"));
    }
}