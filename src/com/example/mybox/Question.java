package com.example.mybox;

import java.util.List;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;

public class Question
{
	public String text;
	public Vector<String> options;
	// public String[] options;
	public int ID;
	public int chosen;

	public Question(int ID, String text, int chosen)
	{
		this(text);
		this.ID = ID;
		this.chosen = chosen;
		options = new Vector<String>();
	}

	public Question(String text)
	{
		this.text = text;
		options = new Vector<String>();
	}

	public JSONObject ToJSONObject() throws Exception
	{
		JSONObject result = new JSONObject();
		JSONArray ops = new JSONArray();
		for (int i = 0; i < options.size(); i++)
			ops.put(options.get(i));
		result.put("options", ops);
		result.put("text", text);
		return result;
	}
}
