package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ModulDetailansichtActivity extends AppCompatActivity
{
    TextView modulName;
    TextView modulBeschreibung;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modul_detailansicht_activity);

        modulName = findViewById(R.id.MODULNAME_TEXTVIEW);
        modulBeschreibung = findViewById(R.id.BESCHREIBUNGMODUL_TEXTVIEW);
        Intent intent = getIntent();

        modulName.setText(intent.getStringExtra("SEMESTER") + ": " + intent.getStringExtra("MODUL"));
        modulBeschreibung.setText(intent.getStringExtra("MODULBESCHREIBUNG"));
    }
}