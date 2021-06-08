package com.example.gesturepractice;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeScreen extends AppCompatActivity {
    String name;
    Button submit;
    EditText box;
    Toast toast;
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        box=(EditText)findViewById(R.id.user_name);
        submit=(Button)findViewById(R.id.btn_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name=box.getText().toString();
                if(name!=null && !TextUtils.isEmpty(box.getText()))
                {
                    Intent intent=new Intent(HomeScreen.this,SelectDropDown.class);
                    intent.putExtra("UserName",box.getText().toString());
                    startActivityForResult(intent,2);
                }
                else
                {
                    toast=Toast.makeText(getBaseContext(),"Please enter last name",Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                    toast.show();
                }
            }
        } );
    }


    @Override
    protected void onRestart()
    {
        super.onRestart();
    }
    @Override
    public void onBackPressed(){
        Intent a=new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}
