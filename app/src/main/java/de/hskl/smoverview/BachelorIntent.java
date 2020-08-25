package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.UserDictionary;
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
import de.hskl.smoverview.databaseClasses.MasterDTO;

public class BachelorIntent extends AppCompatActivity implements View.OnClickListener{

    TextView ausgabe;
    FloatingActionButton addButton;
    ListView addListView;
    ArrayList arrayList;
    ArrayAdapter arrayAdapter;
    DBFachbereich dbFachbereich;
    private int selectedID;
    private String selectName;
    private BachelorDTO bachelorDTO;

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
        loadData();

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
    } // onClick

    // update
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu,view,menuInfo);
       menu.setHeaderTitle("Was möchten Sie tun?");
        menu.add(0,view.getId(),0,"Bearbeiten");
        menu.add(0, view.getId(),0,"Löschen");
    }

    private void loadData() {
        DBFachbereich dbFachbereich = new DBFachbereich(getApplicationContext());
        ArrayList list = dbFachbereich.getALLFachBachelor();
        if (list != null) {
            addListView.setAdapter(arrayAdapter);
            arrayAdapter.notifyDataSetChanged();
            addListView.invalidateViews();
            addListView.refreshDrawableState();

        }
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item)
    {
        if(item.getTitle() == "Löschen") {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.meldung);
        builder.setMessage(R.string.sind_Sie_Sicher);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    AdapterView.AdapterContextMenuInfo adapterContextMenuInfo =
                            (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                    TextView textViewId = adapterContextMenuInfo.targetView.findViewById(R.id.ADD_LIST_VIEW);
                    int id = Integer.parseInt(textViewId.getText().toString());
                    DBFachbereich dbFachbereich = new DBFachbereich(getApplicationContext());
                    if(dbFachbereich.delete(id))
                    {
                        loadData();
                    }else
                    {
                        Toast.makeText(getApplicationContext(), getText(R.string.delete_failed),
                                Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });

            Toast.makeText(getApplicationContext(), "löschen", Toast.LENGTH_LONG).show();
            /*
        }else if(item.getTitle() == "Bearbeiten")
        {
            Toast.makeText(getApplicationContext(),"wird bearbeitet",Toast.LENGTH_LONG).show();
        }else
        {
            return  false;
        }
        return  true;
    }
    */
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }
        return super.onContextItemSelected(item);
    }

    /*
    //löschen Dialog
    public void openLoeschenDialog(final BachelorDTO bachelor){
        bachelorDTO = bachelor;
        final AlertDialog.Builder dialogloeschen= new AlertDialog.Builder(this);
        final BachelorDTO bachelorFinal = bachelor;
        dialogloeschen.setMessage("Wollen Sie Fachbereich wirklich löschen? ");
        dialogloeschen.setPositiveButton("löschen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //TODO hier später mit datenbank löschen
                dbFachbereich.remoteFach(bachelorFinal.getId());
                addListView.invalidateViews();

                Toast toast = Toast.makeText(getApplicationContext(),bachelor.getFachbereich()+ " ist gelöcht ", Toast.LENGTH_SHORT);
                toast.show();
               // isSchowingDialogLöschen=false;

            }
        });
        dialogloeschen.setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
              //  isSchowingDialogLöschen=false;

            }
        });
        //beatbeitungDialog= dialogloeschen.create();

        //isSchowingDialogLöschen=true;

        //beatbeitungDialog.show();
    }
    */

}