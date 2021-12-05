package com.isproject.ey.ui.mesas;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ey.R;
import com.isproject.ey.databinding.FragmentSlideshowBinding;
import com.isproject.ey.firebase.Evento;
import com.isproject.ey.firebase.Invitado;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private FragmentSlideshowBinding binding;
    //
    ListView lvMesa;
    Spinner spnMesa;
    Button btSearch;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Invitado inv;
    Evento ev;
    //List
    private List<Invitado> listaInv= new ArrayList<Invitado>();
    ArrayAdapter<Invitado> arrayAdapterItem;
    //
    String idEv;
    int pqt =1;
    int nm=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                new ViewModelProvider(this).get(SlideshowViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //
        idEv = getActivity().getIntent().getStringExtra("idEv");
        iniciarElementos(root);
        iniciarSpin(root);
        iniciarFB();
        //
        listarDatos();
        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarDatos();
                llenarLista(listaInv);
            }
        });
        //
        return root;
    }
    private void listarDatos()
    {
        try {
            databaseReference.child("Invitado").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    listaInv.clear();
                    nm = spnMesa.getSelectedItemPosition()+1;
                    for(DataSnapshot objtSnapshot: snapshot.getChildren()){
                        inv = objtSnapshot.getValue(Invitado.class);
                        if(inv.getIdEvnt().equals(idEv) && inv.getMesa() == nm)
                        {
                            listaInv.add(inv);
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
    public void llenarLista(List<Invitado> lista){
        try {
            arrayAdapterItem = new ArrayAdapter<Invitado>(
                    getActivity()
                    ,android.R.layout.simple_list_item_1, lista);
            lvMesa.setAdapter(arrayAdapterItem);
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }
    private void iniciarSpin(View root){
        ArrayAdapter<CharSequence> mesaAdapter;
        /*try {
            databaseReference.child("Evento").child(idEv).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    ev = snapshot.getValue(Evento.class);
                    pqt = Integer.parseInt(ev.getPaquete());
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {}
            });
        }catch (Exception e){
            Toast.makeText(getContext(), ""+e.getMessage(), Toast.LENGTH_LONG).show();
        }*/
        switch (pqt){
            case 1:
                mesaAdapter = ArrayAdapter.createFromResource(getContext(), R.array.pq1,android.R.layout.simple_spinner_item);
                spnMesa = root.findViewById(R.id.spn_mesas);
                spnMesa.setAdapter(mesaAdapter);
                break;
            case 2:
                mesaAdapter = ArrayAdapter.createFromResource(getContext(), R.array.pq2,android.R.layout.simple_spinner_item);
                spnMesa = root.findViewById(R.id.spn_mesas);
                spnMesa.setAdapter(mesaAdapter);
                break;
            case 3:
                mesaAdapter = ArrayAdapter.createFromResource(getContext(), R.array.pq3,android.R.layout.simple_spinner_item);
                spnMesa = root.findViewById(R.id.spn_mesas);
                spnMesa.setAdapter(mesaAdapter);
                break;
        }


    }
    private void iniciarElementos(View root){
        btSearch = root.findViewById(R.id.bt_search);
        lvMesa = root.findViewById(R.id.lv_mesa);
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
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}