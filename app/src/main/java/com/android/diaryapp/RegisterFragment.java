package com.android.diaryapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {
    private ImageView ivBack;
    private Button btRegister;
    private EditText edtEmail,edtPass,edtConfirm;

    private FirebaseAuth auth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth=FirebaseAuth.getInstance();
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requireActivity().onBackPressed();
            }
        });
        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.createUserWithEmailAndPassword(edtEmail.getText().toString(),edtPass.getText().toString() ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getActivity(),"Đăng ký thành công",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            //dang ky that bai
                            Toast.makeText(getActivity(),"Đăng ký thất bại",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_register, container, false);
        ivBack=view.findViewById(R.id.iv_back);
        btRegister=view.findViewById(R.id.bt_register);
        edtEmail=view.findViewById(R.id.edt_email);
        edtPass=view.findViewById(R.id.edt_pass);
        edtConfirm=view.findViewById(R.id.edt_confirmpass);

        return view;
    }
}