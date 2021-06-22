package com.android.diaryapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginFragment extends Fragment {
    private EditText edtEmail,edtPass;
    private Button btLogin,btRegister;
    private TextView tvResetPass;

    private FirebaseAuth auth;
    private DatabaseReference dbReference;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //FirebaseApp.initializeApp(getActivity());
        auth=FirebaseAuth.getInstance();
        tvResetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_resetPassFragment, new Bundle());
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_loginFragment_to_registerFragment, new Bundle());
            }
        });
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                   if(!edtEmail.getText().toString().isEmpty()&&!edtPass.getText().toString().isEmpty())
                   {
                       checkUserLogin(edtEmail.getText().toString().trim(),edtPass.getText().toString().trim());
                   }
                   else
                   {
                       Toast.makeText(getActivity(),"Vui lòng điền đủ thông tin",Toast.LENGTH_SHORT).show();
                   }

            }
        });
    }
    public void checkUserLogin(String email,String pass)
    {
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Intent intent =new Intent(getActivity(), MainActivity.class);
                    intent.putExtra("id",auth.getCurrentUser().getUid());
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(),"Sai thông tin đăng nhập",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_login, container, false);
        edtEmail=view.findViewById(R.id.edt_email);
        edtPass=view.findViewById(R.id.edt_password);
        btLogin=view.findViewById(R.id.bt_login);
        btRegister=view.findViewById(R.id.bt_register);
        tvResetPass=view.findViewById(R.id.tv_resetpass);
        return view;
    }
}