package de.hskl.smoverview.javaclasses;

//Datenhaltung für Module
public class ModulDTO
{
    private long m_id;
    private String modulname;
    private String modulbeschreibung;
    private long s_id;

    public ModulDTO(long m_id, String modulname, String modulbeschreibung, long s_id)
    {
        this.m_id = m_id;
        this.modulname = modulname;
        this.modulbeschreibung = modulbeschreibung;
        this.s_id = s_id;
    }

    public long getM_id() { return m_id; }
    public void setM_id(long m_id) { this.m_id = m_id; }

    public String getModulname() { return modulname; }
    public void setModulname(String modulname) { this.modulname = modulname; }

    public String getModulbeschreibung() { return modulbeschreibung; }
    public void setModulbeschreibung(String modulbeschreibung) { this.modulbeschreibung = modulbeschreibung; }

    public long getS_id() { return s_id; }
    public void setS_id(long s_id) { this.s_id = s_id; }

    @Override
    public String toString() {
        return "ModulDTO{" +
                "m_id=" + m_id +
                ", modulname='" + modulname + '\'' +
                ", modulbeschreibung='" + modulbeschreibung + '\'' +
                ", s_id=" + s_id +
                '}';
    }
}
