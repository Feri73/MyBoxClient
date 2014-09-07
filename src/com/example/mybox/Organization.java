package com.example.mybox;

import java.io.Serializable;

public class Organization implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1866448327327222757L;
	public String name ,picAddress ,description ,regFileAddress;
	public RegisterType regType;
	public Boolean asManager;
	public int notifCount;
	public int ID;

	public Organization(int ID, String name, String picAddress, String description, Boolean asManager, int notifCount, RegisterType regType)
	{
		this(name, picAddress, description, null, null);
		this.ID = ID;
		this.asManager = asManager;
		this.notifCount = notifCount;
		this.regType = regType;
	}

	public Organization(String name, String picAddress, String description, RegisterType regType)
	{
		this(name, picAddress, description, regType, "");
	}

	public Organization(String name, String picAddress, String description, RegisterType regType, String regFileAddress)
	{
		this.name = name;
		this.picAddress = picAddress;
		this.description = description;
		this.regType = regType;
		this.regFileAddress = regFileAddress;
	}
}
