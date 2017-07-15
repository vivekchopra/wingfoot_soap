/*
 * Copyright (c) Wingfoot Software Inc. All Rights Reserved.
 */

package com.wingfoot.google;

import com.wingfoot.soap.encoding.*;
import com.wingfoot.google.*;

/**
 * Class which encapsulates the entire response from a google query
 * This is a good example of a class that implements WSerializable.
 */

public class GoogleBean implements WSerializable {
    
    private final int GOOGLE_BEAN_PROPERTY_COUNT = 10;
    private Object documentFiltering;
    private Object searchComments;
    private Object estimatedTotalResultsCount;
    private Object estimatedIsExact;
    private Object[] resultsElementArray; 
    private Object searchQuery;
    private Object startIndex;
    private Object endIndex;
    private Object[] directoryCategoryArray;
    private Object searchTime;

    public int getPropertyCount() {
	return GOOGLE_BEAN_PROPERTY_COUNT;
    }

    public String getPropertyName (int index) {
	if (index == 0)
	    return "documentFiltering";
	else if (index == 1)
	    return "searchComments";
	else if (index == 2)
	    return "estimatedTotalResultsCount";
	else if (index == 3)
	    return "estimatedIsExact";
	else if (index == 4)
	    return "resultsElementArray";
	else if (index == 5)
	    return "searchQuery";
	else if (index == 6)
	    return "startIndex";
	else if (index == 7)
	    return "endIndex";
	else if (index == 8)
	    return "directoryCategoryArray";
	else if (index == 9)
	    return "searchTime";
	else return "";
    } //getPropertyName

    public void setPropertyAt (Object value, int index) {
	if (index == 0)
	    documentFiltering = value;
	else if (index == 1)
	    searchComments = value;
	else if (index == 2)
	    estimatedTotalResultsCount = value;
	else if (index == 3)
	    estimatedIsExact = value;
	else if (index == 4)
	    resultsElementArray = (Object[])value;
	else if (index == 5)
	    searchQuery = value;
	else if (index == 6)
	    startIndex = value;
	else if (index == 7)
	    endIndex = value;
	else if (index == 8)
	    directoryCategoryArray = (Object[]) value;
	else if (index == 9)
	    searchTime = value;
    }

    public Object getPropertyValue(int index) {
	if (index == 0)
	    return documentFiltering;
	else if (index == 1)
	    return searchComments;
	else if (index == 2)
	    return estimatedTotalResultsCount;
	else if (index == 3)
	    return estimatedIsExact;
	else if (index == 4)
	    return resultsElementArray;
	else if (index == 5)
	    return searchQuery;
	else if (index == 6)
	    return startIndex;
	else if (index == 7)
	    return endIndex;
	else if (index == 8)
	    return directoryCategoryArray;
	else if (index == 9)
	    return searchTime;
	else return "";

    } //getPropertyValue

    public void setProperty (String name, Object value) {

	if (name.trim().equals("documentFiltering"))
	    this.documentFiltering = value;
	else if (name.trim().equals("searchComments"))
	    this.searchComments = value;
	else if (name.trim().equals("estimatedTotlaResultsCount"))
	    this.estimatedTotalResultsCount = value;
	else if (name.trim().equals("estimatedIsExact"))
	    this.estimatedIsExact = value;
	else if (name.trim().equals("resultElements")){
	    this.resultsElementArray =  (Object[]) value;
	}
	else if (name.trim().equals("searchQuery"))
	    this.searchQuery= value;
	else if (name.trim().equals("startIndex"))
	    this.startIndex= value;
	else if (name.trim().equals("endIndex"))
	    this.endIndex= value;
	else if (name.trim().equals("DirectoryCategoryArray"))
	    this.directoryCategoryArray = (Object[]) value;
	else if (name.trim().equals("searchTime"))
	    this.searchTime= value;

    }


    public Boolean getDocumentFiltering(){ return (Boolean) documentFiltering; }
    public String getSearchComments(){ return (String) searchComments; }

    public String getEstimatedTotalResultsCount(){ return (String) estimatedTotalResultsCount; }

    public Boolean getEstimatedIsExact(){ return (Boolean) estimatedIsExact;}

    public Object[] getResultsElementArray(){ return  (Object[]) resultsElementArray;}

    public String getSearchQuery(){ return (String) searchQuery;}

    public String getStartIndex(){ return (String) startIndex;}

    public String getEndIndex(){ return (String) endIndex;}

    public Object[] getDirectoryCategoryArray(){ return (Object[]) directoryCategoryArray;}

    public com.wingfoot.google.Double getSearchTime(){ return (com.wingfoot.google.Double) searchTime;}
    
} //class


