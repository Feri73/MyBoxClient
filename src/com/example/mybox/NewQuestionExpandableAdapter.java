package com.example.mybox;

import java.util.Vector;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

public class NewQuestionExpandableAdapter extends BaseExpandableListAdapter
{
	public Vector<Question> quests;
	Resources res;
	LayoutInflater inflater;
	Activity context;
	Typeface typeface;
	ExpandableListView list;

	void refresh()
	{
		context.runOnUiThread(new Runnable()
		{

			@Override
			public void run()
			{
				notifyDataSetChanged();
			}
		});
	}

	class AddOptionClickListener implements OnClickListener
	{
		int gPosition;

		public AddOptionClickListener(int gPosition)
		{
			this.gPosition = gPosition;
		}

		@Override
		public void onClick(View arg0)
		{
			quests.get(gPosition).options.add("");
			refresh();
		}
	}

	OnClickListener addQuestionListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			quests.add(new Question(""));
			refresh();
		}
	};

	public NewQuestionExpandableAdapter(Resources res, LayoutInflater inflater, Activity context, Typeface typeface, ExpandableListView list)
	{
		this.quests = new Vector<Question>();
		this.res = res;
		this.inflater = inflater;
		this.context = context;
		this.typeface = typeface;
		this.list = list;
	}

	@Override
	public Object getChild(int gPosition, int cPosition)
	{
		if (cPosition == 0)
			return null;
		return quests.get(gPosition - 1).options.get(cPosition - 1);
	}

	@Override
	public long getChildId(int gPosition, int cPosition)
	{
		return cPosition;
	}

	@Override
	public View getChildView(final int gPosition, final int cPosition, boolean isLastItem, View convertView, ViewGroup parent)
	{
		// Log.e("!!!!!!!!!!!!!!!!!!!!!!!Child",
		// "!!!!!!!!!!!!!! "+getChildrenCount(gPosition)+" "+gPosition+" "+cPosition);
		if (convertView == null)
		{
			if (cPosition == 0)
			{
				convertView = inflater.inflate(R.layout.add_sth, null);
				((ImageButton) convertView.findViewById(R.id.add_sth_button)).setBackgroundColor(Color.RED);
				((ImageButton) convertView.findViewById(R.id.add_sth_button)).setOnClickListener(new AddOptionClickListener(gPosition - 1));
			}
			else
			{
				EditText optionEditText = (EditText) inflater.inflate(R.layout.new_option_item, null);
				optionEditText.setText(quests.get(gPosition - 1).options.get(cPosition - 1));
				optionEditText.addTextChangedListener(new TextWatcher()
				{

					@Override
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable arg0)
					{
						quests.get(gPosition - 1).options.set(cPosition - 1, arg0.toString());
					}
				});
				convertView = optionEditText;
			}
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int gPosition)
	{
		if (gPosition == 0)
			return 1;
		// Toast.makeText(context, String.valueOf(quests.get(gPosition - 1) ==
		// null), Toast.LENGTH_SHORT).show();
		// return 1;
		return quests.get(gPosition - 1).options.size() + 1;
	}

	@Override
	public Object getGroup(int gPosition)
	{
		if (gPosition == 0)
			return null;
		return quests.get(gPosition - 1);
	}

	@Override
	public int getGroupCount()
	{
		// Toast.makeText(context, String.valueOf(quests.size()),
		// Toast.LENGTH_SHORT).show();
		return quests.size() + 1;
	}

	@Override
	public long getGroupId(int gPosition)
	{
		return gPosition;
	}

	@Override
	public View getGroupView(final int gPosition, boolean isExpanded, View convertView, ViewGroup parent)
	{
		// Log.e("!!!!!!!!!!!!!!!!!!!!!!!Group",
		// "!!!!!!!!!!!!!! "+getGroupCount()+" "+gPosition+" "+isExpanded);
		if (convertView == null)
		{
			if (gPosition == 0)
			{
				convertView = inflater.inflate(R.layout.add_sth, null);
				((ImageButton) convertView.findViewById(R.id.add_sth_button)).setOnClickListener(addQuestionListener);
			}
			else
			{
				convertView = inflater.inflate(R.layout.new_question_list_group_item, null);
				EditText et = (EditText) convertView.findViewById(R.id.new_question_text);
				et.setText(quests.get(gPosition - 1).text);
				et.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View arg0)
					{
						for (int i = 0; i < getGroupCount(); i++)
							list.collapseGroup(gPosition);
						list.expandGroup(gPosition);
					}
				});
				et.addTextChangedListener(new TextWatcher()
				{

					@Override
					public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
					{
						// TODO Auto-generated method stub

					}

					@Override
					public void afterTextChanged(Editable arg0)
					{
						quests.get(gPosition - 1).text = arg0.toString();
					}
				});
				// ((EditText)
				// convertView.findViewById(R.id.new_question_text)).setFocusable(false);
			}
		}
		// else
		// Log.e("#####################", "###########################");
		return convertView;
	}

	@Override
	public boolean hasStableIds()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int gPosition, int cPosition)
	{
		return true;// cPosition == 0;
	}

}
