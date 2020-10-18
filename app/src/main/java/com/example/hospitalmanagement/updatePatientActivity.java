package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class updatePatientActivity extends AppCompatActivity {

    private EditText edName,edAddress,edDob,edPhonenumber,edEmail;
    private String name, address, dob, phonenumber,  email, patientId;
    private TextView tvName;
    private Button btnUpdate;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_patient);

        edName = (EditText)findViewById(R.id.edfullName);
        edAddress = (EditText)findViewById(R.id.edAddress);
        edDob = (EditText)findViewById(R.id.edDob);
        edPhonenumber = (EditText)findViewById(R.id.edPhone);
        tvName = (TextView) findViewById(R.id.tvName);
        edEmail = (EditText)findViewById(R.id.edEmail);
        btnUpdate = (Button)findViewById(R.id.btnUpdate) ;
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Patient");

        setInfo();


        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(isNameChanged()||isAddressChanged()||isPhonenumberchanged()||isEmailchanged()|| isDateofbirthchanged()){

                    Toast.makeText(updatePatientActivity.this, "Successfully changed", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(updatePatientActivity.this, "Data Remain same", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private boolean isDateofbirthchanged() {
        if(!dob.equals(edDob.getEditableText().toString())){
            reference.child(patientId).child("Personal Information").
                    child("DateOfBirth").setValue(edDob.getEditableText().toString());
            return true;
        }
       return false;
    }

    private boolean isEmailchanged() {
        if(!email.equals(edEmail.getEditableText().toString())){
            reference.child(patientId).child("Personal Information").
                    child("EmailId").setValue(edEmail.getEditableText().toString());
            return true;
        }
        return false;
    }

    private boolean isPhonenumberchanged() {
        if(!phonenumber.equals(edPhonenumber.getEditableText().toString())){
            reference.child(patientId).child("Personal Information").
                    child("PhoneNumber").setValue(edPhonenumber.getEditableText().toString());
            return true;
        }
        return false;
    }

    private boolean isAddressChanged() {
        if(!address.equals(edAddress.getEditableText().toString())){
            reference.child(patientId).child("Personal Information").
                    child("Address").setValue(edAddress.getEditableText().toString());
            return true;
        }
        return false;
    }

    private boolean isNameChanged() {
        if(!name.equals(edName.getEditableText().toString())){
            reference.child(patientId).child("Personal Information").
                    child("Name").setValue(edName.getEditableText().toString());
            return true;
        }
        return false;
    }

    private void setInfo() {
        Intent intent = getIntent();
        name = intent.getStringExtra("pName");
        address = intent.getStringExtra("pAddress");
        phonenumber = intent.getStringExtra("pPhone");
        dob = intent.getStringExtra("pDateofBirth");
        email = intent.getStringExtra("pEmail");
        patientId = intent.getStringExtra("Id");

        tvName.setText(name);
        edName.setText(name);
        edEmail.setText(email);
        edAddress.setText(address);
        edPhonenumber.setText(phonenumber);
        edDob.setText(dob);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case (R.id.Logout):
                Intent intent = new Intent(updatePatientActivity.this,MainActivity.class);
                startActivity(intent);

                break;

            default:
                break;

        }
        return true;
    }


}