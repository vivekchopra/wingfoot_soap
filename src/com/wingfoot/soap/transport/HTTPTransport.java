/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */

package com.wingfoot.soap.transport;

import java.util.*;
import java.io.*;
import com.wingfoot.soap.*;
import javax.microedition.io.*;

/**
 * Class to aid in making SOAP calls over HTTP using
 * J2ME generic connection framework.
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */

public class HTTPTransport implements Transport {
  private String url;
  private boolean shouldGetResponse=true;
  private String soapAction;
  private Vector httpHeader;
  private boolean useSession=false;
  private String cookieName="JSESSIONID";
  private String cookieValue=null;

  /**
   * Creates an instance of HTTPTransport with the 
   * url and port number specified.  
   * @since 0.90
   * @param url the URL to connect to.
   * @param SOAPAction the value for SOAPAction HTTP header.
   * If null, the header is set to "".
   */
  public HTTPTransport (String url, String soapAction) {
    this.url  = url;
    this.soapAction = 
      soapAction != null? soapAction : "\"\"";
  }

  /**
   * Identifies if the session has to be maintained.
   * Since HTTP is a stateless protocol, cookies are
   * a popular way to maintain state across multiple
   * requests.  For J2EE based servers, the 
   * JSESSIONID cookie is used to maintain state. For
   * other platforms, the cookie name might be different.
   * This method takes the cookieName that is sent back
   * to the server.
   * @param usage boolean that identifies if a session has
   * to be maintained; true indicates that the cookie 
   * sent back by the server is sent back to the server 
   * thereby maintaining state; false indicates the cookie
   * is not sent back; default is false
   * @param cookieName the name of the cookie that is used
   * to maintain the state; if null JSESSIONID is used.
   */
  public void useSession(boolean usage,
			 String cookieName) {
    this.useSession=usage;
    if (cookieName!=null) this.cookieName=cookieName;
  } 

  /**
   * Method to specify HTTP Header(s).  These headers
   * are  in addition to the following HTTP headers
   * always sent to the server:
   * User-Agent - always set to "Profile/MIDP-1.0 Configuration/CLDC-1.0"
   * Content-Language - always set to "en-US"
   * Content-Type - always set to "text/xml"
   * Accept - always set to  text/xml
   * Content-Length - set to the length of the SOAP payload
   * SOAPAction - the SOAPAction specified in the constructor; 
   * "" is a null was passed to the constructor.
   * @since 1.01
   * @param name the name of the HTTP header; cannot be null
   * @param value the value of the header; cannot be null
   * @throws SOAPException if the key or value is null
   */

  public void addHeader(String name,
			String value) throws SOAPException {
    if (name==null || value==null)
      throw new SOAPException ("Header name and value cannot be null");
    if (httpHeader==null) httpHeader=new Vector();
    httpHeader.addElement (new String[] {name, value});
  } //addHeader(string, string) 

    /**
     * Method to specify if the HTTP call should wait
     * for a response from the service.  If the boolean
     * is set to false, the XML payload is sent and
     * the response is not retrieved or parsed. Default
     * is true (i.e. retrieves response).
     * <p>
     * Please use this option with care.  It has been
     * noticed that a connection is not made to the 
     * server until explicitly opening an InputStream
     * to the server; hence setting the boolean to false
     * will not parse the data but the call is blocked
     * until the server responds.
     * @since 0.90
     * @param shouldGetResponse true if a response is to
     * be retrieved (default); false if response is not
     * to be retrieved.
     */
  public void getResponse(boolean shouldGetResponse) {
    this.shouldGetResponse = shouldGetResponse;
  }

  /**
   * Makes a HTTP connection the the URL and port specified.
   * @param payload the SOAP message as a byte array.
   * @return a byte array with the response from the server;
   * null if shouldGetResponse is false;
   * @throws IOException if any error occurs during connection
   * to the server.
   * @throws SOAPException if the response is not XML.
   */

  public byte[] call (byte[] payload) 
    throws SOAPException, IOException{

    HttpConnection conn = null;
    OutputStream outputStream=null;
    InputStream inputStream=null;

    try {
      conn = (HttpConnection) Connector.open (url,
					      Connector.READ_WRITE);

      conn.setRequestProperty ("User-Agent", 
			       "Profile/MIDP-1.0 Configuration/CLDC-1.0");
      conn.setRequestProperty ("Content-Language", "en-US");
      conn.setRequestProperty ("Content-Type", "text/xml");
      conn.setRequestProperty ("Accept", "text/xml");
      conn.setRequestProperty ("Content-Length", 
			       Integer.toString (payload.length));
      conn.setRequestProperty ("SOAPAction", soapAction);
      
      if (useSession && cookieValue!=null)   
	conn.setRequestProperty("Cookie", cookieValue);
      /**
       * Add HTTP Headers
       */
      if (httpHeader!=null) {
	for (int i=0; i<httpHeader.size(); i++) {
	  String[] theHead = (String[])
	    httpHeader.elementAt(i);
	  conn.setRequestProperty (theHead[0], theHead[1]);
	} //for
      } //if
      //System.err.println("Sending:" + new String(payload));
      conn.setRequestMethod ("POST");
      outputStream = conn.openOutputStream();
      outputStream.write (payload);

      if (shouldGetResponse) {
	try {
	  inputStream = conn.openInputStream();
	  if (useSession) {
	    String tempSes=readCookie(conn);
	    if (tempSes!=null &&
		!tempSes.equals(cookieValue))
	      cookieValue=tempSes;
	  }
	} catch (Exception e){}
	byte[] b = new byte[250];
	ByteArrayOutputStream bos = new ByteArrayOutputStream();

	/**
	 * Is this really required
	
	 if (! (conn.getType().equals("text/xml")))
	 throw new SOAPException("Response is not a SOAP xml");
	*/

	while (true) {
	  int i = inputStream.read(b,0,250);
	  if (i== -1) break;
	  bos.write(b,0,i);
	}
	b=bos.toByteArray();
	//System.err.println("Received:" + new String(b));
	return b;
      } //if
      else return null;
    } finally {
      if (inputStream != null) inputStream.close();
      if (outputStream!= null) outputStream.close();
      if (conn != null) conn.close();
    } //finally
  } //call

  private String readCookie(HttpConnection conn) throws IOException {
    String key=null;
    String value=null;
    String[] substrs=null;
    
    for (int i=0; (key=conn.getHeaderFieldKey(i))!=null; i++) {
      if (key.toLowerCase().equals("set-cookie")) {
	value=conn.getHeaderField(i);
	while (value!=null) {
	  substrs=split(value, ';');
	  if (substrs!=null && 
	      substrs[0].startsWith(cookieName)) 
	    return substrs[0];
	  value=substrs[1];
	} //while
      } //if
    } //for
    return null;
  } //readCookie

  private String[] split(String str, char ch) {
    if (str==null)
      return null;
    String[] retVal = new String[] {null, null};
    int pos = str.indexOf(ch);
    if (pos!=-1){
      retVal[0]=str.substring(0,pos).trim();
      retVal[1]=str.substring(pos+1).trim();
    } else
      retVal[0]=str.trim();
    return retVal;
  }

  /**
   * Provides the ability to set the soapAction.
   * Useful if the a Transport instance is to
   * be used across calls but the soapAction has
   * to be changed.
   * @param soapAction the new soapAction.
   */
  public void setSOAPAction(String soapAction)
  {
    this.soapAction=soapAction;
  }

} /* com.wingfoot.soap.transport.HTTPTransport*/ 

