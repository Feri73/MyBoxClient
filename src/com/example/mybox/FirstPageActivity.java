package com.example.mybox;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

public class FirstPageActivity extends ActionBarActivity
{

	ListView orgList;
	OrganizationAdapter orgAdapter;
	Organization[] orgs;
	WaitAlert alertBox;

	final int ADD_ORG = 1;

	OnItemClickListener orgsItemListener = new OnItemClickListener()
	{

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int position, long id)
		{
			Intent intent = new Intent(FirstPageActivity.this, OrganizationPageActivity.class);
			intent.putExtra("org", orgs[position]);
			startActivity(intent);
		}
	};

	Runnable refresh = new Runnable()
	{
		@Override
		public void run()
		{
			try
			{
				int[] id = Controller.GetUsersOrgs();
				orgs = new Organization[id.length];
				for (int i = 0; i < id.length; i++)
					orgs[i] = Controller.GetOrgInfo(id[i]);
				orgAdapter = new OrganizationAdapter(orgs, getResources(), getLayoutInflater(), FirstPageActivity.this);
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
					ErrorShower.ShowError(FirstPageActivity.this, R.string.connection_failed_mesage);
				else if (e.type.equals("NotLoggedInException"))
					ErrorShower.ShowError(FirstPageActivity.this, R.string.not_logged_in_message);
				else if (e.type.equals("AuthenticationError"))
					ErrorShower.ShowError(FirstPageActivity.this, R.string.authentication_error);
				else if (e.type.equals("NoIDFound"))
					;
			}
			finally
			{
				alertBox.cancel();
			}
		}

	};

	OnClickListener refreshListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			new Thread(refresh).start();
		}
	};

	OnClickListener searchListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			Intent intent = new Intent(FirstPageActivity.this, SeachActivity.class);
			startActivity(intent);
		}
	};

	OnClickListener addListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			Intent intent = new Intent(FirstPageActivity.this, MakeOrgActivity.class);
			startActivityForResult(intent,ADD_ORG);
		}
	};

	void init()
	{
		alertBox = new WaitAlert(this);// (WaitAlert) new
										// AlertDialog.Builder(this).create();
		alertBox.show();
		orgList = (ListView) findViewById(R.id.org_list_view);
		new Thread(refresh).start();

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View customNav = LayoutInflater.from(this).inflate(R.layout.first_page_action_bar, null);
		((ImageButton) customNav.findViewById(R.id.refresh_orgs_button)).setOnClickListener(refreshListener);
		((ImageButton) customNav.findViewById(R.id.add_orgs_button)).setOnClickListener(addListener);
		((ImageButton) customNav.findViewById(R.id.search_orgs_button)).setOnClickListener(searchListener);
		actionBar.setCustomView(customNav, lp1);

		orgList.setOnItemClickListener(orgsItemListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_first_page);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.first_page, menu);
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED)
			return;
		if(requestCode==ADD_ORG)
			new Thread(refresh).start();
	}
}
