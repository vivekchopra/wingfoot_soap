/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
*/


package interop;
import java.util.*;
import com.wingfoot.soap.encoding.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;

public class EchoString  implements InteropInterface
{
	TypeMappingRegistry registry=null;
        J2SEHTTPTransport transport=null;
	String schema, schemaInstance;
	Vector elementMap;
	Vector typeMap;

        public EchoString()
	{
	}
        public void setTransport(String url) {

  	     this.transport = new J2SEHTTPTransport (url,
	                                            "\"http://soapinterop.org/\"");
             this.transport.getResponse(true);
        }
	public void setSchema(String schema) {
            this.schema=schema;        
	}
	public void setSchemaInstance(String schemaInstance) {
            this.schemaInstance=schemaInstance;        
	}
        
	public void setElementMap(Vector elementMap) {
            this.elementMap=elementMap;
	} //setElementMap

	public void setTypeMap(Vector typeMap) {
            this.typeMap=typeMap;
	} //setTypeMap

	public String run() throws Exception
	{
	    if (elementMap !=null ||
	        typeMap != null) {
		registry = new TypeMappingRegistry();
		for (int i=0; elementMap!=null && i<elementMap.size(); i++) {
		    String[] s = (String[]) elementMap.elementAt(i);
		    if (s[2]==null) {
                         registry.mapElements(s[0],
		                              Class.forName(s[1]),
			  	              null);
                    }
                    else {
                         registry.mapElements(s[0],
		                              Class.forName(s[1]),
			          	      Class.forName(s[2]));
                    }
		}

		for (int i=0; typeMap!=null && i<typeMap.size(); i++) {
		    String[] s = (String[]) typeMap.elementAt(i);
                    registry.mapTypes(s[0],
		                      s[1],
				      Class.forName(s[2]),
				      Class.forName(s[3]), 
				      Class.forName(s[4]));
		}
            }
	    Envelope requestEnvelope = new Envelope();
            requestEnvelope.useEncoding("UTF-8");
	    requestEnvelope.setSchema(schema);
	    requestEnvelope.setSchemaInstance(schemaInstance);
            Call call = new Call(requestEnvelope);
	    call.setMappingRegistry(registry);
	    //call.addParameter("inputString", "Hello World �����");
	    call.addParameter("inputString", "Hello World");
	    call.setMethodName("echoString");
	    call.setTargetObjectURI("http://soapinterop.org/");
	    Envelope responseEnvelope = call.invoke(transport);
   	    if (responseEnvelope == null)
	       throw new Exception ("Response envelope is null");
            else if (responseEnvelope.isFaultGenerated()) {
                  Fault f = responseEnvelope.getFault();
		  return "Fault: " + f.getFaultString();
	    }
            else {
                       String returnString = (String) 
                         responseEnvelope.getParameter(0);
	              // if (! returnString.equals("Hello World �����"))
		       if ( ! returnString.equals("Hello World"))
			       throw new Exception("Incorrect return data");
                   }
              return "OK";
	} //run

} //class
