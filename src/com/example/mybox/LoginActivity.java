package com.example.mybox;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity
{
	Typeface typeFace;
	EditText username ,password;
	Button signIn ,signUp;
	WaitAlert alertBox;

	OnClickListener signUpClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			if (username.getText().toString().equals("") || password.getText().toString().equals(arg0))
			{
				ErrorShower.ShowError(LoginActivity.this, R.string.empty_fields_message);
				return;
			}
			alertBox.show();
			new Thread(new Runnable()
			{
				@Override
				public void run()
				{
					try
					{
						if (!Controller.SignUp(username.getText().toString(), password.getText().toString()))
							ErrorShower.ShowError(LoginActivity.this, R.string.user_pass_mismatch);
						else
							goToFistPage();

					}
					catch (CustomException e)
					{
						if (e.type.equals("ConnectionFailed"))
							ErrorShower.ShowError(LoginActivity.this, R.string.connection_failed_mesage);
						else if (e.type.equals("DuplicateUsername"))
							ErrorShower.ShowError(LoginActivity.this, R.string.duplicate_user_message);
					}
					finally
					{
						alertBox.cancel();
					}
				}
			}).start();
		}
	};

	OnClickListener signInClickListener = new OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			if (username.getText().toString().equals("") || password.getText().toString().equals(""))
			{
				ErrorShower.ShowError(LoginActivity.this, R.string.empty_fields_message);
				return;
			}
			alertBox.show();
			new Thread(new Runnable()
			{

				@Override
				public void run()
				{
					try
					{
						if (!Controller.Login(username.getText().toString(), password.getText().toString()))
							ErrorShower.ShowError(LoginActivity.this, R.string.user_pass_mismatch);
						else
							goToFistPage();
					}
					catch (final CustomException e)
					{
						if (e.type.equals("ConnectionFailed"))
							ErrorShower.ShowError(LoginActivity.this, R.string.connection_failed_mesage);
						else
							runOnUiThread(new Runnable()
							{

								@Override
								public void run()
								{
									Toast.makeText(LoginActivity.this, e.type, Toast.LENGTH_LONG).show();
								}
							});
					}
					finally
					{
						alertBox.cancel();
					}
				}
			}).start();
		}
	};

	void goToFistPage()
	{
		Intent intent = new Intent(this, FirstPageActivity.class);
		startActivity(intent);
	}

	void init()
	{
		typeFace = Typeface.createFromAsset(getAssets(), "fonts/A_Soraya.ttf");
		username = (EditText) findViewById(R.id.username_textbox);
		password = (EditText) findViewById(R.id.password_textbox);
		signIn = (Button) findViewById(R.id.sign_in_button);
		signUp = (Button) findViewById(R.id.sign_up_button);
		username.setTypeface(typeFace);
		password.setTypeface(typeFace);
		signIn.setTypeface(typeFace);
		signUp.setTypeface(typeFace);
		signIn.setOnClickListener(signInClickListener);
		signUp.setOnClickListener(signUpClickListener);
		alertBox = new WaitAlert(this);// (WaitAlert) new
										// AlertDialog.Builder(this).create();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

}
