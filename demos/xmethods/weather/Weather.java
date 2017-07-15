/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
*/


package xmethods.weather;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;
import com.wingfoot.soap.encoding.*;
import java.io.*;
import java.util.*;

class View {
     Weather model;
     Display display;
     public View (Weather model, Display display) {
          this.model=model;
	  this.display=display;
     }

     public void displayForm() {
	  /* 
           * Airport codes: KSFO, LFPG 
	   * Additional airport codes can be retrieved from
	   * http://www.ar-group.com/icaoiata.htm 
	   * Please provide the ICAO Code
	   */

	  Form form = new Form("Weather");
          TextField tf = new TextField("ICAO Airport Code", null, 4, 0);
	  form.append(tf);
	  form.addCommand(new Command("Submit", Command.OK, 1));
	  form.addCommand(new Command("Exit", Command.EXIT, 1));
	  form.setCommandListener(new WeatherListener(model, tf));
	  display.setCurrent(form);
	       
     } /* displayForm */

     public void displayResult(String result) {
           TextBox t = new TextBox("Weather Result", 
                                    result, 256, 0);

	   t.addCommand(new Command("Exit", Command.EXIT, 1));
       	   t.setCommandListener(new WeatherListener(model, null));
	   display.setCurrent(t);
     } /* displayResult */

} /* class View */

public class Weather extends MIDlet 
{
        private Display display;
        private View view;
        public Weather() {
	        display = Display.getDisplay(this);
	}

	public void startApp() {
             view = new View(this, display);
             view.displayForm();
	}
	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public Envelope makeRequest(String airportCode) {
           try {

	       /* Prepare the call*/
		Call call = new Call();
                call.addParameter("arg0", airportCode);
		call.setMethodName("getTemperature");
		call.setTargetObjectURI("http://www.capeclear.com/AirportWeather.xsd");

	       /* Prepare the transport*/
		HTTPTransport transport = new HTTPTransport (
                       "http://www.capescience.com/ccgw/GWXmlServlet",
		       "capeconnect:AirportWeather:com.capeclear.weatherstation.Station#getTemperature");
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
		           view.displayResult(responseEnvelope.getParameter(0)+"");
		       } //else
             } //if

	} /* responseEnvelope*/

} /* class Weather */

class WeatherListener implements CommandListener {

     Weather weather;
     TextField tf;

     public WeatherListener(Weather weather, TextField tf) {
          this.weather=weather;
	  this.tf=tf;
     } /* constructor*/

     public void commandAction(Command c, Displayable d)  {

          if (c.getLabel().trim().equals("Submit")) {

               String airportCode = tf.getString().trim();
               Envelope responseEnvelope = 
	                 weather.makeRequest(airportCode);
               
	       weather.parseResponse(responseEnvelope);
	  } /* if submit */
          else if (c.getLabel().trim().equals("Exit")) {
               weather.destroyApp(false);
	       weather.notifyDestroyed();
  	  }
     } /* CommandAction */ 

} /* class WeatherListener */		                       
