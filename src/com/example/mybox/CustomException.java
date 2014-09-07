package com.example.mybox;

public class CustomException extends Throwable
{
	String type;
	public CustomException(String type)
	{
		this.type=type;
	}
	public String GetType()
	{
		return type;
	}
	
}
