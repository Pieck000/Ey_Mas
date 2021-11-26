package com.isproject.ey;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.EventLog;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class PaquetesActivity extends AppCompatActivity implements View.OnClickListener{
    Button btNext, btCancel;
    RadioGroup rgPqt;
    RadioButton rbP1, rbP2, rbP3, rbP4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paquetes);
        iniciarEl();
        btNext = findViewById(R.id.bt_next_p);
        btNext.setOnClickListener(this);
        btCancel = findViewById(R.id.bt_back_p);
        btCancel.setOnClickListener(this);
    }
    private void iniciarEl(){
        rgPqt = findViewById(R.id.rdg_pqt_p);
        rbP1 = findViewById(R.id.rb_pqt1_p);
        rbP2 = findViewById(R.id.rb_pqt2_p);
        rbP3 = findViewById(R.id.rb_pqt3_p);
        rbP4 = findViewById(R.id.rb_pqt4_p);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next_p:
                try {
                    int rbt=0;
                    if(rbP1.isChecked()){
                        rbt = 1;
                    }else if (rbP2.isChecked()){
                        rbt = 2;
                    }else if (rbP3.isChecked()){
                        rbt = 3;
                    }else if (rbP4.isChecked()){
                        rbt = 4;
                    }

                    Intent intent = new Intent(PaquetesActivity.this, PayActivity.class);
                    intent.putExtra("pqt",rbt);
                    Toast.makeText(this, ""+rbt, Toast.LENGTH_LONG).show();
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(this, "Elige un paquete", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}