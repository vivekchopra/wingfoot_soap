/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
*/

package com.wingfoot.google;

import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import javax.microedition.io.*;

import com.wingfoot.soap.*;
import com.wingfoot.soap.transport.*;
import com.wingfoot.soap.encoding.*;
import java.io.*;
import java.util.*;

/**
 * Midlet that searches Google using Wingfoot SOAP API
 * @since 1.0
 */

public class Google extends MIDlet implements CommandListener {


    /* this is the constant for the google key*/
    private final String GOOGLE_KEY = "GNtDPzzEzyEiXsi2p4H4hlPAi06PW52U";

    /* Set up the form objects */
    private Form form;
    private Form resultForm;

    /* setup the command objects */
    private Command submitCommand;
    private Command exitCommand;
    private Command newSearchCommand;

    /* Setup some various things */
    private String queryString;

    /* Setup the display */
    private Display display;
    
    /* Setup the input field */
    private TextField tf;
    
     private String wingfootRec = "Powered by Wingfoot SOAP";

    public Google(){
	display = Display.getDisplay(this);

	submitCommand = new Command("Submit", Command.OK, 1);
	exitCommand = new Command("Exit", Command.EXIT, 1);

	form = new Form("GoogleSearch");
	tf = new TextField("Search Term", null, 20, 0);
	form.append(tf);
	form.append(wingfootRec + "\n");
	form.addCommand(submitCommand);
	form.addCommand(exitCommand);
	form.setCommandListener(this);
	
    }

    public void startApp(){
	display.setCurrent(form);	

    }

    public void pauseApp(){
    }

    public void destroyApp(boolean unconditional){
    }

    public void commandAction(Command c, Displayable d){

	if(c.getLabel().trim().equals("Submit")){
	    if(tf.getString() != null && tf.getString().length() > 0){
		makeTransaction(tf.getString().trim());
	    }

	}
	else if(c.getLabel().trim().equals("New Search")){
	    resetForm(false);
	}
	else if(c.getLabel().trim().equals("Exit")){
	    destroyApp(false);
	    notifyDestroyed();

	}
    }

    public void makeTransaction(String queryParam){

	TypeMappingRegistry registry = null;

	try {
	    Envelope requestEnvelope = new Envelope();
	    requestEnvelope.setSchema(Constants.SOAP_SCHEMA_1999);
	    requestEnvelope.setSchemaInstance(Constants.SOAP_SCHEMA_INSTANCE_1999);
	    
	    Call call = new Call(requestEnvelope);
	    registry = new TypeMappingRegistry();
	    
	    registry.mapTypes("urn:GoogleSearch", 
	                      "GoogleSearchResult", 
			       Class.forName("com.wingfoot.google.GoogleBean"), 
			       Class.forName("com.wingfoot.soap.encoding.BeanSerializer"),
			       Class.forName("com.wingfoot.soap.encoding.BeanSerializer"));

	    registry.mapTypes("urn:GoogleSearch", 
	                      "ResultElement", Class.forName("com.wingfoot.google.ResultsElement"), 
			       Class.forName("com.wingfoot.soap.encoding.BeanSerializer"), 
			       Class.forName("com.wingfoot.soap.encoding.BeanSerializer"));


	    registry.mapTypes("urn:GoogleSearch", 
	                       "DirectoryCategory", 
			       Class.forName("com.wingfoot.google.DirectoryCategory"), 
			       Class.forName("com.wingfoot.soap.encoding.BeanSerializer"), 
			       Class.forName("com.wingfoot.soap.encoding.BeanSerializer"));

	    registry.mapTypes(Constants.SOAP_SCHEMA_1999, 
	                      "double", Class.forName("com.wingfoot.google.Double"), 
			       Class.forName("com.wingfoot.google.DoubleSerializer"), 
			       Class.forName("com.wingfoot.google.DoubleSerializer"));
	    
	    call.setMappingRegistry(registry);
	    call.addParameter("key",GOOGLE_KEY);
	    call.addParameter("q", queryParam);
	    call.addParameter("start", new Integer(0));
	    call.addParameter("maxResults", new Integer(3));
	    call.addParameter("filter", new Boolean(true));
	    call.addParameter("restrict", new String("null"));
	    call.addParameter("safeSearch", new Boolean(false));
	    call.addParameter("lr", new String("null"));
	    call.addParameter("ie", "latin1");
	    call.addParameter("oe", "latin1");
	    
	    call.setMethodName("doGoogleSearch");
	    call.setTargetObjectURI("urn:GoogleSearch");
	    
	    HTTPTransport transport = new HTTPTransport(
                            "http://api.google.com/search/beta2", 
	                    "urn:GoogleSearch");

	    Envelope responseEnvelope = call.invoke(transport);

	    resultForm = new Form("Google Results");
	    
	    if(responseEnvelope == null){
		Fault f = responseEnvelope.getFault();
		resultForm.append("An Error has occured: " + f.getFaultString());
		resultForm.addCommand(new Command("New Search", Command.OK, 1));
		resultForm.addCommand(exitCommand);
		resultForm.setCommandListener(this);
		display.setCurrent(resultForm);
	    } else {
		com.wingfoot.google.GoogleBean gb = 
	            (com.wingfoot.google.GoogleBean) responseEnvelope.getParameter(0);

		Object[] re = (Object[]) gb.getResultsElementArray();
		
		for (int i=0; i<re.length; i++) {
		    ResultsElement relement = (ResultsElement) re[i];
		    resultForm.append(stripHTML(relement.getTitle()) + "\n");
		    resultForm.append(relement.getUrl() + "\n");
		    resultForm.append("------------\n");
		}
		resultForm.addCommand(new Command("New Search", Command.OK, 1));
		resultForm.addCommand(exitCommand);
		resultForm.setCommandListener(this);
		display.setCurrent(resultForm);
	    }
	} catch (Exception e){
	    resetForm(true);
	}
	
    }
    private String stripHTML(String toStrip){
	char[] original = toStrip.toCharArray();
	char[] a = new char[toStrip.length()];
	boolean encounterLTBracket = false;
	for(int i = 0; i < original.length; i++){
	    if(!encounterLTBracket){
		if(original[i] == '<')
		    encounterLTBracket = true;
		else {
		    a[i] = original[i];
		}
	    }
	    else if(encounterLTBracket){
		if(original[i] == '>')
		    encounterLTBracket = false;
	    }
	}
	return String.valueOf(a).trim();
    }
    public void resetForm(boolean error){
	form = new Form("GoogleSearch");
	if(error)
	    form.append("An error has occured\nPlease try again\n");
	tf = new TextField("Search Term", null, 20, 0);
	form.append(tf);
	form.append(wingfootRec + "\n");
	form.addCommand(new Command("Submit", Command.OK, 1));
	form.addCommand(new Command("Exit", Command.EXIT, 1));
	form.setCommandListener(this);
	display.setCurrent(form);
    }

}
