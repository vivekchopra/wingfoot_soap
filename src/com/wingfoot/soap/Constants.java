/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */
package com.wingfoot.soap;

import java.util.*;
/**
 * Class to store common constants.
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */

public class Constants {
    
  /**
   * The only constructor available.  This class
   * can never be instantiated.
   */
  private Constants(){}

  /**
   * Constant representing the namespace for the
   * elements of a SOAP message.
   * http://schemas.xmlsoap.org/soap/envelope/
   * @since 0.90
   */
  public final static String SOAP_NAMESPACE = 
    "http://schemas.xmlsoap.org/soap/envelope/";
  /**
   * Constant representing the default schema instance
   * Used if no specific schema instance is provided
   * in the Envelope
   * http://www.w3.org/2001/XMLSchema-instance
   * @since 0.90
   */
  public final static String SOAP_SCHEMA_INSTANCE =
    "http://www.w3.org/2001/XMLSchema-instance";
  /**
   * Constant representing the default schema.  Used
   * if no specific schema is provided in the Envelope.
   * http://www.w3.org/2001/XMLSchema
   * @since 0.90
   */
  public final static String SOAP_SCHEMA =
    "http://www.w3.org/2001/XMLSchema";
  /**
   * Constant representing the default encoding style.
   * http://schemas.xmlsoap.org/soap/encoding/
   * @since 0.90
   */
  public final static String SOAP_ENCODING_STYLE =
    "http://schemas.xmlsoap.org/soap/encoding/";
  /**
   * The namespace used, for serializing and deserializing
   * java.lang.Vector and java.lang.Hashtable.
   * http://xml.apache.org/xml-soap
   * @since 0.90
   */
  public final static String DEFAULT_NAMESPACE=
    "http://xml.apache.org/xml-soap";

  /**
   * Constant representing the 1999 schema instance
   * Users are encouraged to use this constant if
   * there is a need to use 1999 schema instance
   * instead of the default 2001 schema instance.
   * http://www.w3.org/1999/XMLSchema-instance
   * @since 1.0
   */
  public final static String SOAP_SCHEMA_INSTANCE_1999 =
    "http://www.w3.org/1999/XMLSchema-instance";
  /**
   * Constant representing the 1999 schema. Users are
   * encouraged to use this constant if there is a need
   * to use 1999 schema instead of the default 2001
   * schema.
   * http://www.w3.org/1999/XMLSchema
   * @since 1.0
   */
  public final static String SOAP_SCHEMA_1999 =
    "http://www.w3.org/1999/XMLSchema";
} /* Constants */
