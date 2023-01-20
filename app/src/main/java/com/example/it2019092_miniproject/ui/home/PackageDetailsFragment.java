package com.example.it2019092_miniproject.ui.home;

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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it2019092_miniproject.MainActivity;
import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.Temp;
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
import com.squareup.picasso.Picasso;

public class PackageDetailsFragment extends Fragment {

    private PackageDetailsViewModel mViewModel;
    TextView place,price,des,date,non,nod;
    ImageView coverImg;
    CardView book,edit,del;
    DatabaseReference referance;
    FirebaseDatabase rootNode;

    public static PackageDetailsFragment newInstance() {
        return new PackageDetailsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_package_details, container, false);


        String packID = Temp.getPackageID();
        String userID = Temp.getNIC();
        coverImg =view.findViewById(R.id.packImg);
        place =view.findViewById(R.id.packPlace);
        price =view.findViewById(R.id.packPrice);
        date =view.findViewById(R.id.packDate);
        des =view.findViewById(R.id.packDes);
        book=view.findViewById(R.id.btnBook);
        edit=view.findViewById(R.id.btnEdit);
        non=view.findViewById(R.id.packNon);
        nod=view.findViewById(R.id.packNod);
        del=view.findViewById(R.id.btnDel);

        Toast.makeText(getActivity().getApplicationContext(),userID,Toast.LENGTH_LONG).show();
        if (userID.equals("0000")){
            del.setVisibility(View.VISIBLE);
            edit.setVisibility(View.VISIBLE);
        }else{
            del.setVisibility(View.GONE);
            edit.setVisibility(View.GONE);
        }



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
                    non.setText(snapshot.child(packID).child("non").getValue(String.class)+" Nights");
                    nod.setText(snapshot.child(packID).child("nod").getValue(String.class)+" Days");

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
                }
                else{
//                    Toast.makeText(getActivity().getApplicationContext(),"no data",Toast.LENGTH_LONG).show();
                    FragmentTransaction trans =((MainActivity)view.getContext()).getSupportFragmentManager().beginTransaction();
                    PackageDetailsFragment fragment = new PackageDetailsFragment();
                    trans.replace(R.id.nav_host_fragment_content_main, fragment);
                    trans.remove(fragment);
                    trans.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(del.getContext());
                progressDialog.setTitle("Removing Package...");
                progressDialog.setCancelable(false);

                AlertDialog.Builder builder =new AlertDialog.Builder(del.getContext());
                builder.setMessage("Are you sure,You want to remove the package").setTitle("Confirm Delete").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.show();
                        rootNode = FirebaseDatabase.getInstance();
                        referance = rootNode.getReference("Package");


                        referance.child(packID).removeValue();

                        StorageReference storageReference= FirebaseStorage.getInstance().getReference();
                        String Cimg = "images/" + packID+"/CoverImg";
                        storageReference.child(Cimg).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                progressDialog.dismiss();
                            }
                        });


                        Toast.makeText((MainActivity)v.getContext(),"Post Removed!",Toast.LENGTH_LONG).show();
                        FragmentTransaction trans =((MainActivity)v.getContext()).getSupportFragmentManager().beginTransaction();
                        HomeFragment fragment = new HomeFragment();
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


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PackageDetailsViewModel.class);
        // TODO: Use the ViewModel
    }

}