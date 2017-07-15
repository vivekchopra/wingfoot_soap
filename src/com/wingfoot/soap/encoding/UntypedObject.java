/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
 */

package com.wingfoot.soap.encoding;
import java.util.*;
/**
 * Encapsulates untyped properties.  Some SOAP servers
 * return untyped structs.  Such structs are deserialized
 * to this class.
 * @since 0.90.
 * @author Kal Iyer
 * @author Vivek Chopra
 */

public class UntypedObject implements WSerializable {
   
  private Vector name = new Vector();
  private  Vector value = new Vector();
  public String hrefID=null;
  /**
   * Returns the number of properties
   * @return the number of properties
   */
  public int getPropertyCount() {
    return value.size(); 
  }
  /**
   * Given an index, the name of the property
   * at the given index is returned.
   * @param index the index number
   * @return the property name as String
   */
  public String getPropertyName (int index) {
    return (String) name.elementAt(index);
  }

  /**
   * Given an index, the value of the property
   * at the given index is returned.
   * @param index the index number.
   * @return the value of the property.
   */
  public Object getPropertyValue(int index) {
    return value.elementAt(index);
  }
     
  /**
   * Accepts and stores the name of the property 
   * and the value of the property
   * @param name the name of the property
   * @param the value of the property.
   */
  public void setProperty (String name, Object value) {
    this.name.addElement(name);
    this.value.addElement(value);
  }
  /**
     public void removeProperty(int i) {
     this.name.removeElementAt(i);
     this.value.removeElementAt(i);
     }
  **/
     
  /**
   * Replaces the xisting property at a specified
   * index with the new value.
   * @param value - the new Object
   * @param index - the index where the Object
   * has to be put in.  The existing Object in 
   * the index is discarded.
   */
  public void setPropertyAt(Object value, int index) {
    this.value.setElementAt(value,index);
  }

} //class
