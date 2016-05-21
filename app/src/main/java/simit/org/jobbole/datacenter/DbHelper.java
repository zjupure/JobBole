package simit.org.jobbole.datacenter;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by liuchun on 2016/4/16.
 */
public class DbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "Jobbole.db";
    private static final int DB_VERSION = 1;

    private static final String CREATE_FEED_TABLE = "CREATE TABLE IF NOT EXISTS blogs" +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            "channel TINYINT NOT NULL, " +
            "title VARCHAR(200) NOT NULL, " +
            "link TEXT, " +
            "description TEXT, " +
            "pubDate CHAR(20), " +
            "author CHAR(10), " +
            "category VARCHAR(50), " +
            "content TEXT);";
    private static final String CREATE_INDEX_PUBDATE = "CREATE INDEX chanelDate on blogs (channel, pubDate);";

    public DbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FEED_TABLE);
        db.execSQL(CREATE_INDEX_PUBDATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
