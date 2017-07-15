/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
*/


package interopGroupB;
import com.wingfoot.soap.encoding.*;

public class EmployeeBean implements WSerializable {
    
    /**
     private String name;
     private Integer age;
     private com.wingfoot.soap.encoding.Float salary;
    **/
     private Object name;
     private Object age;
     private Object salary;

     public int getPropertyCount() {
          return 3;
     }

     public String getPropertyName (int index) {
          if (index == 0)
	       return "varString";
          else if (index ==1)
	       return "varInt";
          else if (index ==2)
	       return "varFloat";
          else return "";
     }
/**
     public void removeProperty (int index) {
          if (index == 0)
	       name=null;
          else if (index ==1)
	       age=null;
          else if (index ==2)
	       salary=null;
     }
**/

     public Object getPropertyValue(int index) {
          if (index ==0)
	       return name;
           else if (index == 1)
	        return age;
           else if (index == 2)
	        return salary;
           else 
	        return "";
     }

     public void setPropertyAt(Object newProperty, int index) {
         if (index==0)
	     name=newProperty;
         else if (index==1)
	     age=newProperty;
         else if (index==2)
	     salary=newProperty;
     }

     public void setProperty (String name, Object value) {
          if (name.trim().equals("varString")) 
	       this.name= value;
          else if (name.trim().equals("varInt"))
	       age =  value;
          else if (name.trim().equals("varFloat"))
	       salary = value;
     }

     public String getName() { return (String) name; }
     public Integer getAge() { return (Integer) age; }
     public com.wingfoot.soap.encoding.Float getSalary() { return 
          (com.wingfoot.soap.encoding.Float) salary; }

     public void setName(String name) {
         this.name=name;
     }

     public void setAge(int age) {
         this.age = new Integer(age);
     }

     public void setSalary(com.wingfoot.soap.encoding.Float salary) {
         this.salary=salary;
     }

} //class
