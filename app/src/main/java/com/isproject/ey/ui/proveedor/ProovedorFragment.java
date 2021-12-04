package com.isproject.ey.ui.proveedor;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ey.R;
import com.isproject.ey.firebase.Evento;
import com.isproject.ey.firebase.Invitado;
import com.isproject.ey.firebase.Proovedor;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ProovedorFragment extends Fragment implements View.OnClickListener{

    private ProovedorViewModel mViewModel;

    public static ProovedorFragment newInstance() {
        return new ProovedorFragment();
    }
    //
    Button btAdd, btList;
    EditText etNomb, etCorreo, etTel, etPersonal;
    ListView lvProv;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    String idEv;
    Proovedor prov, provsel;
    //
    private List<Proovedor> listaProov = new ArrayList<Proovedor>();
    ArrayAdapter<Proovedor> arrayAdapterItem;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_proovedor, container, false);
        //
        idEv = getActivity().getIntent().getStringExtra("idEv");
        iniciarFB();
        iniciarElementos(root);
        //
        listarDatos();
        //
        try{
            lvProv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        provsel = (Proovedor) parent.getItemAtPosition(position);
                        String idPr = provsel.getIdProv().toString();
                        String noPr = provsel.getNombre().toString();
                        //
                        AlertDialog.Builder dialog =new AlertDialog.Builder(getContext());
                        dialog.setTitle("Proovedor: \n"+noPr);
                        dialog.setNeutralButton("Ok", null);
                        dialog.setNegativeButton("Eliminar",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /**/
                                try {
                                    databaseReference.child("Proovedor").child(idPr).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            Toast.makeText(getContext(), "Eliminado", Toast.LENGTH_LONG).show();
                                            llenarLista(listaProov);
                                        }
                                    });
                                }catch (Exception e)
                                {
                                    Toast.makeText(getContext(), e.getMessage()+" "+e.getCause(), Toast.LENGTH_LONG).show();
                                }

                            }
                        });
                        dialog.show();
                    }catch (Exception e){
                        Toast.makeText(getContext(), e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return root;
    }
    private void iniciarElementos(View root){
        btAdd = root.findViewById(R.id.bt_add_proov);
        btList = root.findViewById(R.id.bt_list_proov);
        btAdd.setOnClickListener(this);
        btList.setOnClickListener(this);
        //
        etNomb = root.findViewById(R.id.edit_nom_proov);
        etCorreo = root.findViewById(R.id.edit_correo_proov);
        etTel = root.findViewById(R.id.edit_tel_proov);
        etPersonal = root.findViewById(R.id.edit_pers_proov);
        //
        lvProv = root.findViewById(R.id.lv_proov);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProovedorViewModel.class);
        // TODO: Use the ViewModel
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_add_proov:
                if(mValidar()){
                    try {
                        String idProv = UUID.randomUUID().toString();
                        idProv = idProv.substring(1,10);
                        String nom = etNomb.getText().toString();
                        String corr = etCorreo.getText().toString();
                        int personal = Integer.parseInt(etPersonal.getText().toString());
                        String tel = etTel.getText().toString();
                        //
                        prov = new Proovedor();
                        prov.setIdProv(idProv);
                        prov.setIdEv(idEv);
                        prov.setNombre(nom);
                        prov.setCorreo(corr);
                        prov.setPersonal(personal);
                        prov.setTelefono(tel);
                        databaseReference.child("Proovedor").child(idProv).setValue(prov).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getContext(), "Proovedor Registrado", Toast.LENGTH_LONG).show();
                                    llenarLista(listaProov);
                                    mLimpiar();
                                }else {
                                    Toast.makeText(getContext(), "Error en Registro", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }catch (Exception e)
                    {
                        Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.bt_list_proov:
                llenarLista(listaProov);
                break;
        }
    }
    private void listarDatos()
    {
        try {
            databaseReference.child("Proovedor").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listaProov.clear();
                    for(DataSnapshot objtSnapshot: snapshot.getChildren()){
                        Proovedor in = objtSnapshot.getValue(Proovedor.class);
                        if(in.getIdEv().equals(idEv))
                        {
                            listaProov.add(in);
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public void llenarLista(List<Proovedor> lista){
        try {
            arrayAdapterItem = new ArrayAdapter<Proovedor>(
                    getActivity(),
                    android.R.layout.simple_list_item_1, lista);
            lvProv.setAdapter(arrayAdapterItem);
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    private boolean mValidar(){
        if(etNomb.getText().toString().equals("")||etCorreo.getText().toString().equals("")||
            etTel.getText().toString().equals("")||etPersonal.getText().toString().equals("")){
            Toast.makeText(getContext(), "Campos Vacios", Toast.LENGTH_LONG).show();
            return false;
        }
        else {
            return true;
        }
    }
    private void mLimpiar(){
        etNomb.setText(null);
        etCorreo.setText(null);
        etTel.setText(null);
        etPersonal.setText(null);
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
}