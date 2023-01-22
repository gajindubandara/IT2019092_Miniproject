package com.example.it2019092_miniproject.ui.booking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it2019092_miniproject.MainActivity;
import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.model.Booking;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;


public class ViewBookingsAdapter extends RecyclerView.Adapter<ViewBookingsAdapter.ViewHolder> {
    FirebaseDatabase fdb;
    List<Booking> bookingList;
    private Context context;
    DatabaseReference referance;
    FirebaseDatabase rootNode;


    public ViewBookingsAdapter(List<Booking> booking, FirebaseDatabase _db){

        bookingList = booking;
        fdb = _db;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View eventItems = inflater.inflate(R.layout.booking_item,parent,false);
        ViewHolder holder = new ViewHolder(eventItems);
        context = parent.getContext();
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        String userID =booking.getUserId();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
        Query getPackage = reference.orderByChild("userNIC").equalTo(userID);


        getPackage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.name.setText(snapshot.child(userID).child("name").getValue(String.class));
                    holder.no.setText(snapshot.child(userID).child("number").getValue(String.class));

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.status.setText(booking.getStatus());

        if (booking.getStatus().equals("Request pending")){
            holder.accept.setVisibility(View.VISIBLE);
            holder.decline.setVisibility(View.VISIBLE);
            holder.dot.setBackground(AppCompatResources.getDrawable(context, R.drawable.ic_yellow_dot));
        }
        else if (booking.getStatus().equals("Accepted")){
            holder.accept.setVisibility(View.GONE);
            holder.decline.setVisibility(View.GONE);
            holder.dot.setBackground(AppCompatResources.getDrawable(context, R.drawable.ic_green_dot));
        }
        else if (booking.getStatus().equals("Declined")){
            holder.accept.setVisibility(View.GONE);
            holder.decline.setVisibility(View.GONE);
            holder.dot.setBackground(AppCompatResources.getDrawable(context, R.drawable.ic_red_dot));
        }

        holder.passengers.setText("Passengers: "+booking.getNop());
        holder.date.setText("Date: "+booking.getbDate());


        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sending data to the database
                rootNode = FirebaseDatabase.getInstance();
                referance = rootNode.getReference("Booking");
                referance.child(booking.getID()).child("status").setValue("Accepted");

                FragmentTransaction trans =((MainActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                ViewBookingsFragment fragment = new ViewBookingsFragment();
                trans.replace(R.id.nav_host_fragment_content_main, fragment);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Sending data to the database
                rootNode = FirebaseDatabase.getInstance();
                referance = rootNode.getReference("Booking");
                referance.child(booking.getID()).child("status").setValue("Declined");

                FragmentTransaction trans =((MainActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                ViewBookingsFragment fragment = new ViewBookingsFragment();
                trans.replace(R.id.nav_host_fragment_content_main, fragment);
                trans.addToBackStack(null);
                trans.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, no, passengers,status,date;
        Button accept,decline;
        ImageView dot;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.booking_name);
            no =itemView.findViewById(R.id.booking_no);
            passengers =itemView.findViewById(R.id.booking_passengers);
            date =itemView.findViewById(R.id.booking_date);
            status =itemView.findViewById(R.id.booking_status);
            accept = itemView.findViewById(R.id.btnAccpet);
            decline = itemView.findViewById(R.id.btnDecline);
            dot = itemView.findViewById(R.id.status_dot);

        }
    }
}