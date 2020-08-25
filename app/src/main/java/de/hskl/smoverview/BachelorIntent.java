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
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import de.hskl.smoverview.databaseClasses.BachelorDTO;
import de.hskl.smoverview.databaseClasses.DBFachbereich;
import de.hskl.smoverview.databaseClasses.MasterDTO;

public class BachelorIntent extends AppCompatActivity implements View.OnClickListener {

    TextView ausgabe;
    FloatingActionButton addButton;
    ListView addListView;
    ArrayAdapter arrayAdapter;
    DBFachbereich dbFachbereich;
    String bachelorEdit;
    int poition;
    List<ListView> listOfItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bachelor_bachelor);

        ausgabe = findViewById(R.id.TEXT_VIEW_BACHELOR);
        addButton = findViewById(R.id.ADD_BUTTEN);
        addListView = findViewById(R.id.ADD_LIST_VIEW);
        registerForContextMenu(addListView);

        dbFachbereich = new DBFachbereich(BachelorIntent.this);
        updateList();
        arrayAdapter.notifyDataSetChanged();
        addListView.invalidateViews();
        addListView.refreshDrawableState();

        // neue Liste
        addListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                BachelorDTO bachelor = (BachelorDTO) adapterView.getItemAtPosition(position);
                Intent intent = new Intent(BachelorIntent.this, SemesterUebersichtActivity.class);
                intent.putExtra("FACHBEREICHNAME", bachelor.getFachbereich());
                intent.putExtra("MorB", "B");
                intent.putExtra("FACHBEREICH_ID", bachelor.getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onClick(View view) {


        if (view.getId() == addButton.getId()) {
            Intent intentOfAddFach = new Intent(this, Bachelor_Add_Fach.class);
            startActivityForResult(intentOfAddFach, 10);

            Toast toast = Toast.makeText(this, "Fachbereich hinzufügen", Toast.LENGTH_SHORT);
            toast.show();
        }
    } // onClick

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            updateList();
            Log.d("HSKL", "ADDED BRUDI");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Context Löschen und bearbeiten
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Was möchten Sie tun?");
        menu.add(0, view.getId(), 0, "Bearbeiten");
        menu.add(0, view.getId(), 0, "Löschen");
    }


    // ViewList aktualisieren
    public void updateList() {
        ArrayList<BachelorDTO> test1 = dbFachbereich.getALLFachBachelor();
        Log.d("HSKL", "LISTILIST: " + test1);
        arrayAdapter = new BachelorAdapter(this, R.layout.bachelor_item, test1);
        addListView.setAdapter(arrayAdapter);
    }

    // Liste von Datensatz laden
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

    // Löschen und Bearbeiten ein Datensatz
    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        if (item.getTitle() == "Löschen") {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.meldung);
            builder.setMessage(R.string.sind_Sie_Sicher);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo =
                                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                        BachelorDTO bachelor = (BachelorDTO) addListView.getItemAtPosition(adapterContextMenuInfo.position);
                        int id = bachelor.getId();
                        DBFachbereich dbFachbereich = new DBFachbereich(getApplicationContext());
                        if (dbFachbereich.delete(id)) {
                            updateList();
                        } else {
                            Toast.makeText(getApplicationContext(), getText(R.string.delete_failed),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            Toast.makeText(getApplicationContext(), "löschen", Toast.LENGTH_LONG).show();
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }

/*
        // Bearbeiten
        else if (item.getTitle() == "Bearbeiten") {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Name des Fachbereich ändern ");
            builder.setMessage("Geben Sie neue Namen ein:");

            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {
                        AdapterView.AdapterContextMenuInfo adapterContextMenuInfo =
                                (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                        BachelorDTO bachelor = (BachelorDTO) addListView.getItemAtPosition(adapterContextMenuInfo.position);
                      // BachelorDTO data = new BachelorDTO(bachelor.getId());

                        DBFachbereich dbFachbereich = new DBFachbereich(getApplicationContext());
                        if (dbFachbereich.updateBachelor(bachelor.getFachbereich())) {
                            updateList();
                        } else {
                            Toast.makeText(getApplicationContext(), getText(R.string.update_failed),
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });

            Toast.makeText(getApplicationContext(), "Bearbeiten", Toast.LENGTH_LONG).show();
        }

 */
            return super.onContextItemSelected(item);

        }



}


