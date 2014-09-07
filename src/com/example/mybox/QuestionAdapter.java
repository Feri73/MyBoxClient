package com.example.mybox;

import java.util.Calendar;
import java.util.Date;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class QuestionAdapter extends BaseAdapter
{

	Question[] quests;
	Resources res;
	LayoutInflater inflater;
	Activity context;
	Typeface typeface;
	RadioButton[] options;
	Date expiredDate;

	public RadioGroup[] rg;

	public QuestionAdapter(Question[] quests, Resources res, LayoutInflater inflater, Activity context, Date expiredDate)
	{
		this.quests = quests;
		this.res = res;
		this.inflater = inflater;
		this.context = context;
		this.expiredDate = expiredDate;
		rg = new RadioGroup[quests.length];
		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ATaha.ttf");
	}

	@Override
	public int getCount()
	{
		return Math.max(quests.length, 1);
	}

	@Override
	public Object getItem(int position)
	{
		if (position > quests.length)
			return null;
		return quests[position];
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public boolean areAllItemsEnabled()
	{
		return false;
	}

	@Override
	public boolean isEnabled(int position)
	{
		return false;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		TextView qText = null;
		if (convertView == null)
		{
			view = inflater.inflate(R.layout.question_item, null);
			qText = (TextView) view.findViewById(R.id.question_text_view);
		}
		if (quests.length == 0)
			qText.setText("No Data!");
		else if (convertView == null)
		{
			RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.options_group_box);
			rg[position] = radioGroup;
			Question q = quests[position];
			qText.setText(q.text);
			Calendar cal = Calendar.getInstance();
			Date now = cal.getTime();
			options = new RadioButton[q.options.size()];
			for (int i = 0; i < options.length; i++)
			{
				options[i] = new RadioButton(context);
				options[i].setId(i);
				options[i].setButtonDrawable(new StateListDrawable());
				options[i].setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.btn_radio, 0);
				options[i].setText(q.options.get(i));
				if (now.after(expiredDate))
					options[i].setEnabled(false);
				radioGroup.addView(options[i]);
			}
			if (q.chosen != -1)
				options[q.chosen].setChecked(true);
		}
		return view;
	}
}
