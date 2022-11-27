package com.saurabh.justcheckout.user.checkout;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.saurabh.justcheckout.R;
public class CardDetailsFragment extends Fragment {
    public CardDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_card_details, container, false);
        Button btnAddressClick = (Button) view.findViewById(R.id.save_card);
        btnAddressClick.setOnClickListener(view1 -> {
            CheckoutScreenFragment nextFrag= new CheckoutScreenFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.checkoutFrameLayout, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        });
        // Inflate the layout for this fragment
        return view;
    }
}