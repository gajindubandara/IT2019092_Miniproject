package com.example.it2019092_miniproject.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.it2019092_miniproject.MainActivity;
import com.example.it2019092_miniproject.PreLoader;
import com.example.it2019092_miniproject.R;
import com.example.it2019092_miniproject.SharedPreference;
import com.example.it2019092_miniproject.Temp;
import com.example.it2019092_miniproject.databinding.FragmentHomeBinding;
import com.example.it2019092_miniproject.model.Package;
import com.example.it2019092_miniproject.model.TopPlaces;
import com.example.it2019092_miniproject.ui.booking.AllPackagesFragment;
import com.example.it2019092_miniproject.ui.login.LoginFragment;
import com.example.it2019092_miniproject.ui.tour_package.EditPackageFragment;
import com.example.it2019092_miniproject.ui.tour_package.NewPackageFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    FirebaseDatabase fdb = FirebaseDatabase.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        final PreLoader preloader = new PreLoader(getActivity());
        preloader.startLoadingDialog();

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        LinearLayout adminLayout;
        TextView topPlaces;
        RecyclerView topPlaceRCV;
        Button booking,newPackage;
        ImageButton logout;
        adminLayout=root.findViewById(R.id.adminLayout);
        topPlaces=root.findViewById(R.id.topPlaces);
        topPlaceRCV=root.findViewById(R.id.top_places_recycler);
        booking=root.findViewById(R.id.adminBooking);
        newPackage=root.findViewById(R.id.adminPackage);
        logout=root.findViewById(R.id.btnLogout);
        adminLayout.setVisibility(View.GONE);
        logout.setVisibility(View.GONE);


        if(Temp.getNIC()!=null){
            if(Temp.getNIC().equals("0000")){
                adminLayout.setVisibility(View.VISIBLE);
                topPlaces.setVisibility(View.GONE);
                topPlaceRCV.setVisibility(View.GONE);
            }
            logout.setVisibility(View.VISIBLE);
        }

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans =(getActivity().getSupportFragmentManager().beginTransaction());
                AllPackagesFragment fragment = new AllPackagesFragment();
                trans.replace(R.id.nav_host_fragment_content_main, fragment);
                trans.addToBackStack(null);
                trans.detach(fragment);
                trans.attach(fragment);
                trans.commit();
            }
        });

        newPackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction trans =(getActivity().getSupportFragmentManager().beginTransaction());
                NewPackageFragment fragment = new NewPackageFragment();
                trans.replace(R.id.nav_host_fragment_content_main, fragment);
                trans.addToBackStack(null);
                trans.detach(fragment);
                trans.attach(fragment);
                trans.commit();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Temp.setNIC(null);
                SharedPreference preference= new SharedPreference();
                preference.SaveBool(getContext(),false, SharedPreference.LOGIN_STATUS);
                preference.SaveString(getContext(),null,SharedPreference.USER_NIC);

                FragmentTransaction trans =(getActivity().getSupportFragmentManager().beginTransaction());
                LoginFragment fragment = new LoginFragment();
                trans.replace(R.id.nav_host_fragment_content_main, fragment);
                trans.addToBackStack(null);
                trans.detach(fragment);
                trans.attach(fragment);
                trans.commit();
            }
        });

        RecyclerView recyclerView = root.findViewById(R.id.package_recycler);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
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

                    PackageAdapter adapter= new PackageAdapter(packageList,fdb);

                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(root.getContext(), RecyclerView.HORIZONTAL, false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter);
                }else{
//                    nodata.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });


        List<TopPlaces> placesList = new ArrayList<>();
        RecyclerView recyclerViewTopPlaces = root.findViewById(R.id.top_places_recycler);
        TopPlacesAdapter adapterPlaces= new TopPlacesAdapter(root.getContext(),placesList);
        RecyclerView.LayoutManager layoutManagerTopPlaces = new LinearLayoutManager(root.getContext(), RecyclerView.VERTICAL, false);
        recyclerViewTopPlaces.setLayoutManager(layoutManagerTopPlaces);

        placesList.add(new TopPlaces("Brugam Bay Beach","Eastern Province",R.drawable.tp_arugam));
        placesList.add(new TopPlaces("Bambarakanda Falls","Uva Province",R.drawable.tp_bfalls));
        placesList.add(new TopPlaces("Sri Dalada Maligawa","Central Province",R.drawable.tp_maligawa));
        placesList.add(new TopPlaces("Royal Botanical Garden -Peradeniya","Central Province",R.drawable.tp_garden));
        placesList.add(new TopPlaces("Sri Pada / Adam's Peak","Sabaragamuwa province",R.drawable.tp_adam));

        recyclerViewTopPlaces.setAdapter(adapterPlaces);

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}