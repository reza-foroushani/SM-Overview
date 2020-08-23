package de.hskl.smoverview;

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
