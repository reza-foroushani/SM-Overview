package de.hskl.smoverview.databaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public static final String SQL_CREATEBACHELORTABLE ="CREATE  TABLE "+
            TABELLE_NAME+
            "("+TABELLE_ID+
            " INTEGER PRIMARY KEY AUTOINCREMENT, "+
            TABELLE_FACHBEREICH+" varchar(30),"+
            TABELLE_BACHELORORMASTER+" varchar(30))";

    // Anwendungskontext durch Konstruktur
    public DBFachbereich(Context ctx)
    {
        super(ctx, DB_NAMEN, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlOncreateDB) {
        // alle Tabelle erzeugen
        sqlOncreateDB.execSQL(SQL_CREATEBACHELORTABLE);
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
        SQLiteDatabase db = this.getWritableDatabase();
        //key_Value
        ContentValues neueZeille = new ContentValues();
        neueZeille.put(TABELLE_FACHBEREICH, bachelor.getFachbereich());
        neueZeille.put(TABELLE_BACHELORORMASTER, mORb);
        boolean success = db.insert(TABELLE_NAME, null, neueZeille) > 0;
        return success;
    }

    // List Bachelor
    public ArrayList<BachelorDTO> getALLFachBachelor() {
        ArrayList<BachelorDTO> fachbereichHilfe = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABELLE_NAME,new String[]{"id","fachbereich","bORM"},"bORM=?",new String[]{"B"},null,null,null);
        if(cursor.moveToFirst()){
            do {
                String fachberichName=cursor.getString(cursor.getColumnIndex(TABELLE_FACHBEREICH));
                String beschreibung =cursor.getString(cursor.getColumnIndex(TABELLE_BACHELORORMASTER));
                int fachbereich_ID =cursor.getInt(cursor.getColumnIndex(TABELLE_ID));
                BachelorDTO bachelor = new BachelorDTO(fachbereich_ID,fachberichName,beschreibung);
                fachbereichHilfe.add(bachelor);
            }while (cursor.moveToNext());
        }
        return fachbereichHilfe;
    }

    // Datensatze loeschen
    public boolean delete(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = TABELLE_ID + "=?";
        String[] whereArg = new String[]{String.valueOf(id)};
        boolean success = db.delete(TABELLE_NAME, where, whereArg) > 0;
        return success;
    }

    public boolean updateBachelor(BachelorDTO bachelor)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TABELLE_FACHBEREICH,bachelor.getFachbereich());
        boolean success = db.update(TABELLE_NAME,values,TABELLE_ID + " = ?",
                new String[]{ String.valueOf(bachelor.getId())}) > 0;
        return success;
    }

    public ArrayList<BachelorDTO> sucheBereicheBachlor (String wort){
        ArrayList<BachelorDTO>   fachbereichHilfe = new ArrayList<>();
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor=  db.rawQuery("select * from bachelor where fachbereich like '"+wort+"%' and bORM ='B' ",null);

        if(cursor.moveToFirst()){
            do {
                String fachberichName=cursor.getString(cursor.getColumnIndex(TABELLE_FACHBEREICH));
                int fachbereich_ID =cursor.getInt(cursor.getColumnIndex(TABELLE_ID));
                BachelorDTO Bachlor = new BachelorDTO(fachbereich_ID,fachberichName);
                fachbereichHilfe.add(Bachlor);
            }while (cursor.moveToNext());
        }
        return  fachbereichHilfe;
    }
}