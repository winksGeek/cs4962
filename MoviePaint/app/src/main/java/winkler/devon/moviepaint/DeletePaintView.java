package winkler.devon.moviepaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by devonwinkler on 9/19/15.
 */
public class DeletePaintView extends PaintView {
    OnDeleteListener myDeletelistener;

    public interface OnDeleteListener {
        public void onDeletePaint();
    }


    public DeletePaintView(Context context){
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas){
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(10.0f);
        float highlightWidth = getWidth() * .1f;
        RectF paintRect = new RectF();
        paintRect.left = getPaddingLeft()+highlightWidth;
        paintRect.top = getPaddingTop()+highlightWidth;
        paintRect.right = getWidth() - getPaddingRight()-highlightWidth;
        paintRect.bottom = getHeight() - getPaddingBottom()-highlightWidth;
        canvas.drawOval(paintRect, paint);
        canvas.drawLine(paintRect.left, paintRect.top, paintRect.right, paintRect.bottom, paint);
//        canvas.drawLine(paintRect.left, paintRect.bottom, paintRect.right, paintRect.top, paint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if(myDeletelistener != null) {
                    myDeletelistener.onDeletePaint();
                }
                break;
            default:
                return false;
        }
        return true;
    }

    public OnDeleteListener getMyDeletelistener() {
        return myDeletelistener;
    }

    public void setMyDeletelistener(OnDeleteListener myDeletelistener) {
        this.myDeletelistener = myDeletelistener;
    }
}
