/* My other test class for Personal Java
	 @author Baldwin Louie
 */
package xmethods.pjtraffic;

import com.wingfoot.soap.*;
import com.wingfoot.soap.rpc.*;
import com.wingfoot.soap.transport.*;
import com.wingfoot.soap.io.*;
import java.io.*;
import java.util.*;

public class Traffic 
{
		public Traffic(){};
		
		public static void main(String[] args)
		{
				if(args.length <= 0){
						System.err.println("usage: Traffic <Highway Number>");
						System.exit(-1);
				}
				
						
				try{
						new Traffic().init(args);
				}
				catch (Exception e){
						e.printStackTrace();
						System.err.println("There is an error: " + e.getMessage());
				}
				
		}
		public void init(String[] args) throws Exception
		{
				/* The standard Stuff */
				Envelope re = new Envelope();
				re.setSchema("http://www.w3.org/1999/XMLSchema");
				re.setSchemaInstance("http://www.w3.org/1999/XMLSchema-instance");
				re.setBody("hwynums", args[0]);
				
				Call call = new Call(re);
				call.setMethodName("getTraffic");
				call.setTargetObjectURI("urn:xmethods-CATraffic");
				J2SEHTTPTransport transport = new J2SEHTTPTransport("http://services.xmethods.net:80/soap/servlet/rpcrouter", null);
				
				Envelope res = call.invoke(transport);
				
				//now parse the thing and spit out something
				if(res != null){
						if(res.isFaultGenerated()){
								
								Fault f = res.getFault();
								System.err.println("Error: " + f.getFaultString());
								System.exit(-1);
								
						}
						else{
								Vector returnParameter = res.getBody();
								if(returnParameter != null){
										Object o = returnParameter.elementAt(0);
										Object[] theReturnArray = (Object[]) o;
										System.out.println("The status: \n" + (String) theReturnArray[1]);
										
										
								}
								
						}

				}
				
		}
		
}
