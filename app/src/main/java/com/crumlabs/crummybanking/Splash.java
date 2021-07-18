package com.crumlabs.crummybanking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Splash extends Activity {
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            if (hasRegistered()) {
                Intent intent = new Intent(this, Home.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }

        }else{
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
        public boolean hasRegistered() {


            if (currentUser != null) {
                String uid = currentUser.getUid();
                DatabaseReference reference = database.getReference().child(uid);
                if (reference != null) {
                    return true;
                }else {
                    return false;
                }

            } else {
                return false;
            }

        }
}