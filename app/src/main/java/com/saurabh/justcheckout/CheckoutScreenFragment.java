package com.saurabh.justcheckout;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CheckoutScreenFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CheckoutScreenFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CheckoutScreenFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CheckoutScreenFragment newInstance(String param1, String param2) {
        CheckoutScreenFragment fragment = new CheckoutScreenFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_checkout_screen, container, false);
        ImageView btnAddressClick = (ImageView) view.findViewById(R.id.btn_addressClick);
        ImageView btnCardScreen = view.findViewById(R.id.card_screen_btn);
        ImageView btnCartScreen = view.findViewById(R.id.cart_screen_btn);
        btnAddressClick.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AddressScreenFragment nextFrag= new AddressScreenFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.checkoutFrameLayout, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        btnCardScreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                CardDetailsFragment nextFrag= new CardDetailsFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.checkoutFrameLayout, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        btnCartScreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(),CartActivity.class));
            }
        });
        // Inflate the layout for this fragment
        return view;
    }
}