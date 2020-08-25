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
<<<<<<< HEAD
        String tabelle="CREATE  TABLE "+
                TABELLE_NAME+
                "("+TABELLE_ID+
                " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                TABELLE_FACHBEREICH+" varchar(30),"+
                TABELLE_BACHELORORMASTER+" varchar(30))";
=======
        Log.d("HSKL", "create table andere");
        String tabelle = "CREATE TABLE " + TABELLE_NAME + "(" +
                TABELLE_ID + "INTEGER PRIMARY KEY AUTOINCREMENT," +
                TABELLE_FACHBEREICH + " VARCHAR(35)," +
                TABELLE_BACHELORORMASTER + " VARCHAR(10)) ";
>>>>>>> 41a946bfb69d909e7fec95cb25c8467a41a3526e

        sqlOncreateDB.execSQL(tabelle);

        // Tabellen mit Grunddaten füllen
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABELLE_NAME);
        onCreate(db);

    }


    // insert in Table
    public boolean insertBachlor(BachelorDTO bachelor, String mORb) {
        SQLiteDatabase db = this.getWritableDatabase();
        //key_Value
        ContentValues neueZeille = new ContentValues();
        neueZeille.put(TABELLE_FACHBEREICH, bachelor.getFachbereich());
        neueZeille.put(TABELLE_BACHELORORMASTER, mORb);
        db.insert(TABELLE_NAME, null, neueZeille);
        return true;
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
       // String SELECT_QUERY = "SELECT * FROM " + DB_NAMEN;
      /*
        Cursor cursor= dbSql.query
                (TABELLE_NAME,new String[]{"id","fachbereich"},
                        "MorB=?",new String[]
                        {"B"},null,null,null);

        if (cursor.moveToFirst()) {
            do {
                String fachName = cursor.getString(cursor.getColumnIndex(TABELLE_FACHBEREICH));
                int fachId = cursor.getInt(cursor.getColumnIndex(TABELLE_ID));
                BachelorDTO bachelor = new BachelorDTO(fachId, fachName);
                fachList.add(bachelor);

            } while (cursor.moveToNext());
        }
        return fachList;

       */

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
    public void remoteFach (int id)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        String source = TABELLE_ID  + "=?";
        String [] sourceAsArray = new String [] {
                Integer.toString(id)
        };
        database.delete(DB_NAMEN,source,sourceAsArray);
    }

}