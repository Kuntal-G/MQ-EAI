package com.mq.util;

import com.mq.ehcache.MQCache;

public class EvictElement extends Thread{

 
    @Override
    public void run() {
        
        boolean run = true;
        while (run = true) {
           
            try {
                sleep(Integer.valueOf(15000));
                MQCache.instance().expireCache();
               
            } catch (InterruptedException ex) {
                 run = false;
            }
        }
       
    }
}
