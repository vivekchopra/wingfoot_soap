/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
*/

package com.wingfoot.google;

import com.wingfoot.soap.encoding.*;

/**
 * Class which encapsulates the ResultsElement from the search
 * structure returned from Google.  This is an example of a 
 * class that implements WSerializable.
 */

public class ResultsElement implements WSerializable{

    public ResultsElement(){
    } //constructor

    private final int RESULTS_ELEMENT__PROPERTY_COUNT  = 10;
    
    private Object summary = null;
    private Object url = null;
    private Object snippet = null;
    private Object title = null;
    private Object cachedSize = null;
    private Object relatedInformationPresent = null;
    private Object hostName = null;
    private Object directoryCategory = null;
    private Object directoryTitle = null;

    public int getPropertyCount() {
	return RESULTS_ELEMENT__PROPERTY_COUNT;
    }

    public String getPropertyName (int index) {
	if (index == 0)
	    return "summary";
	else if(index == 1)
	    return "url";
	else if(index == 2)
	    return "snippet";
	else if(index == 3)
	    return "title";
	else if(index == 4)
	    return "cachedSize";
	else if(index == 5)
	    return "relatedInformationPresent";
	else if(index == 6)
	    return "hostName";
	else if(index == 7)
	    return "directoryCategory";
	else if(index == 8)
	    return "directoryTitle";
	else
	    return "";
    }

    
    public void setPropertyAt (Object value, int index) {
	if (index == 0)
	    summary = value;
	else if(index == 1)
	    url = value;
	else if(index == 2)
	    snippet = value;
	else if(index == 3)
	    title = value;
	else if(index == 4)
	    cachedSize = value;
	else if(index == 5)
	    relatedInformationPresent = value;
	else if(index == 6)
	    hostName = value;
	else if(index == 7)
	    directoryCategory = value;
	else if(index == 8)
	    directoryTitle = value;
	
    }

    public Object getPropertyValue(int index){

	if (index == 0)
	    return  summary;
	else if(index == 1)
	    return url;
	else if(index == 2)
	    return snippet;
	else if(index == 3)
	    return title;
	else if(index == 4)
	    return cachedSize;
	else if(index == 5)
	    return relatedInformationPresent;
	else if(index == 6)
	    return hostName;
	else if(index == 7)
	    return directoryCategory;
	else if(index == 8)
	    return directoryTitle;
	return "";
    }

    public void setProperty(String name, Object value){
	if (name.trim().equals("summary"))
	    this.summary = value;
	else if(name.trim().equals("URL"))
	    this.url = value;
	else if(name.trim().equals("snippet"))
	    this.snippet = value;
	else if(name.trim().equals("title"))
	    this.title = value;
	else if(name.trim().equals("cachedSize"))
	    this.cachedSize = value;
	else if(name.trim().equals("relatedInformationPresent"))
	    this.relatedInformationPresent = value;
	else if(name.trim().equals("hostName"))
	    this.hostName = value;
	else if(name.trim().equals("directoryCategory"))
	    this.directoryCategory = value;
	else if(name.trim().equals("directoryTitle"))
	    this.directoryTitle = value;

    }
    /* the getters */
    public String getSummary(){
	return (String) summary;
    }
    public String getUrl(){
	return (String) url;
    }
    public String getSnippet(){
	return (String) snippet;
    }
    public String getTitle(){
	return (String) title;
    }
    public String getCachedSize(){
	return (String) cachedSize;
    }
    public Boolean getRelatedInformationPresent(){
	return (Boolean) relatedInformationPresent;
    }
    public String getHostName(){
	return (String) hostName;
    }
    public DirectoryCategory getDirectoryCategory(){
	return (DirectoryCategory) directoryCategory;
    }
    public String getDirectoryTitle(){
	return (String) directoryTitle;
    }

}
