package de.hskl.smoverview.databaseClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MusterahmadDB extends SQLiteOpenHelper
{
    public static final int DATENBANK_VERSION = 1;
    public static final String DATENBANK_NAMEN ="SM_Overview.db";

    //Fachbereich
    public static final String TABELlE_FACHBEREiCH ="FachbereichTabelle";
    public static final String FACHBERECIH_ID ="id";
    public static final String FACHBERECIH_NAMEN ="Fachberecih";
    public static final String FACHBERECIH_BESCHREICHBUNG ="Beschrechbung";
    public static final String MASTER_OR_BACHLER ="MorB";
    public static final String Create_Table = "CREATE  TABLE "+TABELlE_FACHBEREiCH+"("+FACHBERECIH_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+FACHBERECIH_NAMEN+" varchar(30),"+FACHBERECIH_BESCHREICHBUNG+" varchar(30),"+MASTER_OR_BACHLER+" varchar(30))";
    //Fachbereich Ende

    //Modul
    public static final String TABLE_MODUL = "Modul";
    public static final String COLUMN_MODULID = "m_id";
    public static final String COLUMN_MODULNAME = "modulname";
    public static final String COLUMN_MODULBESCHREIBUNG = "modulbeschreibung";
    public static final String COLUMN_SEMESTERIDFK = "s_id";
    public static final String COLUMN_STUDIENGANGIDFK = "studiengang_id";

    public static final String SQL_CREATEMODULTABLE =
            "CREATE TABLE " + TABLE_MODUL +
                    "( " + COLUMN_MODULID + " INTEGER PRIMARY KEY, " +
                    COLUMN_MODULNAME + " TEXT NOT NULL, " +
                    COLUMN_MODULBESCHREIBUNG + " TEXT NOT NULL, " +
                    COLUMN_SEMESTERIDFK + " INTEGER NOT NULL, " +
                    COLUMN_STUDIENGANGIDFK + " INTEGER NOT NULL);";
    //Modul End

    //Semester
    public static final String TABLE_SEMESTER = "Semester";
    public static final String COLUMN_SEMESTERID = "s_id";
    public static final String COLUMN_SEMESTERNAME = "semestername";
    public static final String COLUMN_STUDIENGANGID = "studiengang_id";

    public static final String SQL_CREATESEMESTERTABLE =
            "CREATE TABLE " + TABLE_SEMESTER +
                    "( " + COLUMN_SEMESTERID + " INTEGER PRIMARY KEY, " +
                    COLUMN_SEMESTERNAME + " TEXT NOT NULL, " +
                    COLUMN_STUDIENGANGID + " INTEGER NOT NULL);";
    //Semester End

    public MusterahmadDB(@Nullable Context context) {
        super(context, DATENBANK_NAMEN, null, DATENBANK_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("HSKL", "create table");
        db.execSQL(Create_Table);
        db.execSQL(SQL_CREATESEMESTERTABLE);
        db.execSQL(SQL_CREATEMODULTABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELlE_FACHBEREiCH);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SEMESTER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MODUL);
        onCreate(db);
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public boolean addSemester(SemesterDTO semester)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_SEMESTERNAME, semester.getSemestername());
        values.put(COLUMN_STUDIENGANGID, semester.getStudiengang_id());

        boolean success = db.insert(TABLE_SEMESTER, null, values) > 0;
        return success;
    }

    public boolean deleteSemester(long semesterid){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_SEMESTERID + "=?";
        String[] whereArg = new String[]{String.valueOf(semesterid)};
        boolean success = db.delete(TABLE_SEMESTER, where, whereArg) > 0;
        success = db.delete(TABLE_MODUL, where, whereArg) > 0;
        return success;
    }

    public boolean deleteSemesterFromFachbereich(long studiengangId){
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_STUDIENGANGIDFK + "=?";
        String[] whereArg = new String[]{String.valueOf(studiengangId)};
        boolean success = db.delete(TABLE_MODUL, where, whereArg) > 0;
        success = db.delete(TABLE_SEMESTER, where, whereArg) > 0;
        return success;
    }

    public boolean addModul(ModulDTO modul)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_MODULNAME, modul.getModulname());
        values.put(COLUMN_MODULBESCHREIBUNG, modul.getModulbeschreibung());
        values.put(COLUMN_SEMESTERIDFK, modul.getS_id());
        values.put(COLUMN_STUDIENGANGIDFK, modul.getStudiengang_id());

        boolean success = db.insert(TABLE_MODUL, null, values) > 0;
        return success;
    }

    public boolean deleteModul(long s_id, String modulname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String where = COLUMN_SEMESTERIDFK + "=? AND " + COLUMN_MODULNAME + "=?";
        String[] whereArg = new String[]{String.valueOf(s_id), modulname};
        boolean success = db.delete(TABLE_MODUL, where, whereArg) > 0;
        return success;
    }

    public boolean updateModul(ModulDTO modul, long modulid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MODULNAME, modul.getModulname());
        values.put(COLUMN_MODULBESCHREIBUNG, modul.getModulbeschreibung());
        String where = COLUMN_MODULID + "=?";
        String[] whereArg = new String[]{String.valueOf(modulid)};
        boolean success = db.update(TABLE_MODUL,values,where,whereArg) > 0;
        return success;
    }

    public ModulDTO getModul(String mname, long s_id)
    {
        ModulDTO modul;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_MODUL,
                new String[]{"m_id","modulname","modulbeschreibung","s_id", "studiengang_id"}
                ,"s_id=? AND modulname=?",new String[]{String.valueOf(s_id), mname}
                ,null,null,null);

        if(cursor.moveToFirst())
        {
            long modulid = cursor.getLong(cursor.getColumnIndex(COLUMN_MODULID));
            String modulname = cursor.getString(cursor.getColumnIndex(COLUMN_MODULNAME));
            String modulbeschreibung = cursor.getString(cursor.getColumnIndex(COLUMN_MODULBESCHREIBUNG));
            long semesterid = cursor.getLong(cursor.getColumnIndex(COLUMN_SEMESTERIDFK));
            int studiengangid = cursor.getInt(cursor.getColumnIndex(COLUMN_STUDIENGANGIDFK));
            modul = new ModulDTO(modulid, modulname, modulbeschreibung, semesterid, studiengangid);
            return modul;
        }
        return null;
    }

    public SemesterDTO getSemester(String semestername, int studiengangId)
    {
        SemesterDTO semester;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_SEMESTER,
                new String[]{"s_id","semestername","studiengang_id"}
                ,"studiengang_id=? AND semestername=?"
                ,new String[]{String.valueOf(studiengangId), semestername}
                ,null,null,null);

        if(cursor.moveToFirst())
        {
            long semesterId = cursor.getLong(cursor.getColumnIndex(COLUMN_SEMESTERID));
            String semesterName = cursor.getString(cursor.getColumnIndex(COLUMN_SEMESTERNAME));
            int sid = cursor.getInt(cursor.getColumnIndex(COLUMN_STUDIENGANGID));
            semester = new SemesterDTO(semesterId, semesterName, sid);
            return semester;
        }
        return null;
    }

    public ArrayList<SemesterDTO> getAllSemesterForStudiengang(int studiengang_id)
    {
        ArrayList<SemesterDTO> semesterList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_SEMESTER,
                new String[]{"s_id","semestername","studiengang_id"}
                ,"studiengang_id=?",new String[]{String.valueOf(studiengang_id)},null,null,null);

        if(cursor.moveToFirst()){
            do {
                long semesterId = cursor.getLong(cursor.getColumnIndex(COLUMN_SEMESTERID));
                String semesterName = cursor.getString(cursor.getColumnIndex(COLUMN_SEMESTERNAME));
                int studiengangId = cursor.getInt(cursor.getColumnIndex(COLUMN_STUDIENGANGID));
                SemesterDTO semester = new SemesterDTO(semesterId, semesterName, studiengangId);
                semesterList.add(semester);
            }while (cursor.moveToNext());
        }
        return semesterList;
    }

    public ArrayList<ModulDTO> getModulesForSemester(long s_id)
    {
        ArrayList<ModulDTO> modules = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor= db.query(TABLE_MODUL,
                new String[]{"m_id","modulname","modulbeschreibung","s_id", "studiengang_id"}
                ,"s_id=?",new String[]{String.valueOf(s_id)},null,null,null);

        if(cursor.moveToFirst()){
            do {
                long modulid = cursor.getLong(cursor.getColumnIndex(COLUMN_MODULID));
                String modulname = cursor.getString(cursor.getColumnIndex(COLUMN_MODULNAME));
                String modulbeschreibung = cursor.getString(cursor.getColumnIndex(COLUMN_MODULBESCHREIBUNG));
                long semesterid = cursor.getLong(cursor.getColumnIndex(COLUMN_SEMESTERIDFK));
                int studiengangid = cursor.getInt(cursor.getColumnIndex(COLUMN_STUDIENGANGIDFK));
                ModulDTO modul = new ModulDTO(modulid, modulname, modulbeschreibung, semesterid, studiengangid);
                modules.add(modul);
            }while (cursor.moveToNext());
        }
        return modules;
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
    public void updatMaster(MasterDTO master , int id){
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

        deleteSemesterFromFachbereich(Id);
    }

    public ArrayList<MasterDTO> sucheBereicheMaster (String wort){
        ArrayList<MasterDTO>   fachbereichHilfe = new ArrayList<>();
        String M="M";
        // er nimmt all daten
        //String SELECT_query ="select * from " +TABELlE_FACHBEREiCH+"WHERE"+MASTER_OR_BACHLER+ "="+ "M";
        // copy von db für lesen
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor=  db.rawQuery("select * from FachbereichTabelle where Fachberecih like '"+wort+"%' and MorB ='M' ",null);
        //ohne expression deswegen null ,wir haben ergebniss von select-quere genomen
       // Cursor cursor= db.query(TABELlE_FACHBEREiCH,new String[]{"id","Fachberecih","Beschrechbung"},"MorB=? and Fachberecih=?",new String[]{"M", '%"+wort+"%'},null,null,null);
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
    public Boolean PruefBereicheMaster (String wort){

        // er nimmt all daten
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor=  db.rawQuery("select * from FachbereichTabelle where Fachberecih like '%"+wort+"%' and MorB ='M' ",null);
        if(cursor.moveToFirst()){
                //cursor.getColumnIndex er sucht index von spalte
                String fachberichName=cursor.getString(cursor.getColumnIndex(FACHBERECIH_NAMEN));
                String beschreibung =cursor.getString(cursor.getColumnIndex(FACHBERECIH_BESCHREICHBUNG));
                //ich brauche  inhalb jedes item  seine id  speichern
                int fachbereich_ID =cursor.getInt(cursor.getColumnIndex(FACHBERECIH_ID));
               if(wort.equals(fachberichName)){
                   return false;
               }else {
                   return true  ;
               }
        }else {
            return true  ;
        }
    }
}
