/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */



package com.wingfoot.soap;
import java.util.*;
import java.io.*;
import org.kxml.parser.*;
import org.kxml.*;
import org.kxml.io.*;
import com.wingfoot.soap.encoding.*;
/**
 * Encapsulates the SOAP Envelope element. 
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */
public class Envelope extends SOAPElement {
  private Vector    header;     // Vector of HeaderEntry elements
  /**
   * For RPC, each element of the body is an Object
   * array as follows:
   * 0th index - String Parameter Name
   * 1st index - Object Parameter Value
   * 2nd index - String ID attribute, null
   * if no ID was present.
   * For Document style, the body is one big String.
   */
  private Vector    body;
  private boolean faultGenerated;
  private Fault faultObject;
  private boolean isRPC=false;
  private boolean isDOC=false;
  private String methodName,targetURI;
  private String enc;
  /**
   * Constructor for the SOAP Envelope. <br>
   * This sets the 
   * <ul>
   * <li> Default namespace to http://schemas.xmlsoap.org/soap/envelope
   * <li> Default Schema instance to http://www.w3.org/2001/XMLSchema-instance
   * <li> Default Schema to http://www.w3.org/2001/XMLSchema
   * <li> Encoding Style to http://schemas.xmlsoap.org/soap/encoding
   * </ul>
   * Users desiring to do something different need to do
   * <code> envelope.removeAttribute () </code>
   * followed by <code>envelope.setSchema/SchemaInstance/EncodingStyle()
   * </code>.
   */
  public Envelope () {
    // Set predefined Envelope attributes- Default namespace, Schema, 
    // Schema instance and encoding style.
    // The SOAP_NAMESPACE is set as the default namespace, hence
    // "soap:" namespace prefix is not required (e.g. we dont need to
    // use <soap:Envelope>, just a <Envelope> will do.)
    super.addAttribute ("xmlns:SOAP-ENV", Constants.SOAP_NAMESPACE);
    setSchema          (Constants.SOAP_SCHEMA);
    setSchemaInstance  (Constants.SOAP_SCHEMA_INSTANCE);
    setEncodingStyle   (Constants.SOAP_ENCODING_STYLE);
  }
    
  /**
   * Adds a SOAP HeaderEntry
   * @since 0.90
   * @param headerEntry Instance of HeaderEntry which
   * encapsulates an instance of a header.
   */
  public void addHeader (HeaderEntry headerEntry) {
    if (header==null) header=new Vector();
    header.addElement (headerEntry);
  }
    
  /**
   * Sets the SOAP body with parameters.
   * @since 0.90
   * @param parameterName String with the name of the
   * parameter.
   * @param value Object encapsulating the value assigned
   * to the parameter.  In Bubbles, all primitive data 
   * types are represented as their corresponding wrapper
   * class
   * @throws SOAPException if a Document Style parameter
   * was previously added.
   */
  public void setBody(String parameterName, Object value)
    throws SOAPException
  {
    setBody( parameterName, value,null);

  }

  private void setBody(String parameterName, Object value,
		       String ID) throws SOAPException 
  {
    if (isDOC)
      throw new SOAPException("Cannot add RPC parameters with Document");
    if (body==null) body=new Vector();
    body.addElement( new Object[] {parameterName, value,ID});
    isRPC=true;
  }

  /**
   * Sets the SOAP body with arbitary XML payload.
   * @since 0.90
   * @param body String representing the XML as soap payload
   * @throws SOAPException if a RPC style parameter was already
   * specified in this envelope
   */

  public void setBody(String body) throws SOAPException
  {
    if (isRPC)
      throw new SOAPException("Cannot add Document with RPC parameters");
    if (this.body==null) this.body=new Vector();
    this.body.addElement(body);
    isDOC=true;
  }

  /**
   * Helper method for setting the <i>schemaInstance</i> attribute
   * @since 0.90
   * @param schemaInstance URI of Schema Instance
   */

  public void setSchemaInstance (String schemaInstance) {

    super.addAttribute ("xmlns:xsi", schemaInstance);

  }



  /**

  * Helper method for setting the <i>schema</i> attribute

  * @since 0.90

  * @param schema URI of Schema

  */

  public void setSchema (String schema) {

    super.addAttribute ("xmlns:xsd", schema);

  }



  /**

  * Helper method for setting the <i>encodingStyle</i> SOAP attribute.

  * An null encodingStyle indicates that the payload is a literal

  * (as opposed to encoded).

  * @since 0.90

  * @param schema URI of encodingStyle

  */

  public void setEncodingStyle (String encodingStyle) {

    if (encodingStyle != null)

      super.addAttribute ("SOAP-ENV:encodingStyle",

			  encodingStyle);

    else

      super.removeAttribute("SOAP-ENV:encodingStyle");

  }



  /**

  * Returns the SOAP Headers

  * @return Vector of HeaderEntry

  */

  

  public Vector getHeader () {

    return header;

  }

    

  /**

  * Determines if the envelope contains Document style

  * or RPC style parameters.

  */

  public boolean isDocumentStyle() {

    return isDOC;

  }



  /**

  * Returns the SOAP Body. Use this method

  * to retrieve the Body in a Document style.

  * @return String representing SOAP Body.

  */



  public String getBody () {

    if (isDOC)

      return (String) body.elementAt(0);

    return null;

  }



  /**

  * Returns the number of parameters in the

  * the SOAP Body.

  * @return int the number of parameters; 0

  * if no parameter is present

  */



  public int getParameterCount() {

    return body==null ? 0 : body.size();

  } /*getParameterCount()*/

    

    /**

    * Returns the parameter name at the given

    * index.

    * @param index the parameter number.

    * return String with the parameter name.

    */

  public String getParameterName(int index) {

    return body==null ? null :

      (String)(((Object[])body.elementAt(index))[0]);

  } /*getParameterName(int)*/

    

    /**

    * Returns the parameter at the given

    * index.

    * @param index the parameter number.

    * @return Object ecnapsulating the parameter.

    */

  public Object getParameter(int index) {

    return body==null? null:

      ((Object[])body.elementAt(index))[1];

  } /*getParameter(int)*/



    /**

    * Determines if a fault element was present in

    * the response from a service.

    * @since 0.90

    * @return true if a fault element was present

    * in the response, false if a fault element was

    * not present.

    */

  public boolean isFaultGenerated() {

    return faultGenerated;

  }

     

  /**

  * Returns the fault generated by the service

  * encapsulated as Fault

  * @return fault generated encapsulated in Fault

  * null if no fault was generated by the service

  */

  public Fault getFault() {

    return faultObject;

  }



  /**

  * Convenience method to get the Schema Instance

  * @return String with the schema instance.

  */

  public String getSchemaInstance () {

    String str =  super.getAttribute ("xmlns:xsi");

    if (str == null)

      return Constants.SOAP_SCHEMA_INSTANCE;

    else 

      return str;

  }



  /**

  * Convenience method to get the Schema

  * @return String with the schema.

  */

  public String getSchema () {

    String str =  super.getAttribute ("xmlns:xsd");



    if (str == null)

      return Constants.SOAP_SCHEMA;

    else 

      return str;

  }



  /**

  * Convenience method to get the encodingStyle SOAP attribute.

  * @return String with the encoding style.

  */

  public String getEncodingStyle () {

    String str =  super.getAttribute ("SOAP-ENV:encodingStyle");

    if (str == null)

      return Constants.SOAP_ENCODING_STYLE;

    else

      return str;

  }

    

  /**

  * Method to specify the encoding to use while sending

  * and parsing the SOAP payload (example UTF-8). If null

  * or if not specified, the default encoding is used. Users

  * are encouraged to ensure that the encoding is supported

  * by the device before specifying one here.

  * @since 1.9

  * @param enc the encoding to use.

  */

  public void useEncoding(String enc) {

    this.enc=enc;

  }

    

  /**

  * Utility method to retrieve the encoding (example UTF-8)

  * specified using useEncoding method.

  * @since 1.0

  * @return String the encoding.

  */

  public String getEncoding() {

    return this.enc;

  }



  /**

  * Takes the Envelope (along with the header and body)

  * and converts it to XML that is  SOAP message compliant.

  * @since 0.90

  * @param registry instance of TypeMappingRegistry mapping

  * each custom class to serialize/deserialize to a namespace

  * and the serializer and deserializer class.

  * @param methodName the name of the method to invoke on the

  * on the service

  * @param targetURI URI of the service to invoke

  * @return an array of bytes representing the soap message

  * @throws Exception if problems during serialization

  */

  public byte[] serialize(TypeMappingRegistry registry,

                          String methodName, String targetURI)

    throws Exception

  {



    /**

    * Instantiate XMLWriter to create XML. Constructor expects

    * a hashtable of default namespace-prefix mappings.

    */

    Hashtable hashtable = new Hashtable();

    hashtable.put (Constants.SOAP_NAMESPACE,

		   "SOAP-ENV");

    hashtable.put (getSchemaInstance(), "xsi");

    hashtable.put (getSchema(), "xsd");

    //hashtable.put (getEncodingStyle(), "SOAP-ENC");

    XMLWriter xmlwriter = new XMLWriter(hashtable);



    xmlwriter.startElement ("Envelope", 

			    Constants.SOAP_NAMESPACE);

    //write the attributes for Envelope element

    super.serialize(xmlwriter);



    writeHeader(xmlwriter);

    writeBody(xmlwriter, registry, methodName, targetURI);



    // End the Body and Envelope element

    xmlwriter.endTag();

    return xmlwriter.getPayload(getEncoding());



  } //serialize method

    

    /**

    * Converts instances of HeaderEntry to elements

    * under the <SOAP-ENV:Element> element

    * @since 0.90

    * @param xmlwriter aids in writing the XML

    */

  private void writeHeader(XMLWriter xmlwriter)

  {

    //serialize the headers

    if (header==null)

      return;

    xmlwriter.startElement ("Header", 

			    Constants.SOAP_NAMESPACE);

    for (int i=0; i< header.size(); i++) 

    {

      HeaderEntry he = (HeaderEntry) header.elementAt(i);

      he.serialize(xmlwriter);

    } //for



    //end the header element

    xmlwriter.endTag();

  }



  /**

  * Used the XMLWriter to serialize the Body element.

  * The body is stored in the body Vector.  The elements

  * of the body vector is either String representing 

  * arbitrary XML or Object[] indicating name/value pairs.

  * @param xmlwriter aids in writing the XML.

  * @param registry instance of TypeMappingRegistry mapping

  * each custom class to serialize/deserialize to a namespace

  * and the serializer and deserializer class.

  * @since 0.90

  * @param methodName the name of the method to invoke on the

  * server

  * @param targetURI the name of the service to invoke 

  * throws Exception if any errors occur in serialization

  */

  private void writeBody(XMLWriter xmlwriter, TypeMappingRegistry registry,

			 String methodName, String targetURI)

    throws Exception {

    //write the Body element

    xmlwriter.startElement ("Body", 

			    Constants.SOAP_NAMESPACE);

        

    boolean endMethod=false;

    if (methodName != null /*&& targetURI != null*/) 

    {

      endMethod=true;
      if (targetURI!=null)
        xmlwriter.startElement (methodName, targetURI);
      else
        xmlwriter.startElement (methodName);

    } //if



    //Declare the BaseSerializer.  This is instantiated later,

    // if necessary

    BaseSerializer bs = null;



    for (int i=0; body != null && i < body.size(); i++)

    {

      if (body.elementAt(i) instanceof String)

      {

          //An arbitrary XML body. Just spit it out.
          xmlwriter.elementBodyNotEncoded((String) body.elementAt(i));
      } //if

      else if (body.elementAt(i) instanceof Object[])

      {

	// An Object[] indicates a parameter name and value combination

	if (bs ==null) 

	{

	  bs = new BaseSerializer();

	  bs.setSchemas (getSchema(), 

			 getSchemaInstance(), 

			 getEncodingStyle());

	}//if

	bs.serialize (xmlwriter,

		      registry, 

		      (String) ((Object[]) body.elementAt(i))[0],

		      (Object) ((Object[]) body.elementAt(i))[1]

		      );

      } //else

    } //for



    //End the methodName, if necessary

    if (endMethod) xmlwriter.endTag();

    // End the Body element

    xmlwriter.endTag();

    if (bs!=null)  bs=null;

  } //writeBody



  /**

  * Takes the response from the service as a 

  * byte[] and converts it into Java representation

  * @since 0.90

  * @param response the response from the service

  * @param registry instance of TypeMappingRegistry mapping

  * each custom class to serialize/deserialize to a namespace

  * and the serializer and deserializer class.

  * @throws IOException if any error occurs in parsing the

  * payload returned.

  * @throws SOAPException if any error occurs during

  * deserialization

  * @throws Exception any other unusual exceptions.

  */

  public void deserialize(byte[] response, TypeMappingRegistry registry)

    throws IOException, SOAPException,Exception {

        

    if (response==null)

      return;

    XmlParser parser=null;

    if (getEncoding()!=null) 

      parser = new XmlParser(

			     new InputStreamReader( 

						   new ByteArrayInputStream(response),

						   getEncoding()));

        

    else 

      parser = new XmlParser(

			     new InputStreamReader(

						   new ByteArrayInputStream(response)));

        



    while (parser.peek().getType() != Xml.END_DOCUMENT) {



      if (parser.peek().getType()==Xml.START_TAG &&

	  parser.peek().getName().equals("Envelope")) {

	parseEnvelope(parser);

      }

      else if (parser.peek().getType()==Xml.START_TAG &&

	       parser.peek().getName().equals("Header")) {

	parseHeader(parser);

      }

      else if (parser.peek().getType()==Xml.START_TAG &&

	       parser.peek().getName().equals("Body")) {

	if (this.isDOC==false) 

	  parseBody(parser, registry);

	else

	  break;

      }

      else { 

	/** 

	* Probably encountered a whitespace.  Just skip it.

	*/

	parser.read();

      }

    } //while





  } /* deserialize(byte[], TypeMappingRegistry) */

   

  /**

  * Parses the Envelope element of a SOAP 

  * message.  All we care about is the 

  * encodingStyle, if present.

  * @since 0.90

  * @param parser instance of XmlParser

  * @throws IOException if any error occurs

  * during parsing.

  * @throws SOAPException if any error occurs

  * during deserialization.

  */

  private void parseEnvelope (XmlParser parser)

    throws SOAPException, IOException {

       

    StartTag event=null;

    while (true) {

      event=(StartTag)parser.read();

      PrefixMap pm = event.getPrefixMap();

      String str = pm.getNamespace("xsi");

      if (str != null) setSchemaInstance(str);

      str = pm.getNamespace("xsd");

      if (str != null) setSchema(str);

       

      if (event.getType()==Xml.START_TAG &&

	  event.getName().equals("Envelope")) {

	//Found Envelope. Get all attributes

	super.deserialize(event.getAttributes());

	break;

      } //if

    } //while

  } /* parseEnvelope(XmlParser) */

   

  /**

  * Parse the Header element.  A Header is optional

  * in a soap message

  * @since 0.90

  * @param parser instance of XmlParser to read parse

  * the <SOAP-ENV:Header> element.

  * @throws IOException if any error occurs during

  * parsing.

  */



  private void parseHeader(XmlParser parser)

    throws IOException {

    ParseEvent event=null;

    String headerEntryName=null;

    String headerEntryNS=null;

    String headerEntryValue=null;

    Vector theAttributes=null;



    //Skip the Header element.  We are interested

    //in HeaderEntry

        

    parser.read();



    //Get the HeaderEntry

    while (true) {

      event=parser.read();

      if (event.getType()==Xml.END_TAG && 

	  event.getName().equals("Header")) {

	break;

      }

      else if (event.getType()==Xml.START_TAG) {

	//Process the HeaderEntry

	headerEntryName=event.getName();

	headerEntryNS=event.getNamespace();

	theAttributes = event.getAttributes();



      }

      else if (event.getType()==Xml.TEXT) {

	headerEntryValue=event.getText();

	HeaderEntry he = new HeaderEntry(headerEntryName, 

					 headerEntryValue);

	he.setNamespace(headerEntryNS);

	he.deserialize(theAttributes);

	addHeader(he);

      } //if

    } //while

       

  }  /*parseHeader(XmlParser)*/



  /**

  * Parsers the Body element in the SOAP message. Each

  * return parameter is stored in the Vector body.

  * @param parser instance of XmlParser to parse the

  * payload.

  * @param registry instance of TypeMappingRegistry mapping

  * each custom class to serialize/deserialize to a namespace

  * and the serializer and deserializer class.

  * @throws IOException if any error occurs in parsing the

  * payload returned.

  * @throws SOAPException if any error occurs during

  * deserialization

  * @throws Exception any other unusual exceptions.

  */

  private void parseBody(XmlParser parser, TypeMappingRegistry registry)

    throws SOAPException, IOException,Exception {



    ParseEvent event = null;



    /**

    * Read the Body element. All we care about

    * here is the encodingStyle attribute.

    */

    StartTag tag = (StartTag) parser.read();

    Vector theAttributes=tag.getAttributes();

    for (int i=0; theAttributes !=null && 

	   i<theAttributes.size(); i++) {

      Attribute attr = (Attribute) theAttributes.elementAt(i);

      if (attr.getName().trim().equals("encodingStyle")) {

	setEncodingStyle(attr.getValue().trim());

      }

    } //for

 

    /**

    * Read the first element below Body. Usually

    * this is the method name in the request suffixed

    * with Response or the Fault element.

    * If Fault, process the Fault; else do nothing

    * with this element.

    */
    BaseSerializer bs = new BaseSerializer();

    bs.setSchemas(getSchema(),

		  getSchemaInstance(),

		  getEncodingStyle());

    while (true) {

      /**

      * Peek each starting element in the body 

      * repeatedly until the </Body> is encountered.

      * Determine the namespace of the datatype.

      */

      event = parser.peek();

      if (event.getType()==Xml.END_TAG &&

	  event.getName().trim().equals("Body")) 

	break;

      else if (event.getType()==Xml.START_TAG &&

	       event.getName().equals("Fault") &&

	       event.getNamespace().equals(Constants.SOAP_NAMESPACE)) {

	Fault fault = new Fault(parser);

	this.faultGenerated=true;

	this.faultObject=fault;

      }

      else if (event.getType()==Xml.START_TAG) {

	            

	if (this.methodName==null) {

	  Attribute rootAttr = event.getAttribute(Constants.SOAP_ENCODING_STYLE,

						  "root");

	  if (rootAttr==null || 

	      !(rootAttr.getValue().trim().equals("0"))) {

	    this.methodName=event.getName();
	    this.targetURI=event.getNamespace();

	    parser.read();

	    continue;

	  } //if

	} //if

	String theID=event.getValueDefault("id", null);

	/**

	* Call the deserializer to deserialize the

	* element. The parameter name is strictly

	* not required but is necessary to put the

	* return object in the hashtable.

	*/

	String paramName=event.getName();

	Object o = bs.deserialize(parser, registry);



	if (theID != null && body!=null) { 

	  /**

	  * Go through the body to determine if

	  * there is an href.

	  */

	  Vector substitution=new Vector();

	  for (int i=0; i<body.size(); i++) {

	    Object aParameter=((Object[])body.elementAt(i))[1];

	    if (aParameter instanceof UntypedObject &&

		((UntypedObject)aParameter).hrefID!=null &&

		((UntypedObject)aParameter).hrefID.equals(theID)) {

	      Object[] aParamArray=(Object[])body.elementAt(i);

	      aParamArray[1]=o;

	      substitution.addElement("true");

	    }

	    else

	      consolidateHref(theID,o,body.elementAt(i), 

			      substitution);

	  } //for

	  if (substitution.size()==0)

	    setBody(paramName,o, theID);

	}

	else 

	  setBody(paramName,o, theID);

      }

      else {

	//Not sure what kind of element. Just skip;

	parser.read();

      }

    } //while



    /**

    * The Body is now completely processed. Check to see

    * if there is any outstandng Href to substitute.  Some

    * toolkits emphasise on serialization order (Pocket PC)

    * and send the id first and then the href.

    */

    substituteOutstandingHref();



  } //parseBody



  private void substituteOutstandingHref() {

    if (body==null) 

      return;

    for (int i=0; i<body.size(); i++) {

      Object o[]=(Object[]) body.elementAt(i);

      if (o!=null && o[2]!=null)  {

	/**

	* The parameter in the body has an ID

	*/

	String theID=(String) o[2];

	Vector subst=new Vector();



	for (int j=0; j<body.size(); j++) {

	  Object aParameter=((Object[])body.elementAt(j))[1];

	  if (aParameter instanceof UntypedObject &&

	      ((UntypedObject)aParameter).hrefID!=null &&

	      ((UntypedObject)aParameter).hrefID.equals(theID)) {

	    Object[] aParamArray=(Object[])body.elementAt(j);

	    aParamArray[1]=o[1];

	    subst.addElement("true");

	  }

	  else {

	    consolidateHref(theID,o[1],

		            body.elementAt(j), subst);

	  }

	} //for j

	if (subst.size()>0) {

	  body.removeElementAt(i);

	  i--;

	}

      } //if

    } //for i

  } /*substituteOutstandingHref*/

    

    /**

    * Checks the parameter list to determine if

    * there is an instance of HrefHolder with

    * id equal to paramName. If there is one,

    * instance of HrefHolder is substituted with

    * the Object.

    * @param theID the value of the href attribute

    * @param trueValue the true value of the element that

    * @param theObj the object to interrogate to see if 

    * a href is present.

    * has href attribute.

    * @param substitution - Vector to indicate the number 

    * of substitutions.

    */

  private Object consolidateHref(String theID,

                                 Object trueValue,

				 Object theObj, Vector substitution) {



    if (theObj==null) return "false";



    /**

    * Ultimately everything will be a HrefHolder. If

    * one is found, it is a candidate for replacement.

    */

    if (theObj instanceof UntypedObject &&

	((UntypedObject)theObj).hrefID !=null) {



      UntypedObject obj = (UntypedObject) theObj;

      if (obj.hrefID.equals(theID)) 

	return "true";

    } //if

    else if (theObj instanceof Vector) {

      Vector v = (Vector)theObj;

      for(int i=0; i<v.size(); i++) {

	String ret=(String)consolidateHref(theID, trueValue, 

					   v.elementAt(i),substitution);

	if (ret.equals("true")) {

	  v.removeElementAt(i);

	  v.insertElementAt(trueValue,i);

	  substitution.addElement("true");

	}

      }//for

    }

    else if (theObj instanceof Hashtable) {

      Hashtable h = (Hashtable)theObj;

      Enumeration e = h.keys();

      while (e.hasMoreElements()) {

	Object key=e.nextElement();

	String ret=(String)consolidateHref(theID, trueValue,

					   key,substitution);

	if (ret.equals("true")) {

	  Object value = h.get(key);

	  h.remove(key);

	  h.put(trueValue,value);

	  substitution.addElement("true");

	  //break;

	}

	Object theValue=h.get(key);

	ret=(String)consolidateHref(theID, trueValue,

		                    theValue,substitution);

	if (ret.equals("true")) {

	  h.remove(key);

	  h.put(key,trueValue);

	  substitution.addElement("true");

	}

      }

    } //hashtable

    else if (theObj.getClass().isArray()) {

      Object[] array = (Object[])theObj;

      for (int i=0; i<array.length; i++) {

	String ret=(String)consolidateHref(theID, trueValue,

		                           array[i],substitution);

	if (ret.equals("true")) {

	  array[i]=trueValue;

	  substitution.addElement("true");

	}

      }//for

    }

    else if (theObj instanceof WSerializable) {

      WSerializable ws = (WSerializable) theObj;

      int ctr = ws.getPropertyCount();

      for (int i=0; i<ctr; i++) {

	String ret=(String)consolidateHref(theID, trueValue,

		                           ws.getPropertyValue(i),

					   substitution);

	if (ret.equals("true")) {

	  //String name = ws.getPropertyName(i);

	  //ws.removeProperty(i);

	  ws.setPropertyAt(trueValue,i);

	  substitution.addElement("true");

	  /**

	  * the return below has been added for Angelo

	  * Primeveria.  This has to be tested.

	  */

	  //return ws;

	}

      }//for

    }

    return "false";

 

  } /*consolidateHref*/ 

    /**
     * For RPC style response, the parameter is wrapped
     * with an element.  This can be viewed as an
     * operation name.  The method returns the 
     * operation name.
     * @return String the element name that is wrapped
     * around the parameters; null if none was specified.
     */
  public String getReturnOperationName() 
  {
    return this.methodName;
  }

  /**
   * Returns the namespace associated with the return
   * operation.
   * @return String the namespace associated with the
   * return operation name; null if none is specified.
   */
  public String getReturnNamespace() 
  {
    return this.targetURI;
  }
} /* com.wingfoot.soap.Envelope */

