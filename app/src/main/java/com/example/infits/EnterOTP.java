package com.example.infits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;

import java.util.Objects;
import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EnterOTP extends AppCompatActivity {

    TextView emailTV, resend;
    PinView otpView;
    Button verify;
    ImageView backLogin;

    String otp, email;
    private String senderEmail;
    private String senderPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_otp);

        hooks();

        otp = getIntent().getStringExtra("otp");
        email = getIntent().getStringExtra("email");
        senderEmail = getIntent().getStringExtra("senderEmail");
        senderPassword = getIntent().getStringExtra("senderPassword");

        emailTV.setText(email);

        verify.setOnClickListener(v -> {
            if(Objects.requireNonNull(otpView.getText()).toString().length() < 4) {
                Toast.makeText(this, "Please enter the OTP", Toast.LENGTH_LONG).show();
            }
            else if(otpView.getText().toString().equals(otp)) {
                Toast.makeText(this, "Verified", Toast.LENGTH_LONG).show();

                Intent intent = new Intent(this, OTPVerified.class);
                intent.putExtra("email", email);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Entered OTP is incorrect", Toast.LENGTH_LONG).show();
            }
        });

        backLogin.setOnClickListener(v -> {
            Intent i = new Intent(EnterOTP.this, Login.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
        });

        resend.setOnClickListener(v -> {
            Toast.makeText(this, "Sending...", Toast.LENGTH_LONG).show();
            send(email);
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

            otp = getOTP();

            MimeMessage mm = new MimeMessage(session);
            mm.setSender(addressFrom);
            mm.setSubject("OTP");
            mm.setContent("Your OTP is: " + otp, "text/plain");
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(mm);
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

    private void hooks() {
        emailTV = findViewById(R.id.emailTV);
        resend = findViewById(R.id.resend);
        otpView = findViewById(R.id.otp);
        verify = findViewById(R.id.verifyBtn);
        backLogin = findViewById(R.id.back_login);
    }
}