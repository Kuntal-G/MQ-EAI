package com.mq.util;

import java.io.Serializable;

public class Employee implements Serializable
{
    private Integer emplId;
    private String firstName;
    private String lastname;
     
    public Employee(Integer emplId, String firstName, String lastname)
    {
        super();
        this.emplId = emplId;
        this.firstName = firstName;
        this.lastname = lastname;
    }
     
    public Integer getEmplId()
    {
        return emplId;
    }
     
    public void setEmplId(Integer emplId)
    {
        this.emplId = emplId;
    }
     
    public String getFirstName()
    {
        return firstName;
    }
     
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
     
    public String getLastname()
    {
        return lastname;
    }
     
    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }
     
    @Override
    public String toString()
    {
        return "Employee [firstName=" + firstName + ", lastname=" + lastname + "]";
    }
     
}