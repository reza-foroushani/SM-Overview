package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hskl.smoverview.databaseClasses.BachelorDTO;
import de.hskl.smoverview.databaseClasses.DBFachbereich;

public class Bachelor_Add_Fach extends AppCompatActivity implements View.OnClickListener{

    // Eingabefeld
    EditText addEditText;
    //zwei Button
    Button addSpeichernButton;
    Button addAbbrechenButton;

    // DB
    DBFachbereich dbFachbereich;
    ArrayList arrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bachelor_add_fach);

        addEditText = findViewById(R.id.ADD_TEXT_INPUT);
        addSpeichernButton = findViewById(R.id.ADD_SPEICHERN_BUTTON);
        addAbbrechenButton = findViewById(R.id.ADD_ABBRECHEN_BUTTON);
        addEditText.setOnClickListener(this);
        addAbbrechenButton.setOnClickListener(this);
        addSpeichernButton.setOnClickListener(this);

        // Datenbank Connect
        dbFachbereich = new DBFachbereich(Bachelor_Add_Fach.this);

        // Add Daten
        arrayList = dbFachbereich.getALLFachBachelor();




    }

    @Override
    public void onClick(View view) {

        if(view.getId() == addAbbrechenButton.getId()){
            Intent intentOfViewBachelor = new Intent(this,BachelorIntent.class);
            startActivity(intentOfViewBachelor);
            Toast toast = Toast.makeText(this,"Fachbereich",Toast.LENGTH_SHORT);
            toast.show();

        }

        if(view.getId() == addSpeichernButton.getId())
        {
            String leer = " ";
            String text = addEditText.getText().toString();
            BachelorDTO bachelorDTO = new BachelorDTO(text);

            if(text.isEmpty() || text.equals(leer) ){
                Toast toast = Toast.makeText(this,"kein Datenstz wurde gespeichert",Toast.LENGTH_SHORT);
                toast.show();
                Intent intentOfBachelorIntent = new Intent(this,BachelorIntent.class);
                startActivity(intentOfBachelorIntent);

            } else {
                if (!text.isEmpty() ) {
                    if (dbFachbereich.insertBachlor(bachelorDTO, "b")) {
                        addEditText.setText("");
                        Toast.makeText(Bachelor_Add_Fach.this,
                                "..::: Fachbereich Bachelor :::..",
                                Toast.LENGTH_SHORT).show();
                        arrayList.clear();
                        arrayList.addAll(dbFachbereich.getALLFachBachelor());


                    }
                }


            Intent intentOfBachelorIntent = new Intent(this,BachelorIntent.class);
            startActivity(intentOfBachelorIntent);

            Toast toast = Toast.makeText(this,"Datenstz erfolgreich gespeichert",Toast.LENGTH_SHORT);
            toast.show();
            }
        }

        finish();
    }





}