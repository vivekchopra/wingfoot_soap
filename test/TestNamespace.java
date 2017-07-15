/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
*/

import java.util.*;
import java.net.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;

public class TestNamespace
{
	public static void main(String argv[])
	{
		new TestNamespace().init();
	}

	public void init()
	{
   	    try
 	    {
                Vector v1 = new Vector();
		v1.addElement("Kal Iyer v1");
		v1.addElement("Cindy Crawford v1");

                Vector v2 = new Vector();
		v2.addElement("Kal Iyer v2");
		v2.addElement("Cindy Crawford v2");

		String theURL = new String("http://192.168.1.3:8015/bubbles/servlet/spit");
                        
		J2SEHTTPTransport transport = new J2SEHTTPTransport(theURL, null);
                Envelope requestEnvelope = new Envelope();
	        requestEnvelope.setSchema("http://www.w3.org/1999/XMLSchema");
	        requestEnvelope.setSchemaInstance("http://www.w3.org/1999/XMLSchema-instance");
	        Call call = new Call(requestEnvelope);
		call.addParameter("Vector1", v1);
		call.addParameter("Vector2", v2);
		call.addParameter("Vector3", v2);
		call.setMethodName("TestVector");
		call.setTargetObjectURI("urn:xmethods-Temperature");
		Envelope responseEnvelope = call.invoke(transport);
/*
		Vector response = responseEnvelope.getBody();
		Object[] o = (Object[]) response.get(0);
		System.err.println("The first Vector is " + o[1]);
		o = (Object[]) response.get(1);
		System.err.println("The second Vector is " + o[1]);
*/
  	    } catch (Exception e)
  	    {
		System.err.println(e.getMessage());
		e.printStackTrace();
  	     }
	}

} //class
