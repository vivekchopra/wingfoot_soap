package xmethods.currency;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;
import com.wingfoot.soap.encoding.*;
import java.io.*;
import java.util.*;

class CurrencyView {
     Currency model;
     Display display;
     public CurrencyView (Currency model, Display display) {
          this.model=model;
	  this.display=display;
     }

     public void displayForm() {

	  Form form = new Form("Currency Exchange Rate");
          TextField country1 = new TextField("Country 1", null, 50, 0);
          TextField country2 = new TextField("Country 2", null, 50, 0);
	  form.append(country1);
	  form.append(country2);
	  form.addCommand(new Command("Submit", Command.OK, 1));
	  form.addCommand(new Command("Exit", Command.EXIT, 1));
	  form.setCommandListener(new CurrencyListener(model, country1, country2));
	  display.setCurrent(form);
	       
     } /* displayForm */

     public void displayResult(String result) {
           TextBox t = new TextBox("Currency Result", 
                                    result, 256, 0);

	   t.addCommand(new Command("Exit", Command.EXIT, 1));
       	   t.setCommandListener(new CurrencyListener(model,null,null));
	   display.setCurrent(t);
     } /* displayResult */

} /* class CurrencyView */

public class Currency extends MIDlet 
{
        private Display display;
        private CurrencyView view;
        public Currency() {
	        display = Display.getDisplay(this);
	}

	public void startApp() {
             view = new CurrencyView(this, display);
             view.displayForm();
	}
	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public Envelope makeRequest(String country1,
	                            String country2) {
           try {
	     /* Prepare the Envelope*/
	        Envelope requestEnvelope = new Envelope();
	        requestEnvelope.setSchema("http://www.w3.org/1999/XMLSchema");
	        requestEnvelope.setSchemaInstance(
	                       "http://www.w3.org/1999/XMLSchema-instance");
                requestEnvelope.setBody("country1", country1);
                requestEnvelope.setBody("country2", country2);

	       /* Prepare the call*/
		Call call = new Call(requestEnvelope);
		call.setMethodName("getRate");
		call.setTargetObjectURI("urn:xmethods-CurrencyExchange");

	       /* Prepare the transport*/
		HTTPTransport transport = new HTTPTransport (
                          "http://services.xmethods.net:80/soap", 
	                  null);

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

	     } /* if*/
	} /* responseEnvelope*/

} /* class Currency */

class CurrencyListener implements CommandListener {

     Currency weather;
     TextField country1, country2;

     public CurrencyListener(Currency weather, TextField country1, 
                            TextField country2 ) {
          this.weather=weather;
	  this.country1=country1;
	  this.country2=country2;
     } /* constructor*/

     public void commandAction(Command c, Displayable d)  {

          if (c.getLabel().trim().equals("Submit")) {

               Envelope responseEnvelope = 
	                 weather.makeRequest(country1.getString().trim(),
			                     country2.getString().trim());
               
	       weather.parseResponse(responseEnvelope);
	  } /* if submit */
          else if (c.getLabel().trim().equals("Exit")) {
               weather.destroyApp(false);
	       weather.notifyDestroyed();
  	  }
     } /* CommandAction */ 

} /* class CurrencyListener */		                       
