package com.saurabh.justcheckout.user.authentication;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saurabh.justcheckout.admin.CreateProductActivity;
import com.saurabh.justcheckout.admin.ProductListActivity;
import com.saurabh.justcheckout.user.home.MainActivity;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.user.classes.User;

import java.util.regex.Pattern;

public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private Activity activity;
    LinearLayout progressBar;
    Button loginButton;
    TextView txtCreateAccount;
    EditText loginEmail,loginPassword;
    String email = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    Pattern emailPattern = Pattern.compile(email);
    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference("users");
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        progressBar = view.findViewById(R.id.loginprogressbar);
        loginButton = view.findViewById(R.id.loginbtn);
        loginEmail = view.findViewById(R.id.logintxtemail);
        loginPassword = view.findViewById(R.id.logintxtpassword);
        txtCreateAccount = view.findViewById(R.id.txtCreateAccount);
        if(currentUser!=null){
            activity.startActivity(new Intent(activity, MainActivity.class));
            activity.finish();
        }else{
            progressBar.setVisibility(View.GONE);
        }
        loginButton.setOnClickListener(view1 -> {
            validateInputs();
        });
        txtCreateAccount.setOnClickListener(click->{
            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right,R.anim.slide_out_left)
                    .replace(R.id.loginFrame,new RegisterFragment(), "findThisFragment")
                    .commit();
        });
        return  view;
    }
    private void validateInputs() {
        if(TextUtils.isEmpty(loginEmail.getText().toString().trim())){
            loginEmail.setText(loginEmail.getText().toString().trim());
            loginEmail.setError("Email is required");
            return;
        }
        if(!emailPattern.matcher(loginEmail.getText().toString().trim()).matches()){
            loginEmail.setText(loginEmail.getText().toString().trim());
            loginEmail.setError("Email is not valid");
            return;
        }
        if(TextUtils.isEmpty(loginPassword.getText().toString().trim())){
            loginPassword.setText(loginPassword.getText().toString().trim());
            loginPassword.setError("Password is required");
            return;
        }
        if(loginPassword.getText().toString().trim().length()<6){
            loginPassword.setError("Password should be least 6 characters");
            loginPassword.setText(loginPassword.getText().toString().trim());
            return;
        }
        // if every this is clear then call the login method
        loginUser();
    }

    void loginUser(){
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(loginEmail.getText().toString().trim(), loginPassword.getText().toString().trim())
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        // authentication successfully
                        String uid = mAuth.getCurrentUser().getUid();
                        databaseReference.child(uid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                User user = dataSnapshot.getValue(User.class);
                                SharedPreferences sharedPreferences = activity.getSharedPreferences("userLogin", MODE_PRIVATE);
                                SharedPreferences.Editor myUser = sharedPreferences.edit();
                                myUser.putString("name", user.getName());
                                myUser.putString("phone", user.getPhone());
                                myUser.putString("email", user.getEmail());
                                myUser.putString("userType", user.getUserType());
                                myUser.apply();
                                if(user.getUserType().equals("admin"))
                                    activity.startActivity(new Intent(activity, ProductListActivity.class));
                                else
                                    activity.startActivity(new Intent(activity,MainActivity.class));
                                activity.finish();
                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                progressBar.setVisibility(View.GONE);
                                // displaying a failure message on below line.
                                Toast.makeText(activity, error.getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        // authentication failed
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), task.getException().getMessage().toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}