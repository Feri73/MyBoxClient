package com.example.mybox;

import java.io.Serializable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.LayoutParams;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class SeachActivity extends ActionBarActivity
{

	Organization[] orgs;
	Thread searchThread = new Thread();
	String expression;
	OrganizationAdapter orgAdapter;
	ListView orgList;

	OnItemClickListener itemClickListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> adapter, View view, int position, long id)
		{
			Intent intent = new Intent(SeachActivity.this, EnrollOrgActivity.class);
			intent.putExtra("org", (Serializable) orgs[position]);
			startActivity(intent);
		}
	};

	Runnable search = new Runnable()
	{

		@Override
		public void run()
		{
			try
			{
				int[] id = Controller.SearchOrgs(expression);
				if (Thread.interrupted())
					return;
				orgs = new Organization[id.length];
				for (int i = 0; i < id.length; i++)
				{
					orgs[i] = Controller.GetOrgInfo(id[i]);
					if (Thread.interrupted())
						return;
				}
				orgAdapter = new OrganizationAdapter(orgs, getResources(), getLayoutInflater(), SeachActivity.this);
				if (Thread.interrupted())
					return;
				runOnUiThread(new Runnable()
				{

					@Override
					public void run()
					{
						orgList.setAdapter(orgAdapter);
					}
				});
			}
			catch (CustomException e)
			{
				if (e.type.equals("ConnectionFailed"))
					ErrorShower.ShowError(SeachActivity.this, R.string.connection_failed_mesage);
				else if (e.type.equals("NotLoggedInException"))
					ErrorShower.ShowError(SeachActivity.this, R.string.not_logged_in_message);
				else if (e.type.equals("AuthenticationError"))
					ErrorShower.ShowError(SeachActivity.this, R.string.authentication_error);
				else if (e.type.equals("NoIDFound"))
					;
			}
		}
	};

	TextWatcher watcher = new TextWatcher()
	{
		@Override
		public void onTextChanged(CharSequence expression, int arg1, int arg2, int count)
		{
			if (expression.length() > 2)
			{
				SeachActivity.this.expression=expression.toString();
				searchThread.interrupt();
				searchThread = new Thread(search);
				searchThread.start();
			}
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3)
		{
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable arg0)
		{
			// TODO Auto-generated method stub

		}
	};

	void init()
	{
		orgList = (ListView) findViewById(R.id.searched_org_list_view);
		orgList.setOnItemClickListener(itemClickListener);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View customNav = LayoutInflater.from(this).inflate(R.layout.search_action_bar, null);
		((EditText) customNav.findViewById(R.id.search_edit_text)).addTextChangedListener(watcher);
		actionBar.setCustomView(customNav, lp1);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seach);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.seach, menu);
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
