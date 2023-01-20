package com.example.it2019092_miniproject.ui.tour_package;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.ui.home.HomeFragment;
import com.example.it2019092_miniproject.ui.model.Package;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class NewPackageFragment extends Fragment {

    private NewPackageViewModel mViewModel;
    CardView btnCreate;
    ImageView Cimg;
    EditText Date, Place, NoD, NoN, Des,Price;
    Bitmap pic;
    boolean CPicCheck= false;
    DatabaseReference referance;
    FirebaseDatabase rootNode;
    private Uri coverImgUri;
    String imageRefC =" ";
    private StorageTask uploadTask;
    StorageReference storageReference;
    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;


    public static NewPackageFragment newInstance() {
        return new NewPackageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view= inflater.inflate(R.layout.fragment_new_package, container, false);

        Cimg =view.findViewById(R.id.Cimg);
        Date =view.findViewById(R.id.Date);
        Price =view.findViewById(R.id.Price);
        Place =view.findViewById(R.id.Place);
        NoD =view.findViewById(R.id.NoD);
        NoN =view.findViewById(R.id.NoN);
        Des =view.findViewById(R.id.Des);

        //Date picker
        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                Date.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        //Image picker
        ActivityResultLauncher camLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();
                pic =(Bitmap) intent.getExtras().get("data");
                Cimg.setImageBitmap(pic);
                CPicCheck= true;


                Bitmap imgRoomBitmap =(Bitmap) intent.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                imgRoomBitmap.compress(Bitmap.CompressFormat.JPEG,100,bytes);
                String  path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(),imgRoomBitmap,"val",null);
                coverImgUri =Uri.parse(path);

            }
        });


        ActivityResultLauncher galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();
                coverImgUri = intent.getData();
                Cimg.setImageURI(coverImgUri);
                pic = ((BitmapDrawable)Cimg.getDrawable()).getBitmap();
                CPicCheck= true;
            }
        });

        Cimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =new AlertDialog.Builder(Cimg.getContext());
                builder.setMessage("Please select an option").setTitle("Image Selection").setPositiveButton("Use the camera", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        camLauncher.launch(intent);
                    }
                }).setNegativeButton("Select Form Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        galleryLauncher.launch(intent);
                    }
                });
                AlertDialog dialog= builder.create();
                dialog.show();
            }
        });


        //create post
        btnCreate=view.findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Validation
                if (checkValid()){
                    String date = Date.getText().toString();
                    String place = Place.getText().toString();
                    String non = NoN.getText().toString();
                    String nod = NoD.getText().toString();
                    String des = Des.getText().toString();
                    String price = Price.getText().toString();


                    //Sending data to the database
                    rootNode = FirebaseDatabase.getInstance();
                    referance = rootNode.getReference("Package");
                    String key= referance.push().getKey();

                    //creating object
                    Package pack=new Package(key,place,date,price,non,nod,des,imageRefC);

                    referance.child(key).setValue(pack);


                    //getting the key
                    rootNode = FirebaseDatabase.getInstance();
                    referance = rootNode.getReference("Package");
                    referance.limitToLast(1).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s)
                        {
                            if (dataSnapshot.exists())
                            {
                                String getKey = dataSnapshot.getKey();
                                // uploading image
                                try {
                                    storageReference = FirebaseStorage.getInstance().getReference();
                                    if (coverImgUri != null) {
                                        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                        progressDialog.setTitle("Creating Package...");
                                        progressDialog.show();
                                        progressDialog.setCancelable(false);
                                        StorageReference cRef = storageReference.child("images/" + getKey+"/CoverImg");
//                                        StorageReference brRef = storageReference.child("images/" + key+"/Bathroom");
                                        uploadTask = cRef.putFile(coverImgUri);
//                                        uploadTask = brRef.putFile(imgBathroomUri);
                                        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();

                                                Task<Uri> downloadUrlCoverImg = cRef.getDownloadUrl();

                                                downloadUrlCoverImg.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageRefC = uri.toString();
                                                        referance = rootNode.getReference("Package");
                                                        referance.child(key).child("coverImg").setValue(imageRefC);
                                                        progressDialog.dismiss();

                                                        Toast.makeText(getActivity().getApplicationContext(),"Package Created!",Toast.LENGTH_LONG).show();
                                                        FragmentTransaction trans =getActivity().getSupportFragmentManager().beginTransaction();
                                                        HomeFragment fragment = new HomeFragment();
                                                        trans.replace(R.id.nav_host_fragment_content_main, fragment);
                                                        trans.addToBackStack(null);
                                                        trans.detach(fragment);
                                                        trans.attach(fragment);
                                                        trans.commit();
                                                    }
                                                });
                                            }
                                        });
                                    }
                                }catch(Exception ex){
                                    throw ex;
                                }
                            }
                        }
                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) { }
                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @androidx.annotation.Nullable String previousChilddemo) { }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });

                }
            }
        });



       return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(NewPackageViewModel.class);
        // TODO: Use the ViewModel
    }

    private boolean checkValid() {
        if (Date.getText().toString().equals("")) {
            Toast.makeText(getActivity().getApplicationContext(),"Date cannot be blank",Toast.LENGTH_LONG).show();
            return false;
        }
        if (Place.getText().toString().equals("")) {
            Toast.makeText(getActivity().getApplicationContext(),"Place cannot be blank",Toast.LENGTH_LONG).show();
            return false;
        }
        if (NoN.getText().toString().equals("")) {
            Toast.makeText(getActivity().getApplicationContext(),"No of Nights cannot blank",Toast.LENGTH_LONG).show();
            return false;
        }
        if (NoD.getText().toString().equals("")) {
            Toast.makeText(getActivity().getApplicationContext(),"No of Days cannot blank",Toast.LENGTH_LONG).show();
            return false;
        }
        if (Des.getText().toString().equals("")) {
            Toast.makeText(getActivity().getApplicationContext(),"Description cannot blank",Toast.LENGTH_LONG).show();
            return false;
        }
        if (CPicCheck==false) {
            Toast.makeText(getActivity().getApplicationContext(),"Cover image cannot be blank",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;

    }

}