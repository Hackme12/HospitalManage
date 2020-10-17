package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalmanagement.Users.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private CircleImageView nurse_image, staff_image, doctor_image, ceo_image;
    private Button nurse_btn, staff_btn, doctor_btn,ceo_btn;
    private EditText dUserId,dPassword;
    private TextView newAccount;
    private ProgressDialog loadingBar;
    private String ParentDbname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        nurse_btn= (Button) findViewById(R.id.btn_nurse);
        staff_btn= (Button) findViewById(R.id.btn_staff);
        doctor_btn= (Button) findViewById(R.id.btn_doctor);
        ceo_btn = (Button) findViewById(R.id.btn_ceo);

        nurse_image = (CircleImageView) findViewById(R.id.nurse_icon);
        staff_image = (CircleImageView) findViewById(R.id.staff_icon);
        doctor_image = (CircleImageView) findViewById(R.id.doctor_icon);
        ceo_image = (CircleImageView)findViewById(R.id.image_ceo);
        newAccount = (TextView)findViewById(R.id.new_account);
        loadingBar = new ProgressDialog(this);


        nurse_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentDbname = "Nurse";
                Dialoglogin();
            }
        });
        staff_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentDbname = "Staff";
                Dialoglogin();
            }
        });
        doctor_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentDbname = "Doctor";
                Dialoglogin();
            }
        });
        ceo_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentDbname = "Ceo";
                Dialoglogin();
            }
        });


        nurse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentDbname = "Nurse";
                Dialoglogin();
            }
        });
        staff_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentDbname = "Staff";
                Dialoglogin();
            }
        });
        doctor_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentDbname = "Doctor";
                Dialoglogin();
            }
        });
        ceo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParentDbname = "Ceo";
                Dialoglogin();
            }
        });

        newAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });






    }



    public void Dialoglogin(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_login_dialog, null);
        builder.setView(view);
        dUserId = view.findViewById(R.id.dialogUserId);
        dPassword = view.findViewById(R.id.dialogPassword);

        builder.setPositiveButton("Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String current_userId = dUserId.getText().toString().trim();
                String current_password = dPassword.getText().toString().trim();

                if(current_userId.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please Enter UserID", Toast.LENGTH_SHORT).show();
                }
                else if (current_password.isEmpty()){
                    Toast.makeText(MainActivity.this,"Please Enter your Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingBar.setTitle("Message");
                    loadingBar.setMessage("Please wait While checking your Credentials");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    validateLogin(current_userId,current_password);
                }

                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel                                            ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void validateLogin(final String current_userId, final String current_password) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if(datasnapshot.child(ParentDbname).child(current_userId).exists()){
                    Users userData = datasnapshot.child(ParentDbname).child(current_userId).getValue(Users.class);
                    if(current_userId.equals(userData.getUserId())){
                        if(current_password.equals(userData.getPassword())){
                            if(ParentDbname.equals("Staff")){
                                loadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this,StaffActivity.class);
                                loadingBar.dismiss();
                                startActivity(intent);
                            }
                            else if(ParentDbname.equals("Nurse")){
                                loadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this,NurseActivity.class);
                                startActivity(intent);
                            }
                            else if(ParentDbname.equals("Doctor")){
                                loadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this,DoctorActivity.class);
                                startActivity(intent);
                            }
                            else if(ParentDbname.equals("Ceo")){
                                loadingBar.dismiss();
                                Intent intent = new Intent(MainActivity.this,CeoActivity.class);
                                startActivity(intent);
                            }
                            else{
                                loadingBar.dismiss();
                                Toast.makeText(MainActivity.this, "You are going to Doctor Class", Toast.LENGTH_SHORT).show();
                            }


                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Your Password is Incorrect!! \n Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
                else{
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "UserId not found! Please create a new one.", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                String error1 = error.getMessage();
                Toast.makeText(MainActivity.this, "The error is:"+ error1, Toast.LENGTH_SHORT).show();

            }
        });






    }
}