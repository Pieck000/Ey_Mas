package com.isproject.ey.ui.perfil;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ey.HomeActivity;
import com.isproject.ey.MainActivity;
import com.isproject.ey.R;
import com.isproject.ey.firebase.Usuario;

import org.jetbrains.annotations.NotNull;

public class PerfilFragment extends Fragment implements View.OnClickListener{

    private PerfilViewModel mViewModel;
    public static PerfilFragment newInstance() {
        return new PerfilFragment();
    }
    //
    Button btOut, btMail, btDatos;
    TextView tvNomb, tvCorreo, tvTel, tvFecha;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    //
    String idUs;
    Usuario usu;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_perfil, container, false);
        //
        iniciarFB();
        iniciarElementos(root);
        idUs = mAuth.getCurrentUser().getUid();
        llenarDatos();
        //

        return root;
    }
    private void llenarDatos(){
        try {
            databaseReference.child("Usuario").child(idUs).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    usu = snapshot.getValue(Usuario.class);
                    //
                    tvNomb.setText("Nombre:\n"+usu.getNombre().toString()+" "+usu.getApellidos());
                    tvCorreo.setText("Correo:\n"+usu.getCorreo().toString());
                    tvFecha.setText("Fecha de Nacimiento:\n"+usu.getFecha().toString());
                    tvTel.setText("Telefono:\n"+usu.getTel().toString());
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {}
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "Error "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void iniciarElementos(View root){
        btOut = root.findViewById(R.id.bt_logout_per);
        btDatos = root.findViewById(R.id.bts_datos_per);
        btMail = root.findViewById(R.id.bts_correo_per);
        //
        btOut.setOnClickListener(this);
        btMail.setOnClickListener(this);
        btDatos.setOnClickListener(this);
        //
        tvNomb = root.findViewById(R.id.tv_nom_per);
        tvCorreo = root.findViewById(R.id.tv_correo_per);
        tvFecha = root.findViewById(R.id.tv_fecha_per);
        tvTel = root.findViewById(R.id.tv_tel_per);
    }
    private void iniciarFB()
    {
        try
        {
            FirebaseApp.initializeApp(getContext());
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference();
            mAuth = FirebaseAuth.getInstance();
        }catch(Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PerfilViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_logout_per:
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.bts_datos_per:
                break;
            case R.id.bts_correo_per:
                //mAuth.getCurrentUser().updateEmail();
                break;
        }
    }
}