package com.wo2b.tu123.business.camera;

import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;

public class ImageObserver extends ContentObserver
{
	
	private Cursor mCursor;
	
	public ImageObserver(Handler handler)
	{
		super(handler);
	}
	
	@Override
	public boolean deliverSelfNotifications()
	{
		return super.deliverSelfNotifications();
	}
	
	@Override
	public void onChange(boolean selfChange)
	{
		super.onChange(selfChange);
		
		if (mCursor != null && mCursor.isClosed())
		{
			mCursor.requery();
		}
	}
	
}
