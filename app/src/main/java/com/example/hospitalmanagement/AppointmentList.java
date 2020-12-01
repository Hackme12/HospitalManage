package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hospitalmanagement.Users.AppointmentInformation;
import com.example.hospitalmanagement.Users.PatientInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.text.SimpleDateFormat;

public class AppointmentList extends AppCompatActivity {


    ListView listView;

    FirebaseDatabase database;
    DatabaseReference reference;
    private EditText PId, PName;
    private String pid,pname;
    ArrayList<String>list;
    ArrayAdapter<String> adapter;
   private ProgressDialog loadingBar;
    AppointmentInformation AppInform;
    int hr, min, sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_list);
        AppInform = new AppointmentInformation();

        listView = (ListView) findViewById(R.id.list);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("AppointmentList");
        loadingBar = new ProgressDialog(this);
        list = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        final String Currentdate = DateFormat.getDateInstance().format(c.getTime());

        DateFormat df = new SimpleDateFormat("HH:mm");
       final String CurrentTime = df.format(c.getTime());
        final String hour = CurrentTime.substring(0,2);
        DateFormat date = new SimpleDateFormat("HH:mm:ss");
        Date dt = new Date();
        String currentTime = date.format(dt);
         hr = Integer.parseInt(currentTime.substring(0,2));
        min = Integer.parseInt(currentTime.substring(3,5));
        sec = Integer.parseInt(currentTime.substring(6,8));

        adapter = new ArrayAdapter<>(AppointmentList.this, R.layout.appointmentlist,
                R.id.tvAppointmentList, list);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for (DataSnapshot ds : snapshot.getChildren()) {
                        AppInform = ds.getValue(AppointmentInformation.class);
                    // Automatic No show clear appointment
                        if(AppInform.getDate().equals(Currentdate)&&((hr >= 20)&& hr<=23)){

                            reference.removeValue();
                        }
                        else if((AppInform.getDate().equals(Currentdate))){
                            list.add(AppInform.getPatientName() + "\n"+AppInform.getDate() + "\n"+ AppInform.getTime());
                        }
                     }
                    listView.setAdapter(adapter);
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    CheckIn();

                }
            });
    }


        private void CheckIn() {

            AlertDialog.Builder builder = new AlertDialog.Builder(AppointmentList.this);
            View view = LayoutInflater.from(AppointmentList.this).inflate(R.layout.dialogupdatepatientinfo, null);
            builder.setView(view);

            PId = view.findViewById(R.id.id_patient);
            PName = view.findViewById(R.id.id_fullName);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    pid = PId.getText().toString().trim();
                    pname = PName.getText().toString().trim();

                    if (TextUtils.isEmpty(pid)) {
                        Toast.makeText(AppointmentList.this, "Please Enter Patient Id", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(pname)) {
                        Toast.makeText(AppointmentList.this, "Please Enter Full Name", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        loadingBar.setTitle("Message");
                        loadingBar.setMessage("Please wait while checking your credentials");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();
                        ValidatePatientAppointment(pid, pname);

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
            Date date = new Date();
            Calendar c = Calendar.getInstance();


            String selected_date = DateFormat.getDateInstance().format(c.getTime());
            final String key = pId+":"+selected_date;

            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                    if (datasnapshot.child("Patient").child(pId).exists()&&(datasnapshot.child("AppointmentList").child(key).exists())) {
                        PatientInfo cPatient = datasnapshot.child("Patient").child(pId).child("Personal Information").getValue(PatientInfo.class);

                        if ((pId.equals(cPatient.getPatientID())) && (pName.equals(cPatient.getName()))
                        ) {

                            loadingBar.dismiss();
                            Intent intent = new Intent(AppointmentList.this, CheckInActivity.class);
                            intent.putExtra("id",cPatient.getPatientID());
                            intent.putExtra("pname",cPatient.getName());
                            intent.putExtra("paddress",cPatient.getAddress());
                            intent.putExtra("pphone",cPatient.getPhoneNumber());
                            intent.putExtra("pdateofbirth",cPatient.getDateOfBirth());
                            intent.putExtra("pemail",cPatient.getEmailId());
                            startActivity(intent);
                        }


                    } else
                    {
                        loadingBar.dismiss();
                        Toast.makeText(AppointmentList.this, "Error!!! Please Check provided credentials again. \n Or Patient may " +
                                "have an appointment for another day", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




        }






}