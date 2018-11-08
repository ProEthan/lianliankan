package com.mh.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.mh.view.Score;
import java.util.ArrayList;
import java.util.List;

public class llk_Manager {
    private llk_Helper helper;
    private SQLiteDatabase db;

    public llk_Manager(Context context){
        helper = new llk_Helper(context);
        db = helper.getWritableDatabase();
    }

    //增加排行榜
    public void add(List<Score> scores){
        db.beginTransaction();
        try{
            for(Score score: scores){
                db.execSQL("INSERT INTO score VALUES(?,?,?)",new Object[]{score.name,score.time,score.sc});
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public void addo(Score score)
    {
        db.execSQL("INSERT INTO score VALUES(?,?,?)",new Object[]{score.name,score.time,score.sc});
    }

    //更新分数
    public void updateSc(Score score){
        ContentValues cv = new ContentValues();
        cv.put("sc",score.sc);
        db.update("score",cv,"name=?",new String[]{score.name});
    }

    //更新时间
    public void updateTime(Score score){
        ContentValues cv=new ContentValues();
        cv.put("time",score.time);
        db.update("score",cv,"name=?",new String[]{score.name});
    }

    //按分数降序查询
    public List<Score> querySc(){
        ArrayList<Score> scores = new ArrayList<Score>();
        Cursor c = db.query("score",null,null,null,null,null,"sc desc","0,5");
        while (c.moveToNext()){
            Score score = new Score();
            score.name=c.getString(c.getColumnIndex("name"));
            score.time=c.getInt(c.getColumnIndex("time"));
            score.sc=c.getInt(c.getColumnIndex("sc"));
            scores.add(score);
        }
        c.close();
        return scores;
    }

    //按时间升序查询
    public List<Score> queryTime(){
        ArrayList<Score> scores = new ArrayList<Score>();
        Cursor c = db.query("score",null,null,null,null,null,"time asc","0,5");
        while (c.moveToNext()){
            Score score = new Score();
            score.name=c.getString(c.getColumnIndex("name"));
            score.time=c.getInt(c.getColumnIndex("time"));
            score.sc=c.getInt(c.getColumnIndex("sc"));
            scores.add(score);
        }
        c.close();
        return scores;
    }

    public Cursor queryCursor(){
        Cursor c = db.rawQuery("SELECT * FROM score",null);
        return c;
    }

    //插入
    public void insert(Score score){
        Cursor c = db.query("score",null,"name=?",new String[]{score.name},null,null,null);
        if(c.moveToFirst())
        {
            if(score.sc>c.getInt(c.getColumnIndex("sc")))
                updateSc(score);
            if(score.time<c.getInt(c.getColumnIndex("time")))
                updateTime(score);
        }
        else
        {
            addo(score);
        }
    }

    //关闭数据库
    public void closeDB(){
        db.close();
    }
}
