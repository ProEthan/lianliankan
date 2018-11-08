package com.mh.view;



import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.mh.match.*;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.view.View;

@SuppressLint("ViewConstructor")
public class BoardView extends View {

	/**
	 * x轴方向上的图标数
	 */
	protected static final int xCount = 8;
	/**
	 * y轴方向上的图标数
	 */
	protected static final int yCount = 8;
	/**
	 * map 连连看游戏棋盘
	 */
	protected int[][] map = new int[xCount][yCount];
	/**
	 * 表示该区域没有图标
	 */
	protected final int BLANK = -1;
	/**
	 * View在屏幕上的宽度
	 */
	protected int screenWidth;
	/**
	 * 绘制的小图标大小
	 */
	protected int iconSize;
	/**
	 * iconCounts 图标的数目
	 */
	protected int iconCounts = 64;
	/**
	 * 界面上整个图片和View边界的空隙
	 */
	protected int delta;
	/**
	 * 拐点一
	 */
	protected Point flexPoint1;
	/**
	 * 拐点二
	 */
	protected Point flexPoint2;
	/**
	 * icons 所有的图片
	 */
	protected Bitmap[] icons = new Bitmap[iconCounts];
	/**
	 * 定义两个方块相连的类型
	 */
	private enum LinkType {
		LineType,
		OneCornerType,
		TwoCornerType
	}
	/**
	 * 连通类型
	 */
	protected LinkType lType;
	/**
	 * 需要绘制到View上去的Bitmap
	 */
	protected Bitmap bitmap = null;
	/**
	 * selected 选中的图标
	 */
	protected List<Point> selected = new ArrayList<Point>();


	public BoardView(Context context, int width) {
		super(context);
		screenWidth = width;
		iconSize = width / (xCount + 2);		//设置小图标的宽度
		delta = iconSize;
		bitmap = Bitmap.createBitmap(screenWidth, screenWidth, Bitmap.Config.ARGB_8888);

		Resources r = getResources();
		loadBitmaps(0, r.getDrawable(R.drawable.fruit_00));
		loadBitmaps(1, r.getDrawable(R.drawable.fruit_01));
		loadBitmaps(2, r.getDrawable(R.drawable.fruit_02));
		loadBitmaps(3, r.getDrawable(R.drawable.fruit_03));
		loadBitmaps(4, r.getDrawable(R.drawable.fruit_04));
		loadBitmaps(5, r.getDrawable(R.drawable.fruit_05));
		loadBitmaps(6, r.getDrawable(R.drawable.fruit_06));
		loadBitmaps(7, r.getDrawable(R.drawable.fruit_07));
		loadBitmaps(8, r.getDrawable(R.drawable.fruit_08));
		loadBitmaps(9, r.getDrawable(R.drawable.fruit_09));
		loadBitmaps(10, r.getDrawable(R.drawable.fruit_10));
		loadBitmaps(11, r.getDrawable(R.drawable.fruit_11));
		loadBitmaps(12, r.getDrawable(R.drawable.fruit_12));
		loadBitmaps(13, r.getDrawable(R.drawable.fruit_13));
		loadBitmaps(14, r.getDrawable(R.drawable.fruit_14));
		loadBitmaps(15, r.getDrawable(R.drawable.fruit_15));
	}

	/**
	 * 加载图片
	 * @param key 特定图标的标识
	 * @param drawable下的资源
	 */
	public void loadBitmaps(int key,Drawable d){
		int size = 100;
		Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		d.setBounds(0, 0, size, size);
		d.draw(canvas);
		icons[key]=bitmap;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if(bitmap != null){
			canvas.drawBitmap(bitmap, 0, 0, null);
		}
	}

	/**
	 * 画出所有还存在的小图标
	 */
	protected void drawAllIcons(){
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);	//清除内容
		//绘制棋盘的所有图标 当这个坐标内的值大于0时绘制
		for(int i=0; i < map.length; i += 1){
			for(int j = 0; j < map[i].length; j += 1){
				if(map[i][j] > BLANK){
					Point p = indexToScreen(j, i);
					canvas.drawBitmap(icons[map[i][j]],
							null,
							new Rect(p.x, p.y, p.x + iconSize -1, p.y + iconSize - 1), null);
				}
			}
		}
	}

	/* * 工具方法
	 * 放大画出选中的图标
	 * @param x
	 * @param y
	 */
	protected void zommInIcon(int x, int y) {
		Canvas canvas = new Canvas(bitmap);
		canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);	//清除内容
		for(int i=0; i < map.length; i += 1){
			for(int j = 0; j < map[i].length; j += 1){
				if(map[i][j] > BLANK){
					Point p = indexToScreen(j, i);
					if(isSelected(j, i)) {
						canvas.drawBitmap(icons[map[i][j]],
								null,
								new Rect(p.x - 7, p.y - 7, p.x + iconSize + 6, p.y + iconSize + 6), null);
					} else {
						canvas.drawBitmap(icons[map[i][j]],
								null,
								new Rect(p.x, p.y, p.x + iconSize -1, p.y + iconSize - 1), null);
					}
				}
			}
		}
	}

	/**
	 * 工具方法
	 * 判断该点是否被选中
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isSelected(int x, int y) {
		for(Point p: selected){
			if(x == p.x && y == p.y)
				return true;
		}
		return false;
	}



	/**
	 * 工具方法
	 * 缩小放大的图标
	 * @param y
	 * @param x
	 */
	protected void zoomOutIcon() {
		drawAllIcons();	//这里用重画地图的方法来做
	}

	/**
	 * 工具方法
	 * @param x 屏幕上横坐标
	 * @param y 屏幕上纵坐标
	 * @return
	 */
	protected boolean isEffectiveArea(int x, int y) {
		int min = delta;
		int max = xCount * iconSize + delta - 1;
		boolean con1 = (x >= min) && (x <= max);
		boolean con2 = (y >= min) && (y <= max);
		return con1 && con2;
	}

	/**
	 * 工具方法
	 * @param x 数组中的列
	 * @param y 数组中的行
	 * @return 将图标在数组中的坐标转成在屏幕上的真实坐标
	 */
	protected Point indexToScreen(int x,int y) {
		return new Point(x* iconSize + delta , y * iconSize + delta);
	}

	/**
	 * 工具方法
	 * @param x 屏幕中的横坐标
	 * @param y 屏幕中的纵坐标
	 * @return 将图标在屏幕中的坐标转成在数组上的虚拟坐标
	 */
	protected Point screenToIndex(int x, int y) {
		return new Point((x - delta) / iconSize , (y - delta) / iconSize );
	}

	/**
	 * 判断是否连通和连通类型
	 * @param p1
	 * @param p2
	 * @return
	 */
	protected boolean isLink(Point p1, Point p2) {
		int x1 = p1.x;
		int y1 = p1.y;
		int x2 = p2.x;
		int y2 = p2.y;
		if(isSame(x1, y1, x2, y2)) {
			if(x1==x2) {
				if(vLink(x1,y1,y2)) {
					lType = LinkType.LineType;
					return true;
				}
			}
			if(y1==y2) {
				if(hLink(x1,x2,y1)) {
					lType = LinkType.LineType;
					return true;
				}
			}
			if(x1!=x2&&y1!=y2) {
				if(oneCornerLink(x1,y1,x2,y2)) {
					lType = LinkType.OneCornerType;
					return true;
				}
			}
			if(twoCornerLink(x1,y1,x2,y2)) {
				lType = LinkType.TwoCornerType;
				return true;
			}
		}
		return false;
	}


	/**
	 * 两个小图标是否相同
	 * @return
	 */
	protected boolean isSame(int x1, int y1, int x2, int y2) {
		return map[y1][x1] == map[y2][x2];
	}

	/**
	 * 垂直方向上连通
	 * @param x
	 * @param y1
	 * @param y2
	 * @return
	 */
	protected boolean vLink(int x, int y1, int y2) {
		//保证y2为较大数，其余的方法同理
		if(y1>y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
		}
		boolean islink = true;
		for (int i = y1 + 1; i <= y2 - 1; i++) {
			if(map[i][x] != BLANK) {
				islink = false;
				break;
			}
		}
		return islink;
	}

	/**
	 * 水平方向上连通
	 * @param x1
	 * @param x2
	 * @param y
	 * @return
	 */
	protected boolean hLink(int x1, int x2, int y) {
		if(x1>x2)
		{
			int temp = x1;
			x1 = x2;
			x2 = temp;
		}
		for (int i = x1 + 1; i <= x2 - 1; i++) {
			if(map[y][i] != BLANK) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 一个拐点的连通
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	protected boolean oneCornerLink(int x1, int y1, int x2, int y2) {
		if (y1 > y2) {
			int temp = y1;
			y1 = y2;
			y2 = temp;
			temp = x1;
			x1 = x2;
			x2 = temp;
		}//交换两点坐标
		if (vLink(x1, y1, y2) && hLink(x1, x2, y2) && map[y2][x1] == BLANK) {
			flexPoint1 = new Point(x1, y2);
			return true;
		}
		if (hLink(x1, x2, y1) && vLink(x2, y1, y2) && map[y1][x2] == BLANK) {
			flexPoint1 = new Point(x2, y1);
			return true;
		}
		return false;
	}

	/**
	 * 两个拐点的连通
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	protected boolean twoCornerLink(int x1, int y1, int x2, int y2) {
		for(int i = -1; i <= xCount ; i++) {
			if(i == x1 || i == x2)
				continue;
			if(i == -1 || i == xCount) {
				if(hLink(i, x1, y1) && hLink(i, x2, y2)) {
					flexPoint1 = new Point(i, y1);
					flexPoint2 = new Point(i, y2);
					return true;
				}
			} else {
				if(hLink(i, x1, y1) && hLink(i, x2, y2) && vLink(i, y1, y2)
						&& map[y1][i] == BLANK && map[y2][i] == BLANK) {
					flexPoint1 = new Point(i, y1);
					flexPoint2 = new Point(i, y2);
					return true;
				}
			}
		}

		for(int i = -1; i <= xCount ; i++) {
			if(i == y1 || i == y2)
				continue;
			if(i == -1 || i == xCount) {
				if(vLink(x1, i, y1) && vLink(x2, i, y2)) {
					flexPoint1 = new Point(x1, i);
					flexPoint2 = new Point(x2, i);
					return true;
				}
			} else {
				if(vLink(x1, i, y1) && vLink(x2, i, y2) && hLink(x1, x2, i)
						&& map[i][x1] == BLANK && map[i][x2] == BLANK) {
					flexPoint1 = new Point(x1, i);
					flexPoint2 = new Point(x2, i);
					return true;
				}
			}
		}

		return false;
	}


	/**
	 * 画出连接线
	 * @param innerP1 内部表示点1
	 * @param innerP2 内部表示点2
	 */
	protected void drawLinkLine(Point innerP1, Point innerP2) {
		Point p1 = indexToLinkPoint(innerP1.x, innerP1.y);
		Point p2 = indexToLinkPoint(innerP2.x, innerP2.y);
		Paint paint = new Paint();
		paint.setColor(Color.argb(240, 52, 196, 227));
		paint.setStrokeWidth(5);
		Canvas canvas = new Canvas(bitmap);
		if(lType == LinkType.LineType) {
			canvas.drawLine(p1.x, p1.y, p2.x, p2.y, paint);
		} else if(lType == LinkType.OneCornerType) {
			Point fp = indexToLinkPoint(flexPoint1.x, flexPoint1.y);
			canvas.drawLine(p1.x, p1.y, fp.x, fp.y, paint);
			canvas.drawLine(p2.x, p2.y, fp.x, fp.y, paint);
		} else if(lType == LinkType.TwoCornerType) {
			Point fp1 = indexToLinkPoint(flexPoint1.x, flexPoint1.y);
			Point fp2 = indexToLinkPoint(flexPoint2.x, flexPoint2.y);
			if (flexPoint1.x == innerP1.x || flexPoint1.y == innerP1.y) {
				canvas.drawLine(p1.x, p1.y, fp1.x, fp1.y, paint);
				canvas.drawLine(p2.x, p2.y, fp2.x, fp2.y, paint);
			} else {
				canvas.drawLine(p1.x, p1.y, fp2.x, fp2.y, paint);
				canvas.drawLine(p2.x, p2.y, fp1.x, fp1.y, paint);
			}
			canvas.drawLine(fp1.x, fp1.y, fp2.x, fp2.y, paint);
		}
	}

	/**
	 * 工具方法
	 * 内部表示转化为屏幕上连接点
	 */
	private Point indexToLinkPoint(int x, int y) {
		return new Point(x* iconSize + delta + iconSize / 2 ,
				y * iconSize + delta + iconSize / 2);
	}


	/**
	 * 打乱内部数组顺序从而打乱界面上水果顺序
	 */
	protected void change() {
		Random random = new Random();
		int tmpV, tmpX, tmpY;
		for (int i = 0; i < xCount; i++) {
			for (int j = 0; j < yCount; j++) {
				tmpX = random.nextInt(xCount);
				tmpY = random.nextInt(yCount);
				tmpV = map[i][j];
				map[i][j] = map[tmpX][tmpY];
				map[tmpX][tmpY] = tmpV;
			}
		}
	}

	/**
	 * 智能搜索，找到匹配的水果
	 */
	protected boolean search() {
		for (int i = 0; i < xCount; i++) {
			for (int j = 0; j < yCount; j++) {
				if (map[i][j] == BLANK)
					continue;
				else {
					for (int k = j + 1; k < yCount; k++) {
						if(map[i][k] != BLANK) {
							Point p1 = new Point(j, i);
							Point p2 = new Point(k, i);
							if (isLink(p1, p2)) {
								selected.add(p1);
								selected.add(p2);
								return true;
							}
						}
					}
					for (int k = i + 1; k < xCount; k++) {
						for (int l = 0; l < yCount; l++) {
							if(map[k][l] != BLANK) {
								Point p1 = new Point(j, i);
								Point p2 = new Point(l, k);
								if (isLink(p1, p2)) {
									selected.add(p1);
									selected.add(p2);
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

}
