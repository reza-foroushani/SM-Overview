package de.hskl.smoverview;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.LayoutInflater;
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

import de.hskl.smoverview.databaseClasses.BachelorDTO;

import de.hskl.smoverview.databaseClasses.DatenbankManager;
import de.hskl.smoverview.javaClasses.RequestCodes;

public class BachelorIntent extends AppCompatActivity implements View.OnClickListener {
    TextView ausgabe;
    FloatingActionButton addButton;
    ListView addListView;
    ArrayAdapter arrayAdapter;
    //DBFachbereich dbFachbereich;
    DatenbankManager dbFachbereich;


    EditText suchen ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bachelor_bachelor);

        ausgabe = findViewById(R.id.TEXT_VIEW_BACHELOR);
        addButton = findViewById(R.id.ADD_BUTTEN);
        addListView = findViewById(R.id.ADD_LIST_VIEW);
        registerForContextMenu(addListView);

        dbFachbereich = new DatenbankManager(BachelorIntent.this);
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

        suchen = (EditText) findViewById(R.id.SUCHEN2);
        suchen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                suchList(suchen.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == addButton.getId()) {
            Intent intentOfAddFach = new Intent(this, Bachelor_Add_Fach.class);
            startActivityForResult(intentOfAddFach, 10);
        }
    } // onClick

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.addBachelorSuccess.toInt()) {
            updateList();
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
        arrayAdapter = new BachelorAdapter(this, R.layout.bachelor_item, test1);
        addListView.setAdapter(arrayAdapter);
    }

    public void suchList(String wort){
        ArrayList<BachelorDTO> test2 = dbFachbereich.sucheBereicheBachlor(wort);
        arrayAdapter = new BachelorAdapter(this,R.layout.bachelor_item,test2);
        addListView.setAdapter(arrayAdapter);
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
                        DatenbankManager dbFachbereich = new DatenbankManager(getApplicationContext());
                        if (dbFachbereich.deleteFachbereich(id)) {
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

            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
        }

        // Bearbeiten
        else if (item.getTitle() == "Bearbeiten") {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            AlertDialog dialog;
            final LayoutInflater inflater = LayoutInflater.from(this);
            final View inflateView = inflater.inflate(R.layout.bachelor_bearbeiten, null);
            builder.setView(inflateView);

            builder.setTitle("Name des Fachbereich ändern ")
                .setMessage("Geben Sie neue Namen ein:")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialogInterface, int i)
                    {
                        try {
                            AdapterView.AdapterContextMenuInfo adapterContextMenuInfo =
                                    (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

                            final EditText fachbereichTextView = (EditText) inflateView.findViewById(R.id.BEARBEITEN_BACHELOR);
                            String fachbereichName = fachbereichTextView.getText().toString();
                            BachelorDTO bachelor = (BachelorDTO) addListView.getItemAtPosition(adapterContextMenuInfo.position);
                            bachelor.setFachbereich(fachbereichName);
                            DatenbankManager dbFachbereich = new DatenbankManager(getApplicationContext());
                            if (bachelor.getFachbereich().trim().length() > 0 && !bachelor.getFachbereich().isEmpty()) {
                                if (dbFachbereich.updateBachelor(bachelor)) {
                                    updateList();
                                    Toast.makeText(getApplicationContext(), "Änderung wurde erfolgreich durchgeführt!",
                                            Toast.LENGTH_LONG).show();
                                }
                            }else {
                                    Toast.makeText(getApplicationContext(), "Felder dürfen nicht leer sein!",
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch(Exception e){
                                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                    }
                });

            dialog = builder.create();
            dialog.show();
        }
        return super.onContextItemSelected(item);
        }
}