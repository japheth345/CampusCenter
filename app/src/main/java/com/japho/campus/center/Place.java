package com.japho.campus.center;

public class Place
{
    private String county;
    private String cons;
    private String ward;
    public Place()
    {
        county = "";
        cons = "";
        ward = "";
    }
    public Place(String county, String cons, String ward)
    {
        this.county=county;
        this.cons=cons;
        this.ward=ward;
    }
    public String getCounty()
    {
        return county;
    }
    public String getCons()
    {
        return cons;
    }
    public String getWard()
    {
        return ward;
    }
}
