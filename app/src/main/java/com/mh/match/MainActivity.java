package com.mh.match;

import com.mh.view.*;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

@SuppressLint("HandlerLeak")
public class MainActivity extends Activity
		implements OnToolsChangeListener, OnStateChangeListener, OnClickListener, OnTimeChangeListener {

    private ImageButton btnbgm;
    private ImageButton btngamem;
	private ImageButton btnPlay;
	private ImageButton btnDisrupt;
	private ImageButton btnTip;
	private ImageView ivTitle;
	private GameView gameView;
	private ProgressBar proBarLeftTime;
	private TextView tvDisruptNum;
	private TextView tvTipNum;
	private RelativeLayout rlTop;
	private LinearLayout llBottom;
	private MediaPlayer mpInitMusic;
	boolean isStarted = false;		//判断游戏是否开始
	boolean isStoped = false;		//判断游戏是否结束
	private MatchDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final RelativeLayout layout = addGameView();
		setContentView(layout);

		btnbgm=(ImageButton)findViewById(R.id.btn_main_bgmusic)  ;
		btngamem=(ImageButton)findViewById(R.id.btn_main_gamemusic) ;
		btnPlay = (ImageButton) findViewById(R.id.btn_main_play);
		btnDisrupt = (ImageButton) findViewById(R.id.btn_main_disrupt);
		btnTip = (ImageButton) findViewById(R.id.btn_main_tip);
		ivTitle = (ImageView) findViewById(R.id.iv_main_title);
		tvDisruptNum = (TextView) findViewById(R.id.tv_main_disruptNum);
		tvTipNum = (TextView) findViewById(R.id.tv_main_tipNum);
		proBarLeftTime = (ProgressBar) findViewById(R.id.proBar_main_leftTime);
		rlTop = (RelativeLayout) findViewById(R.id.rl_main_top);
		llBottom = (LinearLayout) findViewById(R.id.ll_main_bottom);

		setTotalNum();
		btnbgm.setOnClickListener(this);
		btngamem.setOnClickListener(this);
		btnPlay.setOnClickListener(this);
		btnDisrupt.setOnClickListener(this);
		btnTip.setOnClickListener(this);
		proBarLeftTime.setProgress(gameView.getTotalTime());
		gameView.setVisibility(View.GONE);
		gameView.setOnToolsChangeListener(this);
		gameView.setOnStateChangeListener(this);
		gameView.setOnTimeChangeListener(this);
		Animation scale = AnimationUtils.loadAnimation(this,R.anim.scale_anim);
		btnPlay.startAnimation(scale);

		//播放刚开始的前导音乐
		mpInitMusic = MediaPlayer.create(MainActivity.this, R.raw.init_bg);
		//单曲循环
		mpInitMusic.setLooping(true);
		mpInitMusic.start();

	}

	/**
	 * 设置界面上打乱工具的总次数，外部方法
	 * @param
	 */
	public void setTotalNum() {
		this.tvDisruptNum.setText(String.valueOf(gameView.getDisruptTotalNum()));
		this.tvTipNum.setText(String.valueOf(gameView.getTipTotalNum()));
	}




	/**
	 * 动态添加GameView控件
	 * @return
	 */
	private RelativeLayout addGameView() {
		final LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
		// 获取需要被添加控件的布局
		final RelativeLayout layout =  (RelativeLayout)inflater.inflate(R.layout.activity_main, null);
		int screeenWidth = getScreenWidth();
		int minus = 10;
		int width = screeenWidth - 2 * minus;
		gameView = new com.mh.view.GameView(this, width);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(width, width);
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		lp.addRule(RelativeLayout.BELOW, R.id.rl_main_top);
		lp.setMargins(10, 0, 10, 0);
		gameView.setLayoutParams(lp);
		layout.addView(gameView);
		return layout;
	}

	private int getScreenWidth(){
		DisplayMetrics dm = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO 自动生成的方法存根
		if(item.getItemId() == R.id.action_exit) {
			MainActivity.this.exit();
			MainActivity.this.finish();
		} else if (item.getItemId() == R.id.action_about) {
			Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG).show();
		}
		return super.onOptionsItemSelected(item);
	}



	@Override
	protected void onPause() {
		// TODO 自动生成的方法存根
		super.onPause();
		if(!isStoped) {
			if(!isStarted) {
				mpInitMusic.pause();
			} else {
				gameView.pause();
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO 自动生成的方法存根
		super.onResume();
		if(!isStarted) {
			mpInitMusic.start();
		} else {
			gameView.resume();
		}
	}

	@SuppressLint("ShowToast")
	@Override
	public void OnStateChange(int state) {
		// TODO 自动生成的方法存根
		switch (state) {
			case GameView.WIN:
				stateChangeHandler.sendEmptyMessage(GameView.WIN);
				break;
			case GameView.LOSE:
				stateChangeHandler.sendEmptyMessage(GameView.LOSE);
				break;
			case GameView.PAUSE:
				gameView.pause();
				break;
			case GameView.QUIT:

				break;
		}
	}



	//游戏一局后的弹窗
	private Handler stateChangeHandler = new Handler(){
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
				case GameView.WIN:
					gameView.changeTime();
					gameView.changeScore();
					dialog = new MatchDialog(MainActivity.this, gameView, "胜利，当前分数为"+gameView.Score,
							gameView.getTotalTime() - proBarLeftTime.getProgress());
					dialog.show();
					break;
				case GameView.LOSE:
					gameView.changeTime();
					gameView.changeScore();
					dialog = new MatchDialog(MainActivity.this, gameView, "失败，当前分数为"+gameView.Score,
							gameView.getTotalTime() - proBarLeftTime.getProgress());
					dialog.show();
			}
		}
	};

	@Override
	public void onDisruptChange(int leftNum, int state) {
		// TODO 自动生成的方法存根
		tvDisruptNum.setText(String.valueOf(leftNum));
		if(state == TOOL_USED_UP) {
			MatchToast.showToast(MainActivity.this, "打乱工具用完了!");
		}
	}


	@Override
	public void onTipChange(int leftNum, int state) {
		// TODO 自动生成的方法存根
		tvTipNum.setText(String.valueOf(leftNum));
		if(state == TOOL_USED_UP) {
			MatchToast.showToast(MainActivity.this, "提示工具用完了!");
		} else if(state == TOOL_FAIL) {
			MatchToast.showToast(MainActivity.this, "智能查找失败!");
		}
	}

	@Override
	public void OnTimeChange(int leftTime) {
		// TODO 自动生成的方法存根
		proBarLeftTime.setProgress(leftTime);
	}


	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		switch (v.getId()) {
			case R.id.btn_main_play:
				stopInitMusic();
				Animation scaleOut = AnimationUtils.loadAnimation(this,R.anim.scale_anim_out);
				Animation transIn = AnimationUtils.loadAnimation(this,R.anim.trans_in);
				btnPlay.startAnimation(scaleOut);
				ivTitle.startAnimation(scaleOut);
				btnPlay.setVisibility(View.GONE);
				ivTitle.setVisibility(View.GONE);
				rlTop.setVisibility(View.VISIBLE);
				llBottom.setVisibility(View.VISIBLE);
				gameView.setVisibility(View.VISIBLE);
				rlTop.startAnimation(transIn);
				llBottom.startAnimation(transIn);
				gameView.startAnimation(transIn);
				gameView.start();
				isStarted = true;
				break;
			case R.id.btn_main_disrupt:
				Animation shake01 = AnimationUtils.loadAnimation(this,R.anim.shake);
				btnDisrupt.startAnimation(shake01);
				gameView.disrupt();
				break;
			case R.id.btn_main_tip:
				Animation shake02 = AnimationUtils.loadAnimation(this,R.anim.shake);
				btnTip.startAnimation(shake02);
				gameView.help();
				break;
            case R.id.btn_main_bgmusic:
				Animation shake03 = AnimationUtils.loadAnimation(this,R.anim.shake);
				btnbgm.startAnimation(shake03);
                if(!gameView.mpBGMusic.isPlaying())
                    gameView.mpBGMusic.start();
                else gameView.mpBGMusic.pause();
                break;
            case R.id.btn_main_gamemusic:
				Animation shake04 = AnimationUtils.loadAnimation(this,R.anim.shake);
				btngamem.startAnimation(shake04);
                if(gameView.control>0)
                    gameView.pauseSound();
                else gameView.resumeSound();
                break;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 自动生成的方法存根
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
			builder.setTitle("提示");
			builder.setMessage("确定残忍退出吗？");
			builder.setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int arg) {
					// TODO 自动生成的方法存根
					dialog.cancel();
				}
			});

			builder.setPositiveButton("确定", new android.content.DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int arg) {
					// TODO 自动生成的方法存根
					MainActivity.this.exit();
				}

			});
			builder.setCancelable(false).create().show();
		}
		return super.onKeyDown(keyCode, event);
	}



	/**
	 * 结束开始音乐，释放资源
	 */
	public void stopInitMusic() {
		mpInitMusic.stop();
		mpInitMusic.release();
	}

	/**
	 * 释放资源，停止线程
	 */
	public void exit() {
		if(!isStarted) {
			stopInitMusic();
		} else {
			gameView.exit();
		}
		isStoped = true;
		MainActivity.this.finish();
	}

}
