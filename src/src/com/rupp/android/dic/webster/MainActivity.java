package com.rupp.android.dic.webster;


import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.text.method.ScrollingMovementMethod;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.rupp.android.dic.webster.DicDatabaseHelper;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	AutoCompleteTextView autoComplete; 
	ArrayAdapter<String> autoAdapter;
	TextView txtview; 
	String[] item = new String[] {"Please search..."};
	private static float  Small_font  = 12;
	private static float  Normal_font = 14;
	private static float  Medium_font = 18;
	private static float  Large_font  = 24;
	private final static String FONT_STYLE = "pref_font_size";
	float FontSize;
	 

	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		
		// instantiate the TextView
		txtview = (TextView) findViewById(R.id.textView1);
		txtview.setMovementMethod(new ScrollingMovementMethod());
		
		 if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
		   // TODO implement copy and paste for older android
		 }else{
			 txtview.setTextIsSelectable(true);
		 }
		
		// Apply new font size from SharedPreferences
		getPrefFont();
				
		//find Imagebutton and register onClick event
		ImageButton ibutton = (ImageButton) findViewById(R.id.imageButton1);
		ibutton.setOnClickListener(new View.OnClickListener() {
	           public void onClick(View v) {
	        	   autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
	        	   String topic = autoComplete.getText().toString();
	        	   getCompleteDefinition(topic);
	        	   
	            
	           }
	        });
		
      try{
		
		
		autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView1);
		autoComplete.setThreshold(3);
		autoComplete.addTextChangedListener(new CustomTextChangedListener(this));
		autoAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,item);
		autoComplete.setAdapter(autoAdapter);
		
		// Listening for the enter key in virtual keyboard
		autoComplete.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ( (actionId == EditorInfo.IME_ACTION_DONE) || 
                		((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN ))){
                	String topic = autoComplete.getText().toString();
                	getCompleteDefinition(topic);
                    return true;
                }
                else{
                    return false;
                }
            }
        });
		
      } catch (NullPointerException e) {
          e.printStackTrace();
          Log.e("MainActivity",e.toString());
      } catch (Exception e) {
          e.printStackTrace();
          Log.e("MainActivity",e.toString());
      }
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		 switch(item.getItemId()){
		 case R.id.action_settings:
					Intent i = new Intent(this, SettingActivity.class);
		            startActivity(i);
		            break;
		 case R.id.copy:
			 ClipboardManager cm = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
		        cm.setText(txtview.getText());
		        Toast.makeText(getBaseContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
		        break;
			 
		 }
		 return true;
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}
	
	public void getCompleteDefinition(String input)
    {	
		//TODO make input proper case before searching database
        DicDatabaseHelper db = new DicDatabaseHelper(this);
        // query the database based on the user input
        txtview.setText(db.getDefnition(input));
        // Scroll the TextView to the top
        txtview.scrollTo(0, 0);
    }
	 @Override
	    protected void onPause() {
	        //Log.e(" " + this.getClass().getSimpleName(), "onPause lifecycle state");
	        super.onPause();
	    }
	@Override
    protected void onResume() {
		//Log.e(" " + this.getClass().getSimpleName(), "1 onResume lifecycle state");
        super.onResume();
     
        getPrefFont();
      
	}
	
	public void getPrefFont()
	{
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
		String fontsize = settings.getString(FONT_STYLE, "");
		FontSize = Normal_font;
		if(fontsize.equals("Small")) FontSize= Small_font;
    	if(fontsize.equals("Large")) FontSize= Large_font;
    	if(fontsize.equals("Medium")) FontSize= Medium_font;
    	txtview.setTextSize(FontSize);
    	
	}

}
