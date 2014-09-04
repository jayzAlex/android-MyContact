package xu.ye.db;

import java.io.File;

import android.database.sqlite.SQLiteDatabase;

public class Database {
	private final String DATABASE_PATH = android.os.Environment   
    .getExternalStorageDirectory().getAbsolutePath()   
    + "/RKET/DB";   
	private final String DATABASE_FILENAME = "RKET_DICTIONARY"; 
	private SQLiteDatabase database;
	
	public  SQLiteDatabase openDatabase()
	{
		
	    try  
	    {   
	    	File file = new File(DATABASE_PATH);
	    	if(!file.exists()){
	    		file.mkdirs();
	    	}
	    
	    	String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
	        database = SQLiteDatabase.openOrCreateDatabase(   
	        		databaseFilename, null);  
	    }   
	    catch (Exception e)   
	    {   
	    	e.printStackTrace();
	    }   
	    return database;   
	} 
	
	/*public void close(){
		if(database!=null)
			database.close();
	}
	
*/
}
