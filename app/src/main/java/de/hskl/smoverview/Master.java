package de.hskl.smoverview;

public class Master {
    private String FachbereichName;
    private String Beschreichbung;
    int fachbereich_Id;



    public Master(String fachbereichName, String beschreichbung) {
        this.FachbereichName = fachbereichName;
       this.Beschreichbung = beschreichbung;
    }

    public Master(int fachbereich_Id,String fachbereichName, String beschreichbung) {
        this.FachbereichName = fachbereichName;
        this.Beschreichbung = beschreichbung;
        this.fachbereich_Id=fachbereich_Id;
    }
    public int getFachbereich_Id() {
        return fachbereich_Id;
    }

    public void setFachbereich_Id(int fachbereich_Id) {
        this.fachbereich_Id = fachbereich_Id;
    }
    public void setFachbereichName(String fachbereichName) {
        FachbereichName = fachbereichName;
    }

    public void setBeschreichbung(String beschreichbung) {
        Beschreichbung = beschreichbung;
    }

    public String getFachbereichName() {
        return FachbereichName;
    }

    public String getBeschreichbung() {
        return Beschreichbung;
    }


}
