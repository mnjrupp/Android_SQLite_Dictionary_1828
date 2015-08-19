package com.rupp.android.dic.webster;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;

import com.rupp.android.dic.webster.DicDatabaseHelper;

public class CustomTextChangedListener implements TextWatcher {
	
	 public static final String TAG = "CustomTextChangedListener.java";
	 Context context;
	public CustomTextChangedListener(Context context){
        this.context = context;
	}
	
	@Override
    public void afterTextChanged(Editable s) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
            int after) {
        // TODO Auto-generated method stub
         
    }
 
    @Override
    public void onTextChanged(CharSequence userInput, int start, int before, int count) {
 
        // if you want to see in the logcat what the user types
        //Log.e(TAG, "User input: " + userInput);
 
        MainActivity mainActivity = ((MainActivity) context);
        DicDatabaseHelper db = new DicDatabaseHelper(this.context);
        // query the database based on the user input
        mainActivity.item = db.getAutosearchArray(userInput.toString());
         
        // update the adapater
        mainActivity.autoAdapter.notifyDataSetChanged();
        mainActivity.autoAdapter = new ArrayAdapter<String>(mainActivity, android.R.layout.simple_dropdown_item_1line, mainActivity.item);
        mainActivity.autoComplete.setAdapter(mainActivity.autoAdapter);
         
    }
    
}
