/*

 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.

 * Please see http://www.wingfoot.com for license details.

 */



package com.wingfoot.soap.encoding;
import org.kxml.io.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.encoding.*;
import org.kxml.parser.*;
import org.kxml.*;
import java.io.*;
/**
 * Converts instances of WSerializable to XML
 * and vice-versa.
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */
public class BeanSerializer implements SerializerDeserializer {
  /**
   * Converts a parameter - the user specified using 
   * Envelope.setBody(String, Object) - that is an instance
   * of WSerializable to XML.
   * @since 0.90
   * @param xmlwriter XMLWriter to aid in writing
   * the XML.
   * @param parameterName the name of the parameter as
   * specified by the user.
   * @param o instance of WSerializable, representing
   * the object to convert to XML.
   * @param registry the TypeMappingRegistry the user 
   * specified in Envelope to map a class to its serializer
   * and deserializer class.
   * @param serialize an instance of BaseSerializer. Custom
   * serializers can use the serialize method in BaseSerializer
   * to serialize primitive data types.
   * @throws  Exception if any error occurs during writing the
   * XML or during serialization process.
   */
  public void marshall (XMLWriter xmlwriter, String parameterName, Object o, 
  TypeMappingRegistry registry,	BaseSerializer serializer) throws Exception 
  {
        xmlwriter.startElement (parameterName);
        String[] strarray = registry.getInfoForClass (o.getClass().getName());
        if (strarray != null)
          xmlwriter.attribute ("type", serializer.getSchemaInstance(), strarray[1], strarray[0]);
        else
          throw new SOAPException ("Class " + o.getClass().getName()+ " not registered");
        WSerializable bs = (WSerializable) o;
        for (int i=0; i<bs.getPropertyCount(); i++) 
        {
          serializer.serialize (xmlwriter, 
              registry, bs.getPropertyName(i), bs.getPropertyValue(i));
        } //for
        xmlwriter.endTag ();
  } /* marshall */

    /**
     * Converts a parameter (represented as XML), that
     * is an instance of WSerializable to XML.
     * @since 0.90
     * @param parser XmlParser to parser the XML.
     * @param registry the TypeMappingRegistry the user specified in
     * Envelope to map a class to its serializer and 
     * deserializer class.
     * @param classInstance an instance of WSerializable
     * that is accessible to the user as a return parameter.
     * @param deserialize an instance of BaseSerializer. Custom
     * deserializers can use the deserialize method in BaseSerializer
     * to deserialize primitive data types.
     * @throws  Exception if any error occurs during writing the
     * XML or during serialization process.
     */

  public void unmarshall(XmlParser parser,TypeMappingRegistry registry,
  Object classInstance, BaseSerializer deserializer) throws Exception
  {
    /* Read the element and store the name*/
    ParseEvent event = parser.read();
    String closingTag = event.getName();
    WSerializable bs = (WSerializable) classInstance;
    while (true) 
    {

      if (parser.peek().getType()==Xml.END_TAG &&
      parser.peek().getName().trim().equals(closingTag)) 
        break;
      else if (parser.peek().getType() == Xml.START_TAG) 
      {
          String propertyName=parser.peek().getName().trim();
          Object propertyValue=deserializer.deserialize(parser,registry);
          bs.setProperty(propertyName, propertyValue);
      }
      else
        parser.read();
    } //while
    // skip past the closing tag
    parser.read();
  } /* unmarshall(XmlParser)*/
} /* com.wingfoot.soap.encoding.BeanSerializer */

