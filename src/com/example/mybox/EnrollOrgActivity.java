package com.example.mybox;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class EnrollOrgActivity extends ActionBarActivity
{

	ViewGroup orgDetailView;
	EditText key ,password;
	Button enrollButton;
	Organization org;
	WaitAlert alertBox;

	Runnable enroll = new Runnable()
	{

		@Override
		public void run()
		{
			try
			{
				String state = Controller.RegisterInOrg(org.ID, key.getText().toString(), password.getText().toString());
				if (state.equals("waitForAccept"))
					ErrorShower.ShowError(EnrollOrgActivity.this, R.string.waitForAccept);
				else if (state.equals("OK"))
					ErrorShower.ShowError(EnrollOrgActivity.this, R.string.enroll_ok_message);
				else if (state.equals("keyPassMistmatch"))
					ErrorShower.ShowError(EnrollOrgActivity.this, R.string.key_pass_mismatch);
			}
			catch (CustomException e)
			{
				if (e.type.equals("ConnectionFailed"))
					ErrorShower.ShowError(EnrollOrgActivity.this, R.string.connection_failed_mesage);
				else if (e.type.equals("NotLoggedInException"))
					ErrorShower.ShowError(EnrollOrgActivity.this, R.string.not_logged_in_message);
				else if (e.type.equals("AuthenticationError"))
					ErrorShower.ShowError(EnrollOrgActivity.this, R.string.authentication_error);
				else if (e.type.equals("HasSentRequest"))
					ErrorShower.ShowError(EnrollOrgActivity.this, R.string.has_sent_request);
				else if (e.type.equals("NoIDFound"))
					ErrorShower.ShowError(EnrollOrgActivity.this, R.string.no_id_found_message);
				else if (e.type.equals("InvalidRequest"))
					;
				ErrorShower.ShowError(EnrollOrgActivity.this, R.string.invalid_request_message);
			}
			finally
			{
				alertBox.cancel();
			}
		}
	};

	OnClickListener listener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			if (org.regType == RegisterType.FromFile && (key.getText().toString().equals("") || password.getText().toString().equals("")))
			{
				ErrorShower.ShowError(EnrollOrgActivity.this, R.string.empty_fields_message);
				return;
			}
			alertBox.show();
			new Thread(enroll).start();
		}
	};

	void init()
	{
		orgDetailView = (ViewGroup) findViewById(R.id.org_detail_view);
		View myView = getLayoutInflater().inflate(R.layout.organization_item, null);
		orgDetailView.addView(myView);
		org = (Organization) getIntent().getExtras().get("org");
		((TextView) orgDetailView.findViewById(R.id.orgName)).setText(org.name);
		ImageDownloader downloader = new ImageDownloader((ImageView) orgDetailView.findViewById(R.id.orgPic));
		downloader.execute(org.picAddress);
		((TextView) orgDetailView.findViewById(R.id.orgDesc)).setText(org.description);

		alertBox = new WaitAlert(this);

		key = (EditText) findViewById(R.id.enrollment_key_edit_text);
		password = (EditText) findViewById(R.id.enrollment_password_edit_text);
		enrollButton = (Button) findViewById(R.id.enroll_button);
		enrollButton.setOnClickListener(listener);
		if (org.regType != RegisterType.FromFile)
		{
			key.setVisibility(View.INVISIBLE);
			password.setVisibility(View.INVISIBLE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_enroll_org);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.enroll_org, menu);
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
