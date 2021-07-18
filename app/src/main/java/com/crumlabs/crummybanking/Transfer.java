package com.crumlabs.crummybanking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.firebase.auth.*;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Transfer extends Activity {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    String bank;
    EditText benAccText, amountText;
    String benAccNum, amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        benAccText = (EditText) findViewById(R.id.ben_acc_input);
        amountText = (EditText) findViewById(R.id.trans_amount_input);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        String uid = user.getUid(); //dd
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
    }


    public void transfer(View view){
        benAccNum = benAccText.getText().toString();
        amount = amountText.getText().toString();
        if(benAccNum.length() < 10){
            Toast.makeText(getApplicationContext(), "Enter a valid account number", Toast.LENGTH_SHORT).show();

        }else{
            Intent i = new Intent(Intent.ACTION_CALL);


            i.setData(ussd(benAccNum, amount));

            if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission not granted", Toast.LENGTH_SHORT).show();

            }
            startActivity(i);
        }

    }




    public Uri ussd(String accnum, String am){
        if(bank.equals("Fidelity Bank")){
            String fidelitycode = "*770*" + accnum + "*" + am + "#";
            return Uri.parse("tel:"+ Uri.encode(fidelitycode));

        }
        if(bank.equals("FirstBank")){
            String firstbankcode = "*894*" + am + "*" + accnum + "#";
            return Uri.parse("tel:"+ Uri.encode(firstbankcode));

        }
        if(bank.equals("Zenith Bank")){
            String zenithcode = "*966*" + am + "*" + accnum + "#";
            return Uri.parse("tel:"+ Uri.encode(zenithcode));

        }
        if(bank.equals("UBA")){
            String ubacode = "*919*" + accnum + "*" + am + "#";
            return Uri.parse("tel:"+ Uri.encode(ubacode));

        }
        if(bank.equals("Wema Bank")){
            String wemacode = "*945*" + accnum + "*" + am + "#";
            return Uri.parse("tel:"+ Uri.encode(wemacode));

        }


        return null;
    }


}