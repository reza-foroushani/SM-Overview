package de.hskl.smoverview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class SubHinzufuegenFachbereichMasterActivity extends AppCompatActivity implements View.OnClickListener {
    EditText konkreteEdettext ;
    EditText konkreteEdettextBeschreibung ;
    Button speicherButton ;
    Button abbrechenButton ;
    String fachbereichName;
    String fachbereichBeschreibung;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);//hallo
        setContentView(R.layout.activitz_subhinzufuegenfachbereichmaster);

        konkreteEdettext = (EditText) findViewById(R.id.FACHBEREICHNAME);
        konkreteEdettextBeschreibung = (EditText) findViewById(R.id.BESCHREIBUNG_SUB);
        speicherButton = (Button) findViewById(R.id.SPEICHERN_BUTTON);
        abbrechenButton = (Button) findViewById(R.id.ABBRECHEN_BUTTON);
        //
         speicherButton.setOnClickListener(this);
        abbrechenButton.setOnClickListener(this);


            }

    @Override
    public void onClick(View view) {
        if (view== speicherButton) {if (konkreteEdettext.getText().toString()!=" ") {
            fachbereichName = konkreteEdettext.getText().toString();
            fachbereichBeschreibung = konkreteEdettextBeschreibung.getText().toString();
            Intent i = new Intent();
            //neu Intent ,damit ich ,was geschriben habe, wiede zurüch schiken
            i.putExtra("NAME", fachbereichName);
            i.putExtra("BESCHREIBUNG", fachbereichBeschreibung);

            // schicke ich zurück
            setResult(Activity.RESULT_OK, i);
        }

            finish();
        }if(view==abbrechenButton){
            finish();
        }
    }
}
