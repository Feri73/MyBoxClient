package com.example.mybox;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MakeSuggestionActivity extends ActionBarActivity
{

	EditText suggestionText;
	Button sendButton;
	int orgID;
	WaitAlert alertBox;

	Runnable make = new Runnable()
	{

		@Override
		public void run()
		{
			try
			{
				Controller.Suggest(suggestionText.getText().toString(), orgID);
				ErrorShower.ShowError(MakeSuggestionActivity.this, R.string.succesfull_suggestion);
				finish();
			}
			catch (final CustomException e)
			{
				if (e.type.equals("ConnectionFailed"))
					ErrorShower.ShowError(MakeSuggestionActivity.this, R.string.connection_failed_mesage);
				else if (e.type.equals("NotLoggedInException"))
					ErrorShower.ShowError(MakeSuggestionActivity.this, R.string.not_logged_in_message);
				else if (e.type.equals("AuthenticationError"))
					ErrorShower.ShowError(MakeSuggestionActivity.this, R.string.authentication_error);
				else if (e.type.equals("NoIDFound"))
					ErrorShower.ShowError(MakeSuggestionActivity.this, R.string.no_id_found_message);
			}
			finally
			{
				alertBox.cancel();
			}
		}
	};

	OnClickListener makeListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			if (suggestionText.getEditableText().toString().equals(""))
			{
				ErrorShower.ShowError(MakeSuggestionActivity.this, R.string.empty_fields_message);
				return;
			}
			alertBox.show();
			new Thread(make).start();
		}
	};

	void init()
	{
		Intent intent = getIntent();
		orgID = intent.getExtras().getInt("orgID");
		alertBox = new WaitAlert(this);
		suggestionText = (EditText) findViewById(R.id.suggestion_edit_text);
		sendButton = (Button) findViewById(R.id.make_suggestion_button);
		sendButton.setOnClickListener(makeListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_suggestion);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.make_suggestion, menu);
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
