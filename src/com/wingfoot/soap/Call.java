/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */

//package com.wingfoot.soap.rpc;
package com.wingfoot.soap;

import java.io.*;
import java.util.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.encoding.*;
import com.wingfoot.soap.transport.*;

/**
 * Entry point for making a SOAP call.
 * The call could be a RPC style body or
 * Document style body.
 * This class is used to send a SOAP payload
 * to the SOAP server.
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */

public class Call {

  // URI to post the SOAP call
  //private String              uri;
  // The SOAP Envelope to send in the RPC call
  private Envelope            envelope;   
    
  // The transport for the SOAP RPC call
  private Transport           transport;

  private TypeMappingRegistry registry;

  String methodName, targetURI;

  /**
   * Default constructor.
   * Since no Envelope is specified, a default Envelope
   * is created.
   * @since 0.90
   */
  public Call () {
    envelope = new Envelope ();
  } /* constructor */

    /**
     * Constructor using a custom SOAP Envelope
     * built by user. A custom Envelope is required
     * if 
     * <ul>
     * <li> an alternative schema and schema instance is needed (default 2001); or
     * <li> a SOAP Header is needed; or
     * <li> a Document Style SOAP Body is required.
     * </ul>
     * @since 0.90
     */
  public Call (Envelope envelope) {
    this.envelope = envelope;
  } /* constructor */

    /**
     * Allows the user to specify the RPC style Parameter
     * name and value.  This parameter name and value is 
     * sent to the service as part of the <SOAP-ENV:Body> 
     * element of a SOAP message.
     * @since 0.90
     * @param paramName the name of the parameter.
     * @param value the value of the parameter represented
     * as a Java object.  Primitive data types are 
     * represented by their corresponding wrapper classes.
     * @throws SOAPException thrown if a Document style parameter
     * was previously added.
     */
  public void addParameter (String paramName, Object value)
    throws SOAPException {
    envelope.setBody(paramName, value);
  } /* addParameter*/

    /**
     * Sets the URI of the service on the server. 
     * This is typically required only for a RPC
     * style service.
     * @since 0.90
     * @param uri URI for the service on the server.
     */
  public void setTargetObjectURI (String uri) {
    this.targetURI=uri;
  } /* setTargetObjectURI*/

    /**
     * Sets the name of the method to invoke on
     * the server.  This is typically required
     * only for a RPC style service.
     * @since 0.90
     * @param methodName the name of the method to
     * invoke on the service.
     */
  public void setMethodName (String methodName) {
    this.methodName=methodName;
  } /* setMethodName */

    /**
     * Sets the registry to associate a Class with
     * the serializer class, desserializer class and
     * the name of the class.  The registry is useful
     * in serialization and deserialization.
     * This is typically required only for a RPC style
     * service.
     * @since 0.90
     * @param registry instance of TypeMappingRegistry
     */
  public void setMappingRegistry (TypeMappingRegistry registry) {
    this.registry = registry;
  }

  /**
   * Call to serialize and send the soap payload to the
   * server and get back the response. The response is
   * encapsulated in Envelope
   * @since 0.90
   * @param transport an implementation of Transport interface.
   * An instance of HTTPTransport is provided to use HTTP as the
   * transport to communicate to the service on the server. 
   * Users can write their own Transport (example SMTP instead of
   * HTTP).
   * @return the response from the service encapsulated as
   * an Envelope.
   */
  public Envelope invoke (Transport transport) 
    throws SOAPException, Exception {
    if (envelope == null)
      throw new SOAPException ("Soap Envelope cannot be null");

    /**
     * Serialize the envelope. This in turn will serialize the
     * header and body along with any attributes.
     */
    byte[] payload = envelope.serialize( registry,
					 methodName, 
					 targetURI);
    /**
     * Make HTTP call and get back the response
     */
    byte[] response = transport.call (payload);

    /**
     * Return back a response as an Envelope.
     */
    Envelope responseEnvelope = new Envelope();
    responseEnvelope.useEncoding(envelope.getEncoding());
    /**
     * If the envelope sent was Document style, there
     * is no need to deserialize the Body element in the
     * response.
     */
    if (envelope.isDocumentStyle()) {
      String str = new String(response);
      int begin = str.indexOf("Body");
      int end = begin > 0 ? str.indexOf("Body", begin+4):-1;
      String theBody = (begin>0 && end>0) ? 
	str.substring(begin+5, end+5):
	null;
      responseEnvelope.setBody(theBody.substring(0,
						 theBody.lastIndexOf('<')));
    } //if
    responseEnvelope.deserialize(response, registry);
    return responseEnvelope;
  }

} /* com.wingfoot.soap.rpc.Call */
