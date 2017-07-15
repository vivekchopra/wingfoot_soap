package xmethods.traffic;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;
import com.wingfoot.soap.encoding.*;
import java.io.*;
import java.util.*;

class TrafficView {
     Traffic model;
     Display display;
     public TrafficView (Traffic model, Display display) {
          this.model=model;
	  this.display=display;
     }

     public void displayForm() {

	  Form form = new Form("Traffic Condition");
          TextField tf = new TextField("Hwy No.", null, 20, 0);
	  form.append(tf);
	  form.addCommand(new Command("Submit", Command.OK, 1));
	  form.addCommand(new Command("Exit", Command.EXIT, 1));
	  form.setCommandListener(new TrafficListener(model, tf));
	  display.setCurrent(form);
	       
     } /* displayForm */

     public void displayResult(String result) {
           TextBox t = new TextBox("Traffic Result", 
                                    result, 256, 0);

	   t.addCommand(new Command("Exit", Command.EXIT, 1));
       	   t.setCommandListener(new TrafficListener(model, null));
	   display.setCurrent(t);
     } /* displayResult */

} /* class TemperatureView */

public class Traffic extends MIDlet 
{

        private Display display;
        private TrafficView view;
        public Traffic() {
	        display = Display.getDisplay(this);
        System.err.println("The class is " + new String(" ").getClass().getName());
	}
	public void startApp() {
             view = new TrafficView(this, display);
             view.displayForm();
	}
	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public Envelope makeRequest(String elementSymbol) {
           try {
	     /* Prepare the Envelope*/
	        Envelope requestEnvelope = new Envelope();
	        requestEnvelope.setSchema("http://www.w3.org/1999/XMLSchema");
	        requestEnvelope.setSchemaInstance(
	                       "http://www.w3.org/1999/XMLSchema-instance");
                requestEnvelope.setBody("hwynums", elementSymbol);

	       /* Prepare the call*/
		Call call = new Call(requestEnvelope);
		call.setMethodName("getTraffic");
		call.setTargetObjectURI("urn:xmethods-CATraffic");

	       /* Prepare the transport*/
		HTTPTransport transport = new HTTPTransport (
                       "http://services.xmethods.net:80/soap/servlet/rpcrouter", 
		        null);
                transport.getResponse(true);

	       /* Make the call*/
	        Envelope responseEnvelope = call.invoke(transport);
	        return responseEnvelope;

           } catch (Exception e) {
                 System.err.println(e.getMessage());
		 e.printStackTrace();
		 return null;
	     } 
	} /* prepareRequest*/

	public void parseResponse (Envelope responseEnvelope) {
             if (responseEnvelope != null) {
	          if (responseEnvelope.isFaultGenerated()) {
                      Fault f = responseEnvelope.getFault();
		      view.displayResult("Error: " + f.getFaultString());
		  }
		  else {
		          String condition = responseEnvelope.getParameter(0)+"";
		          if (condition.length() > 200)
		               view.displayResult( condition.substring(0,200));
                          else
			       view.displayResult( condition);
		      }
		  } //else
	} /* responseEnvelope*/

} /* class Chemistry */

class TrafficListener implements CommandListener {

     Traffic weather;
     TextField tf;

     public TrafficListener(Traffic weather, TextField tf) {
          this.weather=weather;
	  this.tf=tf;
     } /* constructor*/

     public void commandAction(Command c, Displayable d)  {

          if (c.getLabel().trim().equals("Submit")) {

               String elementSymbol = tf.getString().trim();
               Envelope responseEnvelope = 
	                 weather.makeRequest(elementSymbol);
               
	       weather.parseResponse(responseEnvelope);
	  } /* if submit */
          else if (c.getLabel().trim().equals("Exit")) {
               weather.destroyApp(false);
	       weather.notifyDestroyed();
  	  }
     } /* CommandAction */ 

} /* class TemperatureListener */		                       
