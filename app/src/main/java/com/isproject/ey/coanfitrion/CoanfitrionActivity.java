package com.isproject.ey.coanfitrion;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ey.MainActivity;
import com.isproject.ey.R;
import com.isproject.ey.firebase.Coanfitrion;
import com.isproject.ey.firebase.Evento;

import org.jetbrains.annotations.NotNull;

public class CoanfitrionActivity extends AppCompatActivity implements View.OnClickListener {
    //
    Button btInv, btProv, btCad, btOut;
    TextView tvNom, tvFecha, tvHora, tvUbi;
    //FireB
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ViewFlipper vfImgs;
    //
    Evento evnt;
    String idEv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coanfitrion);
        //
        idEv = getIntent().getStringExtra("idEv");
        iniciarElementos();
        iniciarFB();
        llenarDatos();
        //
        int imgs [] = {R.raw.imgey1, R.raw.imgey2, R.raw.imgey3};
        for(int img: imgs){
            llenarFlipper(img);
        }
    }
    private void iniciarElementos(){
        btInv = findViewById(R.id.bt_inv_can);
        btInv.setOnClickListener(this);
        btProv = findViewById(R.id.bt_prov_can);
        btProv.setOnClickListener(this);
        btCad = findViewById(R.id.bts_scan_can);
        btCad.setOnClickListener(this);
        btOut = findViewById(R.id.bts_out_can);
        btOut.setOnClickListener(this);
        //
        tvNom = findViewById(R.id.tv_can);
        tvFecha = findViewById(R.id.tv_fech_can);
        tvUbi = findViewById(R.id.tv_ubi_can);
        tvHora = findViewById(R.id.tv_time_can);
        //
        vfImgs = findViewById(R.id.vf_imgs_can);

    }
    private void llenarDatos(){
        try{
            databaseReference.child("Evento").child(idEv).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    evnt = snapshot.getValue(Evento.class);
                    //
                    tvNom.setText(evnt.getNombre().toString());
                    tvFecha.setText("Fecha: "+evnt.getFecha().toString());
                    tvUbi.setText("Dirección: "+evnt.getUbicacion().toString());
                    tvHora.setText("Hora: "+evnt.getHora().toString()+"\n Paquete "+evnt.getPaquete());
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {}
            });
        }catch (Exception e){

        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_inv_can:
                Intent intent = new Intent(CoanfitrionActivity.this, InvCoanfitrionActivity.class);
                intent.putExtra("idEv",idEv.toString());
                startActivity(intent);
                break;
            case R.id.bt_prov_can:
                Intent intent1 = new Intent(CoanfitrionActivity.this, ProCoanfitrionActivity.class);
                intent1.putExtra("idEv",idEv.toString());
                startActivity(intent1);
                break;
            case R.id.bts_scan_can:
                Intent intent2 = new Intent(CoanfitrionActivity.this, CadCoanfitrionActivity.class);
                intent2.putExtra("idEv",idEv.toString());
                startActivity(intent2);
                break;
            case R.id.bts_out_can:
                Intent intent3 = new Intent(CoanfitrionActivity.this, MainActivity.class);
                startActivity(intent3);
                finish();
                break;
        }
    }
    private void llenarFlipper(int img){
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(img);
        //
        vfImgs.addView(imageView);
        vfImgs.setFlipInterval(3000);
        vfImgs.setAutoStart(true);
        //
        vfImgs.setInAnimation(this, android.R.anim.slide_in_left);
        vfImgs.setOutAnimation(this, android.R.anim.slide_out_right);
    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(this);
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
        }catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}