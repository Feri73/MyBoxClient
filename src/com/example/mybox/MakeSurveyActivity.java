package com.example.mybox;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

public class MakeSurveyActivity extends ActionBarActivity
{

	ExpandableListView list;
	WaitAlert alertBox;

	void init()
	{
		alertBox = new WaitAlert(this);
		list = (ExpandableListView) findViewById(R.id.new_survey_question_list);
		list.setAdapter(new NewQuestionExpandableAdapter(getResources(), getLayoutInflater(), this, null, list));
		list.setDividerHeight(2);
		list.setGroupIndicator(null);
		list.setClickable(true);
		((Button) findViewById(R.id.save_survey_button)).setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				alertBox.show();
				new Thread(new Runnable()
				{

					@Override
					public void run()
					{
						// TODO Auto-generated method stub

						try
						{
							Controller.CreateSurvey(((TextView) findViewById(R.id.new_survey_text)).getText().toString(), ((NewQuestionExpandableAdapter) list.getExpandableListAdapter()).quests, getIntent().getExtras().getInt("orgID"));
						}
						catch (CustomException e)
						{
							if (e.type.equals("ConnectionFailed"))
								ErrorShower.ShowError(MakeSurveyActivity.this, R.string.connection_failed_mesage);
							else if (e.type.equals("NotLoggedInException"))
								ErrorShower.ShowError(MakeSurveyActivity.this, R.string.not_logged_in_message);
							else if (e.type.equals("AuthenticationError"))
								ErrorShower.ShowError(MakeSurveyActivity.this, R.string.authentication_error);
							else if (e.type.equals("NoIDFound"))
								;
						}
						finally
						{
							alertBox.cancel();
						}
					}
				}).start();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_survey);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.make_survey, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
