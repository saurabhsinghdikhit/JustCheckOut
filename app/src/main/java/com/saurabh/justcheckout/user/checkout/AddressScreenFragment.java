package com.saurabh.justcheckout.user.checkout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.saurabh.justcheckout.R;

public class AddressScreenFragment extends Fragment {

    public AddressScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_address_screen, container, false);
        Button btnAddressClick = (Button) view.findViewById(R.id.save_address);
        btnAddressClick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CheckoutScreenFragment nextFrag= new CheckoutScreenFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.checkoutFrameLayout, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}