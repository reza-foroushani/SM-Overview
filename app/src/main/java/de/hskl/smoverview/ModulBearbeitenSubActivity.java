package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ModulBearbeitenSubActivity extends AppCompatActivity
{
    EditText modulNameEditText;
    EditText modulBeschreibungEditText;
    Button speichernButton;
    String modulName;
    String modulBeschreibung;
    int childindex;
    int groupindex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modul_bearbeiten_subactivity);
        modulNameEditText = (EditText) findViewById(R.id.EDIMODULENAME_EDITTEXT);
        modulBeschreibungEditText = (EditText) findViewById(R.id.EDITMODULBESCHREIBUNG_EDITTEXT);
        speichernButton = (Button) findViewById(R.id.EDITMODUL_BUTTON);

        Intent i = getIntent();
        childindex = i.getIntExtra("CHILDINDEX", -1);
        groupindex = i.getIntExtra("GROUPINDEX", -1);
        modulName = i.getStringExtra("MODULNAME");

        modulNameEditText.setText(modulName);
        modulBeschreibung = i.getStringExtra("MODULBESCHREIBUNG");
        modulBeschreibungEditText.setText(modulBeschreibung);
    }

    public void saveData(View v)
    {
        Intent i = new Intent();
        i.putExtra("MODULNAME", modulNameEditText.getText().toString());
        i.putExtra("MODULBESCHREIBUNG", modulBeschreibungEditText.getText().toString());
        i.putExtra("CHILDINDEX", childindex);
        i.putExtra("GROUPINDEX", groupindex);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}