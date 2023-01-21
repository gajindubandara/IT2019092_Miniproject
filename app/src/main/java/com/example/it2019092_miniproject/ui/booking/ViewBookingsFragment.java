package com.example.it2019092_miniproject.ui.booking;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.Temp;
import com.example.it2019092_miniproject.model.Booking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewBookingsFragment extends Fragment {

    private ViewUserBookingsViewModel mViewModel;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    TextView noBookings;

    public static ViewBookingsFragment newInstance() {
        return new ViewBookingsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_view_bookings, container, false);

        noBookings=view.findViewById(R.id.txtNoBookings);
        noBookings.setVisibility(View.GONE);
        String packId = Temp.getPackageID();


        //set recycle view
        RecyclerView recyclerView = view.findViewById(R.id.rcvUserBookings);
        List<Booking> bookingList = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Booking");
        Query getPackageBookings = rootRef.orderByChild("packageId").equalTo(packId);

       getPackageBookings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                preloader.dismissDialog();
                if (snapshot.exists()) {
//                    Toast.makeText(getContext(),"found",Toast.LENGTH_LONG).show();
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Booking booking=postSnapshot.getValue(Booking.class);
                        bookingList.add(booking);
                    }
//
                    ViewBookingsAdapter adapter= new ViewBookingsAdapter(bookingList,fdb);
//
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);

//                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
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