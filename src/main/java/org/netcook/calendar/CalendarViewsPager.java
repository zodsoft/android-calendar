package org.netcook.calendar;

import java.util.Calendar;

import org.netcook.calendar.CalendarView.OnDrillDownListener;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class CalendarViewsPager extends ViewPager {
	
	private Context context;
	private OnDrillDownListener onDrillDownListener;
	private boolean isBigViewPage;
	private Calendar calendar = Calendar.getInstance();
	
	public CalendarViewsPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	public CalendarViewsPager(Context context) {
		this(context, null);
	}
	
	public void setOnDrillDownListener(OnDrillDownListener onDrillDownListener){
		this.onDrillDownListener = onDrillDownListener;
	}
	
	public void setBigViewPage(boolean isBigViewPage){
		this.isBigViewPage = isBigViewPage;
	}
	
	public void init(){
		
		setAdapter(new CalendarViewsAdapter());
		
		// set current item in the adapter to the middle
		setCurrentItem((CalendarViewsAdapter.MULTIPLIER / 2));
	}
	
	private class CalendarViewsAdapter extends PagerAdapter {

		//Some LARGE even number.
		private static final int MULTIPLIER = 50000;
		
		//Previous page starts from middle of the pages.
		private int previousPageNum = (CalendarViewsAdapter.MULTIPLIER / 2);
		
		
		@Override
	    public Object instantiateItem(View collection, int position){
			
			Log.d("CalendarViewsAdapter", "" + position);
			
			final CalendarView calendarView = !isBigViewPage ? 
					new CalendarView(context) : new CalendarView(context, true);
			
			calendarView.setPadding(0, 4, 0, 0);
			calendarView.setOnDrillDownListener(onDrillDownListener);

			if ((position > 0)||(previousPageNum > 0)){
				if (previousPageNum > position){
					if ((previousPageNum - position) > 1){
						calendar.add(Calendar.MONTH, -3);
					}else{
						calendar.add(Calendar.MONTH, -1);
					}
				}else{
					if ((position - previousPageNum) > 1){
						calendar.add(Calendar.MONTH, 3);
					}else{
						calendar.add(Calendar.MONTH, 1);
					}
				}
			}

			calendarView.setDateCalendar(calendar.getTime());
			
			if ((position > 0)||(previousPageNum > 0)){
				previousPageNum = position;
			}	
			
			((ViewPager) collection).addView(calendarView, 0);
			return calendarView;
		}
	
		@Override
		public void destroyItem(View collection, int position, Object view) {
			((ViewPager) collection).removeView((View) view);
		}

		@Override
		public int getCount() {
			return 12 * MULTIPLIER;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0.equals(arg1);
		}
	}
}
