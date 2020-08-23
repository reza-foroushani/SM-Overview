package de.hskl.smoverview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;

public class CostumExpandableListAdapter extends BaseExpandableListAdapter
{
    private Context _context;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;

    public CostumExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData)
    {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent)
    {
        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount()
    {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        Button addModulBtn = (Button)convertView.findViewById(R.id.ADDMODUL_BUTTON);
        Button deleteSemesterBtn = (Button)convertView.findViewById(R.id.DELETESEMESTER_BUTTON);

        setupClickListenerModulAdd(addModulBtn, groupPosition);
        setupClickListenerSemesterDelete(deleteSemesterBtn, groupPosition);

        TextView lblListHeader = (TextView) convertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }

    private void setupClickListenerModulAdd(Button addModulBtn, final int groupPosition)
    {
        addModulBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                LayoutInflater inflater = LayoutInflater.from(_context);
                final View inflateView = inflater.inflate(R.layout.modul_hinzufuegen_dialog, null);
                AlertDialog dialog;
                builder.setView(inflateView);

                builder.setTitle("Neues Modul hinzufügen")
                        .setPositiveButton("Hinzufügen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO: Add to Database
                                final EditText modulNameEditText = (EditText) inflateView.findViewById(R.id.MODULNAME_EDITTEXT);
                                String neuesModul = modulNameEditText.getText().toString();
                                String semesterName = (String)getGroup(groupPosition);

                                if(!neuesModul.isEmpty())
                                {
                                    _listDataChild.get(semesterName).add(neuesModul);
                                    notifyDataSetChanged();
                                    Toast.makeText(_context, "Modul erfolgreich hinzugefügt!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                    Toast.makeText(_context, "Leere Modulnamen nicht erlaubt!", Toast.LENGTH_SHORT).show();
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
    }

    private void setupClickListenerSemesterDelete(Button deleteSemesterBtn, final int groupPosition)
    {
        deleteSemesterBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                LayoutInflater inflater = LayoutInflater.from(_context);
                AlertDialog dialog;

                final String semesterName = _listDataHeader.get(groupPosition);

                builder.setTitle("Willst du wirklich das Semester \"" + semesterName + "\" löschen?")
                        .setPositiveButton("Bestätigen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //TODO: Delete from Database
                                _listDataHeader.remove(semesterName);
                                _listDataChild.remove(semesterName);
                                notifyDataSetChanged();
                                Toast.makeText(_context, "Semester erfolgreich gelöscht!", Toast.LENGTH_SHORT).show();
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
    }

    public void updateView(List<String> listDataHeader, HashMap<String, List<String>> listChildData)
    {
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        notifyDataSetChanged();
    }
}
