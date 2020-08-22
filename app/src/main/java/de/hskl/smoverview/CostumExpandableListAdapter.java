package de.hskl.smoverview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.ArrayList;
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
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
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
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent)
    {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null)
        {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);

            Button addModulBtn = (Button)convertView.findViewById(R.id.ADDMODUL_BUTTON);
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

                                    _listDataChild.get(getGroup(groupPosition)).add(modulNameEditText.getText().toString());
                                    notifyDataSetChanged();
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

            Button deleteSemesterBtn = (Button)convertView.findViewById(R.id.DELETESEMESTER_BUTTON);
            deleteSemesterBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(_context);
                    LayoutInflater inflater = LayoutInflater.from(_context);
                    AlertDialog dialog;

                    final String str = _listDataHeader.get(groupPosition);

                    builder.setTitle("Willst du wirklich das Semester \"" + str + "\" löschen?")
                            .setPositiveButton("Bestätigen", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //TODO: Delete from Database
                                    _listDataHeader.remove(str);
                                    _listDataChild.remove(str);
                                    notifyDataSetChanged();
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

            Button editSemesterBtn = (Button)convertView.findViewById(R.id.EDITSEMESTER_BUTTON);
            editSemesterBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    final String str = _listDataHeader.get(groupPosition);
                    Log.d("HSKL", "Startup Edit Semester Subactivity...");
                    Intent i = new Intent(_context, SemesterBearbeitenSubActivity.class);
                    Log.d("HSKL", "Groupos: " + groupPosition);
                    i.putExtra("INDEX", groupPosition);
                    i.putExtra("SEMESTERNAME", str);
                    ((Activity) _context).startActivityForResult(i, RequestCodes.editSemesterSuccess.toInt());
                }
            });
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
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

    public void updateView(List<String> listDataHeader, HashMap<String, List<String>> listChildData)
    {
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        notifyDataSetChanged();
    }
}
