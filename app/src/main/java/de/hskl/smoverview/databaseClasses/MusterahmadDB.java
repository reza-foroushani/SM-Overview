package de.hskl.smoverview.databaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import de.hskl.smoverview.databaseClasses.MasterDTO;

public class MusterahmadDB extends SQLiteOpenHelper {
    public static final int DATENBANK_VERSION =1;
    public static final String TABELlE_FACHBEREiCH ="FachbereichTabelle";
    public static final String FACHBERECIH_ID ="id";
    public static final String DATENBANK_NAMEN ="SM_Overview.db";
    public static final String FACHBERECIH_NAMEN ="Fachberecih";
    public static final String FACHBERECIH_BESCHREICHBUNG ="Beschrechbung";
    public static final String MASTER_OR_BACHLER ="MorB";



    public MusterahmadDB(@Nullable Context context) {
        super(context, DATENBANK_NAMEN, null, DATENBANK_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String Create_Table="CREATE  TABLE "+TABELlE_FACHBEREiCH+"("+FACHBERECIH_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FACHBERECIH_NAMEN+" varchar(30),"+FACHBERECIH_BESCHREICHBUNG+" varchar(30),"+MASTER_OR_BACHLER+" varchar(30))";
        db.execSQL(Create_Table);
        //TODO Eduard tabele
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF  EXISTS " +TABELlE_FACHBEREiCH);

        //TODO Eduard tabele


        onCreate(db);

    }
    public void addFachberecihMaster(MasterDTO master, String m_or_b ){
        // copy von db für schreicben
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values= new ContentValues();
        //ich lege fachbereich in spalte FACHBERECIH_NAMEN
        values.put(FACHBERECIH_NAMEN,master.getFachbereichName());
        //ich lege Beschreibung daten in spalte FACHBERECIH_BESCHREICHBUNG
        values.put(FACHBERECIH_BESCHREICHBUNG,master.getBeschreichbung());
        //ich lege , was ich gebe master oder bachler ist,  in spalte MASTER_OR_BACHLER
          values.put(MASTER_OR_BACHLER,m_or_b);
        db.insert(TABELlE_FACHBEREiCH,null,values);


    }
    public ArrayList<MasterDTO> getAllFachBereicheMaster (){
        ArrayList<MasterDTO>   fachbereichHilfe = new ArrayList<>();
        // er nimmt all daten
        //String SELECT_query ="select * from " +TABELlE_FACHBEREiCH+"WHERE"+MASTER_OR_BACHLER+ "="+ "M";
        // copy von db für lesen
        SQLiteDatabase db =this.getReadableDatabase();
        //ohne expression deswegen null ,wir haben ergebniss von select-quere genomen
        Cursor cursor= db.query(TABELlE_FACHBEREiCH,new String[]{"id","Fachberecih","Beschrechbung"},"MorB=?",new String[]{"M"},null,null,null);
        //wenn true dann erste zeihle hat daten
        if(cursor.moveToFirst()){
            do {
                //cursor.getColumnIndex er sucht index von spalte
                String fachberichName=cursor.getString(cursor.getColumnIndex(FACHBERECIH_NAMEN));
                String beschreibung =cursor.getString(cursor.getColumnIndex(FACHBERECIH_BESCHREICHBUNG));
                //ich brauche  inhalb jedes item  seine id  speichern
                int fachbereich_ID =cursor.getInt(cursor.getColumnIndex(FACHBERECIH_ID));
                MasterDTO master = new MasterDTO(fachbereich_ID,fachberichName,beschreibung);
                fachbereichHilfe.add(master);
            }while (cursor.moveToNext());
        }
        return  fachbereichHilfe;

    }
    public MasterDTO getmasterbyId(int id){
        MasterDTO master=null ;
        SQLiteDatabase db  = this.getWritableDatabase();
        /* query() führtr eine SQL-anfrage aus, als Parameter wedne die Bestandteil der Anfrage übergeben
        zweite paramiter wehche spalte
        die dritte parameter ist besunders für where , also selection
        vierte parameter id einsetzen , selectionaArg
        */
        Cursor cursor = db.query(TABELlE_FACHBEREiCH,new String[]{"id","Fachberecih","Beschrechbung"},"id=?",new String[]{String.valueOf(id)},null,null,null);
        if(cursor.moveToFirst()){
            int id_bereich =cursor.getInt(cursor.getColumnIndex(FACHBERECIH_ID));
            String   fachbereichName = cursor.getString(cursor.getColumnIndex(FACHBERECIH_NAMEN));
            String   beschreibung = cursor.getString(cursor.getColumnIndex(FACHBERECIH_BESCHREICHBUNG));
            master=new MasterDTO(id_bereich,fachbereichName,beschreibung);
        }
        return master;
    }
    public  void updatMaster(MasterDTO master , int id){
        SQLiteDatabase db =this.getWritableDatabase();
        ContentValues values=  new ContentValues();
        values.put(FACHBERECIH_NAMEN,master.getFachbereichName());
        values.put(FACHBERECIH_BESCHREICHBUNG,master.getBeschreichbung());
        //spaltennamen ,wehlche durchsuche werden soll , um datenstaz zu finden
        String where = FACHBERECIH_ID +"=?";
        //werte des spaltennamens
        String[] whereArg = new String[]{Integer.toString(id)};
        db.update(TABELlE_FACHBEREiCH,values,where,whereArg);

//db.update(TABELlE_FACHBEREiCH,values, "id=?",new String[]{String.valueOf(master.getFachbereich_Id())});
    }
    public void deleteFachbereich(int Id){
        SQLiteDatabase  db= this.getWritableDatabase();
        String where = FACHBERECIH_ID + "=?";
        String[] whereArg = new String[]{Integer.toString(Id)};
        db.delete(TABELlE_FACHBEREiCH,where,whereArg);
    }

    /*public  Cursor getmasterbyIdCursor(int id){
        SQLiteDatabase db  = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM "+TABELlE_FACHBEREiCH+"WHERE"+FACHBERECIH_ID+"="+id,null);
        cursor.moveToFirst();

        return cursor;
    }*/
   /* public Cursor gettAllMaster(){
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor=db.rawQuery(" SELECT * FROM "+ TABELlE_FACHBEREiCH,null);
        cursor.moveToFirst();
        return cursor;
    }*/
  /* public  Master getmasterbyId2(int id){
       Master master=null ;
       SQLiteDatabase db  = this.getWritableDatabase();
       String  select_query ="SELECT * FROM "+TABELlE_FACHBEREiCH+"WHERE id ="+id;
       Cursor cursor = db.rawQuery(select_query,null);
       // wenn ein
       if(cursor.moveToFirst()){
           int id_bereich =cursor.getInt(cursor.getColumnIndex(FACHBERECIH_ID));
           String   fachbereichName = cursor.getString(cursor.getColumnIndex(FACHBERECIH_NAMEN));
           String   beschreibung = cursor.getString(cursor.getColumnIndex(FACHBERECIH_BESCHREICHBUNG));
           master=new Master(id_bereich,fachbereichName,beschreibung);
       }
       return master;
   }*/
}
