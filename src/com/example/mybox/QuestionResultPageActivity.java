package com.example.mybox;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

public class QuestionResultPageActivity extends ActionBarActivity
{
	WaitAlert alertBox;
	QuestionResultExpandableAdapter adapter;
	ExpandableListView list;

	void init()
	{
		alertBox = new WaitAlert(this);
		list = (ExpandableListView) findViewById(R.id.question_result_list);
		final Survey survey = (Survey) getIntent().getExtras().getSerializable("survey");
		alertBox.show();
		new Thread(new Runnable()
		{

			@Override
			public void run()
			{
				try
				{
					Question[] questions = new Question[survey.questionsID.length];
					int[][] ratios = new int[questions.length][];
					for (int i = 0; i < questions.length; i++)
					{
						questions[i] = Controller.GetQuestionInfo(survey.questionsID[i]);
						ratios[i] = Controller.GetQuestionResult(questions[i].ID);
					}
					adapter = new QuestionResultExpandableAdapter(questions, ratios, getResources(), getLayoutInflater(), QuestionResultPageActivity.this, list);
					runOnUiThread(new Runnable()
					{
						
						@Override
						public void run()
						{
							list.setAdapter(adapter);
						}
					});
				}
				catch (CustomException e)
				{
					Log.e("!!!!!!!!!!!!!!!","!!!! "+e.type);
					if (e.type.equals("ConnectionFailed"))
						ErrorShower.ShowError(QuestionResultPageActivity.this, R.string.connection_failed_mesage);
					else if (e.type.equals("NotLoggedInException"))
						ErrorShower.ShowError(QuestionResultPageActivity.this, R.string.not_logged_in_message);
					else if (e.type.equals("AuthenticationError"))
						ErrorShower.ShowError(QuestionResultPageActivity.this, R.string.authentication_error);
					else if (e.type.equals("NoIDFound"))
						ErrorShower.ShowError(QuestionResultPageActivity.this, R.string.no_id_found_message);
				}
				finally
				{
					alertBox.cancel();
				}
			}
		}).start();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_result_page);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.question_result_page, menu);
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
