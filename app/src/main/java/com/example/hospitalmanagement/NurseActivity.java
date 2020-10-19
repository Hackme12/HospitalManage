package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hospitalmanagement.Users.PatientInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class NurseActivity extends AppCompatActivity {



    private EditText edPatientId, edPatientName;
    private String patientId,patientName;
    private Button Btn_Next;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nurse);

        edPatientId = (EditText)findViewById(R.id.id_patient);
        edPatientName = (EditText)findViewById(R.id.id_fullName);
        Btn_Next = (Button)findViewById(R.id.btn_Next);
        loadingBar = new ProgressDialog(this);


        Btn_Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patientId = edPatientId.getText().toString();
                patientName = edPatientName.getText().toString();

                if(TextUtils.isEmpty(patientId)){
                    Toast.makeText(NurseActivity.this, "Enter Patient Id", Toast.LENGTH_SHORT).show();
                }
                else if(TextUtils.isEmpty(patientName)){
                    Toast.makeText(NurseActivity.this, "Enter Patient Id", Toast.LENGTH_SHORT).show();
                }
                else {
                    loadingBar.setMessage("Please wait, while checking the credentials");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    ValidatePatient(patientId, patientName);
                }
            }
        });


    }

    private void ValidatePatient(final String pId, final String pName) {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                if (datasnapshot.child("Patient").child(pId).exists()) {
                    PatientInfo cPatient = datasnapshot.child("Patient").child(pId).child("Personal Information").getValue(PatientInfo.class);

                    if ((pId.equals(cPatient.getPatientID())) && (pName.equals(cPatient.getName()))) {

                        loadingBar.dismiss();
                        Intent intent = new Intent(NurseActivity.this, updatePatientActivity.class);
                        intent.putExtra("Id",cPatient.getPatientID());
                        intent.putExtra("pName",cPatient.getName());
                        intent.putExtra("pAddress",cPatient.getAddress());
                        intent.putExtra("pPhone",cPatient.getPhoneNumber());
                        intent.putExtra("pDateofBirth",cPatient.getDateOfBirth());
                        intent.putExtra("pEmail",cPatient.getEmailId());
                        intent.putExtra("Message","editFalse");
                        startActivity(intent);
                    }


                } else
                {
                    loadingBar.dismiss();
                    Toast.makeText(NurseActivity.this, "patientId Doesn't Exist", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
                Intent intent = new Intent(NurseActivity.this, MainActivity.class);
                startActivity(intent);

                break;

            default:
                break;

        }
        return true;


    }
}