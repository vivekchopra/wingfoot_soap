/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
*/


package com.wingfoot.google;

import org.kxml.io.*;
import org.kxml.parser.*;
import org.kxml.*;
import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;
import com.wingfoot.soap.encoding.*;

/**
 * Encapsulates, as String, a double. This is due to 
 * the absence of support for double data type in CLDC,
 * This is a good example of implementing a custom 
 * serializer and deserializer for data types not supported
 * by Wingfoot.  
 */


public class DoubleSerializer implements SerializerDeserializer {

    public DoubleSerializer(){
    }
    
    /**
     * Method that takes a Double and converts
     * into XML.  Since Google does not expect
     * Double converted as XML, the method is
     * not implemented.
     */
    public void marshall (XMLWriter xmlwriter, 
                          String parameterName, 
                          Object o, 
                          TypeMappingRegistry registry,
                          BaseSerializer serializer) 
	throws Exception {
    }

    /**
     * Method that converts a XML stub to a Double
     * data type.  The toolkit calls this method
     * once a xsd:double datatype is encountered.  To
     * enable this call, this class has to be 
     * bound to the Double data type using TypeMappingRegistry.
     * In this application, this class is bound to the 
     * Double data type in Google.java
     * @param parser an instance of the XML parser that
     * is pointing to the element that contains the double
     * data type. The parser consumes  the underlying
     * XML payload.
     * @param registry an instance of type mapping registry
     * @param classInstance the toolkit sends an instance of
     * com.wingfoot.google.Double.  This method stores the
     * Double value in this calss
     * @param deserializer and instance of the calling method.
     * If there is a need to parse a data type other that Double
     * (as will be the case in a structure), call
     * deserializer.deserialize(parser,registry)
     */

    public void unmarshall(XmlParser parser,
                           TypeMappingRegistry registry,
			   Object classInstance,
			   BaseSerializer deserializer) 
	throws Exception{

	/**
	 * Read the first element.  In the following SOAP
	 * stub : <salary xsi:type=xsd:double>100.002</salary>
	 * the toolkit always calls this method with the
	 * parser pointing to the <salary xsi:type=xsd:double>
	 * element.  The first read will consume this element.
	 */
	ParseEvent event = parser.read();

	//Store the element name (salary in the previous example).
        //Also, typecast the classInstance sent in.  The toolkit
	//in this case, always sends a Double (because that is
	// how it is registered in the TypeMappingRegistry in Google.java
	//

	String closingTag = event.getName();
	com.wingfoot.google.Double doub = (com.wingfoot.google.Double) classInstance;

	/**
	 * Keep consuming the payload from the parser until 
	 * the correct end tag is encountered (</salary> in the
	 * previous example). This class deserializes a double
	 * and hence expects a start tag a TEXT and an end tag.
	 */
	while (true) {

	    /**
	     * Peek ahead, without consuming.  It the next
	     * element is a END_TAG that ends the start element
	     * (</salary> in our example), then get out of the
	     * loop.
	     */
	    if (parser.peek().getType()==Xml.END_TAG &&
		parser.peek().getName().trim().equals(closingTag)) 
		break;

            /**
	     * If the next element is a TEXT, it contains
	     * the text that has to be encapsulated as a
	     * Double. Read the text and store it in the
	     * Double.
	     */
	    else if (parser.peek().getType() == Xml.TEXT) {
		String propertyValue=parser.read().getText().trim();
		doub.setDouble(propertyValue);
	    }

	    else
	       /**
	        * If the element is not a END_TAG or a TEXT
		* then it might just be whitespaces.  Since 
		* they are of no interest just read ahead.
		*/
		parser.read();
	} //while
        
	/**
	 * Consume the end tag (</salary>).  Just like 
	 * the toolkit called this method with the parser
	 * pointing to a start tag, it is the responsibility
	 * of this method, before returning, to ensure that
	 * the parser is pointing to the next start tag (or
	 * alteast past the end tag).
	 */
	parser.read();
    }

} /* com.wingfoot.google.Double */

