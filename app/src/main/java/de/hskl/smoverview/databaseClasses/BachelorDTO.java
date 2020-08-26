package de.hskl.smoverview.databaseClasses;

public class BachelorDTO
{
    private int id;
    private String fachbereich;
    private String morb;

    public BachelorDTO(int id, String fachbereich, String morb)
    {
        this.id = id;
        this.fachbereich = fachbereich;
        this.morb = morb;
    }

    public BachelorDTO(int id, String fachbereich)
    {
        this.id = id;
        this.fachbereich = fachbereich;
    }

    public BachelorDTO(String fachbereich)
    {
        this.fachbereich = fachbereich;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getFachbereich() {
        return fachbereich;
    }
    public void setFachbereich(String fachbereich) {
        this.fachbereich = fachbereich;
    }

    public String getMorb() {
        return morb;
    }
    public void setMorb(String morb) {
        this.morb = morb;
    }
}
