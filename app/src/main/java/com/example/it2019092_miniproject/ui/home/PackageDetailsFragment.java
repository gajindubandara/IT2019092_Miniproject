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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it2019092_miniproject.MainActivity;
import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.Temp;
import com.example.it2019092_miniproject.model.Booking;
import com.example.it2019092_miniproject.ui.booking.UserViewBookingsAdapter;
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
    EditText nop;
    String packagePrice;
    ImageButton less,more;
    boolean check=false;


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
        nop=view.findViewById(R.id.nop);
        less=view.findViewById(R.id.btnLess);
        more=view.findViewById(R.id.btnMore);
        nop.setText("1");
        del.setVisibility(View.GONE);
        edit.setVisibility(View.GONE);


        if(Temp.getNIC()!=null){
            if (userID.equals("0000")){
                del.setVisibility(View.VISIBLE);
                edit.setVisibility(View.VISIBLE);
            }else{
                del.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
            }
        }

        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int noOfPassengers = Integer.valueOf(nop.getText().toString());
                nop.setText(String.valueOf(noOfPassengers+1));
            }
        });

        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int noOfPassengers = Integer.valueOf(nop.getText().toString());
                if(noOfPassengers-1<0){
                    nop.setText("0");
                }else{
                    nop.setText(String.valueOf(noOfPassengers-1));
                }
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Package");
        Query getPackage = reference.orderByChild("packageID").equalTo(packID);


        getPackage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                preloader.dismissDialog();
                if(snapshot.exists()){

                    String Url = snapshot.child(packID).child("coverImg").getValue(String.class);

                    if(Url.equals("")){
                        coverImg.setImageResource(R.drawable.try_later);
                    }else{
                        Picasso.get().load(Url).placeholder(R.drawable.progress_animation).error(R.drawable.try_later).into(coverImg);
                    }

                    date.setText(snapshot.child(packID).child("date").getValue(String.class));
                    price.setText("Rs. "+snapshot.child(packID).child("price").getValue(String.class)+".00/-");
                    packagePrice=snapshot.child(packID).child("price").getValue(String.class);
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
                AlertDialog.Builder builder =new AlertDialog.Builder(del.getContext());
                builder.setMessage("Are you sure,You want to remove the package").setTitle("Confirm Delete").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        rootNode = FirebaseDatabase.getInstance();
                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Booking");
                        Query getBookings = rootRef.orderByChild("packageId").equalTo(packID);
                        getBookings.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.exists()) {
                                    if(check==false) {
                                        AlertDialog.Builder builder2 = new AlertDialog.Builder(del.getContext());
                                        builder2.setMessage("There are booking for this package!,Do you want to remove the package").setTitle("Caution!").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                rootNode = FirebaseDatabase.getInstance();
                                                referance = rootNode.getReference("Package");
                                                referance.child(packID).removeValue();

                                                StorageReference storageReference = FirebaseStorage.getInstance().getReference();
                                                String Cimg = "images/" + packID + "/CoverImg";
                                                storageReference.child(Cimg).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                    }
                                                });

                                                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                                    Booking booking=postSnapshot.getValue(Booking.class);
                                                    if (booking.getPackageId().equals(packID)) {
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
                                    rootNode = FirebaseDatabase.getInstance();
                                    referance = rootNode.getReference("Package");
                                    referance.child(packID).removeValue();

                                    StorageReference storageReference= FirebaseStorage.getInstance().getReference();
                                    String Cimg = "images/" + packID+"/CoverImg";
                                    storageReference.child(Cimg).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
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


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Temp.getNIC() == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "Please Login First", Toast.LENGTH_LONG).show();
                } else{
                    try {
                        int value = Integer.valueOf(nop.getText().toString());
                        if (value > 0) {
                            long millis = System.currentTimeMillis();
                            java.sql.Date date = new java.sql.Date(millis);
                            String dateString = String.valueOf(date);
                            int total = Integer.valueOf(packagePrice) * value;
                            String stringTotal = String.valueOf(total);

                            //Sending data to the database
                            rootNode = FirebaseDatabase.getInstance();
                            referance = rootNode.getReference("Booking");
                            String bookingKey = referance.push().getKey();
                            Booking booking = new Booking(bookingKey, packID, dateString, userID, nop.getText().toString(), stringTotal, "Request pending");
                            referance.child(bookingKey).setValue(booking);

                            Toast.makeText(getActivity().getApplicationContext(), "Booking Created!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Select a valid number", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(getActivity().getApplicationContext(), "Select a valid number", Toast.LENGTH_LONG).show();
                    }
            }



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