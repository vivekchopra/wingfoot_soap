/* Quick test to see if our api will work with Personal Java
	 @author Baldwin Louie $ Date: 2002/02/11 $
 */

package xmethods.pjtemperature;
import java.io.*;
import java.util.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.rpc.*;
import com.wingfoot.soap.transport.*;
import com.wingfoot.soap.encoding.*;

public class Temperature 
{
		public Temperature()
		{
		}
		public static void main (String[] args)
		{
				if(args.length <= 0){
						System.err.println("usage: Temperature <zipcode>");
						System.exit(-1);
				}
				try {
						
						new Temperature().init(args);
				} catch (Exception e){
						e.printStackTrace();
						System.err.print("There is an error: " + e.getMessage());
				}
		}
		
		public void init (String[] args) throws Exception
		{
				String zipcode = args[0];
				/*prepare the request*/
				Envelope requestEnvelope = new Envelope();
				requestEnvelope.setSchema("http://www.w3.org/1999/XMLSchema");
				requestEnvelope.setSchemaInstance("http://www.w3.org/1999/XMLSchema-instance");
				requestEnvelope.setBody("zipcode", zipcode);
						
				Call call = new Call(requestEnvelope);
				call.setMethodName("getTemp");
				call.setTargetObjectURI("urn:xmethods-Temperature");
				J2SEHTTPTransport transport = new J2SEHTTPTransport ("http://services.xmethods.net:80/soap/servlet/rpcrouter", null);
				transport.getResponse(true);
						
				Envelope responseEnvelope = call.invoke(transport);
				//send the envelope to a method to get it taken care of
				if(responseEnvelope != null){
						if(responseEnvelope.isFaultGenerated()){
								Fault f = responseEnvelope.getFault();
								System.out.println("There is an error : " + f.toString());
						}
						else {
								Vector returnParameter = responseEnvelope.getBody();
								if(returnParameter != null) {
										Object o = returnParameter.elementAt(0);
										Object[] theReturnArray = (Object[]) o;
										System.out.println("The temperature is: " + theReturnArray[1].toString());
												
								}
						}
				}
		}
}
