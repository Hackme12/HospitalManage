package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalmanagement.Users.DailyReport;
import com.example.hospitalmanagement.Users.PatientInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PatientChartWithPrescription extends AppCompatActivity {

    EditText tvName,tvAddress, tvDob,tvEmail,tvPhone,tvWeight,tvHeight,tvBp,tvReasonToVisit;
    String  stName, stAddress, stDob, stEmail, stPhone, stWeight, stHeight, stBp,stReason;
    EditText edAddPrescription;
    Button btn_done;
    FirebaseDatabase database;
    DatabaseReference reference;
    PatientInfo patientInfo;
    PatientInfo patientDetails;
   String patientID;
   String DrName, DrId;
    String Prescription;
    DailyReport dailyreport;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chart_with_prescription);
        dailyreport = new DailyReport();
        setAndUpdateInfo();




        btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Prescription = edAddPrescription.getText().toString();


                if (TextUtils.isEmpty(Prescription))
                {
                    Toast.makeText(PatientChartWithPrescription.this,
                            "Please Add Prescription!!!", Toast.LENGTH_SHORT)
                            .show();

                }
                else
                {
                    loadingBar.setMessage("Please wait for few seconds");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    updateDateofbirthchanged();
                    updateAddressChanged();
                    updateEmailchanged();
                    updateisNameChanged();
                    updatePhonenumberchanged();
                    CheckPatient();
                }

            }

        });
    }

    private void setAndUpdateInfo() {
        tvName = (EditText) findViewById(R.id.tvName);
        tvAddress = (EditText) findViewById(R.id.tvAddress);
        tvDob = (EditText) findViewById(R.id.tvDob);
        tvEmail = (EditText) findViewById(R.id.tvEmail);
        tvPhone = (EditText) findViewById(R.id.tvPhone);
        tvWeight = (EditText) findViewById(R.id.tvWeight);
        tvHeight = (EditText) findViewById(R.id.tvHeight);
        tvBp = (EditText) findViewById(R.id.tvShowBP);
        tvReasonToVisit = (EditText) findViewById(R.id.tvReason);
        edAddPrescription = (EditText) findViewById(R.id.edPrescription);
        btn_done = (Button) findViewById(R.id.btn_done_update);
        patientInfo = new PatientInfo();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        loadingBar = new ProgressDialog(PatientChartWithPrescription.this);
        Intent intent = getIntent();
        patientID = intent.getStringExtra("PatientId");
        DrName = intent.getStringExtra("DrName");
        DrId = intent.getStringExtra("DoctorId");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                patientInfo = snapshot.child("Patient").child(patientID).child("Personal Information").getValue(PatientInfo.class);
                patientDetails = snapshot.child("Patient").child(patientID).child("Details").getValue(PatientInfo.class);
                stName = (patientInfo.getName());
                stAddress = (patientInfo.getAddress());
                stDob = (patientInfo.getDateOfBirth());
                stPhone = (patientInfo.getPhoneNumber());
                stEmail = (patientInfo.getEmailId());
                stWeight = (patientDetails).getWeight();
                stHeight = (patientDetails).getHeight();
                stBp = (patientDetails).getBloodPressure();
                stReason = (patientDetails).getReason();
                tvName.setText(stName);
                tvAddress.setText(stAddress);
                tvDob.setText(stDob);
                tvPhone.setText(stPhone);
                tvEmail.setText(stEmail);
                tvWeight.setText(stWeight);
                tvHeight.setText(stHeight);
                tvBp.setText(stBp);
                tvReasonToVisit.setText(stReason);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateDateofbirthchanged() {
        if (!stDob.equals(tvDob.getEditableText().toString())) {
            reference.child("Patient").child(patientID).child("Personal Information").
                    child("DateOfBirth").setValue(tvDob.getEditableText().toString());
        }
    }

    private void updateEmailchanged() {
        if (!stEmail.equals(tvEmail.getEditableText().toString())) {
            reference.child("Patient").child(patientID).child("Personal Information").
                    child("EmailId").setValue(tvEmail.getEditableText().toString());
        }
    }

    private void updatePhonenumberchanged() {
        if (!stPhone.equals(tvPhone.getEditableText().toString())) {
            reference.child("Patient").child(patientID).child("Personal Information").
                    child("PhoneNumber").setValue(tvPhone.getEditableText().toString());
        }
    }

    private void updateAddressChanged() {
        if (!stAddress.equals(tvAddress.getEditableText().toString())) {
            reference.child("Patient").child(patientID).child("Personal Information").
                    child("Address").setValue(tvAddress.getEditableText().toString());
        }
    }

    private void updateisNameChanged() {
        if (stName.equals(tvName.getEditableText().toString())) {
            reference.child("Patient").child(patientID).child("Personal Information").
                    child("Name").setValue(tvName.getEditableText().toString());
        }
    }

    public void CheckPatient() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientChartWithPrescription.this);
        builder.setTitle("WARNING!!!");
        builder.setMessage("If you click 'YES' you will not be edit and go back to patient chart again. \n if you click 'NO' you" +
                "will be directed to patient chart");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Calendar c = Calendar.getInstance();
                int count = 0;
                final String cdate = DateFormat.getDateInstance().format(c.getTime());
                final String key = patientID + ":" + cdate;
                DatabaseReference removeAppoint = database.getReference("AppointmentList").child(key);
                removeAppoint.removeValue();
                //DatabaseReference removeDoctorSchedule = database.getReference("Doctors Schedule");
                reference.child("Patient").child(patientID).child("Prescription").
                        setValue(Prescription);
                final int finalCount = count;
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String key = cdate + " : " + DrId;
                        dailyreport = snapshot.child("DailyReport").child(key).getValue(DailyReport.class);
                        if (!snapshot.child("DailyReport").child(key).exists()) {
                            HashMap<String, Object> dailyreport = new HashMap<>();
                            dailyreport.put("TotalPatientVisitToday", finalCount + 1);
                            dailyreport.put("DrName", DrName);
                            dailyreport.put("DrId", DrId);
                            dailyreport.put("date", cdate);
                            reference.child("DailyReport").child(key).updateChildren(dailyreport).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(PatientChartWithPrescription.this, "Thank you", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(PatientChartWithPrescription.this, DoctorActivity.class);
                                        startActivity(intent);
                                    } else {
                                        Toast.makeText(PatientChartWithPrescription.this, "failed to update", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                        } else {

                            reference.child("DailyReport").child(key).child("TotalPatientVisitToday").setValue(dailyreport.getTotalPatientVisitToday() + 1);
                            Intent intent = new Intent(PatientChartWithPrescription.this, DoctorActivity.class);
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(PatientChartWithPrescription.this, DoctorActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("                                      NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }


}





                                /*AlertDialog.Builder builder = new AlertDialog.Builder(PatientChartWithPrescription.this);
                                builder.setTitle("WARNING!!!");
                                builder.setMessage("If you click 'YES' you will not be edit and go back to patient chart again. \n if you click 'NO' you" +
                                        "will be directed to patient chart");
                                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        final Calendar c = Calendar.getInstance();
                                        int count = 0;
                                        final String cdate = DateFormat.getDateInstance().format(c.getTime());
                                        final String key = patientID + ":" + cdate;
                                        DatabaseReference removeAppoint = database.getReference("AppointmentList").child(key);
                                        removeAppoint.removeValue();
                                        final int finalCount = count;
                                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                String key = cdate+" : "+ DrName;
                                                dailyreport = snapshot.child("DailyReport").child(key).getValue(DailyReport.class);
                                                if(!snapshot.child("DailyReport").child(key).exists())
                                                {
                                                    HashMap<String, Object> dailyreport = new HashMap<>();
                                                    dailyreport.put("TotalPatientVisitToday", finalCount +1   );
                                                    dailyreport.put("DrName",DrName);
                                                    dailyreport.put("date",cdate);
                                                    reference.child("DailyReport").child(key).updateChildren(dailyreport).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()){
                                                                Toast.makeText(PatientChartWithPrescription.this, "Thank you", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(PatientChartWithPrescription.this,DoctorActivity.class);
                                                                startActivity(intent);
                                                            }
                                                            else {
                                                                Toast.makeText(PatientChartWithPrescription.this, "failed to update", Toast.LENGTH_SHORT).show();
                                                            }

                                                        }
                                                    });

                                                }
                                                else {

                                                    reference.child("DailyReport").child(key).child("TotalPatientVisitToday").setValue(dailyreport.getTotalPatientVisitToday()+1);
                                                    Intent intent = new Intent(PatientChartWithPrescription.this,DoctorActivity.class);
                                                    startActivity(intent);
                                                }

                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        Intent intent = new Intent(PatientChartWithPrescription.this, DoctorActivity.class);
                                        startActivity(intent);
                                    }
                                });
                                builder.setNegativeButton("                                      NO", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();

                                    }
                                });


                                AlertDialog alertDialog = builder.create();
                                alertDialog.show();*/
