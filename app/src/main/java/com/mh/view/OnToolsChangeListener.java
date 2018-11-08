package com.mh.view;

public interface OnToolsChangeListener {

	public static final int TOOL_SUCCESS = 0;
	public static final int TOOL_FAIL = 1;
	public static final int TOOL_USED_UP = 2;

	/**
	 * 打乱水果顺序时调用的方法
	 * @param leftNum 剩余次数
	 * @param state 状态
	 */
	public void onDisruptChange(int leftNum, int state);
	/**
	 * 智能提示时回调的方法
	 * @param leftNum
	 * @param state 0表示成功查找，1表示查找失败，2表示次数已用完
	 */
	public void onTipChange(int leftNum, int state);
}
