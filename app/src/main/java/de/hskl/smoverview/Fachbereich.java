package de.hskl.smoverview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Fachbereich extends AppCompatActivity implements View.OnClickListener{
    private Button Bachelor;
    private Button Master;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//hallo
        setContentView(R.layout.activity_fachbereich);
//view binding
        Bachelor=(Button) findViewById(R.id.BACHELOR);
        Master=(Button) findViewById(R.id.MASTER);
        //buttons den onclicklistener mit rintigem aktueleobjekt context zuweisen
        Bachelor.setOnClickListener(this);
        Master.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        if(view== Master){
            //neues objekt von typ intent  mit context und klassen name
            Intent MeinMeister =new Intent(this,MasterActivity.class);
            // neue Aktivity wird gestartet
            startActivity(MeinMeister);
        }
        if (view==Bachelor){

        }

    }
}
