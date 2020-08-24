package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainBachelorActivity extends AppCompatActivity implements View.OnClickListener {

    Button master;
    Button bachelor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bachelor);

    bachelor = findViewById(R.id.BACHELOR_BUTTON);
    master = findViewById(R.id.MASTER_BUTTON);
    bachelor.setOnClickListener(this);
    master.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if(view.getId() == bachelor.getId())
        {
            Intent intentOfBachelor = new Intent(this,BachelorIntent.class);
            startActivity(intentOfBachelor);
        }//if

        if(view.getId() == master.getId())
        {
            Intent intentOfMaster  = new Intent(this,MasterActivity.class);
            startActivity(intentOfMaster);
        }

    }
}