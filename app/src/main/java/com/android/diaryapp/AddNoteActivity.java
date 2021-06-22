package com.android.diaryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.diaryapp.model.Date;
import com.android.diaryapp.model.PostMessage;
import com.android.diaryapp.model.Time;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddNoteActivity extends AppCompatActivity {
    private ImageView ivDate,ivTime,ivColor,ivDelete;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    private ImageView ivColor10,ivColor1,ivColor2,ivColor3,ivColor4,ivColor5,ivColor6,ivColor7,ivColor8,ivColor9;
    private LinearLayout background;
    private EditText edtTittle,edtContent;
    int backgroundColor=Color.parseColor("#FFFFFF");

    private DatePicker datePicker;
    private TimePicker timePicker;
    private ImageView ivCheckTime;
    private FloatingActionButton fabCheckDate;
    private DatabaseReference dbReference;
    private ImageView ivBack;
    FirebaseAuth auth;
    String key;
    boolean checkdeDelete;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        dialogBuilder=new AlertDialog.Builder(this);
        auth=FirebaseAuth.getInstance();
        background=findViewById(R.id.background);
        edtTittle=findViewById(R.id.tv_tittle);
        edtContent=findViewById(R.id.tv_content);
        key=  getIntent().getStringExtra("key");
        if(key!=null)
        {
            //edit
            //Toast.makeText(this,String.valueOf(getIntent().getIntExtra("bg",1)),Toast.LENGTH_SHORT).show();
            backgroundColor=getIntent().getIntExtra("bg",1);
            background.setBackgroundColor(backgroundColor);
            edtTittle.setText(getIntent().getStringExtra("tittle"));
            edtContent.setText( getIntent().getStringExtra("content"));

            View timePopupView=getLayoutInflater().inflate(R.layout.item_time,null);
            timePicker=timePopupView.findViewById(R.id.time);
            timePicker.setCurrentHour(getIntent().getIntExtra("hour",1));
            timePicker.setCurrentMinute(getIntent().getIntExtra("minute",1));

            View datePopupView=getLayoutInflater().inflate(R.layout.item_date,null);
            datePicker=datePopupView.findViewById(R.id.date);
            datePicker.updateDate(getIntent().getIntExtra("year",1),
                    getIntent().getIntExtra("month",1)-1,getIntent().getIntExtra("day",1));
        }
        else
        {
            //create
            background.setBackgroundColor(backgroundColor);
        }
        ivBack=findViewById(R.id.iv_back);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivDate=findViewById(R.id.iv_date);
        ivTime=findViewById(R.id.iv_time);
        ivColor=findViewById(R.id.iv_color);
        ivDelete=findViewById(R.id.iv_delete);
        ivColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createColorDialog();
            }
        });
        ivDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createDateDialog();
            }
        });
        ivTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTimeDialog();
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteDialog();
            }
        });

    }
    public void showDeleteDialog(){
        dialogBuilder.setMessage("Bạn có muốn xóa?");
        dialogBuilder.setCancelable(false);
        dialogBuilder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.setNegativeButton("Xóaaaaa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               //XÓa quay lại main activity
                dbReference=FirebaseDatabase.getInstance().getReference("Posts");
                dbReference.child(key).removeValue();
                checkdeDelete=true;
                dialogInterface.dismiss();
                onBackPressed();

            }
        });
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!checkdeDelete)
        {
            if(!edtTittle.getText().toString().isEmpty()&&!edtContent.getText().toString().isEmpty())
            {
                addNewPost(new PostMessage(auth.getCurrentUser().getUid(),edtTittle.getText().toString(),edtContent.getText().toString(),
                        new Date(datePicker.getDayOfMonth(),datePicker.getMonth()+1,datePicker.getYear()),
                        new Time(timePicker.getHour(),timePicker.getMinute()),backgroundColor));
            }
        }

    }
    public void addNewPost(PostMessage newPost)
    {
        dbReference=FirebaseDatabase.getInstance().getReference("Posts");
        if(key==null)
        {
            //add
            dbReference.child(dbReference.push().getKey()).setValue(newPost);
        }
        else
        {
            //edit
            dbReference.child(key).setValue(newPost);
        }
    }

    public void createTimeDialog()
    {
        View timePopupView=getLayoutInflater().inflate(R.layout.item_time,null);
        timePicker=timePopupView.findViewById(R.id.time);
        ivCheckTime=timePopupView.findViewById(R.id.iv_check_time);
        ivCheckTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),String.valueOf(timePicker.getHour())+":::"+String.valueOf(+timePicker.getMinute()),Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        dialogBuilder.setView(timePopupView);
        dialog=dialogBuilder.create();
        dialog.show();

    }
    public void createDateDialog()
    {
        View datePopupView=getLayoutInflater().inflate(R.layout.item_date,null);
        datePicker=datePopupView.findViewById(R.id.date);
        fabCheckDate=datePopupView.findViewById(R.id.fab_checkdate);
        fabCheckDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogBuilder.setView(datePopupView);
        dialog=dialogBuilder.create();
        dialog.show();

    }
    public void createColorDialog()
    {
        View colorPopupView=getLayoutInflater().inflate(R.layout.notes_color,null);
        ivColor10=colorPopupView.findViewById(R.id.iv_color10);
        ivColor1=colorPopupView.findViewById(R.id.iv_color1);
        ivColor2=colorPopupView.findViewById(R.id.iv_color2);
        ivColor3=colorPopupView.findViewById(R.id.iv_color3);
        ivColor4=colorPopupView.findViewById(R.id.iv_color4);
        ivColor5=colorPopupView.findViewById(R.id.iv_color5);
        ivColor6=colorPopupView.findViewById(R.id.iv_color6);
        ivColor7=colorPopupView.findViewById(R.id.iv_color7);
        ivColor8=colorPopupView.findViewById(R.id.iv_color8);
        ivColor9=colorPopupView.findViewById(R.id.iv_color9);

        dialogBuilder.setView(colorPopupView);
        dialog=dialogBuilder.create();
        dialog.show();
    }

    @SuppressLint("ResourceAsColor")
    public void tickColor(View view)
    {
        ivColor1.setImageResource(0);
        ivColor2.setImageResource(0);
        ivColor3.setImageResource(0);
        ivColor4.setImageResource(0);
        ivColor5.setImageResource(0);
        ivColor6.setImageResource(0);
        ivColor7.setImageResource(0);
        ivColor8.setImageResource(0);
        ivColor9.setImageResource(0);
        ivColor10.setImageResource(0);
        switch (view.getId())
        {
            case R.id.iv_color1:ivColor1.setImageResource(R.drawable.ic_baseline_done_24);
            background.setBackgroundColor(Color.parseColor("#FFBB86FC"));backgroundColor=Color.parseColor("#FFBB86FC");break;
            case R.id.iv_color2:ivColor2.setImageResource(R.drawable.ic_baseline_done_24);
            background.setBackgroundColor(Color.parseColor("#F1CECE"));backgroundColor=Color.parseColor("#F1CECE");break;
            case R.id.iv_color3:ivColor3.setImageResource(R.drawable.ic_baseline_done_24);
            background.setBackgroundColor(Color.parseColor("#B8F863"));backgroundColor=Color.parseColor("#B8F863");break;
            case R.id.iv_color4:ivColor4.setImageResource(R.drawable.ic_baseline_done_24);
            background.setBackgroundColor(Color.parseColor("#FF03DAC5"));backgroundColor=Color.parseColor("#FF03DAC5");break;
            case R.id.iv_color5:ivColor5.setImageResource(R.drawable.ic_baseline_done_24);
            background.setBackgroundColor(Color.parseColor("#E4E084"));backgroundColor=Color.parseColor("#E4E084");break;
            case R.id.iv_color6:ivColor6.setImageResource(R.drawable.ic_baseline_done_24);
            background.setBackgroundColor(Color.parseColor("#CAC2BE"));backgroundColor=Color.parseColor("#CAC2BE");break;
            case R.id.iv_color7:ivColor7.setImageResource(R.drawable.ic_baseline_done_24);
            background.setBackgroundColor(Color.parseColor("#E3AEEC"));backgroundColor=Color.parseColor("#E3AEEC");break;
            case R.id.iv_color8:ivColor8.setImageResource(R.drawable.ic_baseline_done_24);
            background.setBackgroundColor(Color.parseColor("#FFFFFF"));backgroundColor=Color.parseColor("#FFFFFF");break;
            case R.id.iv_color9:ivColor9.setImageResource(R.drawable.ic_baseline_done_24);
            background.setBackgroundColor(Color.parseColor("#EFD1C2"));backgroundColor=Color.parseColor("#EFD1C2");break;
            case R.id.iv_color10:ivColor10.setImageResource(R.drawable.ic_baseline_done_24);
            background.setBackgroundColor(Color.parseColor("#85EFE5"));backgroundColor=Color.parseColor("#85EFE5");break;
        }
    }
}