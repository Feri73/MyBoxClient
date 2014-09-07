package com.example.mybox;

public class Request
{
	String username ,orgName;
	int orgID;
	Boolean accepted;

	public Request(String username, String orgName, int orgID)
	{
		this.username = username;
		this.orgName = orgName;
		this.orgID = orgID;
	}

	public Request(String orgName, int orgID, Boolean accepted)
	{
		this.accepted = accepted;
		this.orgName = orgName;
		this.orgID = orgID;
	}
}
