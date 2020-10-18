package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.TimeZoneFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.hospitalmanagement.Users.PatientInfo;
import com.example.hospitalmanagement.Users.PatientPrevalent;
import com.example.hospitalmanagement.Users.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.jar.Attributes;


public class SelectDate extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button btn_selectDate, btn_SelectTime;
    private TextView date_of_appointment, current_patientName;
    private Spinner spinner, spinnerForDoctorList;
    private String selected_time, selected_date,selected_doctor;
    private Button btn_Create;
    //ValueEventListener listener;
    private ProgressDialog loadingBar;

    ArrayAdapter<String> adapter1;
    private ArrayList<String> spinnerDataList;
    DatabaseReference databaseReference;
    private String CurrentPatientName;
    private String CurrentPatientId;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);

        btn_selectDate = (Button) findViewById(R.id.btn_Select_Date);
        btn_SelectTime = (Button) findViewById(R.id.btn_Select_Time);
        date_of_appointment = (TextView) findViewById(R.id.date_of_appointment);
        current_patientName = (TextView) findViewById(R.id.Current_patient);
        btn_Create = (Button) findViewById(R.id.btn_create_appoint);
        loadingBar = new ProgressDialog(this);

        spinnerForDoctorList = (Spinner) findViewById(R.id.spinner_Doctor);
        user = new Users();
        CurrentPatientName = getIntent().getExtras().get("Name").toString();
        CurrentPatientId = getIntent().getExtras().getString("PatientId").toString();

        current_patientName.setText(CurrentPatientName);

        spinnerDataList = new ArrayList<>();
        adapter1 = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerDataList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerForDoctorList.setAdapter(adapter1);
        spinnerForDoctorList.setOnItemSelectedListener(this);


        spinner = (Spinner) findViewById(R.id.spinner_time);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Time, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);


        btn_selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                int YEAR = c.get(Calendar.YEAR);
                int MONTH = c.get(Calendar.MONTH);
                int DAY_0F_MONTH = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(SelectDate.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.YEAR, year);
                        c.set(Calendar.MONTH, month);
                        c.set(Calendar.DAY_OF_MONTH, day);
                        selected_date = DateFormat.getDateInstance().format(c.getTime());
                        date_of_appointment.setText(selected_date);


                    }
                }, YEAR, MONTH, DAY_0F_MONTH);
                datePickerDialog.show();

            }
        });


        // Getting data from database and storing into the spinner


        databaseReference = FirebaseDatabase.getInstance().getReference().child("Doctor");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                for (DataSnapshot item : datasnapshot.getChildren()) {
                    user = item.getValue(Users.class);

                    spinnerDataList.add(user.getName());
                }
                adapter1.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String appointment_date = selected_date;
                String appointment_time = selected_time;
                String doctor_name = selected_doctor;
                String Patient_name = CurrentPatientName;
                String Patient_ID= CurrentPatientId;

                if(TextUtils.isEmpty(appointment_date)){
                    Toast.makeText(SelectDate.this, "Please Select Appointment date!!!", Toast.LENGTH_SHORT).show();
                }
                else if((appointment_time.equals("00:00 : 00:00"))){
                    Toast.makeText(SelectDate.this, "Please Select Appointment date!!!", Toast.LENGTH_SHORT).show();
                }
                else{

                    loadingBar.setMessage("Please Wait while checking the Appointment Time");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    checkAppointment(appointment_date,appointment_time,doctor_name,Patient_name,Patient_ID);

                }



            }
        });

        spinnerForDoctorList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_doctor = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }

    private void checkAppointment(final String date, final String time, final String doctorName, final String patientName, final String patientId) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();
        final DatabaseReference secondRef  = database.getReference();

       // final String Key = doctor_name + " : "+ appointment_date +" : "+ appointment_time;



        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

               final String key = patientId+":" + date;
               final String secondKey = doctorName +":" + time;


                if(!datasnapshot.child("AppointmentList").child(key).exists()&&(!datasnapshot.child("Doctors Schedule").child(secondKey).exists())){


                    HashMap<String, Object> appointmentData = new HashMap<>();
                    appointmentData.put("doctorName",doctorName);
                    appointmentData.put("patientName",patientName);
                    appointmentData.put("key", key);
                    appointmentData.put("date",date);
                    appointmentData.put("time",time);
                    appointmentData.put("patientId",patientId);

                    myRef.child("AppointmentList").child(key).updateChildren(appointmentData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SelectDate.this, "Congratulation, your appointment has been created", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        //Intent intent = new Intent(RegisterActivitythis, MainActivity.class);
                                        // startActivity(intent);
                                    } else {
                                        loadingBar.dismiss();
                                        Toast.makeText(SelectDate.this, "Failed to create your appointment", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    HashMap<String, Object> doctorScheduleData = new HashMap<>();

                    doctorScheduleData.put("patientId",patientId);
                    doctorScheduleData.put("patientName",patientName);
                    doctorScheduleData.put("time",time);
                    doctorScheduleData.put("doctorName",doctorName);
                    doctorScheduleData.put("secondKey",secondKey);


                    myRef.child("Doctors Schedule").child(secondKey).updateChildren(doctorScheduleData).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SelectDate.this, "Succesfully uploaded at doctor's Schedule", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                //Intent intent = new Intent(RegisterActivitythis, MainActivity.class);
                                // startActivity(intent);
                            } else {
                                loadingBar.dismiss();
                                Toast.makeText(SelectDate.this, "Failed to upload to doctor's Schedule", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                else{

                    loadingBar.dismiss();
                    Toast.makeText(SelectDate.this, "There is already an appointment of "+patientName+ " for today or \n" +
                            "Requested Doctor is not available at that time \n Please try with another time", Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        selected_time = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
                Intent intent = new Intent(SelectDate.this,MainActivity.class);
                startActivity(intent);

                break;

            default:
                break;

        }
        return true;
    }




}
