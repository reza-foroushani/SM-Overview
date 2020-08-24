package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Bachelor_Add_Fach extends AppCompatActivity implements View.OnClickListener{

    EditText addEditText;
    Button addSpeichernButton;
    Button addAbbrechenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bachelor_add_fach);

        addEditText = findViewById(R.id.ADD_TEXT_INPUT);
        addSpeichernButton = findViewById(R.id.ADD_SPEICHERN_BUTTON);
        addAbbrechenButton = findViewById(R.id.ADD_ABBRECHEN_BUTTON);
        addEditText.setOnClickListener(this);
        addSpeichernButton.setOnClickListener(this);
        addAbbrechenButton.setOnClickListener(this);

        // Datenbank wird mithilfe des Anwendungskontext (this) angelegt
        DBFachbereich db = new DBFachbereich(this);

    }

    @Override
    public void onClick(View view) {

        if(view.getId() == addAbbrechenButton.getId())
        {
            Intent intentOfHomeViewFach = new Intent(this,BachelorIntent.class);
            startActivity(intentOfHomeViewFach);
        }
    }
}