package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;


public class AddPatient extends AppCompatActivity {

    private EditText name, dateOfBirth,address;
    private EditText emailId, phoneNumber,SSN;
    private EditText patientId;
    private Button btnAddPatient;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);


        name = (EditText)findViewById(R.id.fullName);
        dateOfBirth = (EditText)findViewById(R.id.dob);
        address = (EditText)findViewById(R.id.address);
        emailId = (EditText)findViewById(R.id.email_id);
        phoneNumber = (EditText)findViewById(R.id.phone_number);
        SSN = (EditText)findViewById(R.id.SSN);
        patientId = (EditText)findViewById(R.id.patientID);
        loadingBar = new ProgressDialog(this);

        btnAddPatient = (Button)findViewById(R.id.btn_Add_Patient);


        btnAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreatePatientChart();
            }
        });





    }

    private void CreatePatientChart() {
        String Name = name.getText().toString().trim();
        String DOB = dateOfBirth.getText().toString().trim();
        String Address = address.getText().toString().trim();
        String Email = emailId.getText().toString().trim();
        String Phone = phoneNumber.getText().toString().trim();
        String SocialSN = SSN.getText().toString().trim();
        String PatientID = patientId.getText().toString().trim();

        if((TextUtils.isEmpty(Name))&& (TextUtils.isEmpty(DOB))
                &&(TextUtils.isEmpty(Address))&& (TextUtils.isEmpty(Email))
                    &&(TextUtils.isEmpty(Phone))&& (TextUtils.isEmpty(SocialSN))&&(TextUtils.isEmpty(PatientID)))
        {
            Toast.makeText(AddPatient.this, "Please Enter All the Credentials above", Toast.LENGTH_SHORT).show();
        }

        else if(SocialSN.length()!=10){
            Toast.makeText(AddPatient.this, "Social Security Number is incorrect.", Toast.LENGTH_SHORT).show();
        }
        else if(Phone.length()!=10){
            Toast.makeText(AddPatient.this, "Phone Number is incorrect.", Toast.LENGTH_SHORT).show();
        }
        else if(!(Email.contains("@"))&& (Email.contains("."))){
            Toast.makeText(AddPatient.this, "Email ID is incorrect.", Toast.LENGTH_SHORT).show();
        }
        else{
            loadingBar.setMessage("Please Wait while creating patient chart");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            UpdatePatientInfoIntoDatabase(PatientID, Name,DOB,Address,Email,Phone,SocialSN);

        }


    }

    private void UpdatePatientInfoIntoDatabase(final String PatientID, final String name, final String dob, final String address,
                                               final String email, final String phone, final String socialSN)
    {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();


        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                if((!datasnapshot.child("Patient").child(PatientID).exists())&&
                        (!datasnapshot.child("Patient").child(PatientID).child("Personal Information")
                                .child(socialSN).exists()))

                {
                    HashMap<String,Object> patientData = new HashMap<>();
                    patientData.put("SocialSecurityNumber", socialSN);
                    patientData.put("PhoneNumber", phone);
                    patientData.put("EmailId", email);
                    patientData.put("Address", address);
                    patientData.put("DateOfBirth", dob);
                    patientData.put("Name", name);
                    patientData.put("PatientID", PatientID);

                    myRef.child("Patient").child(PatientID).child("Personal Information").updateChildren(patientData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){

                                Toast.makeText(AddPatient.this, "Patient Chart has been created.", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(AddPatient.this,Appointment.class);
                                startActivity(intent);
                            }
                            else{
                                loadingBar.dismiss();
                                Toast.makeText(AddPatient.this, "Failed to create patient account", Toast.LENGTH_SHORT).show();
                            }



                        }
                    });


                }

                else{
                    loadingBar.dismiss();
                    Toast.makeText(AddPatient.this, "There is already an Patient chart with this "+ PatientID + " Or "+ socialSN+ ". \n Please Go ahead and make an appointment!!", Toast.LENGTH_SHORT).show();


                    /// send it to the appointment file.

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


}