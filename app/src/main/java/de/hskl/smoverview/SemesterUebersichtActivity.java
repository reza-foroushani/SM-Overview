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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hskl.smoverview.databaseClasses.ModulDTO;
import de.hskl.smoverview.databaseClasses.DatenbankManager;
import de.hskl.smoverview.javaClasses.RequestCodes;
import de.hskl.smoverview.databaseClasses.SemesterDTO;

public class SemesterUebersichtActivity extends AppCompatActivity
{
    FloatingActionButton addSemesterFab;
    CostumExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    TextView studiengangNameTextView;

    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    public static int currentStudiengangId;
    public static String currentStudiengangName;

    public DatenbankManager db;

    private static int MAXSEMESTER;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.semester_uebersicht_activity);

        db = new DatenbankManager(this);

        Intent i = getIntent();
        currentStudiengangName = i.getStringExtra("FACHBEREICHNAME");
        currentStudiengangId = i.getIntExtra("FACHBEREICH_ID", -1);
        setMaxSemesterCount(i.getStringExtra("MorB"));


        studiengangNameTextView = (TextView) findViewById(R.id.STUDIENGANG_TEXTVIEW);
        studiengangNameTextView.setText(currentStudiengangName);

        expListView = (ExpandableListView) findViewById(R.id.MEINE_LISTE);
        addSemesterFab = (FloatingActionButton) findViewById(R.id.ADDSEMESTER_FLOATINGACTIONBUTTON);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listAdapter = new CostumExpandableListAdapter(this, listDataHeader, listDataChild, db, currentStudiengangId);
        expListView.setAdapter(listAdapter);

        initViewWithData(); //Fill List with Data if exists

        setupClickListenerForModulDetailed();
        setupClickListenerForSemesterAdd();

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

        if(childPos == -1)
            return super.onContextItemSelected(item);

        final String semesterName = listDataHeader.get(groupPos);
        final String modulName = listDataChild.get(listDataHeader.get(groupPos)).get(childPos);
        SemesterDTO semester = db.getSemester(semesterName, currentStudiengangId);

        switch(item.getItemId())
        {
            case R.id.CONTEXT_BEARBEITEN:
                Intent i = new Intent(this, ModulBearbeitenSubActivity.class);
                ModulDTO modul = db.getModul(modulName, semester.getS_id());
                i.putExtra("CHILDINDEX", childPos);
                i.putExtra("GROUPINDEX", groupPos);
                i.putExtra("MODULNAME", modulName);
                i.putExtra("MODULBESCHREIBUNG", modul.getModulbeschreibung());
                startActivityForResult(i, RequestCodes.editModuleSuccess.toInt());
                break;
            case R.id.CONTEXT_LOESCHEN:
                listDataChild.get(semesterName).remove(modulName);
                listAdapter.updateView(listDataHeader, listDataChild);

                boolean successs = db.deleteModul(semester.getS_id(), modulName);

                if(successs)
                    Toast.makeText(this, "Modul erfolgreich gelöscht!", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Fehler mit Datenbank!", Toast.LENGTH_SHORT).show();
                break;
            default:
                return super.onContextItemSelected(item);
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == RequestCodes.editModuleSuccess.toInt())
        {
            if(resultCode == Activity.RESULT_OK)
            {
                String modulName = data.getStringExtra("MODULNAME");
                if(isModulNameUnique(modulName))
                {
                    String oldModulName = data.getStringExtra("OLDMODULNAME");
                    String modulBeschreibung = data.getStringExtra("MODULBESCHREIBUNG");
                    String semesterName = listDataHeader.get(data.getIntExtra("GROUPINDEX", -1));
                    List<String> modulesList = listDataChild.get(semesterName);
                    int childIndex = data.getIntExtra("CHILDINDEX", -1);

                    modulesList.set(childIndex, modulName);

                    listDataChild.put(semesterName, modulesList);

                    listAdapter.updateView(listDataHeader, listDataChild);

                    SemesterDTO semester = db.getSemester(semesterName, currentStudiengangId);
                    ModulDTO currentModul = db.getModul(oldModulName, semester.getS_id());
                    ModulDTO newModul = new ModulDTO(currentModul.getM_id(), modulName, modulBeschreibung, currentModul.getS_id(), currentModul.getStudiengang_id());

                    boolean success = db.updateModul(newModul, currentModul.getM_id());

                    if(success)
                        Toast.makeText(this, "Modul erfolgreich verändert!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(this, "Fehler mit Datenbank!", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(this, "Modulname existiert bereits!", Toast.LENGTH_SHORT).show();
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
                SemesterDTO semester = db.getSemester(semesterName, currentStudiengangId);
                ModulDTO modul = db.getModul(modulName, semester.getS_id());
                Intent intent = new Intent(SemesterUebersichtActivity.this, ModulDetailansichtActivity.class);
                intent.putExtra("SEMESTER", semesterName);
                intent.putExtra("MODUL", modulName);
                intent.putExtra("MODULBESCHREIBUNG", modul.getModulbeschreibung());
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
                    final AlertDialog.Builder builder = new AlertDialog.Builder(SemesterUebersichtActivity.this);
                    LayoutInflater inflater = LayoutInflater.from(SemesterUebersichtActivity.this);
                    View inflateView = inflater.inflate(R.layout.semester_hinzufuegen_dialog, null);
                    final AlertDialog dialog;
                    builder.setView(inflateView);

                    String[] numbers = getAvailableSemesterNumberArray();
                    final NumberPicker semesterNp = (NumberPicker) inflateView.findViewById(R.id.SEMESTER_NUMBERPICKER);
                    semesterNp.setDisplayedValues(numbers);
                    semesterNp.setMinValue(1);
                    semesterNp.setMaxValue(numbers.length);

                    builder.setTitle("Neues Semester hinzufügen")
                            .setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int selectedSemesterNr = Integer.parseInt(semesterNp.getDisplayedValues()[semesterNp.getValue() - 1]);
                                    String semesterName = ("Semester" + selectedSemesterNr);
                                    listDataHeader.add(semesterName);
                                    listDataChild.put(semesterName, new ArrayList<String>());
                                    listAdapter.updateView(listDataHeader, listDataChild);

                                    SemesterDTO semester = new SemesterDTO(semesterName, currentStudiengangId);
                                    boolean success = db.addSemester(semester);

                                    if(success)
                                        Toast.makeText(SemesterUebersichtActivity.this, "Semester erfolgreich hinzugefügt!", Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(SemesterUebersichtActivity.this, "Fehler mit Datenbank!", Toast.LENGTH_SHORT).show();
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

        for(String str : listDataHeader) //Get Number from "Semester 1"
            usedNumbers.add(Integer.parseInt(str.replaceAll("\\D+","")));

        for(int i = 1; i <= MAXSEMESTER; ++i)
        {
            if(!usedNumbers.contains(i))
                availableNumbers.add(String.valueOf(i));
        }

        return availableNumbers.toArray(new String[0]);
    }

    private void initViewWithData()
    {
        List<SemesterDTO> semesterList = db.getAllSemesterForStudiengang(currentStudiengangId);

        for(SemesterDTO dto : semesterList)
            listDataHeader.add(dto.getSemestername());

        for(int i = 0; i<semesterList.size(); i++)
        {
            List<ModulDTO> modules = db.getModulesForSemester(semesterList.get(i).getS_id());

            List<String> modulnames = new ArrayList<>();
            for(int j = 0; j < modules.size(); j++)
                modulnames.add(modules.get(j).getModulname());

            listDataChild.put(listDataHeader.get(i), modulnames);
        }

        listAdapter.updateView(listDataHeader, listDataChild);
    }

    public void setMaxSemesterCount(String mOrB)
    {
        if(mOrB.equals("M"))
            MAXSEMESTER = 3;
        else if(mOrB.equals("B"))
            MAXSEMESTER = 7;
        else
        {
            Toast.makeText(this, "Fehler bei Übergabe von MorB!", Toast.LENGTH_SHORT);
            MAXSEMESTER = 0;
        }
    }

    public boolean isModulNameUnique(String modulName)
    {
        for(List<String> list : listDataChild.values())
            if(list.contains(modulName))
                return false;
        return true;
    }
}
