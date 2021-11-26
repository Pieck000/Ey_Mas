package com.isproject.ey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PayActivity extends AppCompatActivity implements View.OnClickListener {
    Button btNext, btBack;
    EditText etCorr, etPass;
    int pqt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);

       pqt = getIntent().getIntExtra("pqt",9);

        btNext = findViewById(R.id.bt_next_pay);
        btNext.setOnClickListener(this);
        btBack = findViewById(R.id.bt_back_pay);
        btBack.setOnClickListener(this);

        etCorr = findViewById(R.id.edit_correo_pay);
        etPass = findViewById(R.id.edit_password_pay);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next_pay:
                if(mValidar()){
                    Intent intent = new Intent(PayActivity.this, EventoActivity.class);
                    intent.putExtra("pqt",pqt);
                    startActivity(intent);
                    Toast.makeText(this, ""+pqt, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.bt_back_pay:
                break;
        }
    }
    private boolean mValidar(){
        if(etPass.getText().toString().equals("")||etCorr.getText().toString().equals("")){
            Toast.makeText(this, "Campos vacios", Toast.LENGTH_LONG).show();
            return false;
        }else{
            return true;
        }
    }
}