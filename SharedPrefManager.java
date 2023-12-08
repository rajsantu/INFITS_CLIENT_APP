package com.example.infits;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class SharedPrefManager {

    Context context;
    SharedPreferences sp;
    SharedPreferences.Editor editor;
    private final String PREF_FILE_NAME = "infit_shared_pref";
    private final int PRIVATE_MODE = 0;
    private final String KEY_PROFILE_PHOTO = "key_session_profile_pic";
    public final String KEY_IF_LOGGEDIN = "KEY_IF_LOGGED_IN";


    public SharedPrefManager(Context context)
    {
        this.context = context;
        sp = context.getSharedPreferences(PREF_FILE_NAME, PRIVATE_MODE);
        editor = sp.edit();
    }

    public boolean checkSession()
    {
        if(sp.contains(KEY_IF_LOGGEDIN))
        {
            return true;
        }
        else {
            return false;
        }
    }

    public void createSession(Bitmap profile_photo)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        profile_photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        editor.putString(KEY_PROFILE_PHOTO, encodedImage);
        editor.putBoolean(KEY_IF_LOGGEDIN, true);
        editor.commit();
    }

    public Bitmap getSessionDetails(String key)
    {
        String decode = sp.getString(key, null);
        byte[] b = Base64.decode(decode, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
        return bitmap;
    }
}
