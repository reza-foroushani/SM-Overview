package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button Bachelor;
    private Button Master;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//hallo
        setContentView(R.layout.activity_main);

Bachelor=(Button) findViewById(R.id.BACHELOR);
Master=(Button) findViewById(R.id.MASTER);
Bachelor.setOnClickListener(this);
Master.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view== Master){
            Intent MeinMeister =new Intent(this,MasterActivity.class);
        }
        if (view==Bachelor){

        }

    }
}