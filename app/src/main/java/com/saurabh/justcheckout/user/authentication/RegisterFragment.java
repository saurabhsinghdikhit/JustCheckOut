package com.saurabh.justcheckout.user.authentication;

import static android.content.Context.MODE_PRIVATE;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.saurabh.justcheckout.user.home.MainActivity;
import com.saurabh.justcheckout.R;
import com.saurabh.justcheckout.user.classes.User;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterFragment extends Fragment {
    String email = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";
    String mobile = "^(\\+\\d{1,2}\\s)?\\(?\\d{3}\\)?[\\s.-]?\\d{3}[\\s.-]?\\d{4}$";
    Pattern emailPattern = Pattern.compile(email);
    Pattern mobilePattern = Pattern.compile(mobile);
    LinearLayout progressBar;
    Button registerBtn;
    EditText registerName, registerEmail, registerMobile, registerPassword, registerConfirmPassword;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private Activity activity;
    public RegisterFragment() {
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
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        mAuth = FirebaseAuth.getInstance();
        initializeControls(view);
        registerBtn.setOnClickListener(view1 -> {
            validateInputs();
        });
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference("users");
        activity = getActivity();
        return view;
    }

    private void initializeControls(View view) {
        registerBtn = view.findViewById(R.id.registerBtn);
        registerName = view.findViewById(R.id.registxtname);
        registerEmail = view.findViewById(R.id.registxtemail);
        registerMobile = view.findViewById(R.id.registxtmobile);
        registerConfirmPassword = view.findViewById(R.id.registxtcpassword);
        registerPassword = view.findViewById(R.id.registxtpassword);
        progressBar = view.findViewById(R.id.registerprogressbar);
    }

    private void validateInputs() {
        if(TextUtils.isEmpty(registerName.getText().toString().trim())){
            registerName.setText(registerName.getText().toString().trim());
            registerName.setError("Name is required");
            return;
        }
        if(TextUtils.isEmpty(registerEmail.getText().toString().trim())){
            registerEmail.setText(registerEmail.getText().toString().trim());
            registerEmail.setError("Email is required");
            return;
        }
        if(!emailPattern.matcher(registerEmail.getText().toString().trim()).matches()){
            registerEmail.setText(registerEmail.getText().toString().trim());
            registerEmail.setError("Email is not valid");
            return;
        }
        if(TextUtils.isEmpty(registerMobile.getText().toString().trim())){
            registerMobile.setText(registerMobile.getText().toString().trim());
            registerMobile.setError("Mobile is required");
            return;
        }
        if(!mobilePattern.matcher(registerMobile.getText().toString().trim()).matches()){
            registerMobile.setText(registerMobile.getText().toString().trim());
            registerMobile.setError("Mobile is not valid");
            return;
        }
        if(TextUtils.isEmpty(registerPassword.getText().toString().trim())){
            registerPassword.setText(registerPassword.getText().toString().trim());
            registerPassword.setError("Password is required");
            return;
        }
        if(registerPassword.getText().toString().trim().length()<6){
            registerPassword.setText(registerPassword.getText().toString().trim());
            registerPassword.setError("Password should be least 6 characters");
            return;
        }
        if(TextUtils.isEmpty(registerConfirmPassword.getText().toString().trim())){
            registerConfirmPassword.setText(registerConfirmPassword.getText().toString().trim());
            registerConfirmPassword.setError("Confirm password is required");
            return;
        }
        if(!registerPassword.getText().toString().trim().equals(registerConfirmPassword.getText().toString().trim())){
            registerConfirmPassword.setText(registerConfirmPassword.getText().toString().trim());
            registerConfirmPassword.setError("Password is not matching");
            return;
        }
        // if every this is clear then call the register method
        registerUser();
    }
    void registerUser(){
        progressBar.setVisibility(View.VISIBLE);
        User user = new User(registerEmail.getText().toString().trim(),
                registerMobile.getText().toString().trim(),registerName.getText().toString().trim(),"user");
        mAuth.createUserWithEmailAndPassword(registerEmail.getText().toString().trim(), registerPassword.getText().toString().trim())
                .addOnCompleteListener(getActivity(),new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // authentication done
                            FirebaseUser userLogin = mAuth.getCurrentUser();
                            databaseReference.child(userLogin.getUid()).setValue(user);
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    Log.i("added","user added");
                                    progressBar.setVisibility(View.GONE);
                                    SharedPreferences sharedPreferences = activity.getSharedPreferences("userLogin", MODE_PRIVATE);
                                    SharedPreferences.Editor myUser = sharedPreferences.edit();
                                    myUser.putString("name", user.getName());
                                    myUser.putString("phone", user.getPhone());
                                    myUser.putString("email", user.getEmail());
                                    myUser.putString("userType", user.getUserType());
                                    myUser.apply();
                                    activity.startActivity(new Intent(activity,MainActivity.class));
                                    activity.finish();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressBar.setVisibility(View.GONE);
                                    // displaying a failure message on below line.
                                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // auth failed
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), Objects.requireNonNull(task.getException()).getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}