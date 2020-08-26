package de.hskl.smoverview.databaseClasses;

import java.util.ArrayList;

//Datenhaltung für Semester
public class SemesterDTO
{
    private long s_id;
    private String semestername;
    private int studiengang_id;
    private ArrayList<ModulDTO> modulListe;

    public SemesterDTO(long s_id, String semestername, int studiengang_id, ArrayList<ModulDTO> modulListe)
    {
        this.s_id = s_id;
        this.semestername = semestername;
        this.studiengang_id = studiengang_id;
        this.modulListe = modulListe;
    }

    public SemesterDTO(long s_id, String semestername, int studiengang_id)
    {
        this.s_id = s_id;
        this.semestername = semestername;
        this.studiengang_id = studiengang_id;
    }

    public SemesterDTO(String semestername, int studiengang_id)
    {
        this.semestername = semestername;
        this.studiengang_id = studiengang_id;
    }

    public long getS_id() { return s_id; }
    public void setS_id(long s_id) { this.s_id = s_id; }

    public String getSemestername() { return semestername; }
    public void setSemestername(String semestername) { this.semestername = semestername; }

    public int getStudiengang_id() { return studiengang_id; }
    public void setStudiengang_id(int studiengang_id) { this.studiengang_id = studiengang_id; }

    public ArrayList<ModulDTO> getModulListe() { return modulListe; }
    public void setModulListe(ArrayList<ModulDTO> modulListe) { this.modulListe = modulListe; }

    @Override
    public String toString() {
        return "SemesterDTO{" +
                "s_id=" + s_id +
                ", semestername='" + semestername + '\'' +
                ", studiengang_id=" + studiengang_id +
                ", modulListe=" + modulListe +
                '}';
    }
}
