package winkler.devon.moviepaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by devonwinkler on 9/17/15.
 */
public class PaintView extends View {

    private boolean active;
    private boolean mix;
    int myColor;
    Paint myPaint, highlightPaint, mixHighlightPaint;
    OnTouchListener listener;

    public PaintView(Context context){
        super(context);
        highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        highlightPaint.setColor(0x990000FF);
        mixHighlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mixHighlightPaint.setColor(0x99FF0000);
        setMix(false);
        setActive(false);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        float highlightWidth = getWidth() * .1f;
        RectF paintRect = new RectF();
        paintRect.left = getPaddingLeft()+highlightWidth;
        paintRect.top = getPaddingTop()+highlightWidth;
        paintRect.right = getWidth() - getPaddingRight()-highlightWidth;
        paintRect.bottom = getHeight() - getPaddingBottom()-highlightWidth;

        RectF higlightRect = new RectF();
        higlightRect.left = paintRect.left - highlightWidth;
        higlightRect.top = paintRect.top - highlightWidth;
        higlightRect.right = paintRect.right + highlightWidth;
        higlightRect.bottom = paintRect.bottom + highlightWidth;
        if(isActive() && !isMix()){
            canvas.drawOval(higlightRect, highlightPaint);
        }else if(isMix()){
            canvas.drawOval(higlightRect, mixHighlightPaint);
        }
        canvas.drawOval(paintRect, myPaint);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event){
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                break;
//            default:
//                return false;
//        }
//        invalidate();
//        return true;
//    }

    public void setColor(int color){
        myColor = color;
        myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        myPaint.setColor(myColor);
    }

    public int getColor(){
        return myColor;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isMix() {
        return mix;
    }

    public void setMix(boolean mix) {
        this.mix = mix;
    }
}
