package de.hskl.smoverview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import de.hskl.smoverview.databaseClasses.MasterDTO;
import de.hskl.smoverview.databaseClasses.MusterahmadDB;

public class MasterActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener  {
    //requestCode
    public final int RequestCodHinzufuegen=11;
    //dialog
Boolean isSchowingDialog=false;
Boolean isSchowingDialogLöschen=false;
AlertDialog beatbeitungDialog;
MasterDTO masterDialog;

    //listView,Adapter und DB
     ListView fachbereichliste ;
     MasterAdapter masterAdapter;
      MusterahmadDB db;

      //für postion von item
      int postion_item;
//
    EditText suchen ;

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
        suchen=(EditText) findViewById(R.id.SUCHEN);
        suchen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                 suchList(suchen.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        //TODO Adabter für datenbank

        db= new MusterahmadDB(this);
        updateList();


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



    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        super.onPause();

        if(beatbeitungDialog!=null&& beatbeitungDialog.isShowing()) {
            beatbeitungDialog.dismiss();
            Toast toast = Toast.makeText(getApplicationContext(), " if onPause", Toast.LENGTH_SHORT);
            toast.show();


        }else {
            // wenn ich kein button ausgewält in bearbeit dialog
            isSchowingDialog=false;
            isSchowingDialogLöschen=false;
        }

    }


    @Override
    protected void onRestart() {
        super.onRestart();


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
                // prüfung ob bereit da oder nciht
                if(db.PruefBereicheMaster(FachbereichNme)){
                    // prüfung ob leer oder nciht
            if(FachbereichNme.trim().length() > 0) {
                MasterDTO master = new MasterDTO(FachbereichNme, FachbereichBeschreibung);
                db.addFachberecihMaster(master, "M");
                // es wird  neue item hinzugefügt

                //  FachbereichMasterItem.add(data.getStringExtra("NAME"));

                Toast toast = Toast.makeText(this, "Nuer Fachbereich hinzufügt", Toast.LENGTH_SHORT);
                toast.show();

                updateList();
            }else {Toast toast = Toast.makeText(getApplicationContext(), " kein  Fachberecih hinzufügt feld war leer ", Toast.LENGTH_SHORT);
                toast.show();

                updateList();
            }

                }else{

                    suchen.setText(FachbereichNme);
                    suchList(FachbereichNme);
                    Toast toast = Toast.makeText(getApplicationContext(), " ist bereit da ", Toast.LENGTH_SHORT);
                    toast.show();
                }


            }

        }
        super.onActivityResult(requestCod,resultCode,data);
    }

    // hier wenn ich auf item gedrüct ist,aslo geh  zu activti von Eduard
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //Object o = fachbereichliste.getItemAtPosition(i);
        //oder
       // String selctItem = adapterView.getItemAtPosition(i).toString();
        MasterDTO master =  (MasterDTO) adapterView.getItemAtPosition(i);
        //TODO hier später mit Eduard verknuebfen
        Intent intent = new Intent(this,SemesterUebersichtActivity.class);
        intent.putExtra("FACHBEREICHNAME",master.getFachbereichName());
        intent.putExtra("MorB","M");
        intent.putExtra("FACHBEREICH_ID",master.getFachbereich_Id());
          startActivity(intent);


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
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
      //  altetext = ((TextView) info.targetView).getText().toString();

      //postiton der Item holen
        postion_item  =  info.position;

      //TODO selsct ein item von databank

        //die gibt objekt von class menuinfaleter
        MenuInflater inflater= getMenuInflater();
        // damit ich menue.xml zugreucfen , und zweite paramiter von cotext menu
        inflater.inflate(R.menu.master_contextmenue,menu);

    }


//itemselected von cotextmenu
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {

        masterDialog = db.getmasterbyId(((MasterDTO) fachbereichliste.getItemAtPosition(postion_item)).getFachbereich_Id());
        if(item.getItemId()==R.id.BEARBEITEN){
         openBearbeitungDialog(masterDialog);
        }
        if(item.getItemId()== R.id.LOESCHEN){
            openLoeschenDialog(masterDialog);
        }
        return super.onContextItemSelected(item);
    }
// bearbeiten Dialog
     public void  openBearbeitungDialog(MasterDTO master){
        //damit  das objeckt nicht verleren, wenn es zwei mal umgedreht wird
         masterDialog = master;

MusterahmadDB dbDialog= new MusterahmadDB(this);
         AlertDialog.Builder dialogBearbeiten= new AlertDialog.Builder(this);
         dialogBearbeiten.setTitle("Fachbereichname berbeiten ");
         dialogBearbeiten.setMessage("Neu Name eingeben");
         //bring die bearbeitlayout  und mit context zusammenlegen
         final  View  bearbeitlayout =this.getLayoutInflater().inflate(R.layout.layout_dialogbearbeitenfachbereichvonmaster,null);
         dialogBearbeiten.setView(bearbeitlayout);
         // such nach ausgewähltete item in DB und bring es hier
         final MasterDTO masterfinal = master;
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
                 String neuFachbereichName=newText.getText().toString();
                 String neuFachbereichBeschreicung=newBeschreibung.getText().toString();


           // prüfung ob bereit da oder nciht
                 if(db.PruefBereicheMaster(neuFachbereichName)){
                     // prüfung ob leer oder nciht
                 if(neuFachbereichName.trim().length() > 0) {
                     MasterDTO nueMaster = new MasterDTO(masterfinal.getFachbereich_Id(), neuFachbereichName, neuFachbereichBeschreicung);
                     db.updatMaster(nueMaster, masterfinal.getFachbereich_Id());
                     updateList();
                     Toast toast = Toast.makeText(getApplicationContext(), "geändert", Toast.LENGTH_SHORT);
                     toast.show();
                     updateList();
                     isSchowingDialog = false;
                 }else{
                     Toast toast = Toast.makeText(getApplicationContext(), " kein Änderung Fachberecih feld war leer ", Toast.LENGTH_SHORT);
                     toast.show();
                     
                     updateList();
                 }

                 }else{

                     suchen.setText(neuFachbereichName);
                     Toast toast = Toast.makeText(getApplicationContext(), " ist bereit da ", Toast.LENGTH_SHORT);
                     toast.show();
                 }
             }
         });
         dialogBearbeiten.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
                 isSchowingDialog=false;

             }
         });

         beatbeitungDialog=  dialogBearbeiten.create();

         isSchowingDialog=true;

         beatbeitungDialog.show();
     }
     //löschen Dialog
     public void openLoeschenDialog(final MasterDTO master){
         masterDialog = master;
         final AlertDialog.Builder dialogloeschen= new AlertDialog.Builder(this);
         final MasterDTO masterFinal = master;
         dialogloeschen.setTitle("Fachbereich löschen ");
         dialogloeschen.setMessage("Sind Sie sicher ");
         dialogloeschen.setPositiveButton("löschen", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 //TODO hier später mit datenbank löschen
           db.deleteFachbereich(masterFinal.getFachbereich_Id());
              updateList();
                 Toast toast = Toast.makeText(getApplicationContext(),master.getFachbereichName()+ " ist gelöcht ", Toast.LENGTH_SHORT);
                 toast.show();
                 isSchowingDialogLöschen=false;

             }
         });
         dialogloeschen.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialogInterface, int i) {
                 dialogInterface.dismiss();
                 isSchowingDialogLöschen=false;

             }
         });
         beatbeitungDialog= dialogloeschen.create();

         isSchowingDialogLöschen=true;

         beatbeitungDialog.show();
     }

// updateliste
     public  void updateList(){
         ArrayList<MasterDTO> test1 = db.getAllFachBereicheMaster();
         masterAdapter = new MasterAdapter(this,R.layout.item_master,test1);
         fachbereichliste.setAdapter(masterAdapter);
     }


     public  void suchList(String wort){
         ArrayList<MasterDTO> test2 = db.sucheBereicheMaster(wort);
         masterAdapter = new MasterAdapter(this,R.layout.item_master,test2);
         fachbereichliste.setAdapter(masterAdapter);
     }





     //save status 
   @Override
    protected void onSaveInstanceState(Bundle outState) {
        //ich ichbeischer daten von obejt der bearebeiten well
        if(isSchowingDialog) {
            outState.putBoolean("IS_SHOWING_DIALOG", isSchowingDialog);
            outState.putString("FACHNAME", masterDialog.getFachbereichName());
            outState.putString("FACHBESCHREIBUNG", masterDialog.getBeschreichbung());
            outState.putInt("FACHID", masterDialog.getFachbereich_Id());
        }
        if(isSchowingDialogLöschen) {
           outState.putBoolean("IS_SHOWING_DIALOG_loeschen", isSchowingDialogLöschen);
           outState.putString("FACHNAME", masterDialog.getFachbereichName());
           outState.putString("FACHBESCHREIBUNG", masterDialog.getBeschreichbung());
           outState.putInt("FACHID", masterDialog.getFachbereich_Id());
       }
        super.onSaveInstanceState(outState);
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!=null) {
            String fachname= savedInstanceState.getString("FACHNAME","");
            String fachbeschreicbung= savedInstanceState.getString("FACHBESCHREIBUNG","");
            int id= savedInstanceState.getInt("FACHID",1702);
            MasterDTO master=new MasterDTO(id,fachname,fachbeschreicbung);
            isSchowingDialog = savedInstanceState.getBoolean("IS_SHOWING_DIALOG", false);
            isSchowingDialogLöschen = savedInstanceState.getBoolean("IS_SHOWING_DIALOG_loeschen", false);

if(isSchowingDialog) {

    openBearbeitungDialog(master);

}if(isSchowingDialogLöschen) {
    openLoeschenDialog(master);


}


}
        }

        }



