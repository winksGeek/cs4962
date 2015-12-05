package winkler.devon.forbiddendesert;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

/**
 * Created by devonwinkler on 12/5/15.
 */
public class PartsCollectedView extends View {

    Part [] _parts;

    public PartsCollectedView(Context context) {
        super(context);
    }

    public void setParts(Part[]parts) {
        _parts = parts;
        invalidate();
    }

    @Override
      protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int measuredWidth = width;
        int measuredHeight = height;
        int desiredHeight = (int)(160.0f * .2f * getResources().getDisplayMetrics().density);
        int desiredWidth = desiredHeight * 4;
        if(widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST){
            measuredWidth = Math.min(width, desiredWidth);
            measuredHeight = Math.min(height, desiredHeight);
        }
        if(widthMode == MeasureSpec.UNSPECIFIED && heightMode == MeasureSpec.UNSPECIFIED){
            measuredHeight = desiredHeight;
        }
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int squareWidth = getHeight();
        int squareHeight = getHeight();
        int squareMargin = 2;
        for (int i = 0; i < _parts.length; i++) {
            Part part = _parts[i];
            int color = 0xFFCC9900;
            int paddingTop = part.yPos == 0 ? getPaddingTop() : 0;
            int paddingRight = part.xPos == 4 ? getPaddingRight() : 0;
            int paddingBottom = part.yPos == 4 ? getPaddingBottom() : 0;
            int paddingLeft = part.xPos == 0 ? getPaddingLeft() : 0;
            Drawable drawable = getDrawableForPart(part);
//            paint.setColor(color);
            Rect squareRect = new Rect();
            squareRect.top = paddingTop + part.yPos * squareHeight + squareMargin;
            squareRect.left = paddingLeft + part.xPos * squareWidth + squareMargin;
            squareRect.right = squareRect.left + squareWidth - paddingRight;
            squareRect.bottom = getHeight() - paddingBottom;
            drawable.setBounds(squareRect);
            drawable.draw(canvas);
        }
    }

    private Drawable getDrawableForPart(Part part) {
        Drawable drawable = null;
        switch(part._type){
            case Propeller:
                drawable = getResources().getDrawable(R.drawable.propeller_part, null);
                if(part.collected){
                    drawable = getResources().getDrawable(R.drawable.propeller_part_collected, null);
                }
                break;
            case Engine:
                drawable = getResources().getDrawable(R.drawable.engine_part, null);
                if(part.collected){
                    drawable = getResources().getDrawable(R.drawable.engine_part_collected, null);
                }
                break;
            case Crystal:
                drawable = getResources().getDrawable(R.drawable.crystal_part, null);
                if(part.collected){
                    drawable = getResources().getDrawable(R.drawable.crystal_part_collected, null);
                }
                break;
            case Navigation:
                drawable = getResources().getDrawable(R.drawable.navigation_part, null);
                if(part.collected){
                    drawable = getResources().getDrawable(R.drawable.navigation_part_collected, null);
                }
                break;
        }

        return drawable;
    }
}
