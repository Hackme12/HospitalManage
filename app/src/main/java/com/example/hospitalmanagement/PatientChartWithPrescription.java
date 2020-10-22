package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalmanagement.Users.PatientInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PatientChartWithPrescription extends AppCompatActivity {

    TextView tvName,tvAddress, tvDob,tvEmail,tvPhone,tvWeight,tvHeight,tvBp,tvReasonToVisit;
    EditText edAddPrescription;
    Button btn_ok;
    Button btn_done;
    FirebaseDatabase database;
    DatabaseReference reference;
    PatientInfo patientInfo;
    PatientInfo patientDetails;
   String patientID;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chart_with_prescription);
        setInfo();
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String prescription = edAddPrescription.getText().toString();
                if(TextUtils.isEmpty(prescription)){
                    Toast.makeText(PatientChartWithPrescription.this,
                            "Please Add Prescription!!!", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingBar.setMessage("Please wait for few seconds");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    reference.child("Patient").child(patientID).child("Prescription").
                            child("Prescription").setValue(prescription).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                loadingBar.dismiss();
                                Toast.makeText(PatientChartWithPrescription.this, "Successfully Added", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                loadingBar.dismiss();
                                Toast.makeText(PatientChartWithPrescription.this, "Failed to update", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PatientChartWithPrescription.this,DoctorActivity.class);
                startActivity(intent);
            }
        });







    }

    private void setInfo() {
        tvName = (TextView)findViewById(R.id.tvName);
        tvAddress = (TextView)findViewById(R.id.tvAddress);
        tvDob = (TextView)findViewById(R.id.tvDob);
        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvPhone = (TextView)findViewById(R.id.tvPhone);
        tvWeight = (TextView)findViewById(R.id.tvWeight);
        tvHeight = (TextView)findViewById(R.id.tvHeight);
        tvBp = (TextView)findViewById(R.id.tvShowBP);
        tvReasonToVisit = (TextView)findViewById(R.id.tvReason);
        edAddPrescription = (EditText)findViewById(R.id.edPrescription);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_done = (Button)findViewById(R.id.btn_done);
        patientInfo = new PatientInfo();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        loadingBar = new ProgressDialog(this);
        Intent intent = getIntent();
        patientID= intent.getStringExtra("PatientId");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientInfo = snapshot.child("Patient").child(patientID).child("Personal Information").getValue(PatientInfo.class);

                System.out.println("********"+patientInfo.getAddress());
                tvName.setText(patientInfo.getName());
                tvAddress.setText(patientInfo.getAddress());
                tvDob.setText(patientInfo.getDateOfBirth());
                tvPhone.setText(patientInfo.getPhoneNumber());
                tvEmail.setText(patientInfo.getEmailId());
                patientDetails = snapshot.child("Patient").child(patientID).child("Details").getValue(PatientInfo.class);
                tvWeight.setText(patientDetails.getWeight());
                tvHeight.setText(patientDetails.getHeight());
                tvBp.setText(patientDetails.getBloodPressure());
                tvReasonToVisit.setText(patientDetails.getReason());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}