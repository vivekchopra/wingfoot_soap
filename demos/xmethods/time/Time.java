package xmethods.time;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;
import com.wingfoot.soap.encoding.*;
import java.io.*;
import java.util.*;
/**
 * An example using Document Style SOAP body.
*/
class TimeView {
     Time model;
     Display display;
     public TimeView (Time model, Display display) {
          this.model=model;
	  this.display=display;
     }

     public void displayForm() {

	  Form form = new Form("Book Price");
          TextField tf = new TextField("Zip Code", null, 5, 0);
	  form.append(tf);
	  form.addCommand(new Command("Submit", Command.OK, 1));
	  form.addCommand(new Command("Exit", Command.EXIT, 1));
	  form.setCommandListener(new TimeListener(model, tf));
	  display.setCurrent(form);
	       
     } /* displayForm */

     public void displayResult(String result) {
           TextBox t = new TextBox("Book Price Result", 
                                    result, 256, 0);

	   t.addCommand(new Command("Exit", Command.EXIT, 1));
       	   t.setCommandListener(new TimeListener(model, null));
	   display.setCurrent(t);
     } /* displayResult */

} /* class BookPriceView */

public class Time extends MIDlet 
{
        private Display display;
        private TimeView view;
        public Time() {
	        display = Display.getDisplay(this);
	}

	public void startApp() {
             view = new TimeView(this, display);
             view.displayForm();
	}
	public void pauseApp() {}

	public void destroyApp(boolean unconditional) {}

	public Envelope makeRequest(String elementSymbol) {
           try {
	     /* Prepare the Envelope*/
	        Envelope requestEnvelope = new Envelope();
                StringBuffer sb = new StringBuffer();
		sb.append("<LocalTimeByZipCode xmlns=\"http://alethea.net/webservices/\">");
		sb.append("<ZipCode>" + elementSymbol + "</ZipCode>");
		sb.append("</LocalTimeByZipCode>");
                requestEnvelope.setBody(sb.toString());


	       /* Prepare the call*/
	 	Call call = new Call(requestEnvelope);
	       /* Prepare the transport*/
		HTTPTransport transport = new HTTPTransport (
                       "http://alethea.net/webservices/LocalTime.asmx", 
		        "http://alethea.net/webservices/LocalTimeByZipCode");
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
		             String theTime = responseEnvelope.getBody();
			     int begin = 
			     theTime.indexOf("<LocalTimeByZipCodeResult>");
			     begin = theTime.indexOf('>', begin);
			     int end = theTime.indexOf("</",begin);
			     String result = theTime.substring(begin+1, end);
			     view.displayResult(result);
		       } //else
	     } //else
       }

} /* class BookPrice */

class TimeListener implements CommandListener {

     Time weather;
     TextField tf;

     public TimeListener(Time weather, TextField tf) {
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

} /* class BookPriceListener */		                       
