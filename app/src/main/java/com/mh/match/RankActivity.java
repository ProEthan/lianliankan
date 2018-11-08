package com.mh.match;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.mh.DataBase.llk_Manager;
import com.mh.view.Score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RankActivity extends Activity implements View.OnClickListener {
    private EditText editName;
    private Button btnlogin;
    private Button btntime;
    private Button btnscore;
    private ListView listView;
    private llk_Manager mgr;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);


        listView=(ListView)findViewById(R.id.list_rank);
        editName = (EditText) findViewById(R.id.editText);
        btnlogin = (Button) findViewById(R.id.login);
        btntime =(Button)findViewById(R.id.timerank);
        btnscore=(Button)findViewById(R.id.scorerank);

        mgr = new llk_Manager(this);
        btnlogin.setOnClickListener(this);
        btnscore.setOnClickListener(this);
        btntime.setOnClickListener(this);
        listView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        return true;
                    default:
                        break;
                }
                return true;
            }
        });
    }


    public void add(View view){
        ArrayList<Score> scores=new ArrayList<Score>();

        Score score1 = new Score("aaa",11,111);
        Score score2 = new Score("bbb", 22,222);
        Score score3 = new Score("ccc",33,333);

        scores.add(score1);
        scores.add(score2);
        scores.add(score3);

        mgr.add(scores);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                String playername=editName.getText().toString();
                Bundle bundle=getIntent().getExtras();
                int time=bundle.getInt("time");
                int score=bundle.getInt("score");
                Score player=new Score(playername,time,score);
                mgr.insert(player);
                break;
            case R.id.timerank:
                queryTime(listView);
                break;
            case R.id.scorerank:
                querySc(listView);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mgr.closeDB();
    }


    public void querySc(View view){
        List<Score> scores = mgr.querySc();
        ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
        for(Score score : scores){
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("name",score.name);
            map.put("time", new String().valueOf(score.time));
            map.put("score",new String().valueOf(score.sc));
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.list_item,
                new String[]{"name","time","score"},new int[]{R.id.name,R.id.time,R.id.score});
        listView.setAdapter(adapter);
    }

    public void queryTime(View view){
        List<Score> scores = mgr.queryTime();
        ArrayList<Map<String,String>> list = new ArrayList<Map<String,String>>();
        for(Score score : scores){
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("name",score.name);
            map.put("time", new String().valueOf(score.time));
            map.put("score",new String().valueOf(score.sc));
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,list,R.layout.list_item,
                new String[]{"name","time","score"},new int[]{R.id.name,R.id.time,R.id.score});
        listView.setAdapter(adapter);
    }
}