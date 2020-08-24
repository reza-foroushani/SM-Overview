package de.hskl.smoverview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hskl.smoverview.databaseClasses.RequestCodes;
import de.hskl.smoverview.databaseClasses.SMOverviewDataSource;

public class SemesterUebersichtActivity extends AppCompatActivity
{
    FloatingActionButton addSemesterFab;
    CostumExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    //TODO: Andere Max Semester?
    private final int MAXSEMESTER = 5;

    private SMOverviewDataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semester_uebersicht_activity);

        expListView = (ExpandableListView) findViewById(R.id.MEINE_LISTE);
        addSemesterFab = (FloatingActionButton) findViewById(R.id.ADDSEMESTER_FLOATINGACTIONBUTTON);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        fillListWithData(); //TODO: Delete later

        listAdapter = new CostumExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

        setupClickListenerForModulDetailed();
        setupClickListenerForSemesterAdd();

        registerForContextMenu(expListView);

        dataSource = new SMOverviewDataSource(this);
        dataSource.open();
        dataSource.close();
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

        if(childPos == -1)
            return super.onContextItemSelected(item);

        final String semesterName = listDataHeader.get(groupPos);
        final String modulName = listDataChild.get(listDataHeader.get(groupPos)).get(childPos);

        switch(item.getItemId())
        {
            case R.id.CONTEXT_BEARBEITEN:
                //TODO: Modul BEARBEITEN
                Intent i = new Intent(this, ModulBearbeitenSubActivity.class);
                i.putExtra("CHILDINDEX", childPos);
                i.putExtra("GROUPINDEX", groupPos);
                i.putExtra("MODULNAME", modulName);
                i.putExtra("MODULBESCHREIBUNG", "Modulbeschreibung");
                startActivityForResult(i, RequestCodes.editModuleSuccess.toInt());
                break;
            case R.id.CONTEXT_LOESCHEN:
                //TODO: In der Datenbank Modul löschen
                listDataChild.get(semesterName).remove(modulName);
                listAdapter.updateView(listDataHeader, listDataChild);
                Toast.makeText(this, "Modul erfolgreich gelöscht!", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        //TODO: In Datenbank speichern
        if(requestCode == RequestCodes.editModuleSuccess.toInt())
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String modulName = data.getStringExtra("MODULNAME");
                String semesterName = listDataHeader.get(data.getIntExtra("GROUPINDEX", -1));
                List<String> modulesList = listDataChild.get(semesterName);
                int childIndex = data.getIntExtra("CHILDINDEX", -1);

                modulesList.set(childIndex, modulName);

                listDataChild.put(semesterName, modulesList);

                listAdapter.updateView(listDataHeader, listDataChild);
                Toast.makeText(this, "Modul erfolgreich verändert!", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setupClickListenerForModulDetailed()
    {
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
    }

    private void setupClickListenerForSemesterAdd()
    {
        addSemesterFab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if(MAXSEMESTER == listDataHeader.size())
                    Toast.makeText(SemesterUebersichtActivity.this, "Maximum an Semester erreicht!", Toast.LENGTH_SHORT).show();
                else
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
                                    int selectedSemesterNr = Integer.parseInt(semesterNp.getDisplayedValues()[semesterNp.getValue() - 1]);
                                    listDataHeader.add("Semester " + selectedSemesterNr);
                                    listDataChild.put("Semester " + selectedSemesterNr, new ArrayList<String>());
                                    listAdapter.updateView(listDataHeader, listDataChild);
                                    Toast.makeText(SemesterUebersichtActivity.this, "Semester erfolgreich hinzugefügt!", Toast.LENGTH_SHORT).show();
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
            }
        });
    }

    private String[] getAvailableSemesterNumberArray()
    {
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

    private void fillListWithData()
    {
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
    }
}
