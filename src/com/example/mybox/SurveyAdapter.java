package com.example.mybox;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SurveyAdapter extends BaseAdapter
{
	Survey[] surveys;
	Resources res;
	LayoutInflater inflater;
	Activity context;
	Typeface typeface;

	public SurveyAdapter(Survey[] surveys, Resources res, LayoutInflater inflater, Activity context)
	{
		this.surveys = surveys;
		this.res = res;
		this.inflater = inflater;
		this.context = context;
		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ATaha.ttf");
	}

	@Override
	public int getCount()
	{
		return Math.max(surveys.length, 1);
	}

	@Override
	public Object getItem(int position)
	{
		if (position > surveys.length)
			return null;
		return surveys[position];
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		TextView surveyText = null;
		if (convertView == null)
		{
			view = inflater.inflate(R.layout.survey_item, null);
			surveyText = (TextView) view.findViewById(R.id.survey_brief_text_view);
		}
		if (surveys.length == 0)
			surveyText.setText("No Data!");
		else if (convertView == null)
		{
			Survey survey = surveys[position];
			surveyText.setText(survey.text);
		}
		return view;
	}
}
