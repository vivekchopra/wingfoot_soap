package xmethods.currency;

import java.applet.Applet;
import java.awt.Graphics;

import com.wingfoot.soap.Call;
import com.wingfoot.soap.Envelope;
import com.wingfoot.soap.Fault;
import com.wingfoot.soap.transport.J2SEHTTPTransport;

public class CurrencyApplet extends Applet {

    static final String METHOD_NAME = "getRate";
    static final String SERVICE_URN = "urn:xmethods-CurrencyExchange";
    static final String SERVICE_URL = "http://services.xmethods.com:80/soap";
    static final String SOAP_ACTION = "";

    public void paint (Graphics graphics) {
        String country1 = getParameter ("country1");;
        String country2 = getParameter ("country2");

        Envelope responseEnvelope = makeRequest (country1, country2);
        if (responseEnvelope != null) {
            if (responseEnvelope.isFaultGenerated()) {
                 Fault f = responseEnvelope.getFault();
                 graphics.drawString ("SOAP Fault : " + f.getFaultString(), 
                                      25, 50);
            } else { // no Fault
              graphics.drawString ("The currency exchange rate between " 
                                   + country1 + " and " + country2 + " is " 
                                   + responseEnvelope.getParameter(0), 25, 50);
            }
        } else {     // responseEnvelope == null
              graphics.drawString ("Unknown error making SOAP call", 25, 50);
        }
    }

    private Envelope makeRequest (String country1, String country2) {    
        try {
            Call theCall = new Call ();
            theCall.setMethodName (METHOD_NAME);
            theCall.setTargetObjectURI (SERVICE_URN);
            theCall.addParameter ("country1", country1);
            theCall.addParameter ("country2", country2);

            J2SEHTTPTransport transport = 
                   new J2SEHTTPTransport (SERVICE_URL, SOAP_ACTION);
            Envelope responseEnvelope = theCall.invoke (transport);
            return responseEnvelope;

        } catch (Exception e) {
            e.printStackTrace ();
            return null;
        }
    }
}
