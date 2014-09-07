package com.example.mybox;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SuggestionAdapter extends BaseAdapter
{
	Suggestion[] sugs;
	Resources res;
	LayoutInflater inflater;
	Activity context;
	Typeface typeface;

	public SuggestionAdapter(Suggestion[] sugs, Resources res, LayoutInflater inflater, Activity context)
	{
		this.sugs = sugs;
		this.res = res;
		this.inflater = inflater;
		this.context = context;
		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ATaha.ttf");
	}

	@Override
	public int getCount()
	{
		return Math.max(sugs.length, 1);
	}

	@Override
	public Object getItem(int position)
	{
		if (position > sugs.length)
			return null;
		return sugs[position];
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
		TextView sugText = null;
		ImageView sugStates = null;
		if (convertView == null)
		{
			view = inflater.inflate(R.layout.suggestion_item, null);
			sugText = (TextView) view.findViewById(R.id.sugegstion_text_text_view);
			sugStates = (ImageView) view.findViewById(R.id.suggestion_state_image);
		}
		if (sugs.length == 0)
			sugText.setText("No Data!");
		else if (convertView == null)
		{
			Suggestion sug = sugs[position];
			sugText.setText(sug.text);
			//sugText.setTypeface(typeface);
			switch(sug.state)
			{
				case Accepted:
					sugStates.setImageResource(R.drawable.ic_action_accept);
					break;
				case Rejected:
					sugStates.setImageResource(R.drawable.ic_action_cancel);
					break;
				default:
					break;
			}
		}
		return view;
	}
}
