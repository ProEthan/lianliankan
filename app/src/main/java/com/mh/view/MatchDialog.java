package com.mh.view;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;
import com.mh.match.*;
import android.app.Dialog;
import android.content.Context;
import android.widget.ImageButton;
import android.widget.TextView;

public class MatchDialog extends Dialog implements OnClickListener {

	private GameView gameView;
	private MainActivity activity;
	private Context context;
	public MatchDialog(Context context, GameView gameView, String msg, int time) {
		// TODO 自动生成的构造函数存根
		super(context, R.style.match_dialog);
		this.gameView = gameView;
		this.activity = (MainActivity)context;
		this.context=context;
		this.setContentView(R.layout.match_dialog);
		TextView txtTitle = (TextView) findViewById(R.id.tv_dialog_title);
		TextView txtUsedTime = (TextView) findViewById(R.id.tv_dialog_usedTime);
		ImageButton btnExit = (ImageButton) findViewById(R.id.btn_dialog_exit);
		ImageButton btnNext = (ImageButton) findViewById(R.id.btn_dialog_next);
		ImageButton btnReplay = (ImageButton) findViewById(R.id.btn_dialog_replay);
		ImageButton btnLogin=(ImageButton)findViewById(R.id.btn_dialog_login);
		txtTitle.setText(msg);
		txtUsedTime.setText(txtUsedTime.getText().toString().replace("$", String.valueOf(time)));
		btnExit.setOnClickListener(this);
		btnNext.setOnClickListener(this);
		btnReplay.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		this.setCancelable(false);
		if (gameView.Score>=320)
			btnLogin.setVisibility(View.VISIBLE);
		if(gameView.win())
			btnNext.setVisibility(View.VISIBLE);
	}


	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
			case R.id.btn_dialog_exit:
				activity.exit();
				break;
			case R.id.btn_dialog_next:
				gameView.next();
				activity.setTotalNum();
				break;
			case R.id.btn_dialog_replay:
				gameView.replay();
				activity.setTotalNum();
				break;
			case R.id.btn_dialog_login:
				if(gameView.win()) gameView.next();
				Intent login = new Intent();
				Bundle bundle=new Bundle();
				bundle.putInt("time",gameView.Time); //传递分数和时间
				bundle.putInt("score",gameView.Score);
				login.putExtras(bundle);
				login.setClass(context, RankActivity.class);
				context.startActivity(login);
				break;
		}

		this.cancel();
	}

}
