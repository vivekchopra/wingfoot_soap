package xmethods.stockquote;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;
import com.wingfoot.soap.encoding.*;
import java.io.*;
import java.util.*;

/**
 * Class not complete.  Look at the URL below and
 * try to fix bubbles client.
 * http://www.xmethods.com/detail.html?id=95
 */

class StockQuoteView {
     StockQuote model;
     Display display;
     public StockQuoteView (StockQuote model, Display display) {

          this.model=model;
	  this.display=display;
     }

     public void displayForm() {

	  Form form = new Form("StockQuote");
          TextField tf = new TextField("Stock Symbol", null, 4, 0);
	  form.append(tf);
	  form.addCommand(new Command("Submit", Command.OK, 1));
	  form.addCommand(new Command("Exit", Command.EXIT, 1));
	  form.setCommandListener(new StockQuoteListener(model, tf));
	  display.setCurrent(form);
	       
     } /* displayForm */

     public void displayResult(String result) {
           TextBox t = new TextBox("StockQuote Result", 
                                    result, 256, 0);

	   t.addCommand(new Command("Exit", Command.EXIT, 1));
       	   t.setCommandListener(new StockQuoteListener(model, null));
	   display.setCurrent(t);
     } /* displayResult */

} /* class StockQuoteView */

public class StockQuote extends MIDlet 
{
        private Display display;
        private StockQuoteView view;
        //long before, after;
        //Runtime runtime;
        public StockQuote() {
	        display = Display.getDisplay(this);
          	//runtime = Runtime.getRuntime();
	  	//System.gc();
	  	//before = runtime.freeMemory();
	}

	public void startApp() {
             view = new StockQuoteView(this, display);
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
                
                requestEnvelope.setBody("symbol", elementSymbol);

	       /* Prepare the call*/
		Call call = new Call(requestEnvelope);
		call.setMethodName("getQuote");
		call.setTargetObjectURI(
		   "urn:xmethods-delayed-quotes");

	       /* Prepare the transport*/
		HTTPTransport transport = new HTTPTransport (
                       "http://services.xmethods.net:80/soap", 
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
		           view.displayResult( "" +
			   responseEnvelope.getParameter(0));
		       } //else
	     } //else
	} /* responseEnvelope*/

} /* class StockQuote */

class StockQuoteListener implements CommandListener {

     StockQuote weather;
     TextField tf;

     public StockQuoteListener(StockQuote weather, TextField tf) {
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

} /* class StockQuoteListener */		                       
