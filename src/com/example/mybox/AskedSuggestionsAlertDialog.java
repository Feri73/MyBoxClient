package com.example.mybox;

import java.util.Vector;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AskedSuggestionsAlertDialog extends AlertDialog
{
	Vector<Suggestion> sugs;
	Activity context;
	WaitAlert alertBox;
	TextView sugText;
	Button nextButton ,lastButton ,acceptButton ,rejectButton;
	int orgID ,sugIndex;

	class AnswerRunnable implements Runnable
	{
		Boolean answer;

		public AnswerRunnable(Boolean answer)
		{
			this.answer = answer;
		}

		@Override
		public void run()
		{
			try
			{
				Controller.AnswerSuggestion(sugs.get(sugIndex).ID, answer);
				sugs.remove(sugIndex);
				if (sugIndex == sugs.size())
					sugIndex--;
			}
			catch (CustomException e)
			{
				if (e.type.equals("ConnectionFailed"))
					ErrorShower.ShowError(context, R.string.connection_failed_mesage);
				else if (e.type.equals("NotLoggedInException"))
					ErrorShower.ShowError(context, R.string.not_logged_in_message);
				else if (e.type.equals("AuthenticationError"))
					ErrorShower.ShowError(context, R.string.authentication_error);
				else if (e.type.equals("NoIDFound"))
					ErrorShower.ShowError(context, R.string.no_id_found_message);
			}
			finally
			{
				alertBox.cancel();
			}
		}

	};

	android.view.View.OnClickListener nextOnClickListener = new android.view.View.OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			sugIndex++;
			refreshView();
		}
	};
	android.view.View.OnClickListener lastOnClickListener = new android.view.View.OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			sugIndex--;
			refreshView();
		}
	};
	android.view.View.OnClickListener acceptOnClickListener = new android.view.View.OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			alertBox.show();
			Thread t = new Thread(new AnswerRunnable(true));
			t.start();
			try
			{
				t.join();
			}
			catch (InterruptedException ie)
			{
			}
			refreshView();
		}
	};
	android.view.View.OnClickListener rejectOnClickListener = new android.view.View.OnClickListener()
	{

		@Override
		public void onClick(View arg0)
		{
			alertBox.show();
			Thread t = new Thread(new AnswerRunnable(false));
			t.start();
			try
			{
				t.join();
			}
			catch (InterruptedException ie)
			{
			}
			refreshView();
		}
	};

	Runnable refresh = new Runnable()
	{

		@Override
		public void run()
		{
			try
			{
				sugs.clear();
				Suggestion[] temp = Controller.GetAskedSuggestions(orgID);
				for (Suggestion s : temp)
					sugs.add(s);
				sugIndex = 0;
				refreshView();
			}
			catch (CustomException e)
			{
				if (e.type.equals("ConnectionFailed"))
					ErrorShower.ShowError(context, R.string.connection_failed_mesage);
				else if (e.type.equals("NotLoggedInException"))
					ErrorShower.ShowError(context, R.string.not_logged_in_message);
				else if (e.type.equals("AuthenticationError"))
					ErrorShower.ShowError(context, R.string.authentication_error);
				else if (e.type.equals("NoIDFound"))
					ErrorShower.ShowError(context, R.string.no_id_found_message);
			}
			finally
			{
				alertBox.cancel();
			}
		}
	};

	protected AskedSuggestionsAlertDialog(Activity context, int orgID)
	{
		super(context);
		this.context = context;
		this.orgID = orgID;
		sugs = new Vector<Suggestion>();
		sugIndex = 0;
		alertBox = new WaitAlert(context);
		alertBox = new WaitAlert(context);
		View myView = LayoutInflater.from(context).inflate(R.layout.asked_suggestion_item, null);
		sugText = (TextView) myView.findViewById(R.id.asked_suggestion_text);
		nextButton = (Button) myView.findViewById(R.id.next_suggestion_button);
		lastButton = (Button) myView.findViewById(R.id.last_suggestion_button);
		acceptButton = (Button) myView.findViewById(R.id.accept_suggestion_button);
		rejectButton = (Button) myView.findViewById(R.id.reject_suggestion_button);
		nextButton.setOnClickListener(nextOnClickListener);
		lastButton.setOnClickListener(lastOnClickListener);
		acceptButton.setOnClickListener(acceptOnClickListener);
		rejectButton.setOnClickListener(rejectOnClickListener);
		setView(myView);
	}

	public void Refresh()
	{
		Thread t = new Thread(refresh);
		t.start();
		try
		{
			t.join();
		}
		catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void refreshView()
	{
		if (sugs.size() == 0)
		{
			sugText.setText(R.string.no_notification_message);
			nextButton.setVisibility(View.INVISIBLE);
			lastButton.setVisibility(View.INVISIBLE);
			acceptButton.setVisibility(View.INVISIBLE);
			rejectButton.setVisibility(View.INVISIBLE);
		}
		else
		{
			sugText.setText(sugs.get(sugIndex).text);
			nextButton.setVisibility(View.VISIBLE);
			lastButton.setVisibility(View.VISIBLE);
			acceptButton.setVisibility(View.VISIBLE);
			rejectButton.setVisibility(View.VISIBLE);
			if (sugIndex == 0)
				lastButton.setEnabled(false);
			else
				lastButton.setEnabled(true);
			if (sugIndex == sugs.size() - 1)
				nextButton.setEnabled(false);
			else
				nextButton.setEnabled(true);
		}

	}

	@Override
	public void show()
	{
		refreshView();
		super.show();
	}
}
