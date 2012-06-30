package name.nanek.greenerpedal.activity;

import java.util.List;

import name.nanek.greenerpedal.R;
import name.nanek.greenerpedal.activity.support.BitmapUtil;
import name.nanek.greenerpedal.activity.support.ShareUtil;
import name.nanek.greenerpedal.db.model.Reading;
import name.nanek.greenerpedal.db.model.dao.DataHelper;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphView.GraphViewData;
import com.jjoe64.graphview.GraphView.GraphViewSeries;
import com.jjoe64.graphview.GraphView.GraphViewStyle;
import com.jjoe64.graphview.GraphView.LegendAlign;
import com.jjoe64.graphview.LineGraphView;

public class GraphAndShareActivity extends Activity {
	
	private static class Stats {
		private Float min;
		private Float max;
		private Float start;
		private Float end;
		
		private void update(Float value) {
			if ( null == min || min < value ) {
				min = value;
			}

			if ( null == max || max > value ) {
				max = value;
			}

			if ( null == start ) {
				start = value;
			}

			if ( null != value ) {
				end = value;
			}
		}
		
		private String getDescription(String name, String units) {
			return name + " started at " + start + " " + units + " and ended at " + end + " " + units + ". The maximum was " + max + ".";
		}
	}
	
	private DataHelper data;
	
	private ViewGroup content;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.graph);
		content = (ViewGroup) findViewById(R.id.content);
		ViewGroup graphContent = (ViewGroup) findViewById(R.id.graph_content);
		
		data = new DataHelper(this);				

		long now = SystemClock.uptimeMillis();
		long oneHourAgo = now - ( 1000 * 60 * 60 );
		
		List<Reading> readings = data.readings.list(oneHourAgo);
		// TODO don't enable graph button until we have readings?
		if ( readings.isEmpty() ) {
			Toast.makeText(this, "Not enough readings to graph. Sorry.", Toast.LENGTH_LONG).show();
			finish();
			return;
		}
		
		GraphViewData[] gasUse = new GraphViewData[readings.size()];
		Stats gasStats = new Stats();
		GraphViewData[] breakUse = new GraphViewData[readings.size()];
		Stats breakStats = new Stats();
		GraphViewData[] moneySaved = new GraphViewData[readings.size()];
		Stats moneyStats = new Stats();

		int i = 0;
		//Float previousSecondsAgo;
		for(Reading reading : readings) {
			float secondsAgo = (now - reading.getTime()) / 1000;
			//if (null == previousSecondsAgo || previousSecondsAgo.equals(secondsAgo)) {
				//previousSecondsAgo = secondsAgo;
				gasUse[i] = new GraphViewData(secondsAgo, reading.getGasUse() / 10);
				breakUse[i] = new GraphViewData(secondsAgo, reading.getBreakUse() / 10);
				moneySaved[i] = new GraphViewData(secondsAgo, reading.getMoneySaved());
				i++;
			//}
		}
		
		GraphViewSeries gasUseSeries = new GraphViewSeries("Gas (10m/s\u00B2)", new GraphViewStyle(Color.RED, 5), gasUse);
		GraphViewSeries breakUseSeries = new GraphViewSeries("Stopping (10m/s\u00B2)", new GraphViewStyle(Color.parseColor("#ec7d00"), 5), breakUse);
		GraphViewSeries moneySavedSeries = new GraphViewSeries("Savings (\u00A2)", new GraphViewStyle(Color.parseColor("#00be31"), 5), moneySaved);
		
		GraphView graphView = new LineGraphView(this, "") {  
			   @Override  
			   protected String formatLabel(double value, boolean isValueX) {  
			      if (isValueX) {  
			    	  if ( value < 1000f) {
			    		  return ((int)(value)) + "ms";
			    	  }
			    	  
			    	  value /= 1000;
			    	  
			    	  if ( value < 60f ) {
			    		  return ((int)(value)) + "s";
			    	  }
			    	  
			    	  int minutes = (int)(value / 60);
			    	  int secondsRemainder = ((int)(value)) - (minutes * 60);
			    	  return minutes + "m" + secondsRemainder + "s";
			    	  
			      } else return super.formatLabel(value, isValueX); // let the y-value be normal-formatted  
			   }  
			};  
		
		graphContent.addView(graphView);
		graphContent.setContentDescription("Recent Driving Stats: " 
				+ gasStats.getDescription("Gas use ", " tens of meters per second squared ")
				+ breakStats.getDescription("Breaking use ", " tens of meters per second squared ")
				+ moneyStats.getDescription("Money saved ", " cents."));
		
		//((LineGraphView) graphView).setDrawBackground(true);

		// custom static labels
		//graphView.setHorizontalLabels(new String[] {"2 days ago", "yesterday", "today", "tomorrow"});
		//graphView.setVerticalLabels(new String[] {"high", "middle", "low"});
		graphView.addSeries(gasUseSeries);
		graphView.addSeries(breakUseSeries);
		graphView.addSeries(moneySavedSeries);
		graphView.setShowLegend(false);
		graphView.setScalable(true);

	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.graph_menu, menu);
        return true;
    } 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

	    switch (item.getItemId()) {
	        case R.id.menuItemShare:
	        	ShareUtil.share(this, BitmapUtil.drawToBitmap(content), 0);
	        	return true;

	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}	
	
}
