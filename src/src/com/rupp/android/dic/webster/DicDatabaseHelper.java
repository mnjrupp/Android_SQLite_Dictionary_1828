package com.rupp.android.dic.webster;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.database.SQLException;
import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

// third party lib for managing sqlite databases in asset folder
// https://github.com/jgilfelt/android-sqlite-asset-helper


import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DicDatabaseHelper extends SQLiteAssetHelper {
	
	 // Database Name
    private static final String DATABASE_NAME = "awebster.db";
	 // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Column
    public static final String COLUMN_TOPIC = "Topic";
    
	public DicDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);  
     // you can use an alternate constructor to specify a database location 
     		// (such as a folder on the sd card)
     		// you must ensure that this folder is available and you have permission
     		// to write to it
     		//super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
    }
 
   
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older Dictionary table if existed
       /* db.execSQL("DROP TABLE IF EXISTS DICTIONARY");
 
        // create 
        this.onCreate(db);*/
    }
    
    public String[] getAutosearchArray(String search)
    {
    	List<String> topics = new ArrayList<String>();
    	//Select Query
    	String Sql = "";
    	Sql+="SELECT Topic FROM Dictionary ";
    	Sql+="Where Topic LIKE '" + search + "%'";
    	Sql+=" ORDER BY Topic ASC LIMIT 0,5";
    	//Log.e("DicDatabaseHelper","SQL =>" +Sql);
    	SQLiteDatabase db = this.getReadableDatabase();
    	try{
	    	Cursor cursor = db.rawQuery(Sql,null);
	    	
	    	//loop through records
	    	if(cursor.moveToFirst()){
	    		do{
	    			String fielddef = cursor.getString(0);
	    			//Log.e("DicDatabaseHelper",fielddef.toString()+" Added to List");
	    			// add to list
	    			topics.add(fielddef);
	    			
	    		}while(cursor.moveToNext());
	    		
    	}
	    	cursor.close();
    	}catch(SQLException mSQLException){
    		 Log.e("DicDatabaseHelper", "getAutosearchArray >>" + mSQLException.toString());
    	}
    	
    	db.close();
    	
    	//return list of records
    	return getStringArray(topics);
    	}
    
    public String[] getStringArray(List<String> cursorList)	
    {
    	int rowCount = cursorList.size();
        
        String[] item = new String[rowCount];
        int x = 0;
         
        for (String record : cursorList) {
             
            item[x] = record;
            x++;
        }
         
        return item;
    }
    
    @SuppressLint("DefaultLocale")
	public String getDefnition(String lookup)
    {
    	byte[] blobs;
    	String definition = "";
    	String Sql = "";
    	String initial;
    	if(lookup.length()==0)return "";
    	//Make sure lowercase except for first character
    	if(lookup.length()==1){lookup = lookup.toUpperCase();}
    	else{lookup = lookup.toLowerCase();
    	initial = lookup.substring(0,1).toUpperCase();
    	lookup = initial + lookup.substring(1,lookup.length());}
    	
    	Sql+="SELECT Definition FROM Dictionary ";
    	Sql+="Where Topic ='" + lookup + "'";
    	
    	//Log.e("DicDatabaseHelper","SQL =>" +Sql);
    	SQLiteDatabase db = this.getReadableDatabase();
    	try{
	    	Cursor cursor = db.rawQuery(Sql,null);
	    	if(cursor.moveToFirst()){
	    		blobs = cursor.getBlob(0);
	    		definition = new String(blobs,"UTF-8");
	    	}
	    	  cursor.close();
	    	  
    		}catch(SQLException mSQLException){
	    		 Log.e("DicDatabaseHelper", "getDefnition >>" + mSQLException.toString());
	    	} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	db.close();
    	return definition;
    }
}
