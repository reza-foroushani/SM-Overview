package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainBachelorActivity extends AppCompatActivity {

    Button master;
    Button bachelor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_bachelor);

        bachelor =(Button) findViewById(R.id.bachelorButton);
        bachelor.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(),
                        "Studiengänge Bachelor",Toast.LENGTH_LONG).show();
            }
        });

        master =(Button) findViewById(R.id.masterButton);
        master.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Toast.makeText(getApplicationContext(),
                        "Studiengänge Master",Toast.LENGTH_LONG).show();
            }
        });
    }
}