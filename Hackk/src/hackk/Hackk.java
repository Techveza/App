/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hackk;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author HEIMDAL13
 */
public class Hackk {

       private boolean pr1 = false;
       private boolean pr2 = false;
       
    
    
    /**
     * @param args the command line arguments
     */
    
    public synchronized static void main(String[] args) throws InterruptedException {
      
        System.out.println("Començant programa");
        
        Control con = new Control();
        // create gpio controller
        final GpioController gpio = GpioFactory.getInstance();

        // provision gpio pin #02 as an input pin with its internal pull down resistor enabled
        final GpioPinDigitalInput pir1 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_DOWN);
        final GpioPinDigitalInput pir2 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, PinPullResistance.PULL_DOWN);
        final GpioPinDigitalInput mic = gpio.provisionDigitalInputPin(RaspiPin.GPIO_01, PinPullResistance.PULL_DOWN);
        
        new Thread(new Runnable() {

            @Override
            public void run() {
long a ,b;
        a = 0;
        b = 0;
              while(true){
                 
                  try {
                      Thread.sleep(3000);
                  } catch (InterruptedException ex) {
                      Logger.getLogger(Hackk.class.getName()).log(Level.SEVERE, null, ex);
                  }
                  
                   a= con.getSound();
             if (a > b)
                 con.activCount();
             b = a;
             
             if(con.getCount())
                 con.addOne();
             else
                 con.addZero();
             
              }
              
            }
        }).start();
 
        // create and register gpio pin listener
        pir1.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                System.out.println("pir 1 activat");
                 con.entra();
               
               
            }
            
        });
        
        
        
        pir2.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                   System.out.println("pir 2 activat");
                   con.surt();
                   
               
            }
            
        });
        
        mic.addListener(new GpioPinListenerDigital() {
            @Override
            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event) {
                  // System.out.println("mic activat");
                   con.noise();
                   
               
            }
            
        });
        
        
        
        
        
       
        
        // keep program running until user aborts (CTRL-C)
        for (;;) {
            Thread.sleep(9000);
            //System.out.println(con.getRelative());
            //System.out.println(con.gent());
            con.send();
                     
            
        }
        
    
        
        // stop all GPIO activity/threads by shutting down the GPIO controller
        // (this method will forcefully shutdown all GPIO monitoring threads and scheduled tasks)
        // gpio.shutdown();   <--- implement this method call if you wish to terminate the Pi4J GPIO controller        
    }
    
   
    
   
    
}
