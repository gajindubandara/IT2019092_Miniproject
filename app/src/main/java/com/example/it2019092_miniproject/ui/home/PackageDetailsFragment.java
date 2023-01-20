package com.example.it2019092_miniproject.ui.home;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.Temp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PackageDetailsFragment extends Fragment {

    private PackageDetailsViewModel mViewModel;
    TextView place,price,des,date;
    ImageView coverImg;

    public static PackageDetailsFragment newInstance() {
        return new PackageDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_package_details, container, false);


        String packID = Temp.getPackageID();
        coverImg =view.findViewById(R.id.packImg);
        place =view.findViewById(R.id.packPlace);
        price =view.findViewById(R.id.packPrice);
        date =view.findViewById(R.id.packDate);
        des =view.findViewById(R.id.packDes);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Package");
        Query checkUser = reference.orderByChild("packageID").equalTo(packID);


        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                preloader.dismissDialog();
                if(snapshot.exists()){

                    String Url = snapshot.child(packID).child("coverImg").getValue(String.class);

//                    loc = snapshot.child(packID).child("location").getValue(String.class);
//                    status =Integer.valueOf(snapshot.child(packID).child("status").getValue(String.class));


                    Picasso.get().load(Url).placeholder(R.drawable.progress_animation).error(R.drawable.try_later).into(coverImg);

                    date.setText(snapshot.child(packID).child("date").getValue(String.class));
                    price.setText("Rs. "+snapshot.child(packID).child("price").getValue(String.class)+".00/-");
                    des.setText(snapshot.child(packID).child("des").getValue(String.class));
                    place.setText(snapshot.child(packID).child("place").getValue(String.class));
//                    No.setText(snapshot.child(jobID).child("noOfRooms").getValue(String.class));
//                    NoBR.setText(snapshot.child(jobID).child("noOfBathrooms").getValue(String.class));
//                    RFT.setText(snapshot.child(jobID).child("rFloorType").getValue(String.class));
//                    BrFT.setText(snapshot.child(jobID).child("bFloorType").getValue(String.class));
//                    viewUserID =snapshot.child(jobID).child("user").getValue(String.class);
//                    contractor =snapshot.child(jobID).child("contractor").getValue(String.class);

//                    if(status==1) {
//                        btnGet.setVisibility(view.VISIBLE);
//
//                    }else{
//                        btnViewCon.setVisibility(view.VISIBLE);
//                    }
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(),"no data",Toast.LENGTH_LONG).show();
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
        mViewModel = new ViewModelProvider(this).get(PackageDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

}