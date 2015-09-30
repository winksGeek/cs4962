package winkler.devon.moviepaint;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
/**
 * Created by devonwinkler on 9/15/15.
*/
public class PaletteView extends ViewGroup implements View.OnTouchListener, DeletePaintView.OnDeleteListener{
    public interface OnPaintChangeListener{
        public void onPaintChange(int color);
    }

    PaintView mixPaint, activePaint;
    private OnPaintChangeListener myPaintChangeListener;

    public PaletteView(Context context){
        super(context);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int measuredWidth = width;
        int measuredHeight = height;
        int desiredSize = (int)(160.0f * 1.25f * getResources().getDisplayMetrics().density);

        if(widthMode == MeasureSpec.EXACTLY){
            measuredHeight = width;
        }
        else if(heightMode == MeasureSpec.EXACTLY){
            measuredWidth = height;
        }
        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            measuredWidth = Math.min(Math.min(height, width), desiredSize);
            measuredHeight = measuredWidth;
        }
        if(widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED){
            measuredWidth = desiredSize;
            measuredHeight = desiredSize;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        RectF paintRect = new RectF();
        paintRect.left = getPaddingLeft();
        paintRect.top = getPaddingTop();
        paintRect.right = getWidth() - getPaddingRight();
        paintRect.bottom = getHeight() - getPaddingBottom();
        Paint palettePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        palettePaint.setColor(0xFFB89470);
        canvas.drawOval(paintRect, palettePaint);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b){
        int childCount = getChildCount();
        float angle = (2.0f * (float)Math.PI)/childCount;
        PointF first = new PointF();
        PointF second = new PointF();
        float minChildWidth = (int)(160.0f * .25f * getResources().getDisplayMetrics().density);
        float minChildHeight = (int)(160.0f * .25f * getResources().getDisplayMetrics().density);


        RectF ovalRect = new RectF();
        ovalRect.left = (float)getPaddingLeft() + minChildWidth * .5f;
        ovalRect.top = (float)getPaddingTop() + minChildHeight * .5f;
        ovalRect.right = (float)getWidth() - getPaddingRight()-minChildWidth *.5f;
        ovalRect.bottom = (float)getHeight() - getPaddingBottom() - minChildHeight*.5f;

        first.x = ovalRect.centerX() + ovalRect.width() * .5f * (float)(Math.cos(angle));
        first.y = ovalRect.centerY() + ovalRect.height() * .5f * (float)(Math.sin(angle));

        second.x = ovalRect.centerX() + ovalRect.width() * .5f * (float)(Math.cos(2 * angle));
        second.y = ovalRect.centerY() + ovalRect.height() * .5f * (float)(Math.sin(2 * angle));

        float childWidth = Math.min(minChildWidth, (float)Math.sqrt((first.x - second.x) * (first.x - second.x) + (first.y - second.y) * (first.y - second.y)));
        float childHeight = childWidth;

        for(int index = 0; index < getChildCount(); index++){
            View childView = getChildAt(index);

            float theta = (float)index / (float)getChildCount() * 2.0f * (float)Math.PI;
            PointF childCenter = new PointF();
            childCenter.x = ovalRect.centerX() + ovalRect.width() * .5f * (float)(Math.cos(theta));
            childCenter.y = ovalRect.centerY() + ovalRect.height() * .5f * (float)(Math.sin(theta));


            Rect childRect = new Rect();
            childRect.left = (int) (childCenter.x - childWidth * .5f);
            childRect.top = (int) (childCenter.y - childHeight * .5f);
            childRect.right = (int) (childCenter.x + childWidth * .5f);
            childRect.bottom = (int) (childCenter.y + childHeight * .5f);
            childView.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        PaintView pv = (PaintView) v;
        boolean mix = pv.isMix();
        boolean active = pv.isActive();
        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!active) {
                    if(this.mixPaint == null) {
                        this.activePaint.setActive(false);
                        this.activePaint.invalidate();
//                        for (int i = 0; i < getChildCount(); i++) {
//                            PaintView childView = (PaintView) getChildAt(i);
//                            childView.setActive(false);
//                            childView.invalidate();
//                        }
                        pv.setActive(true);
                        this.activePaint = pv;
                    }else{
                        PaintView newPaint = mixPaints(pv, mixPaint);
                        this.mixPaint.setActive(false);
                        this.mixPaint.setMix(false);
                        this.mixPaint.invalidate();
                        this.mixPaint = null;
                        newPaint.setActive(true);
                        addView(newPaint);
                        this.activePaint = newPaint;
                    }
                    if(getMyPaintChangeListener() != null) {
                        getMyPaintChangeListener().onPaintChange(this.activePaint.getColor());
                    }
                } else if (!pv.isMix()) {
                    pv.setMix(true);
                    this.mixPaint = pv;
                } else {
                    pv.setMix(false);
                    this.mixPaint = null;
                }
                break;
            default:
                return false;
        }
        pv.invalidate();
        return true;
    }

    @Override
    public void onDeletePaint(){
        if(mixPaint != null){
            removeView(mixPaint);
            mixPaint = null;
            initializePalette();
        }
    }

    public OnPaintChangeListener getMyPaintChangeListener() {
        return myPaintChangeListener;
    }

    public void setMyPaintChangeListener(OnPaintChangeListener myPaintChangeListener) {
        this.myPaintChangeListener = myPaintChangeListener;
    }

    public void initializePalette(){
        if(getChildCount() < 2) {

            //add white
            int color = 0xFFFFFFFF;
            PaintView splotchView = new PaintView(getContext());
            splotchView.setOnTouchListener(this);
            splotchView.setColor(color);
            this.addView(splotchView);

            //add RGB and black
            for (int i = 0; i < 4; i++) {
                splotchView = new PaintView(getContext());
                color = 0x000000FF;
                splotchView.setColor(0xFF000000 | (color << i * 8));
                splotchView.setOnTouchListener(this);
                this.addView(splotchView);
            }
        }
        this.activePaint = (PaintView) getChildAt(getChildCount()-1);
        activePaint.setActive(true);
//        getMyPaintChangeListener().onPaintChange(this.activePaint.getColor());
    }

    private PaintView mixPaints(PaintView first, PaintView second){
        PaintView newPaint = new PaintView(getContext());
        int firstColor = first.getColor();
        int secondColor = second.getColor();
        int redMask = 0x00FF0000;
        int greenMask = 0x0000FF00;
        int blueMask = 0x000000FF;
        int firstRed = firstColor & redMask;
        int firstGreen = firstColor & greenMask;
        int firstBlue = firstColor & blueMask;
        int secondRed = secondColor & redMask;
        int secondGreen = secondColor & greenMask;
        int secondBlue = secondColor & blueMask;
        int averageRed = (firstRed + secondRed)/2;
        int averageGreen = (firstGreen + secondGreen)/2;
        int averageBlue = (firstBlue + secondBlue)/2;
        int newColor = 0xFF000000;
        newColor |= averageRed;
        newColor |= averageGreen;
        newColor |= averageBlue;
        newPaint.setColor(newColor);
        newPaint.setOnTouchListener(this);
        return newPaint;
    }
}