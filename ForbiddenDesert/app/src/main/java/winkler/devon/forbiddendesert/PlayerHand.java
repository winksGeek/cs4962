package winkler.devon.forbiddendesert;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by devonwinkler on 12/9/15.
 */
public class PlayerHand extends View {
    public PlayerHand(Context context) {
        super(context);
    }

    ArrayList<ItemCard> _cards;

    public void setHand(ArrayList<ItemCard> cards) {
        _cards = cards;
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
        int desiredWidth = desiredHeight * _cards.size();
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
        for (int i = 0; i < _cards.size(); i++) {
            ItemCard card = _cards.get(i);
            int color = 0xFFCC9900;
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();
            int paddingLeft = getPaddingLeft();
            Drawable drawable = getDrawableForCard(card);
//            paint.setColor(color);
            Rect squareRect = new Rect();
            squareRect.top = paddingTop;
            squareRect.left = paddingLeft + i * squareWidth + squareMargin;
            squareRect.right = squareRect.left + squareWidth - paddingRight;
            squareRect.bottom = getHeight() - paddingBottom;
            drawable.setBounds(squareRect);
            drawable.draw(canvas);
        }
    }

    private Drawable getDrawableForCard(ItemCard card){
        Drawable drawable = null;
        switch(card.type){
            case Jet:
                drawable = getResources().getDrawable(R.drawable.gear_jet, null);
                break;
            case Solar:
                drawable = getResources().getDrawable(R.drawable.gear_solar, null);
                break;
            case Blaster:
                drawable = getResources().getDrawable(R.drawable.gear_blaster, null);
                break;
            case Throttle:
                drawable = getResources().getDrawable(R.drawable.gear_throttle, null);
                break;
            case Water:
                drawable = getResources().getDrawable(R.drawable.gear_water, null);
                break;
            case Terrascope:
                drawable = getResources().getDrawable(R.drawable.gear_terrascope, null);
                break;
        }

        return drawable;

    }
}
