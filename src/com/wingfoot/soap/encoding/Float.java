/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */

package com.wingfoot.soap.encoding;
/**
 * Encapsulates, as String, a double. This is due to 
 * the absence of support for double data type in CLDC,
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */

public class Float {
  private String soapFloat;
  //private boolean flag = false;
  /**
   * Sets the value of the float.  Class is immutable.
   * @since 0.90
   * @param soapFloat String representation of the float
   */
  public Float (String soapFloat) {
    this.soapFloat=soapFloat;
  }
      
  /**
   * Returns the encapsulated double as a String
   * @since 0.90
   * @return String representation of the double.
   */
  public String toString () {
    return soapFloat;
  }
} /* com.wingfoot.soap.encoding.SoapFloat */
