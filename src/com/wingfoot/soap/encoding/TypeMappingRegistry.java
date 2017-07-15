/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */

package com.wingfoot.soap.encoding;

import java.util.*;

/**
 * Stores association between a Class and its
 * namespace, localpart, serialization class and
 * deserialization class.  This is used for
 * serialization.  
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */

public class TypeMappingRegistry {
  private Hashtable classNameToNamespace = new Hashtable ();
  private Hashtable namespaceToClassName = new Hashtable ();
  private Hashtable elementToClass = new Hashtable();

  /**
   * Maps a Class to its serializer/deserializer class and
   * the URI and namespace it is mapped to on the server.
   * The nameSpace and localPart should be identical to what is 
   * defined in the server deployment descriptor.
   * This method provides means for a user to define their own 
   * custom serializers/deserializers.
   * @since 0.90
   * @param nameSpace the namespace to identify the custom serializer.
   * @param localPart the localPart as defined in the service.  This
   * together with the namespace specified above identifies a
   * unique serializer or deserializer.
   * @param classOfObject instance of Class.  This represents
   * the Class of the parameter.
   * @param serializerClass instance of Class.  This represents
   * the class used to convert the parameter to XML.
   * @param deserializerClass instance of Class.  This represents
   * the class used to convert XMl to first class Java Objects.
   */

  public void mapTypes (String nameSpace, 
			String localPart, 
			Class classOfObject, 
			Class serializerClass, 
			Class deserializerClass) {
    // the nameSpace and localPart should be identical to what is 
    // defined in the server DD.xml
    // This method provides means for a user to define their own 
    // custom serializers/deserializers.

    classNameToNamespace.put (classOfObject.getName (), 
			      new String[] {nameSpace, 
					    localPart, 
					    serializerClass.getName (), 
					    deserializerClass.getName ()}
			      );
    namespaceToClassName.put (nameSpace+localPart,
			      new String[] { 
				classOfObject.getName (), 
				serializerClass.getName (), 
				deserializerClass.getName ()}
			      );
  }
  /**
   * Maps an element name to the class the element
   * body is to be deserialized to.  This mapping
   * is neccessary to address situations where the
   * SOAP response from a service does not specify
   * the data type for an element.  MSSoap, .NET and
   * White Mesa SOAP are examples where typing 
   * information is not available in response.  
   * During deserialization process, Wingfoot SOAP
   * uses this mapping between the XML element name
   * and the Class.
   * If the application is confident that the response
   * includes typing information, this mapping is
   * not required.
   * @since 0.90
   * @param elementName the name of the element as
   * returned in the response.  This information
   * is usually available in the WSDL.
   * @param classOfObject instance of Class that
   * the element should be deserialized to. This
   * instance is returned back to the user.
   * @param deserializerClass instance of Class
   * that represents (if applicable) the class
   * used in deserialization.  Only needed if custom
   * deserialization is required (example BeanSerializer)
   * For primitive wrapper classes, this is left
   * as null.
   */
  public void mapElements (String elementName,
			   Class classOfObject,
			   Class deserializerClass) {
    if (deserializerClass == null) 
      elementToClass.put(elementName,
			 new String[] {
			   classOfObject.getName(),
			   null });
    else
      elementToClass.put(elementName,
			 new String[] {
			   classOfObject.getName(),
			   deserializerClass.getName()});
  } /* mapElements */
    
    /**
     * Given an element name, determines the proper
     * class for the contents of the elements to be
     * deserialized to.
     * @since 0.90
     * @param elementName the name of the element from
     * the SOAP response.
     * @return String[] with the name of the class; null
     * if the element is not mapped to a class. String[0]
     * contains the instance of the class to deserialize
     * to and String[1] contains the deserializer class
     * or null if no deserializer class is needed.
     */
  public String[] getClassForElement(String elementName) {
    return (String[]) elementToClass.get(elementName);
  } /* getClassForElement */

    /**
     * Returns an array of String (String[]) with information
     * for the class. 
     * @since 0.90
     * @param className String representation of the name of
     * the class of the parameter.
     * @return String[] with info for the class.  The elements
     * of the array are as follows:
     * String[0] - namespace
     * String[1] - localPart
     * String[2] - Serializer class name
     * String[3] - Deserializer class name.
     * If the class is not mapped, a null is returned.
     */
  public String[] getInfoForClass (String className) {
    return  (String[]) classNameToNamespace.get (className);
  }

  /**
   * Given an namespace and localpart, returns information
   * that is useful in deserialization of a parameter.
   * @since 0.90
   * @param namespace the namespace of the parameter.
   * @param localpart the localpart of the parameter.
   * @return  String[] with information for the namespace
   * The information in the array is as follows:
   * String[0] - Class.getName()
   * String[1] - serializer class name
   * String[2] - deserializer class name
   */
  public String[] getInfoForNamespace (String namespace,
				       String localPart) {
    return  (String[]) namespaceToClassName.get (namespace+localPart);
  }
} /* com.wingfoot.soap.encoding.TypeMappingRegistry */
