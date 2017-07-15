/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */

package com.wingfoot.soap.encoding;

/**
 * Interface with abstract methods to facilitate 
 * reflection. Typical concrete implementations
 * of this interface are JavaBeans with their
 * properties getting serialized.
 * @since 0.90
 * @author Kal Iyer
 * @author Vivek Chopra
 */

public interface WSerializable {
  /**
   * Implementation of this abstract method returns
   * the number of property to serialize. 
   * @return the number of properties in the Bean.
   */
  public int getPropertyCount ();

  /**
   * Implementation of this abstract method returns
   * the name of the property for the given index.
   * @param index the property number.
   * @return the name of the property corresponding
   * to the index.
   */
  public  String getPropertyName  (int index);

  /**
   * Implementation of this abstract method returns
   * the value of the property for the given index.
   * @param index the property number.
   * @return the value of the property represented
   * as an java object corresponding to the index.
   */
  public  Object getPropertyValue (int index);

  /**
   * Sets a property to a value.
   * @param propertyName the name of the property.
   * @param propertyValue the value of the property
   * represented as an Object.  Primitive data
   * types are represented as their corresponding
   * wrapper class.
   */
  public void setProperty (String propertyName, Object propertyValue);
    
  /**
   * Removes the property at the given index.
   * @param index the property number.
   public void removeProperty(int i);
  */
    
  /**
   * Replaces the property at the specified index
   * with the newProperty.  The existing property
   * is discarded.
   * @param newProperty Object representing the new
   * value to be substituted.
   * @param index the index where newProperty is to
   * be substituted.
   */
  public void setPropertyAt(Object newProperty, int index);
} /* com.wingfoot.soap.encoding.WSerializable */

