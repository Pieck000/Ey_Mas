package com.isproject.ey.coanfitrion;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.isproject.ey.R;
import com.isproject.ey.ui.cadenero.CadeneroFragment;

public class CadCoanfitrionActivity extends AppCompatActivity {
    //
    Button btScan, btBack;
    TextView tvScan;
    String idEv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_coanfitrion);
        //
        idEv = getIntent().getStringExtra("idEv");
        //
        tvScan = findViewById(R.id.tv_scan_cadco);
        btScan = findViewById(R.id.bt_scan_cadco);
        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator integrator = new IntentIntegrator(CadCoanfitrionActivity.this);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Ey+, Escanea una Invitacion");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(true);
                integrator.setBarcodeImageEnabled(true);
                integrator.initiateScan();
            }
        });
        //
        btBack = findViewById(R.id.bts_back_cadco);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CadCoanfitrionActivity.this, CoanfitrionActivity.class);
                intent.putExtra("idEv",idEv.toString());
                startActivity(intent);
                finish();
            }
        });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null) {
            if(result.getContents() == null) {
                Toast.makeText(this, "Escaneo Cancelado", Toast.LENGTH_LONG).show();
            } else {
                tvScan.setText(result.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}