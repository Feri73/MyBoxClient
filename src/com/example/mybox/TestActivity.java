package com.example.mybox;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.LayoutParams;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

public class TestActivity extends ActionBarActivity
{
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		Organization[] orgs = new Organization[4];
		//orgs[0] = new Organization(1, "University of Tehran", "http://t3.gstatic.com/images?q=tbn:ANd9GcSgLAKwd_KQPbqagbU7_eYk6BDQPdW9--brsLcE7CVbDEFXCojTZw", "This university is the best one in iran. Faraz Yazdani studies here!\nYes!!", true, 3);
		//orgs[1] = new Organization(1, "SAMPAD", "http://www.google.com/images/srpr/logo11w.png", "SAMPAD", false, 2);
		//orgs[2] = new Organization(1, "SAMPAD", "http://www.google.com/images/srpr/logo11w.png", "SAMPAD", false, 0);
		//orgs[3] = new Organization(1, "SAMPAD", "http://www.google.com/images/srpr/logo11w.png", "SAMPAD", false, 1);
		OrganizationAdapter adapter = new OrganizationAdapter(orgs, getResources(), getLayoutInflater(), this);
		ListView lv = (ListView) findViewById(R.id.lv);
		lv.setAdapter(adapter);

		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);

		ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		View customNav = LayoutInflater.from(this).inflate(R.layout.actionbar, null);
		actionBar.setCustomView(customNav, lp1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_test, menu);
		return true;
	}

}
