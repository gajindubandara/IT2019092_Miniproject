package com.example.it2019092_miniproject.ui.booking;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it2019092_miniproject.MainActivity;
import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.model.Booking;
import com.example.it2019092_miniproject.ui.home.HomeFragment;
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

import java.util.List;

public class UserViewBookingsAdapter extends RecyclerView.Adapter<UserViewBookingsAdapter.ViewHolder> {
    FirebaseDatabase fdb;
    List<Booking> bookingList;
    private Context context;
    DatabaseReference referance;
    FirebaseDatabase rootNode;


    public UserViewBookingsAdapter(List<Booking> booking, FirebaseDatabase _db){

        bookingList = booking;
        fdb = _db;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View eventItems = inflater.inflate(R.layout.user_booking_item,parent,false);
        ViewHolder holder = new ViewHolder(eventItems);
        context = parent.getContext();
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Booking booking = bookingList.get(position);
        String packId =booking.getPackageId();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Package");
        Query getPackage = reference.orderByChild("packageID").equalTo(packId);


        getPackage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    holder.place.setText(snapshot.child(packId).child("place").getValue(String.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.status.setText(booking.getStatus());
        holder.total.setText("Rs."+booking.getTotal()+".00/-");
        holder.passengers.setText("Passengers: "+booking.getNop());
        holder.date.setText("Date: "+booking.getbDate());
        holder.del.setVisibility(View.GONE);


        if (booking.getStatus().equals("Request pending")){
            holder.del.setVisibility(View.VISIBLE);
            holder.dot.setBackground(AppCompatResources.getDrawable(context, R.drawable.ic_yellow_dot));
        }
        else if (booking.getStatus().equals("Accepted")){
            holder.dot.setBackground(AppCompatResources.getDrawable(context, R.drawable.ic_green_dot));
        }
        else if (booking.getStatus().equals("Declined")){
            holder.dot.setBackground(AppCompatResources.getDrawable(context, R.drawable.ic_red_dot));
        }




        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder =new AlertDialog.Builder(holder.del.getContext());
                builder.setMessage("Are you sure,You want to remove booking").setTitle("Confirm Delete").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        rootNode = FirebaseDatabase.getInstance();
                        referance = rootNode.getReference("Booking");
                        referance.child(booking.getID()).removeValue();

                        Toast.makeText((MainActivity)v.getContext(),"Booking Removed!",Toast.LENGTH_LONG).show();
                        FragmentTransaction trans =((MainActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                        UserViewBookingsFragment fragment = new UserViewBookingsFragment();
                        trans.replace(R.id.nav_host_fragment_content_main, fragment);
                        trans.addToBackStack(null);
                        trans.commit();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //if no action
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return bookingList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView passengers,status,date,total,place;
        ImageView dot;
        ImageButton del;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
//            name =itemView.findViewById(R.id.booking_name);
            place =itemView.findViewById(R.id.place_name);
            passengers =itemView.findViewById(R.id.booking_passengers);
            date =itemView.findViewById(R.id.booking_date);
            status =itemView.findViewById(R.id.booking_status);
            total =itemView.findViewById(R.id.booking_total);
            del=itemView.findViewById(R.id.btnDelBooking);

            dot = itemView.findViewById(R.id.status_dot);

        }
    }
}
