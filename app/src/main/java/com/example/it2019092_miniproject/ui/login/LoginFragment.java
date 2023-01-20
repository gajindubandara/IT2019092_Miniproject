package com.example.it2019092_miniproject.ui.login;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.it2019092_miniproject.PasswordHash.passwordHash;
import com.example.it2019092_miniproject.PreLoader;
import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.Temp;
import com.example.it2019092_miniproject.ui.home.HomeFragment;
import com.example.it2019092_miniproject.ui.register.RegisterFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;
    TextView txtReg;
    EditText txtNIC,txtPw;
    Button btnLog;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        txtReg = view.findViewById(R.id.txtLog);
        txtNIC =view.findViewById(R.id.txtLogNic);
        txtPw=view.findViewById(R.id.txtLogPw);
        btnLog =view.findViewById(R.id.btnLogin);

        final PreLoader preloader = new PreLoader(getActivity());

        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                preloader.startLoadingDialog();
                //Validation
                if (checkValid()){

                    final String enteredNIC = txtNIC.getText().toString();
                    final String enteredPW = passwordHash.getMd5(txtPw.getText().toString());;

                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");
                    Query checkUser = reference.orderByChild("userNIC").equalTo(enteredNIC);

                    checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            preloader.dismissDialog();
                            if(snapshot.exists()){
                                String passwordFromDB =snapshot.child(enteredNIC).child("password").getValue(String.class);
                                String userTypeFromDB =snapshot.child(enteredNIC).child("type").getValue(String.class);


                                if(passwordFromDB.equals(enteredPW)){
                                    Temp.setNIC(enteredNIC);

//                                    String userNameFromDB =snapshot.child(enteredNIC).child("name").getValue(String.class);
//                                    SharedPreference preference=new SharedPreference();
//                                    preference.SaveBool(view.getContext(),true,SharedPreference.REGISTER);
//                                    preference.SaveBool(view.getContext(),true,SharedPreference.LOGIN_STATUS);
//                                    preference.SaveString(view.getContext(),userTypeFromDB,SharedPreference.USER_TYPE);
//                                    preference.SaveString(view.getContext(),enteredNIC,SharedPreference.USER_NIC);
//                                    preference.SaveString(view.getContext(),userNameFromDB,SharedPreference.USER_NAME);


                                    //Move to home frag
                                    FragmentTransaction trans =getActivity().getSupportFragmentManager().beginTransaction();
                                    HomeFragment fragment = new HomeFragment();
                                    trans.replace(R.id.nav_host_fragment_content_main, fragment);
                                    trans.addToBackStack(null);
                                    trans.commit();
                                    Toast.makeText(getActivity().getApplicationContext(),"Login Successful!",Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(getActivity().getApplicationContext(),"Incorrect password",Toast.LENGTH_LONG).show();
                                }
                            }
                            else{

                                Toast.makeText(getActivity().getApplicationContext(),"Incorrect NIC Number",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    preloader.dismissDialog();
                }
            }
        });

        txtReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans =getActivity().getSupportFragmentManager().beginTransaction();
                RegisterFragment fragment = new RegisterFragment();
                trans.replace(R.id.nav_host_fragment_content_main, fragment);
                trans.addToBackStack(null);
                trans.commit();
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        // TODO: Use the ViewModel
    }

    private boolean checkValid() {
        if (txtNIC.getText().toString().equals("")) {
            Toast.makeText(getActivity().getApplicationContext(),"NIC number cannot be blank",Toast.LENGTH_LONG).show();
            return false;
        }
        if (txtPw.getText().toString().equals("")) {
            Toast.makeText(getActivity().getApplicationContext(),"Password cannot be blank",Toast.LENGTH_LONG).show();
            return false;
        }

        return true;

    }


}