/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
*/
import java.util.*;
import java.net.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;

public class TestHeader
{
	public static void main(String argv[])
	{
		new TestHeader().init();
	}

	public void init()
	{
   	    try
 	    {
                Vector v1 = new Vector();
		v1.addElement("Kal Iyer v1");
		v1.addElement("Cindy Crawford v1");
                HeaderEntry requestHE = new HeaderEntry("Golf", "101");
		requestHE.setMustUnderstand(true);
		requestHE.setActor("http://www.yahoo.com/next");
		requestHE.setNamespace("http://www.wingfoot.com");

                HeaderEntry requestHE2 = new HeaderEntry("Tennis", "102");
		requestHE2.setMustUnderstand(false);
		requestHE2.setActor("http://www.kaliyer.com/next");
		requestHE2.setNamespace("http://www.soaprpc.com");

		String theURL = new String("http://192.168.1.3:8015/bubbles/servlet/spit");
                        
		J2SEHTTPTransport transport = new J2SEHTTPTransport(theURL, null);
                Envelope requestEnvelope = new Envelope();
                requestEnvelope.addHeader(requestHE);
                requestEnvelope.addHeader(requestHE2);
	        requestEnvelope.setSchema("http://www.w3.org/1999/XMLSchema");
	        requestEnvelope.setSchemaInstance("http://www.w3.org/1999/XMLSchema-instance");
	        Call call = new Call(requestEnvelope);
		call.addParameter("Vector1", v1);
		call.setMethodName("TestVector");
		call.setTargetObjectURI("urn:xmethods-Temperature");
		Envelope responseEnvelope = call.invoke(transport);
                Vector returnHeader = responseEnvelope.getHeader();
                
		for (int i=0; i<returnHeader.size(); i++) {
                    HeaderEntry he = (HeaderEntry) returnHeader.elementAt(i);
		    System.err.println("Printing Headers....");
		    System.err.println(he.getHeaderName()
                                       + ":" + he.getValue()
                                       + ":" + he.getActor()
                                       + ":" + he.getMustUnderstand());
		    System.err.println("Printed Headers....");
		}

  	    } catch (Exception e)
  	    {
		System.err.println(e.getMessage());
		e.printStackTrace();
  	     }
	}

} //class
