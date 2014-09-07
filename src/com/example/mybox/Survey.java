package com.example.mybox;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class Survey implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 922197532812573799L;
	public int ID,orgID;
	public Date endDate;
	public String text;
	int[] questionsID;
		
	public Survey(int ID,int orgID,Date endDate,String text)
	{
		this.ID=ID;
		this.orgID=orgID;
		this.endDate=endDate;
		this.text=text; 
	}
}
