package com.example.gesturepractice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class SelectDropDown extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner gesture;
    private static String user_name;
    Button change_user;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_drop_down);

        change_user=(Button)findViewById(R.id.btn_change);
        user_name=getIntent().getStringExtra("UserName");
        gesture=(Spinner)findViewById(R.id.select_gesture);

        ArrayAdapter<CharSequence> options=ArrayAdapter.createFromResource(this,R.array.gesture_options,android.R.layout.simple_spinner_item);
        options.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gesture.setAdapter(options);
        gesture.setOnItemSelectedListener(this);

        change_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent=new Intent(SelectDropDown.this,HomeScreen.class);
                startActivityForResult(intent,2);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent,View view,int position,long id)
    {
        if(position>0)
        {
            String gesture_option=parent.getItemAtPosition(position).toString();
            String gesture_url=getResources().getStringArray(R.array.gesture_urls)[position-1].toString();
            Intent intent=new Intent(SelectDropDown.this,DisplayVideo.class);
            intent.putExtra("Gesture",gesture_option);
            intent.putExtra("Url",gesture_url.toString());
            intent.putExtra("UserName",user_name.toString());
            startActivityForResult(intent,2);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent)
    {

    }

    @Override
    protected void onRestart()
    {
        super.onRestart();
        gesture.setSelection(0);
    }

    @Override
    public void onBackPressed()
    {
        Intent intent=new Intent(SelectDropDown.this,HomeScreen.class);
        startActivityForResult(intent,2);
    }
}
