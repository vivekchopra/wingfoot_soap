/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */


package com.wingfoot.soap.transport;

import com.wingfoot.soap.*;
import java.io.*;
/**
 * Interface for all the transport implementations. Users
 * desiring to implement their own transport have to 
 * implement this interface.
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */

public interface Transport {

  /**
   * Method to open a connection to a server and send the
   * payload.  Returns a byte stream representing the
   * response from the server. 
   * @param payload the SOAP message as a byte array. This
   * payload is sent to the SOAP server.
   * @return an array of bytes from the SOAP server representing 
   * the response; null if no response is returned.
   * @throws IOException if any error occurs while connecting
   * to the server.
   * @throws SOAPException if response is not XML.
   */
  public byte[] call (byte[] payload) 
    throws SOAPException,IOException;
}
