package com.example.it2019092_miniproject.ui.booking;

import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.it2019092_miniproject.PreLoader;
import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.model.Package;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllPackagesFragment extends Fragment {

    public static AllPackagesFragment newInstance() {
        return new AllPackagesFragment();
    }

    private AllPackagesViewModel mViewModel;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
    TextView noPacks;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.fragment_all_packages, container, false);

        final PreLoader preloader = new PreLoader(getActivity());
        preloader.startLoadingDialog();

       noPacks=view.findViewById(R.id.txtNoPackages);
       noPacks.setVisibility(View.GONE);

        //set recycle view
        RecyclerView recyclerView = view.findViewById(R.id.rcvAllPackages);
        List<Package> packageList = new ArrayList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("Package");

        rootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                preloader.dismissDialog();
                if (snapshot.exists()) {
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        Package pack=postSnapshot.getValue(Package.class);
                        packageList.add(pack);
                    }

                    AllBookingAdapter adapter= new AllBookingAdapter(packageList,fdb);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext(), RecyclerView.VERTICAL, false);
                    recyclerView.setLayoutManager(layoutManager);

//                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    recyclerView.setAdapter(adapter);
                }else{
                    noPacks.setVisibility(View.VISIBLE);
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
        mViewModel = new ViewModelProvider(this).get(AllPackagesViewModel.class);
        // TODO: Use the ViewModel
    }

}