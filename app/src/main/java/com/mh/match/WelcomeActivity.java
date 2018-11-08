package com.mh.match;

import java.util.*;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class WelcomeActivity extends Activity {

	private Timer timer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		final Intent intent = new Intent(this, MainActivity.class); // 你要转向的Activity
		timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				startActivity(intent); // 执行
				finish();
			}
		};
		timer.schedule(task, 1000 * 3); // 3秒后
	}



	@Override
	protected void onStop() {
		// TODO 自动生成的方法存根
		timer.cancel();
		super.onStop();
	}




}
