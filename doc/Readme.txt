Wingfoot SOAP
----------------

Wingfoot SOAP is an implementation of SOAP 1.1 client for MIDP/CLDC.  It uses kXML,
the open source XML parser from Lutris Technologies.

Wingfoot SOAP works on Personal Java, J2SE and J2EE platforms.

Features:

- Small and fast. 
- Build-in support for int, integer, long, short, float, boolean, 
  string, dateTime, base64 and beans. 
- Serialization and deserialization are done automatically.
- Custom serializer can be easily pluged in. 
- Supports Hashtable and Vector from Apache SOAP. 
- Not bound to a transport protocol.  Support for HTTP provided. Additional
- protocol can be easily plugged in.

Release 1.04 
This release addresses the following:

1. Fault detail is now available in the Fault class.  Use the getDetail
   method to retrieve the detail.
2. If a targetObjectURI is not provided in the Call class, the SOAP payload
   is constructed with the method name but without the namespace.  This
   provides for better interoperability with existing SOAP servers (in
   particular .NET).
3. The return method name is now accessible from the Envelope class using
   the getReturnOperationName and getReturnNamespace methods.
4. Fixed the defect where the SOAP payload was not sent when the getResponse
   was set to to false.

Release contents:

Wingfoot SOAP 1.04 contains a jar file named wsoap_1.04.zip. When extracted
the jar file produces the following directory structure:

1. A directory named wsoap/demos.  This contains some MIDP samples that use
   Wingfoot SOAP to access services hosted at www.xmethods.com.  It also 
   includes a Midlet that uses the Goggle Search API.  

2. A directory named wsoap/doc.  This directory contains the following:

   - javadoc.zip. This is the Wingfoot SOAP API as a Javadoc.  It can be
     extracted in any suitable location.

   - WSOAP_User_Guide1_04.pdf.  A small document that provides an overview of 
     Wingfoot SOAP.

   - interop.html.  Results of the interop test conducted on Wingfoot SOAP.

3. A directory named wsoap/lib.  This has the necessary jarfiles that can be
   used to access Wingfoot SOAP API.

   - j2sewsoap_1.04.jar: This jar file targets the J2SE and J2EE platform. 
     It can be used in CDC/Personal Java platforms. It includes an XML parser.

   - kvmwsoap_1.04.jar: This jar file targets the J2ME (CLDC/MIDP) 
     platform. It is preverified and includes an XML parser.


Please direct any questions to info@wingfoot.com . Information about Wingfoot
Software Inc. and the products offered are available at http://www.wingfoot.com.

