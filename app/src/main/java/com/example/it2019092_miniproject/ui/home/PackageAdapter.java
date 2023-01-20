package com.example.it2019092_miniproject.ui.home;




import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
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

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {
    FirebaseDatabase fdb;
    List<Package> packageList;
    DatabaseReference referance;
    FirebaseDatabase rootNode;
    private Context context;


    public PackageAdapter(List<Package> packages, FirebaseDatabase _db){

        packageList = packages;
        fdb = _db;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View Items = inflater.inflate(R.layout.package_row_item,parent,false);
        ViewHolder holder = new ViewHolder(Items);
        context = parent.getContext();
        return holder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Package pack=packageList.get(position);

        holder.name.setText(pack.getPlace());
        holder.price.setText("Rs."+pack.getPrice()+".00/-");
        String url =pack.getCoverImg();

        if(url.equals("")){
            holder.coverImg.setImageResource(R.drawable.try_later);
        }else{
            Picasso.get().load(url).placeholder(R.drawable.progress_animation).resize(500, 500).
                    centerCrop().error(R.drawable.try_later).into(holder.coverImg);
        }

//        Picasso.with(context).load(url)into(imageView);
        holder.packDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Temp.setPackageID(pack.getPackageID());

                FragmentTransaction trans =((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
                PackageDetailsFragment fragment = new PackageDetailsFragment();
                trans.replace(R.id.nav_host_fragment_content_main, fragment);
                trans.addToBackStack(null);
                trans.detach(fragment);
                trans.attach(fragment);
                trans.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name,price;
        ImageView coverImg;
        CardView packDetails;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name =itemView.findViewById(R.id.place_name);
            coverImg =itemView.findViewById(R.id.place_image);
            price =itemView.findViewById(R.id.price);
            packDetails=itemView.findViewById(R.id.packDetails);

        }
    }
}