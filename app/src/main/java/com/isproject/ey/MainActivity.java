package com.isproject.ey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button btLogin, btRegistra;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        iniciarFB();
        //
        btLogin = findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);
        btRegistra = findViewById(R.id.bts_registrar);
        btRegistra.setOnClickListener(this);

    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            mAuth = FirebaseAuth.getInstance();
        }catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login:
                Intent intent1 = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent1);
                break;
            case R.id.bts_registrar:
                Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
                startActivity(intent);
                break;
        }
    }
    //Sesion
    @Override
    protected void onStart() {
        super.onStart();
        try {
            if(mAuth.getCurrentUser()!=null){
                Toast.makeText(this, ""+mAuth.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, HomeActivity.class));
                finish();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}