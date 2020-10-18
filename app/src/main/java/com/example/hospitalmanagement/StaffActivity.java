package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalmanagement.Users.PatientInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StaffActivity extends AppCompatActivity {

    private ImageView CheckIn, Appointment, AddPatient, UpdatePatientInfo, PayNow, Appointment_List;
    private TextView  textCheckIn, textAppointment, textAddPatient;
    private TextView  textUpdate, textPay, textAppointmentList;
    FirebaseDatabase database;
    DatabaseReference reference;
    private EditText PId, PName;
    private String pId, pName;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff);

        CheckIn = (ImageView) findViewById(R.id.staff_checkIn);
        Appointment =(ImageView) findViewById(R.id.image_Appointment);
        AddPatient = (ImageView) findViewById(R.id.image_addPatient);
        UpdatePatientInfo = (ImageView) findViewById(R.id.image_updatePatientInfo);
        PayNow = (ImageView) findViewById(R.id.image_payment);
        Appointment_List = (ImageView) findViewById(R.id.image_AppointmentList);
        PId = (EditText)findViewById(R.id.id_patient);
        PName = (EditText) findViewById(R.id.id_fullName);
        database= FirebaseDatabase.getInstance();
        reference = database.getReference();
        loadingBar = new ProgressDialog(this);





        CheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffActivity.this, Appointment.class);
                startActivity(intent);

            }
        });
        AddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffActivity.this, AddPatient.class);
                startActivity(intent);
            }
        });

        Appointment_List.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StaffActivity.this, AppointmentList.class);
                startActivity(intent);

            }
        });


        UpdatePatientInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogPatientinfo();
            }
        });

        /*UpdatePatientInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StaffActivity.this);
                view = LayoutInflater.from(StaffActivity.this).inflate(R.layout.dialogupdatepatientinfo,null);
                final String pId= PId.getText().toString();
                final String pName = PName.getText().toString();

                builder.setView(view);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.child("Patient").child(pId).child(pName).exists()){
                                    PatientInfo cPateint = snapshot.child("Patient").child(pId).getValue(PatientInfo.class);

                                    Intent intent = new Intent(StaffActivity.this,updatePatientActivity.class);

                                  intent.putExtra("pName",cPateint.getName());
                                    intent.putExtra("pAddress",cPateint.getAddress());
                                    intent.putExtra("pPhone",cPateint.getPhoneNumber());
                                    intent.putExtra("pDateofBirth",cPateint.getDateOfBirth());
                                    intent.putExtra("pEmail",cPateint.getEmailId());
                                    intent.putExtra("pSocialNumber",cPateint.getSocialSecurityNumber());

                                    startActivity(intent);



                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();




            }
        });*/



    }

    private void DialogPatientinfo() {

        AlertDialog.Builder builder = new AlertDialog.Builder(StaffActivity.this);
        View view = LayoutInflater.from(StaffActivity.this).inflate(R.layout.dialogupdatepatientinfo, null);
        builder.setView(view);

        PId = view.findViewById(R.id.id_patient);
        PName = view.findViewById(R.id.id_fullName);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                pId = PId.getText().toString().trim();
                pName = PName.getText().toString().trim();


                if (TextUtils.isEmpty(pId)) {
                    Toast.makeText(StaffActivity.this, "Please Enter Patient Id", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(pName)) {
                    Toast.makeText(StaffActivity.this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                }
                else {

                    loadingBar.setTitle("Message");
                    loadingBar.setMessage("Please wait while checking your credentials");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    ValidatePatientAppointment(pId, pName);

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

    private void ValidatePatientAppointment(final String pId,final String pName) {


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.child("Patient").child(pId).exists()) {
                    PatientInfo cPatient = datasnapshot.child("Patient").child(pId).child("Personal Information").getValue(PatientInfo.class);

                    if ((pId.equals(cPatient.getPatientID())) && (pName.equals(cPatient.getName()))
                           ) {

                        loadingBar.dismiss();
                        Intent intent = new Intent(StaffActivity.this, updatePatientActivity.class);
                        intent.putExtra("Id",cPatient.getPatientID());
                        intent.putExtra("pName",cPatient.getName());
                        intent.putExtra("pAddress",cPatient.getAddress());
                        intent.putExtra("pPhone",cPatient.getPhoneNumber());
                        intent.putExtra("pDateofBirth",cPatient.getDateOfBirth());
                        intent.putExtra("pEmail",cPatient.getEmailId());
                        startActivity(intent);
                    }


                } else
                {
                    loadingBar.dismiss();
                    Toast.makeText(StaffActivity.this, "patientId Doesn't Exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




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
                Intent intent = new Intent(StaffActivity.this,MainActivity.class);
                startActivity(intent);

                break;

            default:
                break;

        }
        return true;
    }




}