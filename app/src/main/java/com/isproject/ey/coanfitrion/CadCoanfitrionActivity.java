package com.isproject.ey.coanfitrion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.isproject.ey.R;

public class CadCoanfitrionActivity extends AppCompatActivity {
    //
    Button btScan, btBack;
    TextView tvQr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cad_coanfitrion);
    }
}