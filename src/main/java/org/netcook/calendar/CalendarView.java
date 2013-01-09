package org.netcook.calendar;

import java.util.Calendar;
import java.util.Date;

import org.netcook.calendar.utils.DateUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CalendarView extends RelativeLayout {

	public interface OnDateEventUpdateListener {
		void onDateEventUpdate(int year, int moth);
	}
	
	public interface OnDrillDownListener {
		public void onDrillDown(Date date);
	}

	private TextView monthYear;
	private Calendar calendar;
	private GridView calendarView;
	private GridCellAdapter adapter;
	private boolean isBig;

	private OnItemClickListener onDayClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if (null != onDrillDownListener){
				Date date = (Date) view.getTag(R.id.drilldown_id);
				if (null != date){
					onDrillDownListener.onDrillDown(date);
				}
			}
		}
	};
	private OnDrillDownListener onDrillDownListener;

	private OnDateEventUpdateListener onDateEventUpdateListener;
	//private List<? extends Drilldownable> events;
	
	public CalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CalendarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CalendarView(Context context) {
		this(context, false);
	}

	public CalendarView(Context context, boolean isBig) {
		super(context);
		this.isBig = isBig;
		init(context);
	}

	private void init(Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(isBig ? R.layout.calendar_layout_big : R.layout.calendar_layout, this, true);

		calendarView = (GridView) findViewById(R.id.calendar);
		monthYear = (TextView) findViewById(R.id.calendar_title);

		initCalendar();

		calendarView.setOverScrollMode(View.OVER_SCROLL_NEVER);
		
		monthYear
				.setText(getResources().getStringArray(R.array.months)[calendar
						.get(Calendar.MONTH)]
						+ " "
						+ calendar.get(Calendar.YEAR));
		calendarView.setAdapter(adapter);

		calendarView.setOnItemClickListener(onDayClickListener);
		setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		setGravity(Gravity.CENTER);
	}

	public void changeMonth(int index) {
		calendar.add(Calendar.MONTH, index);
		updateMonth();
	}

	public void updateMonth() {
		monthYear.setText(getResources().getStringArray(R.array.months)[calendar.get(Calendar.MONTH)] + " " + calendar.get(Calendar.YEAR));		
		if (null != onDateEventUpdateListener) {
			onDateEventUpdateListener.onDateEventUpdate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
		}
		refresh();
	}
	
	private void initCalendar() {
		//events = new ArrayList<Drilldownable>();
		calendar = Calendar.getInstance();
		adapter = new GridCellAdapter(getContext());
		updateMonth();
	}
	
	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
		updateMonth();
	}
	
	public void setDateCalendar(Date date) {
		calendar.setTime(date);
		updateMonth();
	}

	public OnDateEventUpdateListener getOnDateEventUpdateListener() {
		return onDateEventUpdateListener;
	}

	public void setOnDateEventUpdateListener(
			OnDateEventUpdateListener onDateEventUpdateListener) {
		this.onDateEventUpdateListener = onDateEventUpdateListener;
	}

//	public List<? extends Drilldownable> getEvents() {
//		return events;
//	}
//
//	public void setEvents(List<? extends Drilldownable> events) {
//		this.events = events;
//		refresh();
//	}

	public OnDrillDownListener getOnDrillDownListener() {
		return onDrillDownListener;
	}

	public void setOnDrillDownListener(OnDrillDownListener onDrillDownListener) {
		this.onDrillDownListener = onDrillDownListener;
	}

	private void refresh(){
		adapter.notifyDataSetInvalidated();
		postInvalidate();
	}

	private int getRowsCount(Date date) {
        Date firstDayOfTheMonth = DateUtils.getFirstDayOfTheMonth(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(firstDayOfTheMonth);
        int dayOfWeek = DateUtils.getDayOfWeek(firstDayOfTheMonth);
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int d = dayOfWeek + daysInMonth;
        return (d % 7 > 0 ? 1 : 0) + d / 7;
    }

	private void fillCalendarCellByDefault(GridCellHolder holder) {
		holder.count.setVisibility(View.INVISIBLE);
		holder.triangle.setVisibility(View.INVISIBLE);
		holder.cell.setTextColor(getResources().getColor(R.color.calendarTextBlack));
		holder.background.setVisibility(View.INVISIBLE);
	}

	private class GridCellHolder {
		public TextView cell;
		public TextView count;
		public ImageView triangle;
		public View background;
	}

	public class GridCellAdapter extends BaseAdapter {

        private final Context context;

        public GridCellAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
			return 7 * getRowsCount(calendar.getTime());
        }

        @Override
        public String getItem(int i) {
            return "";
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        	boolean strangerMonth = false;
            Calendar calendarCurrent = Calendar.getInstance();
            final GridCellHolder holder;
            if (convertView == null) {
            	convertView = View.inflate(context, isBig ? R.layout.calendar_cell_big : R.layout.calendar_cell, null);
                holder = new GridCellHolder();
                holder.cell = (TextView) convertView.findViewById(R.id.calendar_cell_month_day);
                holder.count = (TextView) convertView.findViewById(R.id.calendar_cell_tasks_count);
                holder.triangle = (ImageView) convertView.findViewById(R.id.calendar_cell_triangle);
                holder.background = convertView.findViewById(R.id.calendar_cell_background);
                convertView.setTag(holder);
            } else {
            	holder = (GridCellHolder) convertView.getTag();
            }
            fillCalendarCellByDefault(holder);
            Date firstDayOfTheMonth = DateUtils.getFirstDayOfTheMonth(calendar.getTime());
            calendarCurrent.setTime(firstDayOfTheMonth);
            int month = calendarCurrent.get(Calendar.MONTH);
            int dayOfWeek = DateUtils.getDayOfWeek(firstDayOfTheMonth);
            calendarCurrent.set(Calendar.DAY_OF_MONTH, position - dayOfWeek + 1);

            holder.cell.setText(calendarCurrent.get(Calendar.DAY_OF_MONTH) + "");
			if (calendarCurrent.get(Calendar.MONTH) != month) {
				holder.cell.setTextColor(getResources().getColor(R.color.calendarTextGray));
				strangerMonth = true;
			}
			
			//convertView.setTag(R.id.drilldown_id, calendarCurrent.getTime());
			
//            if(!events.isEmpty() && !strangerMonth) {
//            	Drilldownable event = null;
//            	for (Drilldownable drilldownable: events) {
//            		Calendar c = ((TaskCalendar)drilldownable).getDate();
//            		Log.d(TAG, "Compare " + c.get(Calendar.YEAR) + c.get(Calendar.MONTH) + c.get(Calendar.DAY_OF_MONTH) + " and " + calendarCurrent.get(Calendar.YEAR) + calendarCurrent.get(Calendar.MONTH) + calendarCurrent.get(Calendar.DAY_OF_MONTH));
//            		if (calendarCurrent.get(Calendar.DAY_OF_MONTH) == c.get(Calendar.DAY_OF_MONTH)
//            				&& calendarCurrent.get(Calendar.MONTH) == c.get(Calendar.MONTH)
//            				&& calendarCurrent.get(Calendar.YEAR) == c.get(Calendar.YEAR)) {
//            			event = drilldownable;
//            			Log.d(TAG, "Found " + event);
//            			break;
//            		}
//            	}
//            	if (null != event) {
//	                convertView.setTag(R.id.drilldown_id, event);
//	                holder.triangle.setVisibility(View.VISIBLE);
//	                holder.background.setBackgroundColor(Color.parseColor(event.color));
//	                holder.background.setVisibility(View.VISIBLE);
//	                holder.count.setVisibility(View.VISIBLE);
//	                holder.count.setText(event.value + "");
//	                holder.cell.setTextColor(getResources().getColor(R.color.white));
//                }
//            }

            return convertView;
        }
    }
    
}
