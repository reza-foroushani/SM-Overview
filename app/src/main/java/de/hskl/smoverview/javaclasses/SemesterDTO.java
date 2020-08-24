package de.hskl.smoverview.javaclasses;

import java.util.ArrayList;

//Datenhaltung für Semester
public class SemesterDTO
{
    private long s_id;
    private String semestername;
    private ArrayList<ModulDTO> modulListe;

    public SemesterDTO(long s_id, String semestername, ArrayList<ModulDTO> modulListe)
    {
        this.s_id = s_id;
        this.semestername = semestername;
        this.modulListe = modulListe;
    }

    public SemesterDTO(long s_id, String semestername)
    {
        this.s_id = s_id;
        this.semestername = semestername;
    }

    public long getS_id() { return s_id; }
    public void setS_id(long s_id) { this.s_id = s_id; }

    public String getSemestername() { return semestername; }
    public void setSemestername(String semestername) { this.semestername = semestername; }

    @Override
    public String toString() {
        return "SemesterDTO{" +
                "s_id=" + s_id +
                ", semestername='" + semestername +
                '}';
    }
}
