package com.example.mybox;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class OrganizationAdapter extends BaseAdapter
{

	Organization[] orgs;
	Resources res;
	LayoutInflater inflater;
	Activity context;
	Typeface typeface;

	public OrganizationAdapter(Organization[] orgs, Resources res, LayoutInflater inflater, Activity context)
	{
		this.orgs = orgs;
		this.res = res;
		this.inflater = inflater;
		this.context = context;
		typeface = Typeface.createFromAsset(context.getAssets(), "fonts/ATaha.ttf");
	}

	@Override
	public int getCount()
	{
		return Math.max(orgs.length, 1);
	}

	@Override
	public Object getItem(int position)
	{
		if (position > orgs.length)
			return null;
		return orgs[position];
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view = convertView;
		TextView orgName = null;
		TextView orgDesc = null;
		TextView notifs = null;
		ImageView orgPic = null;
		if (convertView == null)
		{
			view = inflater.inflate(R.layout.organization_item, null);
			orgName = (TextView) view.findViewById(R.id.orgName);
			orgDesc = (TextView) view.findViewById(R.id.orgDesc);
			orgPic = (ImageView) view.findViewById(R.id.orgPic);
			notifs = (TextView) view.findViewById(R.id.orgNotifBox);
		}
		if (orgs.length == 0)
			orgName.setText("No Data!");
		else if (convertView == null)
		{
			Organization org = orgs[position];
			if (org.asManager)
				((LinearLayout) orgDesc.getParent()).setBackgroundResource(R.drawable.org_border);
			if (org.notifCount > 0)
			{
				notifs.setBackgroundResource(R.drawable.notif_background);
				notifs.setText(Integer.valueOf(org.notifCount).toString());
			}
			orgName.setText(org.name);
			orgDesc.setText(org.description);
			ImageDownloader downloader = new ImageDownloader(orgPic);
			downloader.execute(org.picAddress);
			orgName.setTypeface(typeface);
			orgDesc.setTypeface(typeface);
			notifs.setTypeface(typeface);
		}
		return view;
	}
}
