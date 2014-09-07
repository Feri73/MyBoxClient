package com.example.mybox;

import java.io.InputStream;
import java.net.URL;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageDownloader extends AsyncTask<String, Void, Bitmap>
{
	ImageView view;

	public ImageDownloader(ImageView view)
	{
		this.view = view;
	}

	@Override
	protected Bitmap doInBackground(String... params)
	{
		try
		{
			URL url = new URL(params[0]);
			InputStream in = url.openStream();
			Bitmap pic = BitmapFactory.decodeStream(in);
			return pic;
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	@Override
	protected void onPostExecute(Bitmap result)
	{
		view.setImageBitmap(result);
	}
}
