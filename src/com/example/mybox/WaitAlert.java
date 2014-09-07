package com.example.mybox;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class WaitAlert extends AlertDialog
{

	public WaitAlert(Context context)
	{
		super(context);
		View view = LayoutInflater.from(context).inflate(R.layout.login_dialog, null);
		Typeface typeFace = Typeface.createFromAsset(context.getAssets(), "fonts/A_Soraya.ttf");
		setView(view);
		setCancelable(false);
		TextView connectingTextView = (TextView) view.findViewById(R.id.logginInTextView);
		connectingTextView.setTypeface(typeFace);
	}
	
	
}
