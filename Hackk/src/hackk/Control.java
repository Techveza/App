/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hackk;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

/**
 *
 * @author HEIMDAL13
 */
public class Control {
    protected boolean pir1;
    protected boolean pir2;
    private int gent;
    private long t;
    private long tlast;
    private long p;
    private long plast;
    private long s;
    private long slast;
    private long scount;
    private int count;
    private boolean mustCount;
    final int MAX_RELATIVE = 200;   
    private LinkedList list;
    
    
    
    
    String broker;
    String clientId;
    MqttClient client;
    MqttClient sampleClient;
    MqttConnectOptions connOpts;
    MemoryPersistence persistence;
    int qos;
    public Control(){
    pir1 = false;
    pir2 = false;
    gent = 0;
    list = new LinkedList();
    mustCount = false;
    
    
    
    
      
        int qos             = 0;
        broker       = "tcp://quickstart.messaging.internetofthings.ibmcloud.com:1883";
        clientId     = "a:quickstart:rasp";
        persistence = new MemoryPersistence();

        try {
            sampleClient = new MqttClient(broker, clientId, persistence);
            connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            System.out.println("Connecting to broker: "+broker);
            sampleClient.connect(connOpts);
            System.out.println("Connected");
            
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    }
    public void pir1on(){
    pir1 = true;
    
    }
    
    public void pir2on(){
    pir2 = true;
    }
    
    
    
  
    
    public void piroff(){
    pir1 = false;
    pir2 = false;
    }
    
    public boolean pir1(){
        return pir1;
    
    }
    
    public boolean pir2(){
        return pir2;
    
    }
    
   synchronized public void surt(){
         p = System.currentTimeMillis();
     if(p-plast > 1000)
    gent--;
     plast = p;
         //this.piroff();
}
    
    synchronized public void entra(){
     t = System.currentTimeMillis();
     if(t-tlast > 1000)
    gent++;
     tlast = t;
    
    
     //this.piroff();
    }
    public int gent(){
    return gent;
    }
    
    public void noise(){
     scount++;
         
    }

   public long getSound() {
        return scount; //To change body of generated methods, choose Tools | Templates.
    }
   
  public void inCount(){
   count++;
      System.out.println("count incrementat " + count);
   
   }
  
   public void activCount(){
       
       mustCount = true;
   
   
   }
   
   
   public void desactCount(){
       
       mustCount = false;
   
   
   }
   
   public boolean getCount(){
   
   return mustCount;
   }
  
  
  


    

    float getRelative() {
        int res = 0;
        for(int i = 0; i<list.size(); i++)
        res = res + (int)list.get(i);
        System.out.println("Resultat " + res +" Size " + list.size());
        
        float x = (float)res/(float)list.size();
        x = x*100;
        
        return x; //To change body of generated methods, choose Tools | Templates.
        
    }

    void addOne() {
        System.out.println("Adding one");
        list.addFirst(1);
        if (list.size() > MAX_RELATIVE)
            list.removeLast();
        mustCount = false;
        
    }

    void addZero() {
        System.out.println("adding zero");
        list.addFirst(0);
        if (list.size() > MAX_RELATIVE)
            list.removeLast();
    }
    
    public void send(){
        String topic        = "iot-2/type/iotsample-raspberrypi/id/b827eb25db0e/evt/status/fmt/json";
        String content      = " { \"d\": {\"people\": "+this.gent()+",  \"noise\": "+this.getRelative()+" }}";
             

        try {
            System.out.println("Publishing message: "+content);
            MqttMessage message = new MqttMessage(content.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            System.out.println("Message published");
           
        } catch(MqttException me) {
            System.out.println("reason "+me.getReasonCode());
            System.out.println("msg "+me.getMessage());
            System.out.println("loc "+me.getLocalizedMessage());
            System.out.println("cause "+me.getCause());
            System.out.println("excep "+me);
            me.printStackTrace();
        }
    
    
    }

    void disconect() {
        try {
            sampleClient.disconnect();
        } catch (MqttException ex) {
            Logger.getLogger(Control.class.getName()).log(Level.SEVERE, null, ex);
        }
            System.out.println("Disconnected");
            
    }
}
