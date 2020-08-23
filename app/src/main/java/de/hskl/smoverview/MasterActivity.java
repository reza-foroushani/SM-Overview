package de.hskl.smoverview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MasterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener  {
    //requestCode
    public final int RequestCodHinzufuegen=11;

    ListView fachbereichliste ;

    MasterAdapter masterAdapter;
    //todo später mit SimpelCursorAdabter  arbeiten mit datenbank
      MusterahmadDB db;

     String altetext;
      int altertextposition;
      int postion_item;


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
        //TODO Adabter für datenbank

        db= new MusterahmadDB(this);



       /* Context cxt= this;
        db= new MusterahmadDB(this);
        int itemLayout = android.R.layout.simple_list_item_2;
        // lifert von datenbank alle Einträger
        Cursor cursor = db.gettAllMaster();
        //anzeigen alle werte aus der spalte
        String[ ]  from = new String[] {db.FACHBERECIH_NAMEN,db.FACHBERECIH_BESCHREICHBUNG};
        //die werte aus spalte weden in das tex1 eingefügt
                int[] to = new int [] {android.R.id.text1,android.R.id.text2};
       // MasterAdapter simpleCursorAdapter = new MasterAdapter(cxt,itemLayout,cursor,from,to,0);

        SimpleCursorAdapter s = new SimpleCursorAdapter(cxt,itemLayout,cursor,from,to,0);
        fachbereichliste.setAdapter(s);*/
        // es wird listeview mit cotextmnue verbunden
        registerForContextMenu(fachbereichliste);




       /* // arryadapter  in view-liste
         items = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, FachbereichMasterItem);
        fachbereichliste.setAdapter(items);
        //wenn ich auf Item von lister gedrükt, listern für items in viewliste*/
        fachbereichliste.setOnItemClickListener(this);

    
    }

    @Override
    protected void onResume() {
        super.onResume();
        //damit wir dirkt hinzufügen und nciht warten bis , dass benutzer raus gen
        updateList();
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
                //TODO hier später mit datenbank

            String FachbereichNme = data.getStringExtra("NAME");
            String FachbereichBeschreibung = data.getStringExtra("BESCHREIBUNG");
                
            if(FachbereichNme.trim().length() > 0) {
                Master master = new Master(FachbereichNme, FachbereichBeschreibung);
                db.addFachberecihMaster(master, "M");
                // es wird  neue item hinzugefügt

                //  FachbereichMasterItem.add(data.getStringExtra("NAME"));

                Toast toast = Toast.makeText(this, "Nuer Fachbereich hinzufügt", Toast.LENGTH_SHORT);
                toast.show();
            }

            }

        }
        super.onActivityResult(requestCod,resultCode,data);
    }
    @Override
    protected  void  onStart(){
        super.onStart();
        //fachbereichliste.setAdapter(items);

    }
    // hier wenn ich auf item gedrüct ist,aslo geh  zu activti von Eduard
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Object o = fachbereichliste.getItemAtPosition(i);
        //oder
       // String selctItem = adapterView.getItemAtPosition(i).toString();
        //TODO hier später mit Eduard verknubfung
        // Intent intent = new Intent(this,);
               //  startActivity();
        Master master =  db.getmasterbyId(1);

        //Cursor meinzeiger =db.getmasterbyIdCursor(3);

        //String pen = o.toString();
        Toast toast = Toast.makeText(getApplicationContext(), "id "+master.getFachbereich_Id() , Toast.LENGTH_SHORT);
        toast.show();
    }

//context menu
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        // durch  AdapterView.AdapterContextMenuInfo ich kann zielwiew (was ich lang gedrucht habe ),die ich erneut in das cast übertrage
        AdapterView.AdapterContextMenuInfo info =
                (AdapterView.AdapterContextMenuInfo) menuInfo;
      //  altetext = ((TextView) info.targetView).getText().toString();
        postion_item  =  info.position;

//TODO selsct ein item von databank
        altetext ="a";
       // altertextposition=info.position;


        //die gibt objekt von class menuinfaleter
        MenuInflater inflater= getMenuInflater();
        // damit ich menue.xml zugreucfen , und zweite paramiter von cotext menu
        inflater.inflate(R.menu.master_contextmenue,menu);
        MenuItem beabeiten  =menu.findItem(R.id.BEARBEITEN);
        MenuItem löschen = menu.findItem(R.id.LOESCHEN);
    }


//itemselected von cotextmenu
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.BEARBEITEN){
         openBearbeitungDialog();
        }
        if(item.getItemId()== R.id.LOESCHEN){
            openLoeschenDialog();
        }
        return super.onContextItemSelected(item);
    }
// bearbeiten Dialog
     public void  openBearbeitungDialog(){
MusterahmadDB dbDialog= new MusterahmadDB(this);
         AlertDialog.Builder dialogBearbeiten= new AlertDialog.Builder(this);
         dialogBearbeiten.setTitle("Fachbereichname berbeiten ");
         dialogBearbeiten.setMessage("Neu Name eingeben");
         //bring die bearbeitlayout  und mit context zusammenlegen
         final  View  bearbeitlayout =this.getLayoutInflater().inflate(R.layout.layout_dialogbearbeitenfachbereichvonmaster,null);
         dialogBearbeiten.setView(bearbeitlayout);
         final Master master = (Master ) fachbereichliste.getItemAtPosition(postion_item);
         final EditText newText=(EditText) bearbeitlayout.findViewById(R.id.BEARBEITEN_DIALOG);
         final EditText newBeschreibung=(EditText) bearbeitlayout.findViewById(R.id.BESCHREIBUNG_DIALOG);
         newText.setText(master.getFachbereichName());
         newBeschreibung.setText(master.getBeschreichbung());

           dialogBearbeiten.setPositiveButton("speichern", new DialogInterface.OnClickListener() {


             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                   //ich füge die neu Bearbeitung  in alter position hinzu und aktuallisieren
               //  FachbereichMasterItem.set(altertextposition,newText.getText().toString());

                  //TODO hier später mit datenbank

                 Master nueMaster = new Master (master.getFachbereich_Id(),newText.getText().toString(),newBeschreibung.getText().toString());
                 db.updatMaster(nueMaster,master.getFachbereich_Id());
                 updateList();
                 Toast toast = Toast.makeText(getApplicationContext(), "geändert", Toast.LENGTH_SHORT);
                 toast.show();

             }
         });
         dialogBearbeiten.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
             }
         });
         dialogBearbeiten.create();
         dialogBearbeiten.show();

     }
     //löschen Dialog
     public void openLoeschenDialog(){
         final AlertDialog.Builder dialogloeschen= new AlertDialog.Builder(this);
         final Master master = (Master ) fachbereichliste.getItemAtPosition(postion_item);
         dialogloeschen.setTitle("Fachbereich löschen ");
         dialogloeschen.setMessage("Sind Sie sicher ");
         dialogloeschen.setPositiveButton("löschen", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 //TODO hier später mit datenbank löschen
           db.deleteFachbereich(master.fachbereich_Id);
              updateList();
                 Toast toast = Toast.makeText(getApplicationContext(), "gelöcht", Toast.LENGTH_SHORT);
                 toast.show();
             }
         });
         dialogloeschen.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
             }
         });
         dialogloeschen.create();
         dialogloeschen.show();
     }

// updateliste
     public  void updateList(){
         ArrayList<Master> test1 = db.getAllFachBereicheMaster();
         masterAdapter = new MasterAdapter(this,R.layout.item_master,test1);
         fachbereichliste.setAdapter(masterAdapter);
     }




     //save status 
   /* @Override
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
    }*/

}
