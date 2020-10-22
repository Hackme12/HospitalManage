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
import android.widget.Toast;

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

public class PatientChartTwo extends AppCompatActivity {
    private EditText edWeight, edHeight,edBp,edReason;
    private Button btnUpdate;
    private String PatientID;
    private ProgressDialog loadingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_chart_two);

        edWeight  = (EditText)findViewById(R.id.edWeight);
        edHeight  = (EditText)findViewById(R.id.edHeight);
        edBp  = (EditText)findViewById(R.id.edBp);
        edReason  = (EditText)findViewById(R.id.edReason);
        btnUpdate = (Button)findViewById(R.id.btn_Update);
        loadingBar  = new ProgressDialog(this);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Weight = edWeight.getText().toString();
                String Height = edHeight.getText().toString();
                String Bp = edBp.getText().toString();
                String Reason = edReason.getText().toString();

                if(TextUtils.isEmpty(Weight)||(TextUtils.isEmpty(Height))
                        ||(TextUtils.isEmpty(Bp))||(TextUtils.isEmpty(Reason))){
                    Toast.makeText(PatientChartTwo.this, "Please fill all the details.", Toast.LENGTH_SHORT).show();
                }
                else{
                    loadingBar.setMessage("Uploading...");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();
                    updateDetail(Height,Weight,Bp,Reason);
                }
            }
        });
    }


    private void updateDetail(final String Height, final String Weight, final String Bp,final String Reason) {
        Intent intent = getIntent();
        PatientID= intent.getStringExtra("PatientId");
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myref = database.getReference();
        Calendar c = Calendar.getInstance();
        final String todaysdate = DateFormat.getDateInstance().format(c.getTime());

        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(!snapshot.child("Patient").child(PatientID).child("Details").exists()){

                    HashMap<String, Object> detail = new HashMap<>();
                    detail.put("Visit_Date",todaysdate);
                    detail.put("Weight", Weight);
                    detail.put("Height", Height);
                    detail.put("BloodPressure", Bp);
                    detail.put("Reason", Reason);

                    myref.child("Patient").child(PatientID).child("Details").
                            updateChildren(detail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                loadingBar.dismiss();
                                Toast.makeText(PatientChartTwo.this, "Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(PatientChartTwo.this,NurseActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}