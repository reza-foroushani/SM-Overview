package de.hskl.smoverview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MasterActivity extends AppCompatActivity implements View.OnClickListener  {
    //requestCode
    public final int RequestCodHinzufuegen=11;

    ListView fachbereichliste ;
    ArrayList<String> FachbereichMasterItem = new ArrayList<>();
    ArrayAdapter<String> items;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);
        //füe pluss floatingButton
        FloatingActionButton myFab = (FloatingActionButton)  findViewById(R.id.fab);
        myFab.setOnClickListener(this);
       /* myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast toast = Toast.makeText(getApplicationContext(), "add", Toast.LENGTH_SHORT);
                toast.show();
            }
        });*/

        fachbereichliste =(ListView) findViewById(R.id.FACHBEREICH_LISTE);
        FachbereichMasterItem.add("Yellow");
        // es wird listeview mit cotextmnue verbunden
        registerForContextMenu(fachbereichliste);


// arryadapter  in view-liste
         items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FachbereichMasterItem);
        fachbereichliste.setAdapter(items);
    
    }


    @Override
    public void onClick(View view) {
        Intent i= new Intent(this,SubHinzufuegenFachbereichMasterActivity.class);
        // ich schicke Intent und ích warte
       startActivityForResult(i,RequestCodHinzufuegen);

        Toast toast = Toast.makeText(this, "add", Toast.LENGTH_SHORT);
        toast.show();
    }
    @Override
    // sobald die daten vin sub zurück ist, funktioniert onActivity direkt
    protected void onActivityResult(int requestCod ,int resultCode , Intent data){
        if(requestCod==RequestCodHinzufuegen ){
            if(resultCode== Activity.RESULT_OK){
                // es wird  neue item hinzugefügt
                FachbereichMasterItem.add(data.getStringExtra("NAME"));

                Toast toast = Toast.makeText(this, "Nuer Fachbereich hinzufügt", Toast.LENGTH_SHORT);
                toast.show();

            }

        }
        super.onActivityResult(requestCod,resultCode,data);
    }
    @Override
    protected  void  onStart(){
        super.onStart();
        fachbereichliste.setAdapter(items);

    }
//context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //die gibt objekt von class menuinfaleter
        MenuInflater inflater= getMenuInflater();
        // damit ich menue.xml zugreucfen , und zweite paramiter von cotext menu
        inflater.inflate(R.menu.master_contextmenue,menu);
    }
//itemselected von cotextmenu
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.BEARBEITEN){

        }
        if(item.getItemId()== R.id.LOESCHEN){

        }
        return super.onContextItemSelected(item);
    }
}
