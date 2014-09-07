package com.example.mybox;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class MakeOrgActivity extends ActionBarActivity
{

	EditText nameText ,contentText;
	Button addButton ,chooseButton ,fileButton;
	ImageView image;
	Spinner spinner;
	WaitAlert alertBox;
	String imagePath="" ,filePath="";

	final int CHOOSE_IMAGE = 1 ,CHOOSE_FILE = 2;

	OnClickListener addListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			if (nameText.getText().toString().equals(""))
			{
				ErrorShower.ShowError(MakeOrgActivity.this, R.string.empty_fields_message);
				return;
			}
			RegisterType regType = null;
			switch (spinner.getSelectedItemPosition())
			{
				case 0:
					regType = RegisterType.Default;
					break;
				case 1:
					regType = RegisterType.NeedAccept;
					break;
				case 2:
					regType = RegisterType.FromFile;
					break;
			}
			final RegisterType type = regType;
			alertBox.show();
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						Controller.CreateOrg(new Organization(nameText.getText().toString(), imagePath, contentText.getText().toString(), type, filePath));
						setResult(RESULT_OK);
						finish();
					}
					catch (CustomException e)
					{
						if (e.type.equals("ConnectionFailed"))
							ErrorShower.ShowError(MakeOrgActivity.this, R.string.connection_failed_mesage);
						else if (e.type.equals("NotLoggedInException"))
							ErrorShower.ShowError(MakeOrgActivity.this, R.string.not_logged_in_message);
						else if (e.type.equals("AuthenticationError"))
							ErrorShower.ShowError(MakeOrgActivity.this, R.string.authentication_error);
					}
					finally
					{
						alertBox.cancel();
					}
				}
			}).start();
		}
	};

	OnClickListener imageListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			try
			{
				Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
				intent.putExtra("CONTENT_TYPE", "image/*");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				final PackageManager packageManager = getPackageManager();
				List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
				if (list.size() > 0)
					startActivityForResult(intent, CHOOSE_IMAGE);
				else
				{
					intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("image/*");
					startActivityForResult(intent, CHOOSE_IMAGE);
				}
			}
			catch (Exception e)
			{
				ErrorShower.ShowError(MakeOrgActivity.this, R.string.no_picker_message);
			}
		}
	};

	OnClickListener fileListener = new OnClickListener()
	{

		@Override
		public void onClick(View v)
		{
			try
			{
				Intent intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
				intent.putExtra("CONTENT_TYPE", "application/vnd.ms-excel");
				intent.addCategory(Intent.CATEGORY_DEFAULT);
				final PackageManager packageManager = getPackageManager();
				List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
				if (list.size() > 0)
					startActivityForResult(intent, CHOOSE_IMAGE);
				else
				{
					intent = new Intent(Intent.ACTION_GET_CONTENT);
					intent.setType("application/vnd.ms-excel");
					startActivityForResult(intent, CHOOSE_IMAGE);
				}
			}
			catch (Exception e)
			{
				ErrorShower.ShowError(MakeOrgActivity.this, R.string.no_picker_message);
			}
		}
	};

	void init()
	{
		nameText = (EditText) findViewById(R.id.new_org_name);
		contentText = (EditText) findViewById(R.id.new_org_content);
		addButton = (Button) findViewById(R.id.create_org_button);
		chooseButton = (Button) findViewById(R.id.new_org_image_choose);
		fileButton = (Button) findViewById(R.id.new_org_file_chooser);
		image = (ImageView) findViewById(R.id.new_org_image_preview);
		spinner = (Spinner) findViewById(R.id.new_org_typ_spinner);
		alertBox = new WaitAlert(this);
		addButton.setOnClickListener(addListener);
		chooseButton.setOnClickListener(imageListener);
		fileButton.setOnClickListener(fileListener);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_make_org);

		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.make_org, menu);
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
		if (requestCode == CHOOSE_IMAGE)
		{
			imagePath = data.getData().toString();
			FileInputStream is;
			try
			{
				is = new FileInputStream(imagePath + "/apple.png");
				BitmapDrawable icon = new BitmapDrawable(getResources(), is);
				image.setImageDrawable(icon);
			}
			catch (FileNotFoundException e)
			{
			}
		}
		if (requestCode == CHOOSE_FILE)
			filePath = data.getData().toString();
	}
}
