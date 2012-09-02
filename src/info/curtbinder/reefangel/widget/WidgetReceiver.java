package info.curtbinder.reefangel.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetReceiver extends AppWidgetProvider {

	@Override
	public void onReceive ( Context context, Intent intent ) {
		// TODO Auto-generated method stub
		if ( intent
				.getAction()
				.equals(	"info.curtbinder.reefangel.service.UPDATE_DISPLAY_DATA" ) ) {
			Log.d( "RA Widget", "Update display data" );
		}
		updateDisplay( context );
		super.onReceive( context, intent );
	}

	@Override
	public void onUpdate (
			Context context,
			AppWidgetManager appWidgetManager,
			int[] appWidgetIds ) {
		updateDisplay( context );
	}

	private void updateDisplay ( Context context ) {
		RemoteViews updateViews =
				new RemoteViews( context.getPackageName(), R.layout.widget );

		String myUri = "content://info.curtbinder.reefangel.db/latest";
		String date, t1, t2, t3, dp, ap, ph;
		date = t1 = t2 = t3 = ap = dp = ph = "";
		try {
			Uri CONTENT_URI = Uri.parse( myUri );
			ContentResolver cr = context.getContentResolver();
			Cursor c = cr.query( CONTENT_URI, null, null, null, null );
			// move the cursor to the first item in the list
			if ( c.moveToFirst() ) {
				// work with the data
				date = c.getString( c.getColumnIndex( "logdate" ) );
				t1 = c.getString( c.getColumnIndex( "t1" ) );
				t2 = "T2: " + c.getString( c.getColumnIndex( "t2" ) );
				t3 = "T3: " + c.getString( c.getColumnIndex( "t3" ) );
				dp = c.getString( c.getColumnIndex( "dp" ) ) + "%";
				ap = c.getString( c.getColumnIndex( "ap" ) ) + "%";
				ph = c.getString( c.getColumnIndex( "ph" ) );
			}
			// close the cursor from the database
			c.close();
		} catch ( Exception e ) {
			e.printStackTrace();
		}

		updateViews.setTextViewText( R.id.textDate, date );
		updateViews.setTextViewText( R.id.textT1, t1 );
		updateViews.setTextViewText( R.id.textT2, t2 );
		updateViews.setTextViewText( R.id.textT3, t3 );
		updateViews.setTextViewText( R.id.textDP, dp );
		updateViews.setTextViewText( R.id.textAP, ap );
		updateViews.setTextViewText( R.id.textPH, ph );
		// get the Component Name to identify the widget to update
		ComponentName widgetComponentName =
				new ComponentName( context, WidgetReceiver.class );
		// get the global AppWidgetManager
		AppWidgetManager manager = AppWidgetManager.getInstance( context );

		// update the Weather AppWdiget
		manager.updateAppWidget( widgetComponentName, updateViews );
	}
}
