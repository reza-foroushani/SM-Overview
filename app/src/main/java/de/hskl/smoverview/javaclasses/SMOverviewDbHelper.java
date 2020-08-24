package de.hskl.smoverview.javaclasses;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

//Erstellen der Datenbank
public class SMOverviewDbHelper extends SQLiteOpenHelper
{
    //Allgemein
    public static final String DB_NAME = "smoverview.db";
    public static final int DB_VERSION = 1;

    //Modul
    public static final String TABLE_MODUL = "Modul";
    public static final String COLUMN_MODULID = "m_id";
    public static final String COLUMN_MODULNAME = "modulname";
    public static final String COLUMN_MODULBESCHREIBUNG = "modulbeschreibung";
    public static final String COLUMN_SID = "s_id";

    public static final String SQL_CREATEMODULTABLE =
            "CREATE TABLE " + TABLE_MODUL +
                    "( " + COLUMN_MODULID + " INTEGER PRIMARY KEY, " +
                    COLUMN_MODULNAME + " TEXT NOT NULL, " +
                    COLUMN_MODULBESCHREIBUNG + " TEXT);";

    public static final String SQL_CREATE = "Create TABLE MODUL ();";
    //Modul End

    //Semester
    public static final String TABLE_SEMESTER = "Semester";
    public static final String COLUMN_SEMESTERID = "s_id";
    public static final String COLUMN_SEMESTERNAME = "semestername";
    public static final String COLUMN_MID = "m_id";

    public static final String SQL_CREATESEMESTERTABLE =
            "CREATE TABLE " + TABLE_SEMESTER +
                    "( " + COLUMN_SEMESTERID + " INTEGER PRIMARY KEY, " +
                    COLUMN_SEMESTERNAME + " TEXT NOT NULL, " +
                    COLUMN_MID + " INTEGER NOT NULL);";
    //Semester End

    public SMOverviewDbHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d("HSKL", "DBHelper Datenbankname: " + getDatabaseName());
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            Log.d("HSKL", "createTable");
           // db.execSQL(SQL_CREATESEMESTERTABLE);
            //db.execSQL(SQL_CREATEMODULTABLE);
            db.execSQL(SQL_CREATE);
        }
        catch(Exception ex)
        {
            Log.e("HSKL", "Fehler beim Anlegen der Tabelle: " + ex.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        Log.d("HSKL", "onUpgrade TAble");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODUL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEMESTER);

        onCreate(db);
    }
}

