package com.mq.util;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
 
public class SimpleEHCache
{
    /**
     * The CacheManager provides us access to individual Cache instances
     */
    private static final CacheManager cacheManager;
     
    static
    {
         
       /* ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        InputStream resourceAsStream = .getResourceAsStream("ehcache.xml");
        File file = new File("./src/ehcache.xml");
        FileReader  = new FileReader(file);*/
        cacheManager = CacheManager.create("src/ehcache.xml");
    }
     
    /**
     * A cache that we're designating to hold Employee instances
     */
    private Ehcache employeeCache;
     
    public SimpleEHCache()
    {
        // Load our employees cache:
        employeeCache = cacheManager.getEhcache("MQCache");
    }
     
    /**
     * Adds a new Employee to the Cache
     */
    public void addEmployee(Integer id, Employee employee)
    {
        // Create an EHCache Element to hold the Employee
        Element element = new Element(id, employee);
         
        // Add the element to the cache
        employeeCache.put(element);
    }
     
    /**
     * Retrieves a Employee from the cache
     */
    public Employee getEmployee(Integer id)
    {
        // Retrieve the element that contains the requested Employee
        Element element = employeeCache.get(id);
        if (element != null)
        {
             
            return (Employee) element.getValue();
        }
         
        return null;
    }
}
