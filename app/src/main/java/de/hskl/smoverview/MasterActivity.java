package de.hskl.smoverview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MasterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener  {
    //requestCode
    public final int RequestCodHinzufuegen=11;

    ListView fachbereichliste ;
    ArrayList<String> FachbereichMasterItem = new ArrayList<>();
    ArrayAdapter<String> items;
    String altetext;
    int altertextposition;


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

        // es wird listeview mit cotextmnue verbunden
        registerForContextMenu(fachbereichliste);



        // arryadapter  in view-liste
         items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FachbereichMasterItem);
        fachbereichliste.setAdapter(items);
        //wenn ich auf Item von lister gedrükt, listern für items in viewliste
        fachbereichliste.setOnItemClickListener(this);

    
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

    }// hier wenn ich auf item gedrüct ist zu activti von Eduard
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Object o = fachbereichliste.getItemAtPosition(i);
        //oder
       // String selctItem = adapterView.getItemAtPosition(i).toString();
        String pen = o.toString();
        Toast toast = Toast.makeText(getApplicationContext(), pen , Toast.LENGTH_SHORT);
        toast.show();
    }

//context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // durch  AdapterView.AdapterContextMenuInfo ich kann zielwiew (was ich lang gedrucht habe ),die ich erneut in das cast übertrage
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;
        altetext = ((TextView) info.targetView).getText().toString();
        altertextposition=info.position;


        //die gibt objekt von class menuinfaleter
        MenuInflater inflater= getMenuInflater();
        // damit ich menue.xml zugreucfen , und zweite paramiter von cotext menu
        inflater.inflate(R.menu.master_contextmenue,menu);
    }


//itemselected von cotextmenu
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.BEARBEITEN){
         openDialog();
        }
        if(item.getItemId()== R.id.LOESCHEN){

        }
        return super.onContextItemSelected(item);
    }
// bearbeiten Dialog
     public void openDialog(){
         AlertDialog.Builder dialogBearbeiten= new AlertDialog.Builder(this);
         dialogBearbeiten.setTitle("Fachbereichname berbeiten ");
         dialogBearbeiten.setMessage("Neu Name eingeben");
         //bring die bearbeitlayout  und mit context zusammenlegen
         final  View  bearbeitlayout =this.getLayoutInflater().inflate(R.layout.layout_dialogbearbeitenfachbereichvonmaster,null);
         dialogBearbeiten.setView(bearbeitlayout);
         final EditText newText=(EditText) bearbeitlayout.findViewById(R.id.BEARBEITEN_DIALOGBUTTON);
         newText.setText(altetext);

         dialogBearbeiten.setPositiveButton("speichern", new DialogInterface.OnClickListener() {


             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                   //ich füge die neu Bearbeitung  in alter position hinzu und aktuallisieren
                 FachbereichMasterItem.set(altertextposition,newText.getText().toString());
                 items.notifyDataSetChanged();

                  //TODO hier später mit datenbank
                 Toast toast = Toast.makeText(getApplicationContext(), "geändert", Toast.LENGTH_SHORT);
                 toast.show();

             }
         });
         dialogBearbeiten.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {

             }
         });
         dialogBearbeiten.create();
         dialogBearbeiten.show();

     }
     //save status 
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<String> title=new ArrayList<>();
        for(int i=0;i<FachbereichMasterItem.size();i++){

            title.add(FachbereichMasterItem.get(i));
        }
        outState.putStringArrayList("ARRYLIST",title);
        Log.d("HSKL", "onSaveInstanceState() "+outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d("HSKL", "onRestoreInstanceState() "+savedInstanceState);

        for(int i=0;i<savedInstanceState.getStringArrayList("ARRYLIST").size();i++){

            FachbereichMasterItem.add(savedInstanceState.getStringArrayList("ARRYLIST").get(i));
        }
    }

}
