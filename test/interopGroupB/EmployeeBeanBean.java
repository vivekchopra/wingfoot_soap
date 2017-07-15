/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 * Please see http://www.wingfoot.com for license details.
*/


package interopGroupB;
import com.wingfoot.soap.encoding.*;

public class EmployeeBeanBean implements WSerializable {
    
    /**
     private String name;
     private Integer age;
     private com.wingfoot.soap.encoding.Float salary;
    **/
     private Object name;
     private Object age;
     private Object salary;
     private Object bean;

     public int getPropertyCount() {
          return 4;
     }

     public String getPropertyName (int index) {
          if (index == 0)
	       return "varString";
          else if (index ==1)
	       return "varInt";
          else if (index ==2)
	       return "varFloat";
          else if (index==3)
	       return "varStruct";
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
          else if (index==3)
	       bean=null;
     }
**/

     public Object getPropertyValue(int index) {
          if (index ==0)
	       return name;
           else if (index == 1)
	        return age;
           else if (index == 2)
	        return salary;
           else if (index ==3)
	        return bean;
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
	 else if (index==3)
	     bean=newProperty;
     }

     public void setProperty (String name, Object value) {
          if (name.trim().equals("varString")) 
	       this.name= value;
          else if (name.trim().equals("varInt"))
	       this.age =  value;
          else if (name.trim().equals("varFloat"))
	       this.salary = value;
          else if (name.trim().equals("varStruct"))
	       this.bean = value;
     }

     public String getName() { return (String) name; }
     public Integer getAge() { return (Integer) age; }
     public com.wingfoot.soap.encoding.Float getSalary() { return 
          (com.wingfoot.soap.encoding.Float) salary; }
     public EmployeeBean getBean() { return (EmployeeBean) bean; }

     public void setName(String name) {
         this.name=name;
     }

     public void setAge(int age) {
         this.age = new Integer(age);
     }

     public void setSalary(com.wingfoot.soap.encoding.Float salary) {
         this.salary=salary;
     }

     public void setBean(EmployeeBean bean) {
         this.bean = bean;
     }
} //class
