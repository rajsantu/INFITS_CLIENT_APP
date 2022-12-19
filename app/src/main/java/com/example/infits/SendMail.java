package com.example.infits;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.infits.Config;

import java.util.Properties;
import java.util.Random;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class SendMail{
    private Context context;
    private Session session;
    private String email;
    private String message;

    private String senderEmail = "2002atulgarg@gmail.com";
    private String senderPassword = "wqqcijugktsufkjx";

    static boolean sent = false;

    private ProgressDialog progressDialog;
    public SendMail(Context context, String email,  String message){
        System.out.println("Hi");
        this.context = context;
        this.email = email;
        this.message = message;
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

            MimeMessage mm = new MimeMessage(session);
            mm.setSender(addressFrom);
            mm.setSubject("OTP");
            mm.setContent("Your OTP is: " + getOTP(), "text/plain");
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            Thread thread = new Thread(() -> {
                try {
                    Transport.send(mm);
                    sent = true;
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }

//        sendMsg();
    }

    private String getOTP() {
        Random random = new Random();
        int otp = random.nextInt(9999 - 1000) + 1000;
        return String.valueOf(otp);
    }

    private void sendMsg() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
//        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", /*"465"*/"587");
        session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail,senderPassword);
            }
        });
        try {
            MimeMessage mm = new MimeMessage(session);
//            mm.setFrom(new InternetAddress(receiverEmail));
            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mm.setSubject("Order");
            mm.setText("message");

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(mm);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        }
        catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}