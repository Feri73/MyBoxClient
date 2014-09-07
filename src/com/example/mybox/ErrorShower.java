package com.example.mybox;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Toast;

public class ErrorShower
{
	public static void ShowError(final Activity activity, final int id)
	{
		final Typeface typeFace = Typeface.createFromAsset(activity.getAssets(), "fonts/A_Soraya.ttf");
		activity.runOnUiThread(new Runnable()
		{
			public void run()
			{
				Toast toast = new Toast(activity);
				TextView tv = new TextView(activity);
				tv.setPadding(10, 10, 10, 10);
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText(id);
				tv.setTypeface(typeFace);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(tv);
				toast.show();
			}
		});
	}
	public static void ShowError(final Activity activity, final String text)
	{
		final Typeface typeFace = Typeface.createFromAsset(activity.getAssets(), "fonts/A_Soraya.ttf");
		activity.runOnUiThread(new Runnable()
		{
			public void run()
			{
				Toast toast = new Toast(activity);
				TextView tv = new TextView(activity);
				tv.setPadding(10, 10, 10, 10);
				tv.setBackgroundColor(Color.GRAY);
				tv.setTextColor(Color.WHITE);
				tv.setText(text);
				tv.setTypeface(typeFace);
				toast.setDuration(Toast.LENGTH_LONG);
				toast.setView(tv);
				toast.show();
			}
		});
	}
}
