package com.example.infits;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ResetPassword extends AppCompatActivity {

    private final String senderEmail = "teaminfits@gmail.com";
    // password is the app password for the email-id
    private final String senderPassword = ""; // https://support.google.com/accounts/answer/185833?hl=en

    ImageView back_login;
    Button sendMailBtn;
    EditText mailET;

    String userEmail = "", otp = "";
    String url = String.format("%ssendMail.php",DataFromDatabase.ipConfig);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        back_login = findViewById(R.id.back_login);
        sendMailBtn = findViewById(R.id.sendMailBtn);
        mailET = findViewById(R.id.mailID);

        back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        sendMailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        try {
//                            new SendMail();
//                            System.out.println("Sent");
//                        } catch (Exception e) {
//                            Log.e("SendMail", e.getMessage(), e);
//                        }
//                    }
//                }).start();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        String emailId = "azarcrackzz@gmail.com";
//                        String message = "item + price";
//                        SendMail sm = new SendMail(getApplicationContext(), emailId, message);
//                    }
//                }).start();
                if(mailET.getText().toString().isEmpty()) {
                    Toast.makeText(ResetPassword.this, "please enter your E-mail", Toast.LENGTH_LONG).show();
                } else {
//                    new SendMail(getApplicationContext(), mailET.getText().toString(), "This is a message");
                    Toast.makeText(ResetPassword.this, "Sending...", Toast.LENGTH_LONG).show();
                    send(mailET.getText().toString());
                }
            }
        });
    }

    private void send(String email) {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(senderEmail,senderPassword);
                    }
                });

        try {
            Transport transport = session.getTransport();
            InternetAddress addressFrom = new InternetAddress(senderEmail);

            String otp = getOTP();

            MimeMessage mm = new MimeMessage(session);
            mm.setSender(addressFrom);
            mm.setSubject("OTP");
            mm.setContent("Your OTP is: " + otp, "text/plain");
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(mm);

                    Intent intent = new Intent(ResetPassword.this, EnterOTP.class);
                    intent.putExtra("otp", otp);
                    intent.putExtra("email", email);
                    intent.putExtra("senderEmail", senderEmail);
                    intent.putExtra("senderPassword", senderPassword);
                    startActivity(intent);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getOTP() {
        Random random = new Random();
        int otp = random.nextInt(9999 - 1000) + 1000;
        return String.valueOf(otp);
    }
}