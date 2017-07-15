/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */

package com.wingfoot.soap.encoding;

import java.util.*;
import org.kxml.io.*;
import com.wingfoot.soap.*;
import org.kxml.parser.*;
import org.kxml.*;
import java.io.*;
/**
 * Converts primitive wrapper classes to XML. Conversely
 * converts XML to primitive wrapper classes.  Determines 
 * if a custom class is registered in an instance of
 * TypeMappingRegistry.  If it is, the appropriate instance
 * of custom SerializerDeserializer is created and 
 * invoked.
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */

public class BaseSerializer {

  /**
   * Following three variables used during the serialization process.
   * TO DO: look at ways to avoid this.  bad design.
   */
  private String schema; 
  private String schemaInstance; 
  private String encodingStyle;

  /**
   * Utility method to set the schema, schemaInstance and 
   * encodingStyle.  Usually set by the Envelope.
   * @since 0.90
   * @param schema String representing the schema.
   * @param schemaInstance String representing the schemaInstance.
   * @param encodingStyle String representing the encodingStyle.
   */
  public void setSchemas (String schema, 
                          String schemaInstance, 
                          String encodingStyle) 
  {
    this.schema = schema;
    this.schemaInstance = schemaInstance;
    this.encodingStyle = encodingStyle;
  } /* setSchema */


  /**
   * Returns the schemaInstance
   * @return the schemaInstance
   */
  public String getSchemaInstance() 
  {
    return schemaInstance;
  } /* getSchemaInstance */

    /**
     * Converts a parameter represented as Java objects to XML. 
     * The parameter is specified using the setBody method in Envelope.
     * Specifically, converts instances of Vector, Hashtable, Date
     * and primitive wrapper (Byte, Short ...) and arrays to XML.  
     * If the Java object is not an instance of one of the above
     * mentioned  class, then  the TypeMappingRegistry is accessed 
     * to determine the appropriate serializer class.
     * Custom serializers (which implement SerializerDeserializer interface) call
     * this method to serialize parameters.
     * @since 0.90
     * @param xmlwriter instance of XMLWriter to aid in writing XML.
     * @param registry TypeMappingRegistry provided by the user in
     * in the Envelope.
     * @param parameterName the name of the parameter
     * @param objectToSerialize the value of the parameter represented
     * as an Object.
     * @throws Exception if any error occurs during writing XML 
     * or during serialization process.
     */

  public void serialize (XMLWriter xmlwriter, 
                         TypeMappingRegistry registry, 
                         String parameterName,
                         Object objectToSerialize ) 
                         throws Exception 
    {
         
    if (objectToSerialize==null) 
    {
      xmlwriter.startElement(parameterName);
      xmlwriter.attribute ("xsi:null",
			   "true");
      xmlwriter.endTag();
      return;
    } /* if */

    /**
     * Check the registry to see if the vectorElement has a registered serializer
     * Instances of BubblesSerializer will fall here.
     * First preference is always given to the mappings created by the user.
     * If mapping is not found, the default mappings are used.
     */
         
    String[] strarray = registry != null ? 
      registry.getInfoForClass(objectToSerialize.getClass().getName()) :
      null;
    if (strarray != null) {
      Class serializerClass = Class.forName(strarray[2]);
      SerializerDeserializer serializer = 
      (SerializerDeserializer) serializerClass.newInstance();
      serializer.marshall (xmlwriter, 
                           parameterName, 
                           objectToSerialize, 
                           registry, 
                           this);
    } //if
         
    /**
     * The class is not mapped by the user.
     * Check to see if this is one of the class automatically serialized by Bubbles
     * The following classes are automatically serialized
     * Byte, Short, Integer, Long, Float, Boolean, 
     * Date, Vector, Hashtable and Base64
     */

    else if (objectToSerialize instanceof java.util.Vector) 
    {
      serializeVector (xmlwriter, parameterName, objectToSerialize, registry);
    } 
    else if (objectToSerialize instanceof java.util.Hashtable) 
    {
      serializeHashtable (xmlwriter, parameterName, objectToSerialize, registry);
    } 
    else if (objectToSerialize instanceof java.util.Date) 
    {
      serializeDate(xmlwriter, parameterName, objectToSerialize /*, registry*/);
    } 
    else if (objectToSerialize.getClass().isArray()) {
    
    
      /* Only primitive array will come here.
         Custom arrays have to be registered.
         Vector and Hashtable arrays will also drop here.
      */
      serializePrimitiveArray (xmlwriter, parameterName, objectToSerialize, registry);
    } 
    else 
    {
      /**
       * In all probability this is a primitive wrapper type.  
       * If not an exception is thrown in serializePrimitive
       */
      serializePrimitive(xmlwriter, parameterName, objectToSerialize);
    }
  } /* serialize(XMLWriter, TypeMappingRegistry, String, Object) */

  /**
   * Converts a parameter that is an instance of Date to 
   * XML.  
   * @since 0.90
   * @param xmlwriter instance of XMLWriter to aid in writing XML.
   * @param parameterName the name of the parameter
   * @param objectToSerialize the value of the parameter represented
   * @param registry TypeMappingRegistry provided by the user in
   * in the Envelope.
   * @throws Exception if any error occurs during writing XML 
   * or during serialization process.
   */
  private void serializeDate (XMLWriter xmlwriter, 
                              String parameterName, 
                              Object o /*, 
                              TypeMappingRegistry registry */)
                              throws Exception 
  { 
        
    Date d = (Date) o;
    Calendar cal = Calendar.getInstance();
    cal.setTime(d);
    TimeZone tz = cal.getTimeZone();
    int offset = tz.getOffset(
			      1,
			      cal.YEAR,
			      cal.MONTH+1,
			      cal.DAY_OF_MONTH,
			      cal.DAY_OF_WEEK,
			      01);
    d.setTime(d.getTime() - offset);
    cal = Calendar.getInstance();
    cal.setTime(d);
    StringBuffer sb = new StringBuffer();
    sb.append(cal.get(cal.YEAR)+"-" +
	      padInteger((cal.get(cal.MONTH))+1) + "-" +
	      padInteger(cal.get(cal.DAY_OF_MONTH)) + "T"+
	      padInteger(cal.get(cal.HOUR_OF_DAY))+":"+
	      padInteger(cal.get(cal.MINUTE))+":"+
	      padInteger(cal.get(cal.SECOND))+"."+
	      padInteger(cal.get(cal.MILLISECOND))+"Z");

    xmlwriter.startElement(parameterName);
    xmlwriter.attribute ("xsi:type",
			 "xsd:dateTime");
    xmlwriter.elementBody(sb.toString());
    xmlwriter.endTag();
  } /*serializeDate*/

    /**
     * Utility method to return a two digited string
     * representation for a int.
     * @since 0.90
     * @param value the integer for which a two digited
     * string representation is needed.
     * @return String representation of the int.
     */
  private String padInteger(int value) 
  {
    return value < 10 ? "0"+value : value+"";
  }

  /**
   * Converts a parameter that is an instance of Vector to 
   * XML.  
   * @since 0.90
   * @param xmlwriter instance of XMLWriter to aid in writing XML.
   * @param parameterName the name of the parameter
   * @param objectToSerialize the value of the parameter represented
   * @param registry TypeMappingRegistry provided by the user in
   * in the Envelope.
   * @throws Exception if any error occurs during writing XML 
   * or during serialization process.
   */
  private void serializeVector (XMLWriter xmlwriter, 
                                String parameterName, 
                                Object o, 
                                TypeMappingRegistry registry)
                                throws Exception 
  {

    xmlwriter.startElement(parameterName);
        
    xmlwriter.attribute ("type", schemaInstance, 
			 "Vector", Constants.DEFAULT_NAMESPACE);

    Vector vector = (Vector) o;

    for (int i=0; i<vector.size(); i++)
    {
      Object vectorElement = vector.elementAt(i);
      this.serialize (xmlwriter, registry, "vectoritem", vectorElement);
    } //for

    //Close the element tag
    xmlwriter.endTag();
  } /* serializeVector(XMLWriter, String, Object, TypeMappingRegistry) */

  /**
   * Converts a parameter that is an instance of Hashtable to 
   * XML.  
   * @since 0.90
   * @param xmlwriter instance of XMLWriter to aid in writing XML.
   * @param parameterName the name of the parameter
   * @param objectToSerialize the value of the parameter represented
   * @param registry TypeMappingRegistry provided by the user in
   * in the Envelope.
   * @throws Exception if any error occurs during writing XML 
   * or during serialization process.
   */
  private void serializeHashtable(XMLWriter xmlwriter, 
                                  String parameterName, 
                                  Object o, 
                                  TypeMappingRegistry registry)
                                  throws Exception 
  {
    xmlwriter.startElement(parameterName);

    xmlwriter.attribute ("type", schemaInstance, 
			 "Map",Constants.DEFAULT_NAMESPACE);

    Hashtable hashtable = (Hashtable) o;
    Enumeration keys = hashtable.keys();
    while (keys.hasMoreElements()) 
    {
      xmlwriter.startElement("mapitem");
      Object theKey = keys.nextElement();
      this.serialize (xmlwriter, registry, "key", theKey);
      this.serialize (xmlwriter, registry, "value", hashtable.get(theKey));
      xmlwriter.endTag();
    } //while

    //Close the element tag
    xmlwriter.endTag();
  } /* serializeHashtable(XMLWriter, String, Object, TypeMappingRegistry) */

  /**
   * Converts a parameter that is an instance of 
   * primitive wrapper instance to XML. Also included
   * here are encoding.Float and encoding.Base64.
   * @since 0.90
   * @param xmlwriter instance of XMLWriter to aid in writing XML.
   * @param parameterName the name of the parameter
   * @param data the value of the parameter represented
   * @throws Exception if any error occurs during writing XML 
   * or during serialization process.
   */
  private void serializePrimitive (XMLWriter xmlwriter, 
                                   String parameterName, 
                                   Object data) 
  { 
    xmlwriter.startElement(parameterName);

    if (data instanceof Integer) 
    {
      xmlwriter.attribute ("xsi:type", "xsd:int");
    }
    else if (data instanceof Base64) 
    {
      if (schema.equals(Constants.SOAP_SCHEMA))
        xmlwriter.attribute("type",schemaInstance, 
                            "base64Binary", schema);
      else
        xmlwriter.attribute("type",schemaInstance, 
                            "base64", Constants.SOAP_ENCODING_STYLE);
    }
    else 
    {
      String className = data.getClass().getName();
      int index = className.lastIndexOf('.');
      xmlwriter.attribute ("xsi:type",
                           "xsd:"+className.substring(index+1).toLowerCase());
    }
    xmlwriter.elementBody(data.toString());
    xmlwriter.endTag();

  } /* serializePrimitive( XMLWriter, String, Object)*/

    /**
     * Converts a parameter that is an instance of a primitive
     * wrapper array to XML.
     * @since 0.90
     * @param xmlwriter instance of XMLWriter to aid in writing XML.
     * @param parameterName the name of the parameter
     * @param data the value of the parameter represented
     * @param registry TypeMappingRegistry provided by the user in
     * in the Envelope.
     * @throws Exception if any error occurs during writing XML 
     * or during serialization process.
     */
  private void serializePrimitiveArray (XMLWriter xmlwriter, 
                                        String parameterName, 
                                        Object data,
                                        TypeMappingRegistry registry) 
                                        throws Exception 
    {

    if(data instanceof byte[]) {
      /**
       * This block will catch a byte array and Base64 it for
       * transport
       */
      Base64 b64 = new Base64((byte[])data);
      serialize (xmlwriter, registry, parameterName, b64);
    } else {
      String theNamespace=this.schema;
      Object[] objectArray = (Object[]) data;
      String arrayType="";
	    
      if (isArrayHetrogeneous(objectArray))
        arrayType="ur-type[";
	    
      else {  /* A homogeneous array */
		
        /**
         * This might be an array of custom JavaBeans.  Look at
         * the registry
         */
        String[] strarray = registry != null ? 
        registry.getInfoForClass(objectArray[0].getClass().getName()) :
        null;
        if (strarray!=null) {
          /** this is a homogeneous array of struct **/
          theNamespace = strarray[0];
          arrayType=strarray[1]+"[";
        }
        else if (data instanceof Integer[]) 
          arrayType="int[";
        else if (data instanceof Hashtable[])
          arrayType="Map[";
        else {
		    
          String className=data.getClass().getName();
          int index=className.lastIndexOf('.');
          String xsdVar="";
          xsdVar=className.endsWith(";") ?
          className.substring(index+1, className.length()-1) :
          className.substring(index+1);
          arrayType=xsdVar.toLowerCase()+"[";
        } //else
      } //else
	    
      /**
       * Write the array type. Retrieve each element of the
       * array and serialize it.
       */
      xmlwriter.startElement(parameterName);
      xmlwriter.attribute ("type", schemaInstance, "Array", encodingStyle); 
	    
      xmlwriter.attribute ("arrayType", encodingStyle, 
			   arrayType+objectArray.length+"]", theNamespace);
      for (int i=0; i<objectArray.length; i++) {
        serialize (xmlwriter, registry,"item", objectArray[i]);
      }
      xmlwriter.endTag();
    } //else
  } /* serializePrimitiveArray */
    
    /**
     * Determines if a given array is hetrogeneous.  An
     * Array is hetrogeneous if any element of the array
     * belongs to a different class that any other
     * element of the array.
     * @param array the array to interrogate.
     * @return true if the array is hetrogeneous, false
     * if not.
     */
  private boolean isArrayHetrogeneous(Object[] array) 
  {
        
    boolean returnValue=false;
    Object previousElement=null;

    for (int i=0; i<array.length; i++) 
    {
      if (i!=0 && array[i]!=null && (! array[i].getClass().getName().equals(
									    previousElement.getClass().getName()))) 
      {
        returnValue=true;
        break;
      } //else
      previousElement=array[i];
    } //for

    return returnValue;

  } /* isArrayHetrogeneous */

  /**
   * Identical to the overloaded deserialize except that
   * the datatype the XML element has to be deserialized
   * to is not known and will be determined by the method.
   * @since 0.90
   * @parser instance of XmlParser to read the incoming
   * XML payload.
   * @param registry TypeMappingRegistry provided by the user in
   * in the Envelope.
   * @throws IOException if any error occurs in parsing
   * the XML.
   * @throws SOAPException if any error occurs in deserialziing
   * the parameter
   * @throws Exception if any other error occurs.
   */
  public Object deserialize (XmlParser parser,
                             TypeMappingRegistry registry)
                             throws IOException, SOAPException,
                             Exception 
  {
    return deserialize(parser,registry,null,null);
  } /*deserialize(XmlParser, TypeMappingRegistry)*/

  /**
   * Takes a <SOAP-ENV:Body> element from a SOAP
   * message and converts the return parameters, if 
   * present, to Java Objects.  The parameter name 
   * and the parameter value, represented as Java
   * object, are stored using Envelope.setBody(String, Object)
   * method.
   * Custom deserializers (which implement SerializerDeserializer)
   * call this method to convert the return parameters
   * to first class java object.
   * @since 0.90
   * @parser instance of XmlParser to read the incoming
   * XML payload.
   * @param registry TypeMappingRegistry provided by the user in
   * in the Envelope.
   * @param expectedNamespace
   * @param expectedType this and expectedNamespace is required
   * only if the type the parameter should be deserialized to is
   * already known.
   * @return the value of the parameter as a Java object.
   * @throws IOException if any error occurs in parsing
   * the XML.
   * @throws SOAPException if any error occurs in deserialziing
   * the parameter
   * @throws Exception if any other error occurs.
   */
  public Object deserialize(XmlParser parser,
                            TypeMappingRegistry registry,
                            String expectedNamespace,
                            String expectedType)
                            throws IOException,SOAPException,
                            Exception 
  {
    /**
     * Deserialize is always called with the parser
     * pointing to a Start_Tag.  CONFIRM THIS
     */
    StartTag event = (StartTag)parser.peek();
    /**
     * If this element has a href attribute, then
     * create and UntypedObject, mark it as a href
     * and simply return.  No need to do anything else
     */

    if (event.getValueDefault("href", null) !=null) 
    {
      UntypedObject uo = new UntypedObject();
      uo.hrefID=event.getValueDefault("href",null).substring(1);
      parser.read();
      //Read the end tag
      parser.read();
      return uo;
    } //if

    /**
     * The first task is to determine the namespace and
     * datatype.
     */
    String[] valueNS = parseAttributeValue(
                                           "type",
                                           schemaInstance,
                                           event);

    if (valueNS[0]=="" && valueNS[1].equals(event.getName()) &&
      expectedType !=null) 
    {
        valueNS=new String[]
                { expectedNamespace,
                  expectedType};
    } //if

    String[] nsMap=null;
    String[] elementMap = null;
    
    if (registry != null) 
    {
      nsMap = registry.getInfoForNamespace(valueNS[0],
					   valueNS[1]);
      /**
       *  If the type is not mapped, maybe the element
       *  is mapped.  Look to see if the element has been
       *  mapped.
       */
      if (nsMap ==null)
        elementMap = registry.getClassForElement(event.getName());
    }

    if (nsMap!=null || (elementMap != null &&
        elementMap[0] != null &&
        elementMap[1]!= null)) 
    {

      //Call Serializer based on the class
      String beanInstanceName=null;
      String deserializerClassName=null;
	      
      if (nsMap != null) 
      {
        beanInstanceName=nsMap[0].trim();
        deserializerClassName=nsMap[2].trim();
      }
      else 
      {
        beanInstanceName=elementMap[0].trim();
        deserializerClassName=elementMap[1].trim();
      }
      Object beanInstance = Class.forName(beanInstanceName).newInstance();
      SerializerDeserializer deserializerClass = (SerializerDeserializer) Class.forName(deserializerClassName).newInstance();
      deserializerClass.unmarshall(parser,
                                   registry,
                                   beanInstance,
                                   this);
      return beanInstance;
    }
    else if (valueNS[1].trim().equals("Vector") ||
	     (elementMap!=null && elementMap[0].endsWith("Vector"))) 
    {
      return deserializeVector(parser,registry,null,null);
    }
    else if (valueNS[1].trim().equals("Map")) 
    {
      return deserializeHashtable(parser,registry);
    }
    else if (valueNS[1].trim().equals("dateTime") ||
	     (elementMap!=null && elementMap[0].endsWith("Date"))) 
    {
      return deserializeDate(parser);
    }
    else if ((valueNS[1].trim().equals("Array") &&
	      valueNS[0].trim().equals(Constants.SOAP_ENCODING_STYLE)) 
	     || (elementMap!=null && 
		 Class.forName( elementMap[0]).isArray()))
    {
      return  deserializeArray(parser, registry);
    }
    else if (valueNS[0]=="" && valueNS[1].equals(event.getName())
	     && elementMap != null && elementMap[0] !=null) 
      return  deserializePrimitive(parser, 
                                   elementMap[0].substring((elementMap[0].lastIndexOf('.'))+1)) ;
    else if (valueNS!=null && valueNS[0] !="" &&
	     (valueNS[0].equals(Constants.SOAP_SCHEMA) ||
	      valueNS[0].equals(Constants.SOAP_SCHEMA_1999) ||
	      valueNS[1].equals("base64") || 
	      valueNS[1].equals("base64Binary")))
      return deserializePrimitive(parser , valueNS[1]);
    else
      return deserializeUndetermined(parser,registry);
  } /* deserialize */

  /**
   * This method attempts to deserialize elements that are
   * not encoded.
   * @since 0.90
   * @parser instance of XmlParser to read the incoming
   * XML payload.
   * @param registry TypeMappingRegistry provided by the user in
   * in the Envelope.
   * @return Object encapsulating the XML element.
   */
  private Object deserializeUndetermined( XmlParser parser,
                                          TypeMappingRegistry registry) 
                                        throws Exception 
  {

    Object o = null;
    ParseEvent event = parser.read();
    String end = event.getName().trim();
    while (true) {
      if ( parser.peek().getType() == Xml.END_TAG &&
        parser.peek().getName().trim().equals(end))
        break;

      else if (parser.peek().getType()==Xml.TEXT) {
      /** Create a custom privitive object **/
        o = new String (parser.read().getText().trim()); 
      }

      else if (parser.peek().getType()==Xml.START_TAG) {
        /** Create a custom Java object **/
        UntypedObject uo = (UntypedObject)new UntypedObject();
        do 
        {
          String name=parser.peek().getName().trim();
          Object robject = deserialize(parser,registry);
          uo.setProperty(name, robject);
          while (parser.peek().getType() != Xml.END_TAG &&
                parser.peek().getType() != Xml.START_TAG)
            parser.read();
          } 
          while (!(parser.peek().getType() == Xml.END_TAG &&
                parser.peek().getName().trim().equals(end)));
          o=uo;
      }
      else {
        // might just be a whitespace.
        parser.read();
      }
    } /* while true */
    parser.read();
    return o;
  } /*deserializeUndetermined*/
    
  /**
   * Utility method to break up the attribute value
   * into the namespace component and value component.
   * In the example xsi:type=n2:Vector, namespace is the
   * URI corresponding to n2 and the name is Vector.
   * @since 0.90
   * @param attribute the name of the attribute.
   * @param attributeNS the namespace for the attribute.
   * @param event instance of org.kxml.parser.StartTag.
   * This points to a element tag.
   * @return String[] where String[0] is namespace
   * and String[1] is name
   * @throws IOException if any error occurs during
   * XML parsing
   */
  private String[] parseAttributeValue(String attribute,
                                       String attributeNS,
                                       StartTag event) 
                                       throws IOException 
  {
    Attribute typeAttr = event.getAttribute(attributeNS, attribute);
    String name=null;
    String namespace=null;
    if (typeAttr != null) 
    {
      String type = typeAttr.getValue ();
      int cut = type.indexOf (':');
      name = type.substring (cut + 1);
      String prefix = cut == -1 ? "" : type.substring (0, cut);
      namespace = event.getPrefixMap ().getNamespace (prefix);
    }
    else 
    {
      name = event.getName ();
      //namespace = event.getNamespace ();
      namespace="";
    }
    return new String[] { namespace, name};
  } /* parseAttributeValue */
    
  /**
   * Converts a Vector, represented as XML to a Vector object.
   * @since 0.90
   * @parser instance of XmlParser to read the incoming
   * XML payload.
   * @param registry TypeMappingRegistry provided by the user in
   * in the Envelope.
   * @param expectedNamespace
   * @param expectedType this and expectedNamespace is required
   * only if the type the parameter should be deserialized to is
   * already known.
   * @return instance of Vector
   * @throws IOException if any error occurs in parsing
   * the XML.
   * @throws SOAPException if any error occurs in deserialziing
   * the parameter
   * @throws Exception if any other error occurs.
   */
  private Vector deserializeVector(XmlParser parser,
                                   TypeMappingRegistry registry,
                                   String expectedNamespace,
                                   String expectedType)
                                   throws IOException,
                                   SOAPException,Exception 
  {
    Vector v = new Vector();
    ParseEvent event = parser.read();
    String elementName=event.getName().trim();
    while(true) {
      if (parser.peek().getType()==Xml.END_TAG &&
        parser.peek().getName().trim().equals(elementName)) 
      {
        break;
      }
      else if (parser.peek().getType()==Xml.START_TAG)
      {
        v.addElement(deserialize(parser, registry,
				expectedNamespace, expectedType));
      }              
      else 
      {
        //Not sure what it is. Just skip;
        parser.read();
      }
    } //while
    //read the closing tag.
    parser.read();
    return v;
  }/*deserializeVector*/

  /**
   * Converts a Hashtable, represented as XML to a Hashtable object.
   * @since 0.90
   * @parser instance of XmlParser to read the incoming
   * XML payload.
   * @param registry TypeMappingRegistry provided by the user in
   * in the Envelope.
   * @return instance of Hashtable
   * @throws IOException if any error occurs in parsing
   * the XML.
   * @throws SOAPException if any error occurs in deserialziing
   * the parameter
   * @throws Exception if any other error occurs.
   */
  private Hashtable deserializeHashtable(XmlParser parser,
                                         TypeMappingRegistry registry) 
                                         throws IOException,
                                         SOAPException,Exception
  {
    Object key=null, value=null;
    Hashtable v = new Hashtable();
    ParseEvent event = parser.read();
    String elementName=event.getName().trim();
    while (true) {
      if (parser.peek().getType()==Xml.END_TAG &&
        parser.peek().getName().trim().equals(elementName))
        break;
      else if (parser.peek().getType()==Xml.START_TAG &&
	       parser.peek().getName().trim().equals("key"))
        key=deserialize(parser,registry);
      else if (parser.peek().getType()==Xml.START_TAG &&
	       parser.peek().getName().trim().equals("value"))
        value=deserialize(parser,registry);
      else 
      {
        // Not sure what element. Just skip.
        parser.read();
      }
      if (key != null && value != null) 
      {
        v.put(key,value);
        key=null; value=null;
      }
    } //while

    //read the closing tag.
    parser.read();
    return v;
  } /*deserializeHashtable*/
    
  private Date deserializeDate(XmlParser parser /*,
                               TypeMappingRegistry registry */) 
                               throws IOException,
                               SOAPException,Exception 
  {
    Date theDate = null;
    ParseEvent event = parser.read();
    String elementName=event.getName().trim();
    while (true) {
      event = parser.read();
      if (event.getType()==Xml.END_TAG &&
        event.getName().trim().equals(elementName))
        break;
      else if (event.getType()==Xml.TEXT) 
      {
        String strDate=event.getText();
        Calendar cal = Calendar.getInstance();

        cal.set(cal.YEAR, 
          Integer.parseInt(strDate.substring(0,4)));
        cal.set(cal.MONTH, 
          (Integer.parseInt(strDate.substring(5,7))-1));
        cal.set(cal.DAY_OF_MONTH, 
          Integer.parseInt(strDate.substring(8,10)));
        cal.set(cal.HOUR_OF_DAY, 
          Integer.parseInt(strDate.substring(11,13)));
        cal.set(cal.MINUTE, 
          Integer.parseInt(strDate.substring(14,16)));
        cal.set(cal.SECOND, 
          Integer.parseInt(strDate.substring(17,19)));
        /*
          cal.set(cal.MILLISECOND, 
          Integer.parseInt(strDate.substring(20,23)));
        */
        int offset=0; 
        if (strDate.endsWith("Z")) 
          cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        else if (strDate.indexOf('-',11) != -1 ||
           strDate.indexOf('+',11) != -1) {
          int theIndex = strDate.indexOf('+', 11) == -1 ?
            strDate.indexOf('-', 11) :
            strDate.indexOf('+', 11);
          String offsetHour = strDate.substring(
                  theIndex+1, 
                  theIndex+3);
          String offsetMinutes = strDate.substring(
                     theIndex+4);
          offset = 
            (Integer.parseInt(offsetHour)*3600000 +
             Integer.parseInt(offsetMinutes)*60000);
          if (strDate.indexOf('-',11) != -1)
            offset = 0-offset;

          cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        } //else
        theDate = new Date(cal.getTime().getTime()-offset);
      }
    } //while true
    return theDate;
  } /* deserializeDate */
  
  /**
   * Converts a primitive parameter, represented as XML 
   * to a the corresponding primitive wrapper object.
   * @since 0.90
   * @parser instance of XmlParser to read the incoming
   * XML payload.
   * @param registry TypeMappingRegistry provided by the user in
   * in the Envelope.
   * @param parameterType the suffix following xsd: in the return payload.
   * @return Object a primitive wrapper object.
   * @throws IOException if any error occurs in parsing
   * the XML.
   * @throws SOAPException if any error occurs in deserialziing
   * the parameter
   * @throws Exception if any other error occurs.
   */
  private Object deserializePrimitive(XmlParser parser,
                                      /*TypeMappingRegistry registry,*/
                                      String parameterType) 
                                      throws IOException,
                                      SOAPException,Exception 
  {
         
    String value=null;
    ParseEvent event = parser.read();
    parameterType=parameterType.trim().toLowerCase();
    if (parser.peek().getType() == Xml.TEXT)
      value = parser.read().getText().trim();   
    //Read the closing tag

    parser.read();
    if (value==null)
      return null;
    else if (parameterType.equals("string"))
      return new String(value);
    else if (parameterType.equals("byte")) 
      return new Byte(Byte.parseByte(value));
    else if (parameterType.equals("short")) 
      return new Short(Short.parseShort(value));
    else if (parameterType.equals("int") ||
	     parameterType.equals("integer"))
      return new Integer(Integer.parseInt(value));
    else if (parameterType.equals("long")) 
      return new Long(Long.parseLong(value));
    else if ( parameterType.equals("float")) 
      return new com.wingfoot.soap.encoding.Float(value);
    else if (parameterType.equals("date")) 
      return new Date(Long.parseLong(value));
    else if (parameterType.equals("boolean")) 
      return (value.equals("true") 
	      || value.equals("1")) ? 
      new Boolean(true) : new Boolean(false);
    else if (parameterType.equals("base64") ||
	     parameterType.equals("base64binary"))
      //automatically returning the byte[] instead of sending back base64 - Baldwin
      //return new Base64(value).getBytes();
      return new Base64(value);
    else
      throw new SOAPException("Unsupported data type:" 
			      + parameterType );
  } /* deseerializePrimitive */

  /**
   * Converts a primitive array, represented as XML 
   * to a the corresponding primitive wrapper array.
   * @since 0.90
   * @parser instance of XmlParser to read the incoming
   * XML payload.
   * @param registry TypeMappingRegistry provided by the user in
   * in the Envelope.
   * @return Object[] a primitive wrapper array.
   * @throws IOException if any error occurs in parsing
   * the XML.
   * @throws SOAPException if any error occurs in deserialziing
   * the parameter
   * @throws Exception if any other error occurs.
   */
  private Object[] deserializeArray(XmlParser parser,
				    TypeMappingRegistry registry) 
    throws IOException,
	   SOAPException,Exception {

    StartTag event = (StartTag)parser.peek();
    String[] valueNS = parseAttributeValue(
					   "arrayType",
					   Constants.SOAP_ENCODING_STYLE,
					   event);
    Vector v = (valueNS==null ||
		( valueNS[1].equals(event.getName())
		  && valueNS[0]=="")) ? deserializeVector(parser, 
							  registry,
							  null,
							  null) :
      deserializeVector(parser, 
			registry,
			valueNS[0],
			valueNS[1].substring(0, valueNS[1].indexOf('[')));

    if (v!= null && v.size()>0) {
      Object[] o = new Object[v.size()];
      for (int i=0; i< v.size();  i++) {
        o[i]=v.elementAt(i);
      } //for
      return o;
    }
    else return null;
  } /* deserializeArray */ 

} /* com.wingfoot.soap.encoding.BaseSerializer */
