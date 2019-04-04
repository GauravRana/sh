package com.sydehealth.sydehealth.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.sydehealth.sydehealth.adapters.Actors;

import java.util.ArrayList;


public class DatabaseHelper extends SQLiteOpenHelper 
{
	    private static final String DB_NAME = "sydeuser.db";
	    private static final int DB_VERSION_NUMBER = 1;
	    public static final String recent_user = "recent_user";
	    static Context dbContext;
	    public static SQLiteDatabase db = null;
	    String TAG="--DatabaseHelper--";


	 
	    public DatabaseHelper(Context context)
	    {
	        super(context, DB_NAME, null, DB_VERSION_NUMBER);
	        dbContext = context;
	        Log.d(TAG,"---odatabase helper construter---");// Environment.getExternalStorageDirectory().toString()+"/"+DB_NAME
	    }
	    @Override
	    public void onOpen(SQLiteDatabase db) 
	    {
	    	  Log.d("has open a dattabsae","*****"+DB_NAME);

	    }
	 
	    @Override
	    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	    {
	        Log.d(TAG,"---onupgrade---");
	        
	        db.execSQL("DROP TABLE IF EXISTS " + recent_user);
//			db.execSQL("DROP TABLE IF EXISTS " + recent_chat);
//	        db.execSQL("DROP TABLE IF EXISTS " + tableNewsFeedNames);
//	        db.execSQL("DROP TABLE IF EXISTS " + tableCityNames);
//	        db.execSQL("DROP TABLE IF EXISTS " + tableCountryNames);
//	        db.execSQL("DROP TABLE IF EXISTS " + tableSelectedTopicNames);
//	        db.execSQL("DROP TABLE IF EXISTS " + tableSelectedFeedNames);
	         onCreate(db);
	    }
	 
	    @Override
	    public void onCreate(SQLiteDatabase db)
	    {
//	        db.execSQL("create table " + recent_user + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, user_email TEXT not null , password TEXT not null);");
			db.execSQL("create table " + recent_user + "(email TEXT PRIMARY KEY , user_name TEXT not null , user_password TEXT not null, remember boolean not null default 0);");

	    }
	 
	   public void openDB() throws SQLException
	    {
	        Log.i("openDB", "openDB() called...");
	        
	        if (db == null || (db != null && !db.isOpen()))
	        {
	            db = getWritableDatabase();
	            
	        }
	        else
	        {
	        	Toast.makeText(dbContext, "db == null", Toast.LENGTH_SHORT).show();
	        }
	        
	       
	    }
	 
	    public void closeDB()
	    {
	    	Log.i(TAG,"---closeDB..");
	        if(db != null)
	    	{
		            	db.close();
		            	Log.d("", "db is close");
		     
	           /* if(db.isOpen())
	            {
	            	db.close();
	            	Log.d("","db is close");
	            }*/
	        }
	    }//close db 
	    


	public void insert_user(String name, String email, String pswd, boolean remember)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put("email" , email);
		contentValues.put("user_name" , name);
		contentValues.put("user_password" , pswd);
		contentValues.put("remember" , remember);


	boolean InsertionBool = db.insertWithOnConflict(recent_user, null, contentValues,SQLiteDatabase.CONFLICT_REPLACE)>0;

		Log.e(TAG,recent_user+"Insert Bool:"+InsertionBool);
	}//insert
	    

	public ArrayList<Actors> Execute_recent_user()
	{
		//	String fetch = "SELECT * FROM "+tableTopicNames+" WHERE chat_id=" + chatid;
		// db.execSQL(fetch);

		ArrayList<Actors> recentlist= new ArrayList<Actors>();
		Cursor c = db.rawQuery("SELECT * FROM "+recent_user +" ORDER BY email DESC LIMIT 3 ",null);
		if(c.getCount() >0)
		{

			while(c.moveToNext())
			{



				Actors actor = new Actors();
				actor.setusername(c.getString((c.getColumnIndex("email"))));
				actor.setid(c.getString((c.getColumnIndex("user_name"))));
				actor.settitle(c.getString((c.getColumnIndex("user_password"))));
				actor.setselected(c.getInt((c.getColumnIndex("remember")))==1);

				recentlist.add(actor);

			}

		}
		c.close();
		return  recentlist;


	}

	    public void updateTableContent(String columnName, String value, String email)
	    {
	    	ContentValues contentValues = new ContentValues();
	        contentValues.put(columnName, value);
//	        boolean bool = db.update(recent_user, contentValues, "email"+" = "+email, null)>0;
//	    	Log.e("DbHelper","update table+"+bool);
			db.update(recent_user, contentValues, "email = ?", new String[]{email});

//			String strSQL = "UPDATE "+recent_user+" SET "+columnName+" = "+value+" WHERE email = "+ email;

//			db.execSQL(strSQL);
	    	
	    }
	    
	    // using 
	    public void DeleteTableContent(String tableName)
	    {
	        db.execSQL("DELETE FROM " + tableName);
	      
	    }
	    

}
	
	

