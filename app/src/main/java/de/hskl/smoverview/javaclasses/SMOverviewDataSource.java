package de.hskl.smoverview.javaclasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//Zuständig für das Bearbeiten der Daten
public class SMOverviewDataSource
{
    private SQLiteDatabase db;
    private SMOverviewDbHelper dbHelper;
    private Context context;

    public SMOverviewDataSource(Context context)
    {
        this.context = context;
    }

    public SMOverviewDataSource open() throws SQLException
    {
        dbHelper = new SMOverviewDbHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close()
    {
        dbHelper.close();
    }

    public boolean addSemester(SemesterDTO semester)
    {
        ContentValues values = new ContentValues();
        values.put(SMOverviewDbHelper.COLUMN_SEMESTERNAME, semester.getSemestername());

        return db.insert(SMOverviewDbHelper.TABLE_SEMESTER, null, values) > 0;
    }
/*
    public ArrayList<SemesterDTO> getAllSemester()
    {

    }

    public boolean deleteSemester()
    {

    }

    public boolean addModul(ModulDTO modul)
    {

    }

    public ArrayList<ModulDTO> getModulForSemester(SemesterDTO semester)
    {

    }

    public boolean updateModul(ModulDTO modul)
    {

    }

    public boolean deleteModul(ModulDTO modul)
    {

    }

    public HashMap<String, List<String>> getAllSemesterAndModules()
    {
        
    }
    */
}
