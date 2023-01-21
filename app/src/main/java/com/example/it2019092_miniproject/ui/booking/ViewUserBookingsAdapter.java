package com.example.it2019092_miniproject.ui.booking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it2019092_miniproject.MainActivity;
import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.model.Booking;
import com.example.it2019092_miniproject.ui.home.PackageDetailsFragment;
import com.example.it2019092_miniproject.ui.tour_package.EditPackageFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ViewUserBookingsAdapter extends RecyclerView.Adapter<ViewUserBookingsAdapter.ViewHolder> {
    FirebaseDatabase fdb;
    List<Booking> bookingList;
    private Context context;
    DatabaseReference referance;
    FirebaseDatabase rootNode;


    public ViewUserBookingsAdapter(List<Booking> booking, FirebaseDatabase _db){

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
//                preloader.dismissDialog();
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
        holder.passengers.setText(booking.getNop());


//        if(url.equals("")){
//            holder.imgPlace.setImageResource(R.drawable.try_later);
//        }else{
//            Picasso.get().load(url).placeholder(R.drawable.progress_animation).resize(500, 500).
//                    centerCrop().error(R.drawable.try_later).into(holder.imgPlace);
//        }


//        holder.seeMore.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Temp.setPackageID(pack.getPackageID());
//                ViewUserBookingsFragment fragment =new ViewUserBookingsFragment();
//                FragmentTransaction ft=((MainActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
//                ft.replace(R.id.nav_host_fragment_content_main,fragment);
//                ft.addToBackStack(null);
//                ft.commit();
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, no, passengers,status;
        Button accept,decline;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.booking_name);
            no =itemView.findViewById(R.id.booking_no);
            passengers =itemView.findViewById(R.id.booking_passengers);
            status =itemView.findViewById(R.id.booking_status);
            accept = itemView.findViewById(R.id.btnAccpet);
            decline = itemView.findViewById(R.id.btnDecline);

        }
    }
}