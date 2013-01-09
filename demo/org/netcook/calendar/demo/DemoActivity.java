package org.netcook.calendar.demo;

import java.util.Date;

import org.netcook.calendar.CalendarView.OnDrillDownListener;
import org.netcook.calendar.CalendarViewsPager;
import org.netcook.calendar.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Toast;

public class DemoActivity extends Activity {

	protected OnDrillDownListener onDrillDownListener = new OnDrillDownListener() {
		@Override
		public void onDrillDown(Date date) {
			Toast.makeText(self(), 
				date.toGMTString(), 
				Toast.LENGTH_SHORT)
				.show();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_demo);
		
		CalendarViewsPager calendarViewsPager = (CalendarViewsPager)
				findViewById(R.id.activity_demo_calendar);
		calendarViewsPager.setOnDrillDownListener(onDrillDownListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_demo, menu);
		return true;
	}
	
	private DemoActivity self(){
		return this;
	}

}
