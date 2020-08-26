package de.hskl.smoverview.databaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBFachbereich  extends SQLiteOpenHelper {

    public static final int DATENBANK_VERSION = 1;
    public static final String DATENBANK_NAMEN ="SM_Overview.db";
    public static final String TABELLE_NAME ="FachbereichTabelle";


    //Fachbereich
    public static final String FACHBERECIH_ID ="id";
    public static final String FACHBERECIH_NAMEN ="Fachberecih";
    public static final String FACHBERECIH_BESCHREICHBUNG ="Beschrechbung";
    public static final String MASTER_OR_BACHLER ="MorB";

    public static final String SQL_CREATEBACHELORTABLE = "CREATE  TABLE "+ TABELLE_NAME +
            "("+FACHBERECIH_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            FACHBERECIH_NAMEN+" varchar(30),"+FACHBERECIH_BESCHREICHBUNG+
            " varchar(30),"+MASTER_OR_BACHLER+" varchar(30))";

    // Anwendungskontext durch Konstruktur
    public DBFachbereich(Context ctx)
    {
        super(ctx, DATENBANK_NAMEN, null, DATENBANK_VERSION);
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
        neueZeille.put(FACHBERECIH_NAMEN, bachelor.getFachbereich());
        neueZeille.put(MASTER_OR_BACHLER, mORb);
        boolean success = db.insert(TABELLE_NAME, null, neueZeille) > 0;
        return success;
    }

    // List Bachelor
    public ArrayList<BachelorDTO> getALLFachBachelor() {
        ArrayList<BachelorDTO> fachbereichHilfe = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABELLE_NAME,new String[]{"id","Fachberecih","MorB"},"MorB=?",new String[]{"B"},null,null,null);
        if(cursor.moveToFirst()){
            do {
                String fachberichName=cursor.getString(cursor.getColumnIndex(FACHBERECIH_NAMEN));
                String beschreibung =cursor.getString(cursor.getColumnIndex(MASTER_OR_BACHLER));
                int fachbereich_ID =cursor.getInt(cursor.getColumnIndex(FACHBERECIH_ID));
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
        String where = FACHBERECIH_ID + "=?";
        String[] whereArg = new String[]{String.valueOf(id)};
        boolean success = db.delete(TABELLE_NAME, where, whereArg) > 0;
        return success;
    }

    public boolean updateBachelor(BachelorDTO bachelor)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FACHBERECIH_NAMEN,bachelor.getFachbereich());
        boolean success = db.update(TABELLE_NAME,values,FACHBERECIH_ID + " = ?",
                new String[]{ String.valueOf(bachelor.getId())}) > 0;
        return success;
    }

    public ArrayList<BachelorDTO> sucheBereicheBachlor (String wort){
        ArrayList<BachelorDTO>   fachbereichHilfe = new ArrayList<>();
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor=  db.rawQuery("select * from FachbereichTabelle where Fachberecih like '"+wort+"%' and MorB ='B' ",null);

        if(cursor.moveToFirst()){
            do {
                String fachberichName=cursor.getString(cursor.getColumnIndex(FACHBERECIH_NAMEN));
                int fachbereich_ID =cursor.getInt(cursor.getColumnIndex(FACHBERECIH_ID));
                BachelorDTO Bachlor = new BachelorDTO(fachbereich_ID,fachberichName);
                fachbereichHilfe.add(Bachlor);
            }while (cursor.moveToNext());
        }
        return  fachbereichHilfe;
    }
}