/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */

package com.wingfoot.soap;

import java.util.*;
import org.kxml.parser.*;
import org.kxml.*;
import java.io.*;

/**
 * Encapsulates a  Fault element in a SOAP message.
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */

public class Fault {

  /**
   * private variables to store the contents
   * of the Fault
   */
  private String faultcode,faultstring;
  private Vector detail;

  /**
   * Creates a new instance of Fault. 
   * This is the only constructor.
   * The  constructor initiates the 
   * process of parsing the Soap Fault.
   * @since 0.90
   * @param parser instance of XmlParser to
   * parse the Fault
   */

  public Fault(XmlParser parser) throws IOException
  {
    this.parse(parser);
  } /* Fault(XmlParser)*/

    /**
     * Parses the Fault in a SOAP message.
     * @since 0.90
     * @param parser instance of XmlParser
     * to parse the SOAP Fault element.
     */
  private void parse(XmlParser parser) throws IOException 
  {
    ParseEvent event;
    String name=null;
    while (true) 
    {
      event = parser.peek();
      if (event.getType()==Xml.END_TAG &&
	  event.getName().trim().equals("Fault")) 
      {
	parser.read();
	break;
      }
      else if (event.getType() == Xml.START_TAG) 
      {
	name = event.getName().trim();
	if (name.equals("detail")) 
	{
	  try 
	  {
	    detail=new Vector();
	    parser.readTree(detail);
	  } catch (Exception e){}
	} //if
	else 
	  parser.read();
      } //else if
      else if (event.getType() == Xml.TEXT) 
      {
        event=parser.read();
        String text=event.getText();
        if (name.equals("faultcode"))
	  faultcode=event.getText();
        else if (name.equals("faultstring"))
	  faultstring=event.getText();
      } // else if
      else
	parser.read();
    } //while
  } /* parse */

     

  /**
   * Retrieves the <faultcode> element in
   * the Fault element of a SOAP message.
   * @since 0.90
   * @return String with the faultcode.
   * null if not faultcode is detected.
   */
  public String getFaultCode() {
    return faultcode;
  } /* getFaultCode() */

  /**
   * Retrieves the <faultstring> element
   * in the Fault element of a SOAP message.
   * @since 0.90
   * @return String with the faultstring,
   * null if not faultstring is detected.
   */
  public String getFaultString() {
    return faultstring;
  } /* getFaultString() */

  /**
   * Retrieves the <detail> element in
   * the Fault element of a SOAP message.
   * @since 0.90
   * @return Vector with each element of
   * the vector an instance of org.kxml.parser.ParseEvent.
   * Convert each element of the Vector to a String using
   * the toString method to get the Fault detail.
   */

  public Vector getDetail() {
    return detail;
  } /* getDetail */

} /* class Fault */

