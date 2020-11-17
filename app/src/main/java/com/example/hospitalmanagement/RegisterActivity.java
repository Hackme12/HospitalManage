package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.admin.SystemUpdateInfo;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.SQLOutput;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private EditText name, dob, email, userId;
    private EditText password, confirm_password;
    private TextView tvhaveAcc;
    private Button btn_Create_Acc;
    private String UserType;

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        name = (EditText) findViewById(R.id.Name_RA);
        dob = (EditText) findViewById(R.id.Date_of_birth);
        email = (EditText) findViewById(R.id.Email_RA);
        userId = (EditText) findViewById(R.id.Username_RA);
        password = (EditText) findViewById(R.id.Password_RA);
        tvhaveAcc = (TextView)findViewById(R.id.have_account) ;
        confirm_password = (EditText) findViewById(R.id.confirm_passwordRA);

        btn_Create_Acc = (Button) findViewById(R.id.btn_Create);
        loadingBar = new ProgressDialog(this);
        spinner = (Spinner) findViewById(R.id.spinner_item);


        spinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.UserType, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        btn_Create_Acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }


        });

        tvhaveAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });


    }

    public void CreateAccount() {

        String Name = name.getText().toString().trim();
        String Dob = dob.getText().toString().trim();
        String Email = email.getText().toString().trim();
        String UserId = userId.getText().toString().trim();
        String Password = password.getText().toString().trim();
        String Confirm_Password = confirm_password.getText().toString().trim();
        String spinner_item = "Please Select User Type";


        if (TextUtils.isEmpty(Name)) {
            Toast.makeText(this, "Please Enter your name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Dob)) {
            Toast.makeText(this, "Please Enter Date of Birth", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Email)) {
            Toast.makeText(this, "Please Enter your email id", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(UserId)) {
            Toast.makeText(this, "Please Enter your User Id", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Password) || (!Password.equals(Confirm_Password))) {
            Toast.makeText(this, "Please Enter your Password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Confirm_Password)) {
            Toast.makeText(this, "Please Enter your Confirm Password", Toast.LENGTH_SHORT).show();
        } else if (UserType.equals(spinner_item)) {
            Toast.makeText(this, "Please Select the User Type", Toast.LENGTH_SHORT).show();
        } else {

            loadingBar.setTitle("Message");
            loadingBar.setMessage("Please wait while checking your credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();
            UpdateLoginInfoToDatabase(Name, Dob, Email, UserId, Password, Confirm_Password);

        }
    }

    public void UpdateLoginInfoToDatabase(final String Name, final String Dob, final String Email, final String UserId, final String Password, final String Confirm_Password) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (!datasnapshot.child(UserType).child(UserId).exists()) {
                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("Name", Name);
                    userDataMap.put("UserId", UserId);
                    userDataMap.put("Dateofbirth", Dob);
                    userDataMap.put("Email", Email);
                    userDataMap.put("Password", Password);
                    userDataMap.put("ConfirmPassword", Confirm_Password);

                    myRef.child(UserType).child(UserId).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this, "Congratulation, your account has been created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(RegisterActivity.this, "Failed to create your account", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });
                } else {
                    Toast.makeText(RegisterActivity.this, "There is already an account with " + UserId, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                String errorMessage = error.getMessage();
                System.out.println(errorMessage);

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        UserType = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

        Toast.makeText(this, "Please Select User Type: ", Toast.LENGTH_SHORT).show();

    }
}