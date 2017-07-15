/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
*/


import java.util.*;
import com.wingfoot.soap.encoding.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;

public class TestUntype  
{
	TypeMappingRegistry registry=null;
        J2SEHTTPTransport transport = 
	   new J2SEHTTPTransport("http://www.whitemesa.net/interop/std",
	                         "\"http://soapinterop.org/\"");
	                         
	public void run() throws Exception
	{
            Base64 b64 = new Base64("Hello World".getBytes());
            System.err.println("Encoded is " + b64);
            Call call = new Call();
	    call.setMappingRegistry(registry);
	    call.addParameter("inputBase64", b64);
	    call.setMethodName("echoBase64");

	    call.setTargetObjectURI("http://soapinterop.org/");
	    Envelope responseEnvelope = call.invoke(transport);

   	    if (responseEnvelope == null)
	       throw new Exception ("Response envelope is null");
            else if (responseEnvelope.isFaultGenerated()) {
                  Fault f = responseEnvelope.getFault();
		  System.err.println("Fault: " + f.getFaultString());
	    }
            else {
	              for (int i=0; i<responseEnvelope.getParameterCount(); i++) {
                           Object o = responseEnvelope.getParameter(i);
			   //System.err.println("Class is " + o.getClass().getName());
			   System.err.println("Name is " + responseEnvelope.getParameterName(i)
			                       + " " + o);
		      }
                   }
	} //run

	public static void main(String a[]) throws Exception {
             new TestUntype().run();
	}

} //class
