package sandeep.project.com.sandeepprojectfinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Pupa on 4/21/2016.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "proj1.db";
    private static final String TABLE_SLIDE_CONTENT = "SLIDE_CONTENT";
    private static final String TABLE_QUOTE_CONTENT = "QUOTE_CONTENT";
    private static final String TABLE_CATEGORY = "CATEGORY";

    private static String CREATE_TABLE_SLIDE_CONTENT = "Create table "
            + TABLE_SLIDE_CONTENT
            + " (" +
            "image_name text," +
            "location text," +
            "category_name text" +
            ")";

    private static String CREATE_TABLE_CATEGORY = "Create TABLE IF NOT EXISTS "
            + TABLE_CATEGORY
            + " (" +
            "category_name text," +
            "type text" +
            ")";

    private static String CREATE_TABLE_QUOTE_CONTENT = "Create table " +
            TABLE_QUOTE_CONTENT +
            " (" +
            "name text," +
            "category_name text" +
            ")";

    public DBHelper(Context mContext) {
        super(mContext, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create table here

        db.execSQL(CREATE_TABLE_CATEGORY);
        db.execSQL(CREATE_TABLE_QUOTE_CONTENT);
        db.execSQL(CREATE_TABLE_SLIDE_CONTENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SLIDE_CONTENT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUOTE_CONTENT);
    }

    /**
     * @param imageName
     * @param location
     * @param categoryName
     * @return
     */
    public boolean insertSlides(String imageName, String location, String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("image_name", imageName);
        cv.put("location", location);
        cv.put("category_name", categoryName);

        if (db.insert(TABLE_SLIDE_CONTENT, null, cv) > 1) {
            return true;
        } else return false;
    }

    /**
     * @param categoryName
     * @param type
     * @return
     */
    public boolean insertCategory(String categoryName, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("category_name", categoryName);
        cv.put("type", type);

        if (db.insert(TABLE_CATEGORY, null, cv) > 1) {
            return true;
        } else return false;
    }


    public ArrayList<String> getCategories(String cateslides) {


        ArrayList<String> categories=new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_CATEGORY +" WHERE type='"+cateslides+"'";
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        //String[] data      = null;

        if (cursor.moveToFirst()) {
            do {
                // get the data into array, or class variable
                categories.add(cursor.getString(cursor.getColumnIndexOrThrow("category_name")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return categories;
    }

    public ArrayList<String> getSlideImages(String category) {
        ArrayList<String> content=new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_SLIDE_CONTENT +" WHERE category_name LIKE '%"+category+"%'";
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        //String[] data      = null;
        if (cursor.moveToFirst()) {
            do {
                // get the data into array, or class variable
                content.add(cursor.getString(cursor.getColumnIndexOrThrow("location")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return content;
    }


}
