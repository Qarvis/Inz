package com.project.my.inz.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.my.inz.Model.DataModel;
import com.project.my.inz.Model.QuestM;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Luki on 2014-07-25.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "questsManager";

    // Quests table name
    private static final String TABLE_QUESTS = "quests";

    // Quests Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_POINTS = "points";
    private static final String KEY_DESC = "description";
    private static final String KEY_DATA = "data_create";
    private static final String KEY_STATE = "state"; //10-ok_achive, 11-not ok; 21- ok_quest, 20-not ok quest

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_questS_TABLE = "CREATE TABLE " + TABLE_QUESTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT," + KEY_POINTS + " TEXT," + KEY_DATA + " TEXT," + KEY_STATE + " TEXT" + ")";
        db.execSQL(CREATE_questS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTS);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new quest
    public void addQuest(DataModel data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, data.getId());
        values.put(KEY_NAME, data.getName()); //
        values.put(KEY_DESC, data.getDesc());
        values.put(KEY_POINTS, data.getPoints());
        values.put(KEY_DATA, data.getData());
        values.put(KEY_STATE, data.getState());

        // Inserting Row
        db.insert(TABLE_QUESTS, null, values);
        db.close(); // Closing database connection
    }
    // Adding new quest list
    public void addQuestList(List<DataModel> list) {
        SQLiteDatabase db = this.getWritableDatabase();

        for(DataModel data : list) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, data.getName()); //
            values.put(KEY_DESC, data.getDesc());
            values.put(KEY_POINTS, data.getPoints());
            values.put(KEY_DATA, data.getData());
            values.put(KEY_STATE, data.getState());
            db.insert(TABLE_QUESTS, null, values);
        } // Inserting Row

        db.close(); // Closing database connection
    }
    // Getting single quest
    public DataModel getQuest(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_QUESTS, new String[] {KEY_ID,
                        KEY_NAME, KEY_DESC, KEY_POINTS, KEY_DATA, KEY_STATE }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DataModel quest = new QuestM(cursor.getString(0),
                cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),cursor.getString(5) );
        return quest;
    }
    // Updating single contact
    public int updateQuest(DataModel data) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, data.getId());
        values.put(KEY_DESC, data.getDesc());
        values.put(KEY_NAME, data.getName()); //
        values.put(KEY_POINTS, data.getPoints());
        values.put(KEY_DATA, data.getData());
        values.put(KEY_STATE, data.getState());

        // updating row
        return db.update(TABLE_QUESTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
    }
    // Getting All quests
    public List<DataModel> getAllQuests() {
        List<DataModel> questList = new ArrayList<DataModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_QUESTS;


        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                QuestM quest = new QuestM();
                quest.setId(cursor.getString(0));
                quest.setName(cursor.getString(1));
                quest.setDesc(cursor.getString(2));
                quest.setPoints(cursor.getString(3));
                quest.setData(cursor.getString(4));
                quest.setState(cursor.getString(5));
                questList.add(quest);
            } while (cursor.moveToNext());
        }
        return questList;
    }
    // Deleting single quest
    public void deleteQuest(DataModel quest) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTS, KEY_ID + " = ?",
                new String[] { String.valueOf(quest.getId()) });
        db.close();
    }
    // Getting quests Count
    public int getquestsCount() {
        String countQuery = "SELECT * FROM " + TABLE_QUESTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int number = cursor.getCount();
        cursor.close();

        // return count
        return number;
    }

}
