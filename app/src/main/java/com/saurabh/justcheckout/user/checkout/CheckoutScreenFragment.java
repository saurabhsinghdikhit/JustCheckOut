package com.saurabh.justcheckout.user.checkout;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.saurabh.justcheckout.user.CartActivity;
import com.saurabh.justcheckout.R;

public class CheckoutScreenFragment extends Fragment {
    public CheckoutScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_screen, container, false);
        ImageView btnAddressClick = (ImageView) view.findViewById(R.id.btn_addressClick);
        ImageView btnCardScreen = view.findViewById(R.id.card_screen_btn);
        ImageView btnCartScreen = view.findViewById(R.id.cart_screen_btn);
        btnAddressClick.setOnClickListener(view1 -> {
            AddressScreenFragment nextFrag= new AddressScreenFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.checkoutFrameLayout, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        });
        btnCardScreen.setOnClickListener(view1 ->{
            CardDetailsFragment nextFrag= new CardDetailsFragment();
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.checkoutFrameLayout, nextFrag, "findThisFragment")
                    .addToBackStack(null)
                    .commit();
        });
        btnCartScreen.setOnClickListener(view1 -> {
            startActivity(new Intent(getActivity(), CartActivity.class));
        });
        // Inflate the layout for this fragment
        return view;
    }
}