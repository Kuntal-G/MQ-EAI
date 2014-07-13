/*package com.mq.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
 
public class SimpleEHCacheTest
{
    private SimpleEHCache simpleEHCacheExample;
     
    @Before
    public void setUp()
    {
        // Create a sample SimpleEHCacheExample object
        simpleEHCacheExample = new SimpleEHCache();
         
        // Add a few employees
        simpleEHCacheExample.addEmployee(1, new Employee(1, "John", "Sander"));
        simpleEHCacheExample.addEmployee(2, new Employee(2, "Peter", "Sander"));
        simpleEHCacheExample.addEmployee(3, new Employee(3, "Emma", "Sander"));
    }
     
    @Test
    public void testRetrieve()
    {
        Employee employee = simpleEHCacheExample.getEmployee(1);
        Assert.assertNotNull("The employee was not found in the cache", employee);
        Assert.assertEquals("Did not find the correct employee", "John", employee.getFirstName());
    }
     
    @Test
    public void testRetrieveNotFound()
    {
        Employee employee = simpleEHCacheExample.getEmployee(5);
        Assert.assertNull("I did not expect find the employee in the cache but it was there", employee);
    }  
}
*/