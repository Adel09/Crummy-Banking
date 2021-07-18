package com.crumlabs.crummybanking;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends Activity implements AdapterView.OnItemSelectedListener {

    EditText firstNameInput, lastNameInput, emailInput, phoneInput, accNumInput;
    Spinner bankSpinner;
    String fname, lname, email, phone, bank, accnum;
    Boolean reg;
    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstNameInput = (EditText) findViewById(R.id.first_name_input);
        lastNameInput = (EditText) findViewById(R.id.last_name_input);
        emailInput = (EditText) findViewById(R.id.email_input);
        phoneInput = (EditText) findViewById(R.id.phone_input);
        bankSpinner = (Spinner) findViewById(R.id.bank_spinner);
        accNumInput = (EditText) findViewById(R.id.acc_num_input);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.banks_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        bankSpinner.setAdapter(adapter);
        bankSpinner.setOnItemSelectedListener(this);
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        //email = user.getEmail();
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (acct != null) {
            fname = acct.getGivenName();
            lname = acct.getFamilyName();
            email = acct.getEmail();
            firstNameInput.setText(fname);
            lastNameInput.setText(lname);
            emailInput.setText(email);

        }else{
            Toast.makeText(this, "No Account", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (hasRegistered()) {
            Intent intent = new Intent(this, Home.class);
            startActivity(intent);
        }
    }

    public void create(View view){
        reg = true;
        phone = phoneInput.getText().toString();
        if(phone.isEmpty()){Toast.makeText(this, "Enter your phone number", Toast.LENGTH_LONG).show();}
        accnum = accNumInput.getText().toString();
        if(accnum.isEmpty()){Toast.makeText(this, "Enter your Account number", Toast.LENGTH_LONG).show();}

        database = FirebaseDatabase.getInstance();
        HashMap<String, Object> map = new HashMap<>();
        map.put("fname", fname);
        map.put("lname", lname);
        map.put("email", email);
        map.put("phone", phone);
        map.put("bank", bank);
        map.put("accountnumber", accnum);
        try {
            String uid = user.getUid();
            database.getReference().child(uid).updateChildren(map);

        }catch (Exception e){
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);

    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

        bank = parent.getItemAtPosition(pos).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        parent.setSelection(0);
    }
    public boolean hasRegistered() {


        if (user != null) {
            String uid = user.getUid();
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