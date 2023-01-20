package com.example.it2019092_miniproject.ui.tour_package;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.cardview.widget.CardView;
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
import com.example.it2019092_miniproject.Temp;
import com.example.it2019092_miniproject.ui.model.Package;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class EditPackageFragment extends Fragment {

    private EditPackageViewModel mViewModel;
    CardView btnUpdate;
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
    String imageURiDB,dbPrice,dbDate,dbPlace,dbNon,dbNod,dbDes,imgUrl;

    public static EditPackageFragment newInstance() {
        return new EditPackageFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_edit_package, container, false);

        String packID = Temp.getPackageID();
        Cimg =view.findViewById(R.id.Cimg);
        Date =view.findViewById(R.id.Date);
        Price =view.findViewById(R.id.Price);
        Place =view.findViewById(R.id.Place);
        NoD =view.findViewById(R.id.NoD);
        NoN =view.findViewById(R.id.NoN);
        Des =view.findViewById(R.id.Des);
        btnUpdate =view.findViewById(R.id.btnUpdate);

        //set data
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Package");
        Query getPack = reference.orderByChild("packageID").equalTo(packID);


        getPack.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (@NonNull DataSnapshot snapshot){
//                preloader.dismissDialog();
                if (snapshot.exists()) {
//                    Toast.makeText(getActivity().getApplicationContext(), packID, Toast.LENGTH_LONG).show();
                    dbPrice=snapshot.child(packID).child("price").getValue(String.class);
                    dbDate=snapshot.child(packID).child("date").getValue(String.class);
                    dbPlace=snapshot.child(packID).child("place").getValue(String.class);
                    dbNod=snapshot.child(packID).child("nod").getValue(String.class);
                    dbNon=snapshot.child(packID).child("non").getValue(String.class);
                    dbDes=snapshot.child(packID).child("des").getValue(String.class);
                    imageURiDB=snapshot.child(packID).child("coverImg").getValue(String.class);



//
                    Date.setText(snapshot.child(packID).child("date").getValue(String.class));
                    Price.setText(snapshot.child(packID).child("price").getValue(String.class));
                    Place.setText(snapshot.child(packID).child("place").getValue(String.class));
                    NoD.setText(snapshot.child(packID).child("nod").getValue(String.class));
                    NoN.setText(snapshot.child(packID).child("non").getValue(String.class));
                    Des.setText(snapshot.child(packID).child("des").getValue(String.class));
                    imgUrl = snapshot.child(packID).child("coverImg").getValue(String.class);
//                    Toast.makeText(getActivity().getApplicationContext(), imgUrl, Toast.LENGTH_LONG).show();


                    Picasso.get().load(imgUrl).placeholder(R.drawable.progress_animation).error(R.drawable.try_later).into(Cimg);






                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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


        //update data
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance();
                referance=rootNode.getReference("Package");

                //Validation
                if (checkValid()){
                    String newDate = Date.getText().toString();
                    String newPrice = Price.getText().toString();
                    String newPlace = Place.getText().toString();
                    String newNod  = NoD.getText().toString();
                    String newNon = NoN.getText().toString();
                    String newDes = Des.getText().toString();


//
//                    String[] splitRF = rF.split("[,]", 0);
//                    String roomFloor = splitRF[0];
//                    String brF = String.valueOf(bathroomF.getSelectedItem());
//                    String[] splitBRF = brF.split("[,]", 0);
//                    String bathroomFloor =splitBRF[0];
//                    String nobr = editNoOfBr.getText().toString();
//                    String user = Temp.getNIC();
//                    String status ="1";
//                    int RSqFt = Integer.valueOf(editRSqFt.getText().toString());
//                    int BrSqFt = Integer.valueOf(editBrSqFt.getText().toString());
//                    String rsqft =editRSqFt.getText().toString();
//                    String bsqft =editBrSqFt.getText().toString();


//                    if( editDate.getText().toString().equals(dateDb)&&editLoc.getText().toString().equals(locDb)&&
//                            editNoOfR.getText().toString().equals(NoOfRDb)&&editNoOfBr.getText().toString().equals(NoOfBrDb)&&
//                            editRSqFt.getText().toString().equals(RSqFtDb)&&editBrSqFt.getText().toString().equals(BrSqFtDb)&&
//                            rfSpinner.equals(roomFloor)&&bfSpinner.equals(bathroomFloor)&&RPicCheck==false&&BrPicCheck==false){
//
//
//                        Toast.makeText(getActivity().getApplicationContext(),"Nothing to update!",Toast.LENGTH_LONG).show();
//
//                    }
//                    else{

                    //Storing the job data
                    try{

                        //Sending data to the database
                        rootNode = FirebaseDatabase.getInstance();
                        referance = rootNode.getReference("Package");


                        //creating object
                        Package pack=new Package(//add variables..........................................................................);

                        referance.child(jobID).setValue(job);
                        Toast.makeText(getActivity().getApplicationContext(),"Post Updated!",Toast.LENGTH_LONG).show();
                    }catch(Exception ex)
                    {
                        throw ex;
                    }

                        //getting the key
                        rootNode = FirebaseDatabase.getInstance();
                        referance = rootNode.getReference("Job");
                        referance.limitToLast(1).addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s)
                            {
                                if (dataSnapshot.exists())
                                {
                                    // uploading image
                                    try {
                                        storageReference = FirebaseStorage.getInstance().getReference();
                                        if (imgRoomUri != null) {
                                            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                            progressDialog.setTitle("Uploading Images...");
                                            progressDialog.show();
                                            progressDialog.setCancelable(false);
                                            StorageReference rRef = storageReference.child("images/" + jobID+"/Room");
                                            uploadTask = rRef.putFile(imgRoomUri);
                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                                                    Task<Uri> downloadUrlR = rRef.getDownloadUrl();


                                                    downloadUrlR.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            imageRefR = uri.toString();
                                                            referance = rootNode.getReference("Job");
                                                            referance.child(jobID).child("imageR").setValue(imageRefR);
                                                            progressDialog.dismiss();
                                                        }
                                                    });
                                                }
                                            });
                                        }
                                        if (imgBathroomUri != null) {
                                            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
                                            progressDialog.setTitle("Uploading Images...");
                                            progressDialog.show();
                                            progressDialog.setCancelable(false);

                                            StorageReference brRef = storageReference.child("images/" + jobID+"/Bathroom");

                                            uploadTask = brRef.putFile(imgBathroomUri);
                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                    StorageMetadata snapshotMetadata = taskSnapshot.getMetadata();
                                                    Task<Uri> downloadUrlBR = brRef.getDownloadUrl();
                                                    downloadUrlBR.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            imageRefBr = uri.toString();
                                                            referance = rootNode.getReference("Job");
                                                            referance.child(jobID).child("imageBr").setValue(imageRefBr);
                                                            progressDialog.dismiss();
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

//                    }




                }

            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(EditPackageViewModel.class);
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
        return true;

    }
}