package de.hskl.smoverview.databaseClasses;

public enum RequestCodes
{
    editModuleSuccess(11),
    addBachelorSuccess(10);

    private int requestCode;

    RequestCodes(int requestCode)
    {
        this.requestCode = requestCode;
    }

    public int toInt() { return requestCode; }
}
