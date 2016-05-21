package simit.org.jobbole.datacenter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import simit.org.jobbole.bean.BlogItem;
import simit.org.jobbole.bean.RSSItem;

/**
 * Created by liuchun on 2016/4/16.
 */
public class DbManager {
    private AtomicInteger mOpenCounter = new AtomicInteger();

    private static DbManager instance = null;
    private DbHelper helper;
    private SQLiteDatabase db;

    private DbManager(Context context){
        helper = new DbHelper(context);
    }

    public static DbManager getInstance(Context context){
        if(instance == null){
            synchronized (DbManager.class){
                if(instance == null){
                    instance = new DbManager(context);
                }
            }
        }
        return instance;
    }

    /** 插入多条记录 */
    public synchronized void insert(List<RSSItem> rssItems){
        List<RSSItem> ins = filter(rssItems);

        db = openDatabase();
        db.beginTransaction();
        try{
            for(RSSItem item : ins){
                insert(item);
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            closeDatabase();
        }
    }

    /** 插入单条记录 */
    public synchronized void insert(RSSItem item){
        ContentValues cv = new ContentValues();
        cv.put("channel", item.getChannel());
        cv.put("title", item.getTitle());
        cv.put("link", item.getLink());
        cv.put("description", item.getDescription());
        cv.put("pubDate", item.getPubDate());
        cv.put("author", item.getAuthor());
        cv.put("content", item.getContent() == null ? "" : item.getContent());
        StringBuilder sb = new StringBuilder();
        for(String category : item.getCategory()){
            sb.append(category + ";");
        }
        cv.put("category", sb.toString());

        db.insert("blogs", null, cv);
    }

    /** the rss Item is exist in the database */
    public synchronized boolean isExist(RSSItem rssItem){
        String sql = "SELECT id FROM blogs WHERE link = '%s' ";
        sql = String.format(Locale.getDefault(), sql, rssItem.getLink());

        db = openDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        boolean exist = cursor.moveToFirst();
        cursor.close();
        closeDatabase();

        return exist;
    }

    /** filter the duplicated items */
    public List<RSSItem> filter(List<RSSItem> items){
        List<RSSItem> res = new ArrayList<>();

        for(RSSItem item : items){
            if(isExist(item)){
                continue;
            }
            res.add(item);
        }
        return res;
    }

    public synchronized List<BlogItem> getBlogItems(int channel){
        return getBlogItems(channel, 0);
    }

    public synchronized List<BlogItem> getBlogItems(int channel, int limit){
        String sql = "SELECT * FROM blogs WHERE channel = %d ORDER BY pubDate DESC ";
        List<BlogItem> blogItems = new ArrayList<>();

        sql = String.format(Locale.getDefault(), sql, channel);
        if(limit > 0){
            sql += " LIMIT " + limit;
        }
        db = openDatabase();
        Cursor cursor = db.rawQuery(sql, null);

        while(cursor.moveToNext()){
            BlogItem item = new BlogItem();
            item.setChannel(cursor.getInt(cursor.getColumnIndex("channel")));
            item.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            item.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            item.setLink(cursor.getString(cursor.getColumnIndex("link")));
            item.setPubDate(cursor.getString(cursor.getColumnIndex("pubDate")));
            blogItems.add(item);
        }
        cursor.close();
        closeDatabase();

        return blogItems;
    }

    public synchronized SQLiteDatabase openDatabase(){
        if(mOpenCounter.incrementAndGet() == 1){
            // Opening new database
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public synchronized void closeDatabase(){
        if(mOpenCounter.decrementAndGet() == 0){
            // closing database
            db.close();
        }
    }

}
