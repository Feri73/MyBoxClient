package com.example.mybox;

import java.io.Serializable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class OrganizationPageActivity extends ActionBarActivity
{

	Organization org;
	WaitAlert alertBox;
	ListView surveysList ,sugsList;
	Survey[] surveys;
	Suggestion[] sugs;
	SuggestionAdapter sugAdapter;
	SurveyAdapter surveyAdapter;
	AskedSuggestionsAlertDialog askedSugsAlert;
	RequestAlertDialog reqsAlert;

	OnClickListener showNotifs = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			if (org.asManager)
			{
				reqsAlert.Refresh();
				reqsAlert.show();
			}
			else
			{
				askedSugsAlert.Refresh();
				askedSugsAlert.show();
			}
		}
	};

	OnItemClickListener showSurveyListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
		{
			Intent intent = new Intent(OrganizationPageActivity.this, (org.asManager ? QuestionResultPageActivity.class : SurveyPageActivity.class));
			intent.putExtra("survey", (Serializable) surveys[position]);
			startActivity(intent);
		}
	};

	OnClickListener addSthListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			if (org.asManager)
			{
				Intent intent = new Intent(OrganizationPageActivity.this, MakeSurveyActivity.class);
				intent.putExtra("orgID", org.ID);
				startActivity(intent);
			}
			else
			{
				Intent intent = new Intent(OrganizationPageActivity.this, MakeSuggestionActivity.class);
				intent.putExtra("orgID", org.ID);
				startActivity(intent);
			}
		}
	};

	OnClickListener sugListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			alertBox.show();
			surveysList.setVisibility(View.INVISIBLE);
			sugsList.setVisibility(View.VISIBLE);
			new Thread(showSuggestions).start();
		}
	};

	OnClickListener surveyListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			alertBox.show();
			surveysList.setVisibility(View.VISIBLE);
			sugsList.setVisibility(View.INVISIBLE);
			new Thread(showSurveys).start();
		}
	};

	Runnable showSuggestions = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				sugs = Controller.GetMySuggestions(org.ID);
				sugAdapter = new SuggestionAdapter(sugs, getResources(), getLayoutInflater(), OrganizationPageActivity.this);
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						sugsList.setAdapter(sugAdapter);
					}
				});
			}
			catch (final CustomException e)
			{
				if (e.type.equals("ConnectionFailed"))
					ErrorShower.ShowError(OrganizationPageActivity.this, R.string.connection_failed_mesage);
				else if (e.type.equals("NotLoggedInException"))
					ErrorShower.ShowError(OrganizationPageActivity.this, R.string.not_logged_in_message);
				else if (e.type.equals("AuthenticationError"))
					ErrorShower.ShowError(OrganizationPageActivity.this, R.string.authentication_error);
				else if (e.type.equals("NoIDFound"))
					ErrorShower.ShowError(OrganizationPageActivity.this, R.string.no_id_found_message);
			}
			finally
			{
				alertBox.cancel();
			}
		}

	};

	Runnable showSurveys = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				int[] id = Controller.GetOrgsSurveys(org.ID);
				surveys = new Survey[id.length];
				for (int i = 0; i < id.length; i++)
					surveys[i] = Controller.GetSurveyInfo(id[i]);
				surveyAdapter = new SurveyAdapter(surveys, getResources(), getLayoutInflater(), OrganizationPageActivity.this);
				runOnUiThread(new Runnable()
				{
					@Override
					public void run()
					{
						surveysList.setAdapter(surveyAdapter);
					}
				});
			}
			catch (final CustomException e)
			{
				if (e.type.equals("ConnectionFailed"))
					ErrorShower.ShowError(OrganizationPageActivity.this, R.string.connection_failed_mesage);
				else if (e.type.equals("NotLoggedInException"))
					ErrorShower.ShowError(OrganizationPageActivity.this, R.string.not_logged_in_message);
				else if (e.type.equals("AuthenticationError"))
					ErrorShower.ShowError(OrganizationPageActivity.this, R.string.authentication_error);
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
		Intent intent = getIntent();
		org = (Organization) intent.getExtras().getSerializable("org");
		alertBox = new WaitAlert(this);
		alertBox.show();
		surveysList = (ListView) findViewById(R.id.surveys_list_view);
		sugsList = (ListView) findViewById(R.id.suggestions_list_view);
		surveysList.setVisibility(View.INVISIBLE);
		surveysList.setOnItemClickListener(showSurveyListener);
		new Thread(showSuggestions).start();

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View customNav = LayoutInflater.from(this).inflate(R.layout.organization_page_action_bar, null);
		((ImageButton) customNav.findViewById(R.id.suggestions_button)).setOnClickListener(sugListener);
		((ImageButton) customNav.findViewById(R.id.surveys_button)).setOnClickListener(surveyListener);
		((ImageButton) customNav.findViewById(R.id.add_sth_button)).setOnClickListener(addSthListener);
		((ImageButton) customNav.findViewById(R.id.get_notifs_button)).setOnClickListener(showNotifs);
		actionBar.setCustomView(customNav, lp1);

		askedSugsAlert = new AskedSuggestionsAlertDialog(this, org.ID);
		reqsAlert = new RequestAlertDialog(this, org.ID);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_organization_page);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.organization_page, menu);
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
