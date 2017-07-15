package xmethods.temperature;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;
import com.wingfoot.soap.encoding.*;
import java.io.*;
import java.util.*;

class TemperatureView {
     Temperature model;
     Display display;
     public TemperatureView (Temperature model, Display display) {
          this.model=model;
	  this.display=display;
     }

     public void displayForm() {

	  Form form = new Form("Temperature");
          TextField tf = new TextField("Zip Code", null, 5, 0);
	  form.append(tf);
	  form.addCommand(new Command("Submit", Command.OK, 1));
	  form.addCommand(new Command("Exit", Command.EXIT, 1));
	  form.setCommandListener(new TemperatureListener(model, tf));
	  display.setCurrent(form);
	       
     } /* displayForm */

     public void displayResult(String result) {
           TextBox t = new TextBox("Temperature Result", 
                                    result, 256, 0);

	   t.addCommand(new Command("Exit", Command.EXIT, 1));
       	   t.setCommandListener(new TemperatureListener(model, null));
	   display.setCurrent(t);
     } /* displayResult */

} /* class TemperatureView */

public class Temperature extends MIDlet 
{
        private Display display;
        private TemperatureView view;
	//long before, after;
	//Runtime runtime;
        public Temperature() {
	       // runtime = Runtime.getRuntime();
		//System.gc();
		//before = runtime.freeMemory();

	        display = Display.getDisplay(this);
	}

	public void startApp() {
             view = new TemperatureView(this, display);
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
                requestEnvelope.setBody("zipcode", elementSymbol);

	       /* Prepare the call*/
		Call call = new Call(requestEnvelope);
		call.setMethodName("getTemp");
		call.setTargetObjectURI("urn:xmethods-Temperature");

	       /* Prepare the transport*/
		HTTPTransport transport = new HTTPTransport (
                       "http://services.xmethods.net:80/soap/servlet/rpcrouter", 
		        null);
                transport.getResponse(true);

	       /* Make the call*/
	        Envelope responseEnvelope = call.invoke(transport);
	        //after = runtime.freeMemory();
	        //System.err.println("***Memory occupied is " + (before-after));
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
		      view.displayResult(responseEnvelope.getParameter(0)+"");
		  } //if
	     } 
	} /* responseEnvelope*/

} /* class Chemistry */

class TemperatureListener implements CommandListener {

     Temperature weather;
     TextField tf;

     public TemperatureListener(Temperature weather, TextField tf) {
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
