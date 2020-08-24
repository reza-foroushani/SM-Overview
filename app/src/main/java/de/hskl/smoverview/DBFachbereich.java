package de.hskl.smoverview;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBFachbereich  extends SQLiteOpenHelper {

    // Version und Name von Datenbank
    public static final int DB_VERSION = 1;
    public static final String DB_NAMEN = "Bachelor";

    // Tabelle
    public static final String TABELLE_NAME = "bachelor";
    public static final String TABELLE_ID = "id";
    public static final String TABELLE_FACHBEREICH = "fachbereich";
    public static final String TABELLE_BACHELORORMASTER = "bORM";


    // Anwendungskontext durch Konstruktur
    public DBFachbereich(Context ctx) {
        super(ctx, DB_NAMEN, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlOncreateDB) {
        // alle Tabelle erzeugen
        String tabelle = "CREATE TABLE " + TABELLE_NAME + "(" +
                TABELLE_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABELLE_FACHBEREICH + " VARCHAR(35)," +
                TABELLE_BACHELORORMASTER + " VARCHAR(10)) ";

        sqlOncreateDB.execSQL(tabelle);

        // Tabellen mit Grunddaten füllen
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        /*
        Datensätze parken
        Tabellen löschen
        onCreate Methode aufrufen
        geparkte Datensätze neu einspielen
         */

        onCreate(db);

    }


    // insert in Table
    public void insertBachlor(Bachelor bachelor,String mORb) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues neueZeille = new ContentValues();
        neueZeille.put(TABELLE_FACHBEREICH, bachelor.getFachbereich());
        neueZeille.put(TABELLE_BACHELORORMASTER, bachelor.getMorb());

        db.insert(DB_NAMEN, null, neueZeille);
    }


    // Update Tabelle
    public void updateTabelleBachelor(Bachelor bachelor, int id)
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


    public ArrayList<Bachelor> getALLFachBachelor() {
        ArrayList<Bachelor> fachList = new ArrayList<>();
        String SELECT_QUERY = "SELECT * FROM " + DB_NAMEN;

        SQLiteDatabase dbSql = this.getReadableDatabase();
        Cursor cursor = dbSql.rawQuery(SELECT_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                String fachName = cursor.getString(cursor.getColumnIndex(TABELLE_FACHBEREICH));
                int fachId = cursor.getInt(cursor.getColumnIndex(TABELLE_ID));
                Bachelor bachelor = new Bachelor(fachId, fachName);
                fachList.add(bachelor);

            } while (cursor.moveToNext());
        }
        return fachList;
    }

    public Bachelor getBachelorById(int id)
    {
        Bachelor bachelor = null;
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

            bachelor  = new Bachelor(idFach,fachbereich);
        }

        return bachelor;
    }




}