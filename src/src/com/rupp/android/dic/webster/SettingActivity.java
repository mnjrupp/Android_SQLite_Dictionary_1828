package com.rupp.android.dic.webster;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Build;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
 
public class SettingActivity extends PreferenceActivity 
	 {
	
		
	 @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        
	        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
	            onCreatePreferenceActivity();
	        } else {
	            onCreatePreferenceFragment();
	        }
	    }
	 

	    /**
	     * Wraps legacy {@link #onCreate(Bundle)} code for Android < 3 (i.e. API lvl
	     * < 11).
	     */
	    @SuppressWarnings("deprecation")
	    private void onCreatePreferenceActivity() {
	        addPreferencesFromResource(R.xml.preferences);
	     // show the current value in the settings screen
	        for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
	          initSummary(getPreferenceScreen().getPreference(i));
	        }
	    }

	    /**
	     * Wraps {@link #onCreate(Bundle)} code for Android >= 3 (i.e. API lvl >=
	     * 11).
	     */
	    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	    private void onCreatePreferenceFragment() {
	        getFragmentManager().beginTransaction()
	                .replace(android.R.id.content, new MyPreferenceFragment ())
	                .commit();
	    }
	    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
		public class MyPreferenceFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener
	    {
	        @Override
	        public void onCreate(final Bundle savedInstanceState)
	        {
	            super.onCreate(savedInstanceState);
	            addPreferencesFromResource(R.xml.preferences);
	         // show the current value in the settings screen
	            for (int i = 0; i < getPreferenceScreen().getPreferenceCount(); i++) {
	              initSummary(getPreferenceScreen().getPreference(i));
	            }
	            
	       
	    }
	        @Override
			public void onResume() {
		      super.onResume();
		      getPreferenceScreen().getSharedPreferences()
		          .registerOnSharedPreferenceChangeListener(this);
		    }
	        
	        @Override
			public void onPause() {
		      super.onPause();
		      getPreferenceScreen().getSharedPreferences()
		          .unregisterOnSharedPreferenceChangeListener(this);
		     }
	        
	        @Override
			public void onSharedPreferenceChanged(
					SharedPreferences sharedPreferences, String key) {
				// TODO Auto-generated method stub
				//Log.e(" " + this.getClass().getSimpleName(), "onSharedPreferenceChanged Callback");
				updatePreferences(findPreference(key));
			}
	  }
	    
		private void initSummary(Preference p) {
		    if (p instanceof PreferenceCategory) {
		      PreferenceCategory cat = (PreferenceCategory) p;
		      for (int i = 0; i < cat.getPreferenceCount(); i++) {
		        initSummary(cat.getPreference(i));
		      }
		    } else {
		      updatePreferences(p);
		    }
		  }

		  private void updatePreferences(Preference p) {
		    if (p instanceof ListPreference) {
		      ListPreference listPref = (ListPreference) p;
		      p.setSummary(listPref.getValue());
		    }
		  }


		} 
		
