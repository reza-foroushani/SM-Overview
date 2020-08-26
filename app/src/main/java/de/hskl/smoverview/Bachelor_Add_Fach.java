package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import de.hskl.smoverview.databaseClasses.BachelorDTO;

import de.hskl.smoverview.databaseClasses.DatenbankManager;

public class Bachelor_Add_Fach extends AppCompatActivity implements View.OnClickListener{
    // Eingabefeld
    EditText addEditText;

    //zwei Button
    Button addSpeichernButton;
    Button addAbbrechenButton;

    DatenbankManager dbFachbereich;
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
        dbFachbereich = new DatenbankManager(Bachelor_Add_Fach.this);
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

        if(view.getId() == addSpeichernButton.getId()) {
            String text = addEditText.getText().toString();
            BachelorDTO bachelorDTO = new BachelorDTO(text);

            if (!text.isEmpty() && text.trim().length() > 0) {
                if (dbFachbereich.insertBachlor(bachelorDTO, "B")) {
                    addEditText.setText("");
                    Toast toast = Toast.makeText(this,"Datensatz erfolgreich gespeichert",Toast.LENGTH_SHORT);
                    toast.show();
                }
            }else {
                Toast toast = Toast.makeText(this, "Felder dürfen nicht leer sein!", Toast.LENGTH_SHORT);
                toast.show();
                Intent intentOfBachelorIntent = new Intent(this, BachelorIntent.class);
                startActivity(intentOfBachelorIntent);
            }
            }
                Intent i = new Intent();
                setResult(Activity.RESULT_OK, i);
                finish();
            }



}