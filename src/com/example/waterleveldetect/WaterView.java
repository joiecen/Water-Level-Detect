package com.example.waterleveldetect;

import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

public class WaterView extends View{
	private Bitmap waterBitmap;
	private int height;
	private Paint rectpaint;

	public WaterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		rectpaint = new Paint();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		rectpaint.setStyle(Paint.Style.STROKE);
		rectpaint.setStrokeWidth(5);
		rectpaint.setColor(0xff000080);
		canvas.drawRect(new RectF(200,0,800,600), rectpaint);		
		Rect rect = new Rect(200, 800-height, 800, 800);
		Rect rect2 = new Rect(200,600-height,800,600);
		canvas.drawBitmap(waterBitmap , rect, rect2, null);
		
	}

	public boolean setviewparam(int height){
		this.height = height;
		waterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.lidwater);
		Bitmap bitmap = Bitmap.createBitmap(waterBitmap.getWidth()/2, waterBitmap.getHeight()/2, Config.ARGB_8888)  ;  
		Canvas canvas = new Canvas(bitmap);
		return true;
	}
}
