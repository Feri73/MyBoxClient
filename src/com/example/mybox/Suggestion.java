package com.example.mybox;

public class Suggestion
{
	public int ID;
	public String text;
	public SuggestionState state;

	public Suggestion(int ID, String text, SuggestionState state)
	{
		this.ID = ID;
		this.text = text;
		this.state = state;
	}
}
