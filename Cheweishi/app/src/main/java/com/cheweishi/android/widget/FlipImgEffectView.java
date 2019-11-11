package com.cheweishi.android.widget;

import com.cheweishi.android.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.view.View;

	class FlipImgEffectView extends View {  
		  
	    private Context context ;  
	    private Bitmap showBmp ;  
	    private Matrix matrix ; //作用矩阵   
	    private Camera camera ;   
	    private int deltaX , deltaY ; //翻转角度差值   
	    private int centerX , centerY ; //图片中心点   
	      
	    public FlipImgEffectView(Context context) {  
	        super(context);  
	        this.context = context ;  
	        initData();  
	    }  
	      
	    private void initData(){  
	        showBmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher);  
	        centerX = showBmp.getWidth()/2 ;  
	        centerY = showBmp.getHeight()/2 ;  
	        matrix = new Matrix();  
	        camera = new Camera();
	    }  
	  
	    int lastMouseX ;  
	    int lastMouseY ;  
	    @Override  
	    public boolean dispatchTouchEvent(MotionEvent event) {  
	        int x = (int) event.getX();      
	        int y = (int) event.getY();      
	        switch(event.getAction()) {      
	         case MotionEvent.ACTION_DOWN:   
	             lastMouseX = x ;  
	             lastMouseY = y ;  
	             break;      
	         case MotionEvent.ACTION_MOVE:  
	             int dx = x - lastMouseX ;  
	             int dy = y - lastMouseY ;  
	             deltaX += dx ;  
	             deltaY += dy ;  
	             break;      
	         }     
	         
	        invalidate();  
	        return true;  
	    }  
	  
	    @Override  
	    protected void onDraw(Canvas canvas) {  
	          
	        camera.save();  
	        //绕X轴翻转   
	        camera.rotateX(-deltaY);  
	        //绕Y轴翻转   
	        camera.rotateY(deltaX);  
	        //设置camera作用矩阵   
	        camera.getMatrix(matrix);  
	        camera.restore();  
	        //设置翻转中心点   
	        matrix.preTranslate(-this.centerX, -this.centerY);  
	        matrix.postTranslate(this.centerX, this.centerY);  
	          
	        canvas.drawBitmap(showBmp, matrix, null);  
	    }             
	  
	}  