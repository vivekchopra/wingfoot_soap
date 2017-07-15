/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */

package com.wingfoot.soap.encoding;
/**
 * Interface to aid in parameter serialization. 
 * and deserialization. Custom serializers and
 * deserializers implement this interface.
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */
import org.kxml.io.*;
import com.wingfoot.soap.encoding.*;
import org.kxml.parser.*;
public interface SerializerDeserializer {

  /**
   * Converts a parameter specified by the user
   * using Envelope.setBody(String, Object) to XML.
   * @param xmlwriter instance of XMLWriter to aid
   * in writing XML.
   * @param parameterName the name of the parameter as
   * specified by the user.
   * @param objectToMarshall the value of the parameter
   * as specified by the user.
   * @param registry the TypeMappingRegistry specified
   * by the user in the Envelope.
   * @param serializer instance of BaseSerializer. 
   * Custom serializers can call the serialize method in
   * BaseSerializer to convert primitive parameters 
   * to XML..
   * @throws Exception  if any error occurs during
   * XML writing or during serialization.
   */
  public abstract void marshall (XMLWriter xmlwriter, 
				 String parameterName, 
				 Object objectToMarshall, 
				 TypeMappingRegistry registry, 
				 BaseSerializer serializer) 
    throws Exception;

  /**
   * Abstract method to convert XML parameter to
   * a Java object.
   * @since 0.90
   * @param parser instance of XmlParser to parse
   * the XML.
   * @param registry TypeMappingRegistry specified by the
   * user in the Envelope.
   * @param classInstance an instance of the class to
   * deserialize to.
   * @param deserializer instance of BaseSerializer; custom
   * deserializers can use the deserialize method in this
   * BaseSerializer to deserialize primitive data types.
   * @throws Exception if any error occurs during
   * deserialization.
   */

  void unmarshall (XmlParser parser,
		   TypeMappingRegistry registry,
		   Object classInstance, 
		   BaseSerializer deserializer)
    throws Exception;
} /* com.wingfoot.soap.encoding.Serializer */
