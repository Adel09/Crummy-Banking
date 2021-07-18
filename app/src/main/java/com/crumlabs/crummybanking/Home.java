package com.crumlabs.crummybanking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends Activity {
    private FirebaseAuth mAuth;
    FirebaseUser user;
    TextView welcomeText, accNumText, bankText;
    String name, accNum, bank;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        accNumText = (TextView) findViewById(R.id.acc_num_text);
        bankText = (TextView) findViewById(R.id.bank_text);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = mAuth.getCurrentUser();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        name = acct.getGivenName();
        String email = user.getEmail();
        String uid = user.getUid();
        DatabaseReference ref = database.getReference().child(uid).child("accountnumber");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                accNum = dataSnapshot.getValue(String.class);
                accNumText.setText(accNum);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        DatabaseReference ref1 = database.getReference().child(uid).child("bank");
        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bank = dataSnapshot.getValue(String.class);
                bankText.setText(bank);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        welcomeText.setText("WELCOME, " + name);



    }

    public void signOut(View view){
        mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void buyAirtime(View view){
        Intent intent = new Intent(this, Airtime.class);
        startActivity(intent);
    }
    public void transfer(View view){
        Intent intent = new Intent(this, Transfer.class);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}