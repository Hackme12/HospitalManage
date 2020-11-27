package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class CheckInActivity extends AppCompatActivity {
    private EditText edName,edAddress,edDob,edPhonenumber,edEmail;
    private String name, address, dob, phonenumber,  email, patientId;
    private TextView tvName;
    private Button btnUpdate;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_in);
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


                if(edDob.getEditableText().toString().isEmpty()){
                    Toast.makeText(CheckInActivity.this, "Error: Please check Date Of Birth", Toast.LENGTH_SHORT).show();

                }
                else if(edName.getEditableText().toString().isEmpty()){
                    Toast.makeText(CheckInActivity.this, "Error: Please check your Name", Toast.LENGTH_SHORT).show();

                }
                else if(edAddress.getEditableText().toString().isEmpty()){
                    Toast.makeText(CheckInActivity.this, "Error: Please check your Address", Toast.LENGTH_SHORT).show();

                }
                else if(edPhonenumber.getEditableText().toString().isEmpty()){
                    Toast.makeText(CheckInActivity.this, "Error: Please check your Phone Number", Toast.LENGTH_SHORT).show();

                }
                else if(edEmail.getEditableText().toString().isEmpty()){
                    Toast.makeText(CheckInActivity.this, "Error: Please check your Email", Toast.LENGTH_SHORT).show();

                }


                else
                    if((isNameChanged()||isAddressChanged()||isPhonenumberchanged()||isEmailchanged()|| isDateofbirthchanged())) {
                        Toast.makeText(CheckInActivity.this, "Information has been Successfully changed", Toast.LENGTH_SHORT).show();
                    }
                    dialogbox();


            }
        });


    }



    private void setInfo() {
        Intent intent = getIntent();
        name = intent.getStringExtra("pname");
        address = intent.getStringExtra("paddress");
        phonenumber = intent.getStringExtra("pphone");
        dob = intent.getStringExtra("pdateofbirth");
        email = intent.getStringExtra("pemail");
        patientId = intent.getStringExtra("id");

        tvName.setText(name);
        edName.setText(name);
        edEmail.setText(email);
        edAddress.setText(address);
        edPhonenumber.setText(phonenumber);
        edDob.setText(dob);

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

    public void dialogbox(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to checkIn?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


               Intent intent = new Intent(CheckInActivity.this,PaymentType.class);
               startActivity(intent);


            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();


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
                Intent intent = new Intent(CheckInActivity.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                        Intent.FLAG_ACTIVITY_CLEAR_TASK |
                        Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                break;

            default:
                break;

        }
        return true;
    }






}