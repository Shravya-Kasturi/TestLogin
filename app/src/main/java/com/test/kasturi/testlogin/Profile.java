package com.test.kasturi.testlogin;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {
    Button ok,signout;
    TextView past;
    EditText no;
    DatabaseReference databaseReference;
    FirebaseAuth.AuthStateListener mAuthListener;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ok=(Button)findViewById(R.id.ok);
        signout=(Button)findViewById(R.id.signout);
        past=(TextView)findViewById(R.id.past);
        no=(EditText)findViewById(R.id.no);
        databaseReference= FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(Profile.this,MainActivity.class));
                }
            }
        };
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String s=no.getText().toString();
                if(Integer.parseInt(s)<=0 || Integer.parseInt(s)>25)
                {
                    past.setText("Please enter a valid number");
                    return;
                }
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        databaseReference.removeEventListener(this);
                        String a=dataSnapshot.child(s).getValue().toString();
                        past.setText(a);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(Profile.this,MainActivity.class));
            }
        });
    }
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
