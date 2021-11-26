package com.isproject.ey;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ActionMenuView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ey.firebase.Evento;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ListView lvEvt;
    Button btOut, btAdd;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Evento evento;
    private List<Evento> listaEvento = new ArrayList<Evento>();
    ArrayAdapter<Evento> arrayAdapterItem;
    //
    String idAn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        iniciarFB();
        //LOGOUT
        btOut = findViewById(R.id.bt_logout_h);
        btOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        //ADD
        btAdd = findViewById(R.id.bt_nevento_h);
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, PaquetesActivity.class);
                intent.putExtra("usuR", true);
                startActivity(intent);
            }
        });
        //
        lvEvt = findViewById(R.id.lv_eventos_h);
        idAn = mAuth.getCurrentUser().getUid();
        listarDatos();
        //
        try {
            lvEvt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        evento = (Evento)parent.getItemAtPosition(position);
                        String idEv = evento.getIdEVento().toString();
                        Toast.makeText(HomeActivity.this, ""+idEv,Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                        intent.putExtra("idEv", idEv);
                        startActivity(intent);
                    }catch (Exception e){
                        Toast.makeText(HomeActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(HomeActivity.this, e.getMessage(),Toast.LENGTH_LONG).show();
        }


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
    private void listarDatos()
    {
        try {
            databaseReference.child("Evento").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot objtSnapshot: snapshot.getChildren()){
                        evento = objtSnapshot.getValue(Evento.class);
                        if(evento.getIdAnfitrion().equals(idAn))
                        {
                            listaEvento.add(evento);
                            llenarLista(listaEvento);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void llenarLista(List<Evento> lista){
        try {
            arrayAdapterItem = new ArrayAdapter<Evento>(
                    HomeActivity.this,
                    android.R.layout.simple_list_item_1, lista);
            lvEvt.setAdapter(arrayAdapterItem);
        }catch (Exception e){
            Toast.makeText(HomeActivity.this, ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
}