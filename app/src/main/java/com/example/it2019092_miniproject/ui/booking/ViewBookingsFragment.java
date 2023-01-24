package com.example.it2019092_miniproject.ui.booking;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it2019092_miniproject.MainActivity;
import com.example.it2019092_miniproject.PreLoader;
import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.Temp;
import com.example.it2019092_miniproject.model.Booking;
import com.example.it2019092_miniproject.ui.home.HomeFragment;
import com.example.it2019092_miniproject.ui.tour_package.EditPackageFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ViewBookingsFragment extends Fragment {

    private ViewUserBookingsViewModel mViewModel;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    TextView noBookings;
    CardView del,edit;
    Button delete;
    DatabaseReference referance;
    FirebaseDatabase rootNode;
    boolean check=false;

    public static ViewBookingsFragment newInstance() {
        return new ViewBookingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_view_bookings, container, false);

        final PreLoader preloader = new PreLoader(getActivity());
        preloader.startLoadingDialog();

        del=view.findViewById(R.id.btnDel);
        edit=view.findViewById(R.id.btnEdit);
        noBookings=view.findViewById(R.id.txtNoBookings);
        noBookings.setVisibility(View.GONE);
        String packId = Temp.getPackageID();

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction trans =((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
                EditPackageFragment fragment = new EditPackageFragment();
                trans.replace(R.id.nav_host_fragment_content_main, fragment);
                trans.addToBackStack(null);
                trans.detach(fragment);
                trans.attach(fragment);
                trans.commit();
            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Removing Package...");
                progressDialog.setCancelable(false);

                AlertDialog.Builder builder =new AlertDialog.Builder(del.getContext());
                builder.setMessage("Are you sure,You want to remove the package").setTitle("Confirm Delete").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        rootNode = FirebaseDatabase.getInstance();
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Booking");
                        Query getBookings = rootRef.orderByChild("packageId").equalTo(packId);
                        getBookings.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    if(check==false) {
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(del.getContext());
                                        builder2.setMessage("There are booking for this package!,Do you want to remove the package").setTitle("Caution!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                progressDialog.show();
                                                rootNode = FirebaseDatabase.getInstance();
                                                referance = rootNode.getReference("Package");
                                                referance.child(packId).removeValue();

                                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                                String Cimg = "images/" + packId + "/CoverImg";
                                                storageReference.child(Cimg).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog.dismiss();
                                                    }
                                                });

                                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                    Booking booking=postSnapshot.getValue(Booking.class);
                                                    if (booking.getPackageId().equals(packId)) {
                                                        rootRef.child(booking.getID()).removeValue();
                                                    }
                                                }

                                                Toast.makeText((MainActivity) v.getContext(), "Package Removed!", Toast.LENGTH_LONG).show();
                                                FragmentTransaction trans = ((MainActivity) v.getContext()).getSupportFragmentManager().beginTransaction();
                                                HomeFragment fragment = new HomeFragment();
                                                trans.replace(R.id.nav_host_fragment_content_main, fragment);
                                                trans.addToBackStack(null);
                                                trans.commit();

                                            }
                                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                            }
                                        });
                                        AlertDialog dialog2 = builder2.create();
                                        dialog2.show();
                                        check = true;
                                    }

                                }else{
                                    progressDialog.show();
                                    rootNode = FirebaseDatabase.getInstance();
                                    referance = rootNode.getReference("Package");
                                    referance.child(packId).removeValue();

                                    StorageReference storageReference= FirebaseStorage.getInstance().getReference();
                                    String Cimg = "images/" + packId+"/CoverImg";
                                    storageReference.child(Cimg).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressDialog.dismiss();
                                        }
                                    });

                                    Toast.makeText((MainActivity)v.getContext(),"Package Removed!",Toast.LENGTH_LONG).show();
                                    FragmentTransaction trans =((MainActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                                    HomeFragment fragment = new HomeFragment();
                                    trans.replace(R.id.nav_host_fragment_content_main, fragment);
                                    trans.addToBackStack(null);
                                    trans.commit();
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                            }
                        });
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //set recycle view
        RecyclerView recyclerView = view.findViewById(R.id.rcvUserBookings);
        List<Booking> bookingList = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Booking");
        Query getPackageBookings = rootRef.orderByChild("packageId").equalTo(packId);

       getPackageBookings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                preloader.dismissDialog();
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Booking booking=postSnapshot.getValue(Booking.class);
                        bookingList.add(booking);
                    }

                    ViewBookingsAdapter adapter= new ViewBookingsAdapter(bookingList,fdb);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }else{
                    noBookings.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ViewUserBookingsViewModel.class);
        // TODO: Use the ViewModel
    }

}