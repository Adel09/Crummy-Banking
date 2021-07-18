package com.crumlabs.crummybanking;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import androidx.core.app.ActivityCompat;

import com.google.firebase.auth.*;
import com.google.firebase.database.*;

public class Airtime extends Activity {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    String bank;
    EditText phoneText, amountText;
    String phonenum, amount, myNum;
    Boolean forSelf = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_airtime);
        phoneText = (EditText) findViewById(R.id.phone_text);
        amountText = (EditText) findViewById(R.id.amount_text);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        String uid = user.getUid();
        DatabaseReference ref1 = database.getReference().child(uid).child("bank");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bank = dataSnapshot.getValue(String.class);
                Toast.makeText(getApplicationContext(), bank, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        DatabaseReference ref = database.getReference().child(uid).child("phone");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                myNum = dataSnapshot.getValue(String.class);
                Toast.makeText(getApplicationContext(), bank, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


    }


    public void onCheckboxClicked(View view){
        forSelf = ((CheckBox) view).isChecked();;
        if(forSelf){
            phoneText.setText(myNum);
        }
    }


    public void buy(View view){
        phonenum = phoneText.getText().toString();
        amount = amountText.getText().toString();
        Intent i = new Intent(Intent.ACTION_CALL);


        i.setData(ussd(phonenum, amount));

        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_SHORT).show();

        }
        startActivity(i);



    }


    public Uri ussd(String phone, String am){
        if(bank.equals("Fidelity Bank")){
            String fidelitycode = "*770*" + phone + "*" + am + "#";
            return Uri.parse("tel:"+ Uri.encode(fidelitycode));

        }
        if(bank.equals("FirstBank")){
            String firstbankcode = "*894*" + am + "*" + phone + "#";
            return Uri.parse("tel:"+ Uri.encode(firstbankcode));

        }
        if(bank.equals("Zenith Bank")){
            String zenithcode = "*966*" + am + "*" + phone + "#";
            return Uri.parse("tel:"+ Uri.encode(zenithcode));

        }
        if(bank.equals("UBA")){
            String ubacode = "*919*" + phone + "*" + am + "#";
            return Uri.parse("tel:"+ Uri.encode(ubacode));

        }
        if(bank.equals("Wema Bank")){
            String wemacode = "*945*" + phone + "*" + am + "#";
            return Uri.parse("tel:"+ Uri.encode(wemacode));

        }


        return null;
    }


}