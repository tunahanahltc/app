package com.example.ytuobs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends Activity implements View.OnClickListener {
    int typeMember = -1;
    Button register_botton, custom_diaglog_confirm_button;

    private EditText e_mail, student_no, name, password, custom_dialog_editText;
    Dialog resetPassDialog;

    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private HashMap<String, Object> mData;
    private FirebaseUser mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        this.name = findViewById(R.id.name);
        this.e_mail = findViewById(R.id.e_mail_adress);
        this.student_no = findViewById(R.id.studentId);
        this.password = findViewById(R.id.password);
        register_botton = findViewById(R.id.register_button);
        register_botton.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onResume() {
        super.onResume();

        this.typeMember = classification_member(e_mail.getText().toString());
        e_mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String enteredText = charSequence.toString();
                typeMember = classification_member(enteredText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public int classification_member(String e_mail) {

        if (e_mail.contains("@yildiz.edu.tr")) {
            student_no.setVisibility(View.GONE);
            return 1;
        } else if (e_mail.contains("@std.yildiz.edu.tr")) {
            student_no.setVisibility(View.VISIBLE);
            return 2;
        }
        student_no.setVisibility(View.GONE);
        return 0;
    }


    public int inform_control(int control) {
        if (getPassword().getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), "Geçersiz Şifre", Toast.LENGTH_SHORT).show();

            return 1;

        } else if (getName().getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Geçersiz İsim", Toast.LENGTH_SHORT).show();

            return 1;

        } else if ((control == 2) && (getStudent_no().getText().toString().length() < 6)) {
            Toast.makeText(getApplicationContext(), "Geçersiz Öğrenci No", Toast.LENGTH_SHORT).show();
            return 1;

        } else {
            return 0;
        }
    }


    @Override
    public void onClick(View v) {
            signUp();

    }



public void signUp(){

    if (inform_control(typeMember) == 0 && typeMember > 0) {
        mAuth.createUserWithEmailAndPassword(e_mail.getText().toString(), password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mUser = mAuth.getCurrentUser();
                            mData = new HashMap<>();
                            mData.put("isim", name.getText().toString());
                            mData.put("eMail", e_mail.getText().toString());
                            mData.put("password", password.getText().toString());

                            mData.put("educateInform", "");
                            mData.put("telNo", "");
                            if (typeMember == 2) {
                                mData.put("statu","ÖĞRENCİ");
                                mData.put("studentNo", student_no.getText().toString());
                                mReference.child("Ogrenciler").child(mUser.getUid())
                                        .setValue(mData)
                                        .addOnCompleteListener(Register.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register.this, "Kayit islemi basarili", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                mData.put("statu","AKADEMISYEN");
                                mReference.child("Akademisyenler").child(mUser.getUid())
                                        .setValue(mData)
                                        .addOnCompleteListener(Register.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register.this, "Kayit islemi basarili", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                        });
                            }
                        } else {
                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    } else {
        Toast.makeText(getApplicationContext(), "Geçersiz E-Mail" + typeMember, Toast.LENGTH_SHORT).show();
    }
}














/*
    @Override
    public void onClick(View v) {

        if (inform_control(typeMember) == 0 && typeMember > 0) {
            Random random = new Random();
            int min = 111111;
            int max = 987654;
            final int randomNumber = random.nextInt(max - min + 1) + min;
            send_email(getE_mail().getText().toString(), "YTU Kurs Merkezi", "Account Verify",""+randomNumber);
            showResetPasswordDialog(randomNumber);
        }
        else {
            Toast.makeText(getApplicationContext(), "Geçersiz E-Mail" + typeMember, Toast.LENGTH_SHORT).show();
        }
    }


*/


/*
    @Override
    public void onClick(View v) {
        if (typeMember == 1) {
                    Member instructor = new Instructor(getE_mail().getText().toString(), getPassword().getText().toString()
                            , getName().getText().toString());
                    Toast.makeText(getApplicationContext(), instructor.toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);


        }
        if (typeMember == 2) {

                    Member student = new Student(getE_mail().getText().toString(), getPassword().getText().toString()
                            , getName().getText().toString(), getStudent_no().getText().toString());
                    Toast.makeText(getApplicationContext(), student.toString(), Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, Login.class);
                    startActivity(intent);

        }
    }
*/

/*

    private void send_email(String mail, String name, String title, String text) {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", AppData.Gmail_Host);
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(AppData.Sender_Email_address, AppData.Sender_Email_password);
            }
        });
        MimeMessage message = new MimeMessage(session);

        try {
            AppData.setReciver_Email_address(mail);
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(AppData.Reciver_Email_address));
            message.setSubject(title);
            message.setText("From : " + name + "Text: " + text);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Transport.send(message);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }
            });
            thread.start();
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

    }

*/

    public EditText getE_mail() {
        return e_mail;
    }

    public EditText getStudent_no() {
        return student_no;
    }

    public EditText getName() {
        return name;
    }

    public EditText getPassword() {
        return password;
    }


   /* private void showResetPasswordDialog(int verify_code){


        resetPassDialog = new Dialog(this);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(resetPassDialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;

        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        resetPassDialog.setContentView(R.layout.activation_mail_custom_dialog);

        custom_diaglog_confirm_button = (Button) resetPassDialog.findViewById(R.id.custom_dialog_activation_confirm_button);
        custom_dialog_editText = (EditText) resetPassDialog.findViewById(R.id.custom_dialog_activation_editText);

        custom_diaglog_confirm_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(custom_dialog_editText.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Onay Kodunu Giriniz",Toast.LENGTH_SHORT).show();
                } else if (custom_dialog_editText.getText().toString().equals("" + verify_code)) {
                    Toast.makeText(getApplicationContext(),"Hesabınız Onaylandı",Toast.LENGTH_SHORT).show();
                    create_account();
                    resetPassDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(),"Geçersiz onay kodu",Toast.LENGTH_SHORT).show();
                    custom_dialog_editText.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                }
            }

        });
        Window window = resetPassDialog.getWindow();
        window.setBackgroundDrawableResource(R.drawable.custom_dialog_window);
        resetPassDialog.getWindow().setAttributes(params);
        resetPassDialog.show();

    }
*/


/*
public void create_account(){
    if (typeMember == 1) {
        Member instructor = new Instructor(getE_mail().getText().toString(), getPassword().getText().toString()
                , getName().getText().toString());
        Toast.makeText(getApplicationContext(), instructor.toString(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);


    }
    if (typeMember == 2) {

        Member student = new Student(getE_mail().getText().toString(), getPassword().getText().toString()
                , getName().getText().toString(), getStudent_no().getText().toString());
        Toast.makeText(getApplicationContext(), student.toString(), Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(this, Login.class);
        startActivity(intent);

    }

}
*/


}
