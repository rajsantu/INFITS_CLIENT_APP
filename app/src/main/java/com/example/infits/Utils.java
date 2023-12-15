package com.example.infits;

import android.util.Log;

public class Utils {
    //To resolve overlapping of text
    private static final String TAG = "Utils";

    public static String DisplayNameFormatter(String fullName, int DashBoardMaxNameLength) {
        if (fullName == null || fullName.isEmpty() || DashBoardMaxNameLength <= 0) {
            Log.d(TAG, "Invalid input or fixed length");
            return ""; // Or another default value, depending on your use case
        }

        // Split the input string into words
        String[] nameParts = fullName.split("\\s+");

        // Check if there is at least one part
        if (nameParts.length == 0) {
            Log.d(TAG, "No name parts to format");
            return ""; // Or another default value, depending on your use case
        }

        // Initialize the result with the first part
        StringBuilder result = new StringBuilder(nameParts[0]);

        // Format the remaining parts
        for (int i = 1; i < nameParts.length; i++) {
            String currentPart = nameParts[i];
            // Check if adding the current part exceeds the fixed length
            if (result.length() + currentPart.length() + 1 > DashBoardMaxNameLength) {
                break; // Break if adding the next part exceeds the fixed length
            }
            result.append(' ').append(currentPart);
        }

        // Trim the result to the fixed length
        String formattedName = result.toString();
        Log.d(TAG, "Formatted Display Name: " + formattedName);
        return formattedName;
    }
}
