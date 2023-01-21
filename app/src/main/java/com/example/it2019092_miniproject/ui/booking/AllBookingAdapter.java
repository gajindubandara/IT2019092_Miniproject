package com.example.it2019092_miniproject.ui.booking;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it2019092_miniproject.MainActivity;
import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.Temp;
import com.example.it2019092_miniproject.model.Package;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AllBookingAdapter extends RecyclerView.Adapter<AllBookingAdapter.ViewHolder> {
    FirebaseDatabase fdb;
    List<Package> packageList;
    private Context context;



    public AllBookingAdapter(List<Package> packages, FirebaseDatabase _db){

        packageList = packages;
        fdb = _db;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View eventItems = inflater.inflate(R.layout.all_booking_item,parent,false);
        ViewHolder holder = new ViewHolder(eventItems);
        context = parent.getContext();
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Package pack = packageList.get(position);
        holder.txtDate.setText("Departure date :"+pack.getDate());
        holder.txtPrice.setText("Rs. "+pack.getPrice()+".00/-");
        holder.txtPlace.setText(pack.getPlace());
        String url =pack.getCoverImg();

        if(url.equals("")){
            holder.imgPlace.setImageResource(R.drawable.try_later);
        }else{
            Picasso.get().load(url).placeholder(R.drawable.progress_animation).resize(500, 500).
                    centerCrop().error(R.drawable.try_later).into(holder.imgPlace);
        }


        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Temp.setPackageID(pack.getPackageID());
                ViewUserBookingsFragment fragment =new ViewUserBookingsFragment();
                FragmentTransaction ft=((MainActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment_content_main,fragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtDate, txtPrice, txtPlace;
        ImageView imgPlace,seeMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate =itemView.findViewById(R.id.place_date);
            txtPrice =itemView.findViewById(R.id.place_price);
            txtPlace =itemView.findViewById(R.id.place_name);
            imgPlace =itemView.findViewById(R.id.place_image);
            seeMore = itemView.findViewById(R.id.place_seeMore);

        }
    }
}

