package com.isproject.ey.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.isproject.ey.EditeventoActivity;
import com.isproject.ey.HomeActivity;
import com.isproject.ey.R;
import com.isproject.ey.databinding.FragmentHomeBinding;
import com.isproject.ey.firebase.Evento;

import org.jetbrains.annotations.NotNull;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    //
    Button btEdit, btEvts;
    TextView tvNom, tvFech, tvUbi, tvHo, tvCB;
    //Fire
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth mAuth;
    Evento evento;
    //
    String idEv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        idEv = getActivity().getIntent().getStringExtra("idEv");
        iniciarFB();
        iniciarCom(root);
        //
        /**/try {
            databaseReference.child("Evento").child(idEv).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    evento = snapshot.getValue(Evento.class);

                    tvNom.setText(evento.getNombre().toString());
                    tvUbi.setText("Dirección: "+evento.getUbicacion().toString());
                    tvFech.setText("Fecha: "+evento.getFecha().toString());
                    tvHo.setText("Hora: "+evento.getHora().toString());
                }
                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {}
            });
        }catch (Exception e){
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return root;
    }
    private void iniciarCom(View root){
        btEdit = root.findViewById(R.id.bt_edit_fh);
        btEdit.setOnClickListener(this);
        btEvts = root.findViewById(R.id.bts_evts_fh);
        btEvts.setOnClickListener(this);
        //
        tvNom = root.findViewById(R.id.tv_frhome);
        tvFech = root.findViewById(R.id.tv_frhome_fech);
        tvUbi = root.findViewById(R.id.tv_frhome_ubi);
        tvHo = root.findViewById(R.id.tv_frhome_time);
        tvCB = root.findViewById(R.id.tv_frhome_cr);

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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_edit_fh:
                Intent intent1 = new Intent(getActivity(), EditeventoActivity.class);
                intent1.putExtra("idEv",idEv);
                startActivity(intent1);
                getActivity().finish();
                Toast.makeText(getContext(), "Edit!", Toast.LENGTH_LONG).show();
                break;
            case R.id.bts_evts_fh:
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                startActivity(intent);
                getActivity().finish();
                break;
        }
    }
}