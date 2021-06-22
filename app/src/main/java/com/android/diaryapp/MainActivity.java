package com.android.diaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.diaryapp.model.PostMessage;
import com.android.diaryapp.viewmodel.PostsAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private DatabaseReference dbReference;

    private PostsAdapter postsAdapter;
    private ArrayList<PostMessage> posts;

    private RecyclerView rvNote;
    private FloatingActionButton fabCreate;
    private ArrayList<String> key;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        key=new ArrayList<>();
        fabCreate=findViewById(R.id.fab_create);
        FirebaseApp.initializeApp(this);
        auth=FirebaseAuth.getInstance();
        posts=new ArrayList<>();
        if(auth.getCurrentUser()!=null)
        {
            //already logined in
            dbReference=FirebaseDatabase.getInstance().getReference("Posts");
            //Toast.makeText(this,auth.getCurrentUser().getUid(),Toast.LENGTH_SHORT).show();

        }
        rvNote = findViewById(R.id.rv_notes);
        postsAdapter = new PostsAdapter(posts,MainActivity.this, key);
        rvNote.setLayoutManager(new GridLayoutManager(this,1));
        rvNote.setAdapter(postsAdapter);
        getAllPost();
        fabCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddNoteActivity.class));
            }
        });
    }

    public void signOut()
    {
        auth.signOut();
    }
    public void getAllPost()
    {
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                posts.clear();
                for(DataSnapshot item:snapshot.getChildren())
                {
                    PostMessage post=item.getValue(PostMessage.class);
                    if(post.getId().equals(auth.getCurrentUser().getUid()))
                    {
                        posts.add(post);
                        key.add(item.getKey());
                    }
                }
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}