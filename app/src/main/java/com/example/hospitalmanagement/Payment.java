package com.example.hospitalmanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.lang.Math;
public class Payment extends AppCompatActivity {

    private EditText patientId,cardNumber,expDate,amount;
    private Button btnPayNow;;
    public FirebaseDatabase database;
    private long theRandomNum;
    private Boolean checkReferenceNumber;
    CancelAppointment objCancelAppointment;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        database = FirebaseDatabase.getInstance();
        objCancelAppointment = new CancelAppointment();
        patientId = (EditText)findViewById(R.id.edPatientId);
        cardNumber = (EditText)findViewById(R.id.edCardNumber);
        expDate = (EditText)findViewById(R.id.edExpiredate);
        amount = (EditText)findViewById(R.id.edPaymentAmount);
        checkReferenceNumber = false;
        theRandomNum = 0;

         Intent intent = getIntent();
         name = intent.getStringExtra("patName");
         System.out.println("***"+ name);


        btnPayNow = (Button)findViewById(R.id.btn_payment);
        btnPayNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String PateintID = patientId.getText().toString();
                String CardNumber= cardNumber.getText().toString();
                String expdate= expDate.getText().toString();
                String PayAmnt= amount.getText().toString();


                if(TextUtils.isEmpty(PateintID)){
                    Toast.makeText(Payment.this, "Please Enter Patient Id", Toast.LENGTH_SHORT).show();
                }

                else  if(TextUtils.isEmpty(CardNumber)||(CardNumber.length()!=16)){
                    Toast.makeText(Payment.this, "Please Enter Your Card Number ", Toast.LENGTH_SHORT).show();
                }
                else  if(TextUtils.isEmpty(expdate)){
                    Toast.makeText(Payment.this, "Please Enter Expiration date", Toast.LENGTH_SHORT).show();
                }
                else  if(TextUtils.isEmpty(PayAmnt)){
                    Toast.makeText(Payment.this, "Please Enter Payment Amount", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(Payment.this,"Authorization of card",Toast.LENGTH_LONG).show();
                    Bank(CardNumber, expdate, PayAmnt);
                    if(checkReferenceNumber){
                        Payment(PateintID,PayAmnt);
                    }
                    else{
                        Toast.makeText(Payment.this, "CARD AUTHORIZXATION FAILED \n PLEASE TRY ANOTEHR CARD",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });




    }

    private void Payment(final String patientID, final String PayAmnt) {

        DatabaseReference reference = database.getReference();

        final Calendar c = Calendar.getInstance();
        final String cdate = DateFormat.getDateInstance().format(c.getTime());
        final String Name = name;
        System.out.println("*************************************"+name);


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DatabaseReference myref1 = database.getReference();

                if(!snapshot.child("Payment").child(patientID).child(cdate).exists()){
                    //deleting the appointment from appointment list
                    //objCancelAppointment.deletePatientAppointment(patientID,cdate);
                    addToCheckedInList(patientID,Name,PayAmnt,cdate);

                    //updating payment into the database
                    HashMap<String, Object> payment_Profile = new HashMap<>();
                    payment_Profile.put("PatientId",patientID);
                    payment_Profile.put("Name",Name);
                    payment_Profile.put("Amount",PayAmnt );
                    payment_Profile.put("Date",cdate);
                    payment_Profile.put("Payment Type","CARD");
                    payment_Profile.put("Reference Number",theRandomNum);

                    String key = patientID+":"+ cdate;
                    DatabaseReference removeKey = database.getReference("AppointmentList").child(key);
                    removeKey.removeValue();



                    myref1.child("Payment").child(patientID).child(cdate).updateChildren(payment_Profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(Payment.this,"Payment Successfully Update",Toast.LENGTH_LONG).show();

                               Intent intent = new Intent(Payment.this,StaffActivity.class);
                               startActivity(intent);
                           }
                           else{
                               Toast.makeText(Payment.this,"Failed to Update",Toast.LENGTH_LONG).show();
                           }
                       }
                   });
                }
                else
                    {
                        String key = patientID+":"+ cdate;
                        DatabaseReference removeKey = database.getReference("AppointmentList").child(key);
                        removeKey.removeValue();
                        addToCheckedInList(patientID,Name,PayAmnt,cdate);
                        myref1.child("Payment").child(patientID).
                            child(cdate).child("Other Payment").setValue(PayAmnt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                System.out.println("Succesfully update");
                                Intent intent = new Intent(Payment.this,StaffActivity.class);
                                startActivity(intent);
                            }
                            else{
                                System.out.println("failed to update payment");
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

    public void Bank(String CardNumber, String expDate, String PayAmnt){

         if(TextUtils.isEmpty(CardNumber)||(CardNumber.length()!=16)){
            Toast.makeText(Payment.this, "Please Enter Your Card Number ", Toast.LENGTH_SHORT).show();

        }
        else  if(TextUtils.isEmpty(expDate)){
            Toast.makeText(Payment.this, "Please Enter Expiration date", Toast.LENGTH_SHORT).show();
        }
        else  if(TextUtils.isEmpty(PayAmnt)){
            Toast.makeText(Payment.this, "Please Enter Payment Amount", Toast.LENGTH_SHORT).show();
        }
        else{

            theRandomNum = (long) (Math.random() * Math.pow(10, 10));
            checkReferenceNumber = true;
        }

    }




    public void addToCheckedInList(String patientID, String name, String PayAmnt, String cdate)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myref1 = database.getReference();

        HashMap<String, Object> CheckedInList = new HashMap<>();
        CheckedInList.put("PatientId",patientID);
        CheckedInList.put("Name",name);
        CheckedInList.put("Amount",PayAmnt );
        CheckedInList.put("Date",cdate);
        CheckedInList.put("Status","PAID");
        myref1.child("CheckedInList").child(patientID).updateChildren(CheckedInList).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                    //Toast.makeText(Payment.this, "Successfully added to checked-in list", Toast.LENGTH_SHORT).show();
                    System.out.println("Here");
                }
                else{
                    Toast.makeText(Payment.this,"Payment Successfully Update",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}