package de.hskl.smoverview.databaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBFachbereich  extends SQLiteOpenHelper {

    // Version und Name von Datenbank und Tabelle
    public static final int DB_VERSION = 1;
    public static final String DB_NAMEN = "Bachelor_db";
    public static final String TABELLE_NAME = "bachelor";

    // Spalten von Tabelle
    public static final String TABELLE_ID = "id";
    public static final String TABELLE_FACHBEREICH = "fachbereich";
    public static final String TABELLE_BACHELORORMASTER = "bORM";


    // Anwendungskontext durch Konstruktur
    public DBFachbereich(Context ctx)
    {
        super(ctx, DB_NAMEN, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlOncreateDB) {
        // alle Tabelle erzeugen
        Log.d("HSKL", "create table andere");
        String tabelle="CREATE  TABLE "+
                TABELLE_NAME+
                "("+TABELLE_ID+
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                TABELLE_FACHBEREICH+" varchar(30),"+
                TABELLE_BACHELORORMASTER+" varchar(30))";




        sqlOncreateDB.execSQL(tabelle);

        // Tabellen mit Grunddaten füllen
    }


    // Datenbank löschen und wieder erstellen
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABELLE_NAME);
        onCreate(db);

    }


    // insert in Table
    public boolean insertBachlor(BachelorDTO bachelor, String mORb) {
        // key-value
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues neueZeille = new ContentValues();

        neueZeille.put(TABELLE_FACHBEREICH, bachelor.getFachbereich());
        neueZeille.put(TABELLE_BACHELORORMASTER, mORb);

        Log.d(DB_NAMEN, "addData: Adding " + bachelor + " to " + TABELLE_NAME);

        long result = db.insert(TABELLE_NAME, null, neueZeille);

        // falls Datensatz vorhanden ist

        if(result == -1)
        {
            return  false;
        } else
        {
            return true;
        }
    }


    // Update Tabelle
    public void updateTabelleBachelor(BachelorDTO bachelor, int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABELLE_FACHBEREICH,bachelor.getFachbereich());
        String where = TABELLE_ID + "?";
        String [] whereAsArray = new String[]
        {
            Integer.toString(id)
        };
        db.update(DB_NAMEN,values,where,whereAsArray);
    }

    // List Bachelor
    public ArrayList getALLFachBachelor() {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<String> fachList = new ArrayList<>();

      Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM " + TABELLE_NAME,null);
      cursor.moveToFirst();
      while (!cursor.isAfterLast())
      {
          fachList.add(cursor.getString(cursor.getColumnIndex(TABELLE_FACHBEREICH)));
          cursor.moveToNext();
      }
      return fachList;
    }


    // call Bachelor Fachbereich by ID
    public BachelorDTO getBachelorById(int id)
    {
        BachelorDTO bachelor = null;
        SQLiteDatabase database = this.getWritableDatabase();

        Cursor cursor = database.query(DB_NAMEN, new String []
                {
                        "id","facchbereich"
                }, "id=?",new String []
                {
                        String.valueOf(id)
                }, null,null,null);

        if(cursor.moveToFirst())
        {
            int idFach = cursor.getInt(cursor.getColumnIndex(TABELLE_ID));
            String fachbereich = cursor.getString(cursor.getColumnIndex(TABELLE_FACHBEREICH));
            bachelor  = new BachelorDTO(idFach,fachbereich);
        }
        return bachelor;
    }


    // Datensatze loeschen
    public boolean delete(int id) {
        boolean result = true;
        try {
            SQLiteDatabase sqLiteDatabase = getWritableDatabase();
            result = sqLiteDatabase.delete(TABELLE_NAME, TABELLE_ID +
                    " = ?", new String[]{ String.valueOf(id) }) > 0;
        } catch (Exception e) {
            result = false;
        }
        return result;
    }



}