package com.example.mybox;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class QuestionResultExpandableAdapter extends BaseExpandableListAdapter
{
	Question[] quests;
	int[][] ratios;
	Resources res;
	LayoutInflater inflater;
	Activity context;
	Typeface typeface;
	ExpandableListView list;

	public QuestionResultExpandableAdapter(Question[] quests, int[][] ratios, Resources res, LayoutInflater inflater, Activity context, ExpandableListView list)
	{
		this.quests = quests;
		this.ratios = ratios;
		this.res = res;
		this.inflater = inflater;
		this.context = context;
		this.list = list;
	}

	@Override
	public Object getChild(int gPosition, int cPosition)
	{
		return quests[gPosition].options.get(cPosition);
	}

	@Override
	public long getChildId(int gPosition, int cPosition)
	{
		return cPosition;
	}

	@Override
	public View getChildView(int gPosition, int cPosition, boolean isLastItem, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.option_result_item, null);
			((TextView) convertView.findViewById(R.id.result_text_view)).setText(ratios[gPosition][cPosition] + " " + res.getString(R.string.person_unit));
			((TextView) convertView.findViewById(R.id.option_text_view)).setText(quests[gPosition].options.get(cPosition));
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int gPosition)
	{
		return quests[gPosition].options.size();
	}

	@Override
	public Object getGroup(int gPosition)
	{
		return quests[gPosition];
	}

	@Override
	public int getGroupCount()
	{
		return quests.length;
	}

	@Override
	public long getGroupId(int gPosition)
	{
		return gPosition;
	}

	@Override
	public View getGroupView(final int gPosition, final boolean isExpanded, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			convertView = inflater.inflate(R.layout.question_result_item, null);
			((TextView) convertView.findViewById(R.id.question_result_text_view)).setText(quests[gPosition].text);
		}
		((Button) convertView.findViewById(R.id.expand_question_result_button)).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				if (isExpanded)
					list.collapseGroup(gPosition);
				else
					list.expandGroup(gPosition);
			}
		});
		return convertView;

	}

	@Override
	public boolean hasStableIds()
	{
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isChildSelectable(int gPosition, int cPosition)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
