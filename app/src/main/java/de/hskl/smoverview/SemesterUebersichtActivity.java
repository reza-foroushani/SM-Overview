package de.hskl.smoverview;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SemesterUebersichtActivity extends AppCompatActivity
{
    CostumExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semester_uebersicht_activity);

        expListView = (ExpandableListView) findViewById(R.id.MEINE_LISTE);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        // Adding child data
        listDataHeader.add("Semester 1");
        listDataHeader.add("Semester 2");

        // Adding child data
        List<String> s1 = new ArrayList<>();
        s1.add("Test1");
        s1.add("Test2");
        s1.add("Test3");

        // Adding child data
        List<String> s2 = new ArrayList<>();
        s2.add("Test1");
        s2.add("Test2");
        s2.add("Test3");

        listDataChild.put(listDataHeader.get(0), s1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), s2);

        listAdapter = new CostumExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition
                    , int childPosition, long id)
            {
                final String semesterName = listDataHeader.get(groupPosition);
                final String modulName = listDataChild.get(listDataHeader.get(groupPosition)).get(childPosition);
                Intent intent = new Intent(SemesterUebersichtActivity.this, ModulDetailansichtActivity.class);
                intent.putExtra("SEMESTER", semesterName);
                intent.putExtra("MODUL", modulName);
                startActivity(intent);
                return false;
            }
        });

        FloatingActionButton addSemesterFab = findViewById(R.id.ADDSEMESTER_FLOATINGACTIONBUTTON);
        addSemesterFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(SemesterUebersichtActivity.this);
                LayoutInflater inflater = LayoutInflater.from(SemesterUebersichtActivity.this);
                View inflateView = inflater.inflate(R.layout.semester_hinzufuegen_dialog, null);
                AlertDialog dialog;
                builder.setView(inflateView);

                String[] numbers = getAvailableSemesterNumberArray();
                final NumberPicker semesterNp = (NumberPicker) inflateView.findViewById(R.id.SEMESTER_NUMBERPICKER);
                semesterNp.setDisplayedValues(numbers);
                semesterNp.setMinValue(1);
                semesterNp.setMaxValue(numbers.length);

                builder.setTitle("Neues Semester hinzufügen")
                        .setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO: Semester in Datenbank einfügen
                                int selectedSemesterNr = Integer.parseInt(semesterNp.getDisplayedValues()[semesterNp.getValue()-1]);
                                listDataHeader.add("Semester " + selectedSemesterNr);
                                listDataChild.put("Semester " + selectedSemesterNr, new ArrayList<String>());
                                listAdapter.updateView(listDataHeader, listDataChild);
                            }
                        })
                        .setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //Nichts tun
                            }
                        });

                dialog = builder.create();
                dialog.show();
            }
        });

        registerForContextMenu(expListView);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.start_activity_contextmenu, menu);

        MenuItem bearbeiten = menu.findItem(R.id.CONTEXT_BEARBEITEN);
        MenuItem loeschen = menu.findItem(R.id.CONTEXT_LOESCHEN);

        bearbeiten.setTitle("Bearbeiten");
        loeschen.setTitle("Löschen");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        ExpandableListView.ExpandableListContextMenuInfo info =
                (ExpandableListView.ExpandableListContextMenuInfo) item.getMenuInfo();
        int groupPos = ExpandableListView.getPackedPositionGroup(info.packedPosition);
        int childPos = ExpandableListView.getPackedPositionChild(info.packedPosition);

        //TODO: FIX => Warum kann ich Gruppe (Semester) Context Menü aufrufen?
        if(childPos == -1)
            return super.onContextItemSelected(item);

        final String semesterName = listDataHeader.get(groupPos);
        final String modulName = listDataChild.get(listDataHeader.get(groupPos)).get(childPos);

        switch(item.getItemId())
        {
            case R.id.CONTEXT_BEARBEITEN:
                //TODO: Modul BEARBEITEN
                Log.d("HSKL", "Startup Subactivity for Editing Modul...");
                break;
            case R.id.CONTEXT_LOESCHEN:
                //TODO: In der Datenbank Modul löschen
                listDataChild.get(semesterName).remove(modulName);
                listAdapter.updateView(listDataHeader, listDataChild);
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

    private String[] getAvailableSemesterNumberArray()
    {
        //TODO: Andere Max Semester?
        final int MAXSEMESTER = 10;

        ArrayList<String> availableNumbers = new ArrayList<>();
        ArrayList<Integer> usedNumbers = new ArrayList<>();

        //TODO: Semester von Datenbank
        for(String str : listDataHeader) //Get Number from "Semester 1"
            usedNumbers.add(Integer.parseInt(str.replaceAll("\\D+","")));

        for(int i = 1; i <= MAXSEMESTER; ++i)
        {
            if(!usedNumbers.contains(i))
                availableNumbers.add(String.valueOf(i));
        }

        return availableNumbers.toArray(new String[0]);
    }
}
