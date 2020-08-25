package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import de.hskl.smoverview.databaseClasses.BachelorDTO;
import de.hskl.smoverview.databaseClasses.DBFachbereich;

public class BachelorIntent extends AppCompatActivity implements View.OnClickListener{

    TextView ausgabe;
    FloatingActionButton addButton;
    ListView addListView;
    ArrayList arrayList;
    ArrayAdapter arrayAdapter;
    DBFachbereich dbFachbereich;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bachelor_bachelor);

        ausgabe = findViewById(R.id.TEXT_VIEW_BACHELOR);
        addButton = findViewById(R.id.ADD_BUTTEN);
        addListView = findViewById(R.id.ADD_LIST_VIEW);
        registerForContextMenu(addListView);

        dbFachbereich = new DBFachbereich(BachelorIntent.this);
        arrayList = dbFachbereich.getALLFachBachelor();
        arrayAdapter = new ArrayAdapter(BachelorIntent.this,
                                        android.R.layout.simple_list_item_1,arrayList);

        addListView.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
        addListView.invalidateViews();
        addListView.refreshDrawableState();
    }

    @Override
    public void onClick(View view) {


        if(view.getId() == addButton.getId())
        {
            Intent intentOfAddFach = new Intent(this,Bachelor_Add_Fach.class);
            startActivity(intentOfAddFach);

            Toast toast = Toast.makeText(this,"Fachbereich hinzufügen",Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    // update
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu,view,menuInfo);
        menu.setHeaderTitle("Select The Action");
        menu.add(0,view.getId(),0,"Bearbeiten");
        menu.add(0, view.getId(),0,"Löschen");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        if(item.getTitle() == "Löschen") {
            Toast.makeText(getApplicationContext(), "löschen", Toast.LENGTH_LONG).show();
        }else if(item.getTitle() == "Bearbeiten")
        {
            Toast.makeText(getApplicationContext(),"wird bearbeitet",Toast.LENGTH_LONG).show();
        }else
        {
            return  false;
        }
        return  true;
    }



}