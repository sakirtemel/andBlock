package com.github.sakirtemel.andblock;

import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper { 
    private static DatabaseHelper mInstance = null;

    private static final String DATABASE_NAME = "databaseName";
    private static final String DATABASE_TABLE = "tableName";
    private static final int DATABASE_VERSION = 3;

    private Context mCxt;
    
 
    

    public static DatabaseHelper getInstance(Context ctx) {
        /** 
         * use the application context as suggested by CommonsWare.
         * this will ensure that you dont accidentally leak an Activitys
         * context (see this article for more information: 
         * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
         */
        if (mInstance == null) {
            mInstance = new DatabaseHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * constructor should be private to prevent direct instantiation.
     * make call to static factory method "getInstance()" instead.
     */
    private DatabaseHelper(Context ctx) {
        super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        this.mCxt = ctx;
    }

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// SQL statement to create book table
        /*String CREATE_BOOK_TABLE = "CREATE TABLE books ( " +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " + 
                "title TEXT, "+
                "author TEXT );";
 
        // create books table
        db.execSQL(CREATE_BOOK_TABLE);*/
        
        String CREATE_BLOCKED_NUMBERS_TABLE = "CREATE TABLE blocked_numbers ( " +
                "number_hash INTEGER PRIMARY KEY " + 
                ");";
 
        // create books table
        db.execSQL(CREATE_BLOCKED_NUMBERS_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		  // Drop older books table if existed
        /*db.execSQL("DROP TABLE IF EXISTS books");*/
        db.execSQL("DROP TABLE IF EXISTS blocked_numbers");

        // create fresh books table
        this.onCreate(db);
	}
	
	
	
	  /**
     * CRUD operations (create "add", read "get", update, delete) book + get all books + delete all books
     */
 /*
    // Books table name
    private static final String TABLE_BOOKS = "books";
 
    // Books Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_AUTHOR = "author";
 
    private static final String[] COLUMNS = {KEY_ID,KEY_TITLE,KEY_AUTHOR};
 
    public void addBook(Book book){
        Log.d("addBook", book.toString());
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, book.getTitle()); // get title 
        values.put(KEY_AUTHOR, book.getAuthor()); // get author
 
        // 3. insert
        db.insert(TABLE_BOOKS, // table
                null, //nullColumnHack
                values); // key/value -> keys = column names/ values = column values
 
        // 4. close
        db.close(); 
    }
 
    public Book getBook(int id){
 
        // 1. get reference to readable DB
        SQLiteDatabase db = this.getReadableDatabase();
 
        // 2. build query
        Cursor cursor = 
                db.query(TABLE_BOOKS, // a. table
                COLUMNS, // b. column names
                " id = ?", // c. selections 
                new String[] { String.valueOf(id) }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                null); // h. limit
 
        // 3. if we got results get the first one
        if (cursor != null)
            cursor.moveToFirst();
 
        // 4. build book object
        Book book = new Book();
        book.setId(Integer.parseInt(cursor.getString(0)));
        book.setTitle(cursor.getString(1));
        book.setAuthor(cursor.getString(2));
 
        Log.d("getBook("+id+")", book.toString());
 
        // 5. return book
        return book;
    }
 
    // Get All Books
    public List<Book> getAllBooks() {
        List<Book> books = new LinkedList<Book>();
 
        // 1. build the query
        String query = "SELECT  * FROM " + TABLE_BOOKS;
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        Book book = null;
        if (cursor.moveToFirst()) {
            do {
                book = new Book();
                book.setId(Integer.parseInt(cursor.getString(0)));
                book.setTitle(cursor.getString(1));
                book.setAuthor(cursor.getString(2));
 
                // Add book to books
                books.add(book);
            } while (cursor.moveToNext());
        }
 
        Log.d("getAllBooks()", books.toString());
 
        // return books
        return books;
    }
 
     // Updating single book
    public int updateBook(Book book) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. create ContentValues to add key "column"/value
        ContentValues values = new ContentValues();
        values.put("title", book.getTitle()); // get title 
        values.put("author", book.getAuthor()); // get author
 
        // 3. updating row
        int i = db.update(TABLE_BOOKS, //table
                values, // column/value
                KEY_ID+" = ?", // selections
                new String[] { String.valueOf(book.getId()) }); //selection args
 
        // 4. close
        db.close();
 
        return i;
 
    }
 
    // Deleting single book
    public void deleteBook(Book book) {
 
        // 1. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
 
        // 2. delete
        db.delete(TABLE_BOOKS,
                KEY_ID+" = ?",
                new String[] { String.valueOf(book.getId()) });
 
        // 3. close
        db.close();
 
        Log.d("deleteBook", book.toString());
 
    }
    */
    /**/
    public boolean isBlocked(String number){
    	String number_hash = number;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = 
                db.query("blocked_numbers", // a. table
                new String[]{}, // b. column names
                " number_hash = ?", // c. selections 
                new String[] { number_hash }, // d. selections args
                null, // e. group by
                null, // f. having
                null, // g. order by
                "1"); // h. limit
 
        // 3. if we got results get the first one
        if (cursor == null)
        	return false; // we should'n block that number

        if (cursor.getCount() == 0)
        	return false;
        return true;
    }
    public void updateDatabase(String[] numbers){
        SQLiteDatabase db = this.getWritableDatabase();
 
        for(String number : numbers){
	        ContentValues values = new ContentValues();
	        values.put("number_hash", number); // get title 
	 
	        // 3. insert
	        try{
	        	db.insert("blocked_numbers", // table
	        			null, //nullColumnHack
	        			values); // key/value -> keys = column names/ values = column values
	        }catch(Exception ex){
	        	//who cares
	        }
        }
 
        // 4. close
        //db.close(); 
    }
    public void insertMessage(){}
    
    public void getAllNumbers() {
 
        // 1. build the query
        String query = "SELECT  * FROM blocked_numbers";
 
        // 2. get reference to writable DB
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
 
        // 3. go over each row, build book and add it to list
        if (cursor.moveToFirst()) {
            do {
            	Log.d("getNumbers()", cursor.getString(0));

            } while (cursor.moveToNext());
        }
 
 
    }
    
    /**/
}
	
	