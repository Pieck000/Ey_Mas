package com.isproject.ey.coanfitrion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.isproject.ey.R;

public class ProCoanfitrionActivity extends AppCompatActivity implements View.OnClickListener{
    //
    Button btAdd, btList, btBack;
    EditText etNombre, etCorreo, etTel, etPers;
    ListView lvPro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_coanfitrion);
        //
        iniciarElementos();
    }
    private void iniciarElementos(){
        btAdd = findViewById(R.id.bt_add_proca);
        btAdd.setOnClickListener(this);
        btList = findViewById(R.id.bt_list_proca);
        btList.setOnClickListener(this);
        btBack = findViewById(R.id.bts_back_proca);
        btBack.setOnClickListener(this);
        //
        etNombre = findViewById(R.id.edit_nom_proca);
        etCorreo = findViewById(R.id.edit_correo_proca);
        etPers = findViewById(R.id.edit_pers_proca);
        etTel = findViewById(R.id.edit_tel_proca);
        //
        lvPro = findViewById(R.id.lv_proca);
    }

    @Override
    public void onClick(View v) {

    }
}