package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class BachelorIntent extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    TextView ausgabe;
    FloatingActionButton addButton;
    ListView addListView;
    public final int requestAdd = 10;

    DBFachbereich dbFachbereich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bachelor_bachelor);

        ausgabe = findViewById(R.id.TEXT_VIEW_BACHELOR);
        addButton = findViewById(R.id.ADD_BUTTEN);
        addListView = findViewById(R.id.ADD_LIST_VIEW);
        dbFachbereich = new DBFachbereich(this);

        registerForContextMenu(addListView);

    }

    @Override
    public void onClick(View view) {


        if(view.getId() == addButton.getId())
        {
            Intent intentOfAddFach = new Intent(this,Bachelor_Add_Fach.class);
            startActivity(intentOfAddFach);

            Toast toast = Toast.makeText(this,"ADD",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCod ,int resultCode , Intent data){
        if(requestCod == requestAdd ){
            if(resultCode== Activity.RESULT_OK){
                //TODO hier sp?ter mit datenbank

                String fachbereich = data.getStringExtra("NAME");

                if(fachbereich.trim().length() > 0) {
                    Bachelor bachelor = new Bachelor(fachbereich);
                    dbFachbereich.insertBachlor(bachelor,"b");

                    Toast toast = Toast.makeText(this, "Fachbereich wurde erfolgreich hinzugefuegt."
                            , Toast.LENGTH_SHORT);
                    toast.show();
                }

            }

        }
        super.onActivityResult(requestCod,resultCode,data);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}