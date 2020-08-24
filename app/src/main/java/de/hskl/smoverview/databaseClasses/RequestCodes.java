package de.hskl.smoverview.databaseClasses;

public enum RequestCodes
{
    editModuleSuccess(11);

    private int requestCode;

    RequestCodes(int requestCode)
    {
        this.requestCode = requestCode;
    }

    public int toInt() { return requestCode; }
}
