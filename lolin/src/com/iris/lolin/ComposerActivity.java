package com.iris.lolin;

import com.iris.lolin.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

@SuppressLint("NewApi")
public class ComposerActivity extends Activity {

	private Spinner 					rankSpinner,teaSpinner,positionSpinner,timeSpinner;		
	private String[] 					rankData,teaData,positionData,timeData;
	private ArrayAdapter<String> 		rankSpinnerAdapter,teaSpinnerAdapter,positionSpinnerAdapter,timeSpinnerAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_composer);
	    
		init();
		DataInit();
	    
	}

	private void init() {
		rankSpinner = (Spinner)findViewById(R.id.composer_spinner_rank);
		teaSpinner = (Spinner)findViewById(R.id.composer_spinner_tea);
		positionSpinner = (Spinner)findViewById(R.id.composer_spinner_position);
		timeSpinner = (Spinner)findViewById(R.id.composer_spinner_time);
	}

	private void DataInit() {
		//ActionBar Init
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setTitle(R.string.composer_activity_title);
		
		//spinner init
		rankData = getResources().getStringArray(R.array.composer_rank_array_list);
		rankSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_item,rankData);
		rankSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		rankSpinner.setAdapter(rankSpinnerAdapter); 
		rankSpinner.setOnItemSelectedListener(rankOnItemSelectedListener);
		
		teaData = getResources().getStringArray(R.array.composer_tea_array_list);
		teaSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_item,teaData);
		teaSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		teaSpinner.setAdapter(teaSpinnerAdapter); 
		teaSpinner.setOnItemSelectedListener(teaOnItemSelectedListener);
		
		positionData = getResources().getStringArray(R.array.composer_position_array_list);
		positionSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_item,positionData);
		positionSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		positionSpinner.setAdapter(positionSpinnerAdapter); 
		positionSpinner.setOnItemSelectedListener(positionOnItemSelectedListener);

		timeData = getResources().getStringArray(R.array.composer_time_array_list);
		timeSpinnerAdapter= new ArrayAdapter<>
		(getApplicationContext(), R.layout.spinner_item,timeData);
		timeSpinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
		timeSpinner.setAdapter(timeSpinnerAdapter); 
		timeSpinner.setOnItemSelectedListener(timeOnItemSelectedListener);
		
	}
	
	OnItemSelectedListener rankOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			
			System.err.println("@@@@@@@@@@@@@@  :  " + rankData[position]);
			
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	OnItemSelectedListener teaOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			System.err.println("@@@@@@@@@@@@@@  :  " + teaData[position]);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	OnItemSelectedListener positionOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			System.err.println("@@@@@@@@@@@@@@  :  " + positionData[position]);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};
	
	OnItemSelectedListener timeOnItemSelectedListener = new OnItemSelectedListener(){
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
			System.err.println("@@@@@@@@@@@@@@  :  " + timeData[position]);
		}
		@Override
		public void onNothingSelected(AdapterView<?> parent) {}
	};

}
