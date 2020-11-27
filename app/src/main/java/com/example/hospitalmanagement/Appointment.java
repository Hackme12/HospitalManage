package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.CircularPropagation;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalmanagement.Users.AppointmentInformation;
import com.example.hospitalmanagement.Users.PatientInfo;
import com.example.hospitalmanagement.Users.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class Appointment extends AppCompatActivity {


    private CircleImageView Create_appointment, Change_appointment, Cancel_appointment;
    private Button Create_btn, Change_btn, Cancel_btn;
    private EditText PatientId, FullName, Dob;
    private TextView Previous_page;
    private ProgressDialog loadingBar;
    private String temp;
    private Button select_date;
    private AppointmentInformation API;
    private ArrayList<String> patientId;
    String Patient_ID ;
    String Full_Name ;
    String Date_of_birth ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        Create_appointment = (CircleImageView) findViewById(R.id.create_image);
        Change_appointment = (CircleImageView) findViewById(R.id.change_image);
        Cancel_appointment = (CircleImageView) findViewById(R.id.cancel_image);

        Create_btn = (Button) findViewById(R.id.btn_create);
        Change_btn = (Button) findViewById(R.id.btn_change);
        Cancel_btn = (Button) findViewById(R.id.btn_cancel);
        API = new AppointmentInformation();

        Previous_page = (TextView) findViewById(R.id.previous_page);
        loadingBar = new ProgressDialog(this);

        Create_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = "Create";
                CreateAppointment();
            }


        });
        Change_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = "Change";
                Intent intent = new Intent(Appointment.this, AppointmentList.class);
                intent.putExtra("ChangeAppointment",temp);
                startActivity(intent);
            }


        });

        Cancel_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent  =new Intent(Appointment.this, CancelAppointment.class);
               startActivity(intent);
            }


        });


        Previous_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Appointment.this, StaffActivity.class);
                startActivity(intent);

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case (R.id.Logout):
                Intent intent = new Intent(Appointment.this, MainActivity.class);
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

    private void CreateAppointment() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Appointment.this);
        View view = LayoutInflater.from(Appointment.this).inflate(R.layout.patient_appointment_dialog, null);
        builder.setView(view);

        PatientId = view.findViewById(R.id.patient_id);
        FullName = view.findViewById(R.id.fullName);
        Dob = view.findViewById(R.id.dob);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                Patient_ID = PatientId.getText().toString().trim();
                Full_Name = FullName.getText().toString().trim();
                Date_of_birth = Dob.getText().toString().trim();


                if (TextUtils.isEmpty(Patient_ID)) {
                    Toast.makeText(Appointment.this, "Please Enter Patient Id", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Full_Name)) {
                    Toast.makeText(Appointment.this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(Full_Name)) {
                    Toast.makeText(Appointment.this, "Please Enter Date Of Birth", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Message");
                    loadingBar.setMessage("Please wait while checking your credentials");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    ValidatePatientInfoOnAppointment(Patient_ID, Full_Name, Date_of_birth);
                }
            }
        });

        builder.setNegativeButton("Cancel                                             ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private void ValidatePatientInfoOnAppointment(final String patientId, final String fullName, final String Dob) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.child("Patient").child(patientId).exists()) {
                    PatientInfo patientData = datasnapshot.child("Patient").child(patientId).child("Personal Information").getValue(PatientInfo.class);

                    if ((patientId.equals(patientData.getPatientID())) && (fullName.equals(patientData.getName()))
                            && (Dob.equals(patientData.getDateOfBirth())) && temp.equals("Create"))
                    {

                        loadingBar.dismiss();
                        Intent intent = new Intent(Appointment.this, SelectDate.class);
                        intent.putExtra("Name", fullName);
                        intent.putExtra("PatientId",patientId);
                        intent.putExtra("Temp","Create");
                        startActivity(intent);
                    }
                } else
                    {
                    loadingBar.dismiss();
                    Toast.makeText(Appointment.this, "patientId Doesn't Exist", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Appointment.this, StaffActivity.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    }

