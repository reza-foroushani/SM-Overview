package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SemesterBearbeitenSubActivity extends AppCompatActivity
{
    EditText semesterNameEditText;
    Button speichernButton;
    String semesterName;
    int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semester_bearbeiten_subactivity);

        semesterNameEditText = (EditText) findViewById(R.id.EDITSEMESTERNAME_EDITTEXT);
        speichernButton = (Button) findViewById(R.id.EDITSEMESTER_BUTTON);

        Intent i = getIntent();
        index = i.getIntExtra("INDEX", -1);
        semesterName = i.getStringExtra("SEMESTERNAME");

        semesterNameEditText.setText(semesterName);
    }

    public void saveData(View v)
    {
        Intent i = new Intent();
        i.putExtra("SEMESTERNAME", semesterNameEditText.getText().toString());
        i.putExtra("INDEX", index);
        setResult(Activity.RESULT_OK, i);
        finish();
    }
}