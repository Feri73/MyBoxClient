package com.example.mybox;

import java.util.Dictionary;
import java.util.HashMap;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

public class SurveyPageActivity extends ActionBarActivity
{

	Survey survey;
	Question[] questions;
	WaitAlert alertBox;
	QuestionAdapter qAdapter;
	ListView qList;
	Button voteButton;

	Runnable vote = new Runnable()
	{

		@Override
		public void run()
		{
			try
			{
				RadioGroup[] rg = qAdapter.rg;
				SparseIntArray qo = new SparseIntArray();
				for (int i = 0; i < qAdapter.getCount(); i++)
					qo.put(questions[i].ID, rg[i].getCheckedRadioButtonId());
				Controller.Vote(survey.ID, qo);
			}
			catch (CustomException e)
			{
				if (e.type.equals("ConnectionFailed"))
					ErrorShower.ShowError(SurveyPageActivity.this, R.string.connection_failed_mesage);
				else if (e.type.equals("NotLoggedInException"))
					ErrorShower.ShowError(SurveyPageActivity.this, R.string.not_logged_in_message);
				else if (e.type.equals("AuthenticationError"))
					ErrorShower.ShowError(SurveyPageActivity.this, R.string.authentication_error);
				else if (e.type.equals("NoIDFound"))
					ErrorShower.ShowError(SurveyPageActivity.this, R.string.no_id_found_message);
			}
			finally
			{
				alertBox.cancel();
			}
		}
	};

	Runnable show = new Runnable()
	{

		@Override
		public void run()
		{
			try
			{
				questions = new Question[survey.questionsID.length];
				for (int i = 0; i < questions.length; i++)
					questions[i] = Controller.GetQuestionInfo(survey.questionsID[i]);
				qAdapter = new QuestionAdapter(questions, getResources(), getLayoutInflater(), SurveyPageActivity.this, survey.endDate);
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						qList.setAdapter(qAdapter);
					}
				});
			}
			catch (CustomException e)
			{
				if (e.type.equals("ConnectionFailed"))
					ErrorShower.ShowError(SurveyPageActivity.this, R.string.connection_failed_mesage);
				else if (e.type.equals("NotLoggedInException"))
					ErrorShower.ShowError(SurveyPageActivity.this, R.string.not_logged_in_message);
				else if (e.type.equals("AuthenticationError"))
					ErrorShower.ShowError(SurveyPageActivity.this, R.string.authentication_error);
				else if (e.type.equals("NoIDFound"))
					;
			}
			finally
			{
				alertBox.cancel();
			}
		}
	};

	void init()
	{
		alertBox = new WaitAlert(this);

		Intent intent = getIntent();
		survey = (Survey) intent.getExtras().getSerializable("survey");
		qList = (ListView) findViewById(R.id.questions_list_view);
		((TextView) findViewById(R.id.survey_text_view)).setText(survey.text);
		alertBox.show();
		new Thread(show).start();

		voteButton = (Button) findViewById(R.id.vote_button);
		voteButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0)
			{
				alertBox.show();
				new Thread(vote).start();
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_survey_page);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question_page, menu);
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
