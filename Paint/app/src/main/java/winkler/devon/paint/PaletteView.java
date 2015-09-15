package winkler.devon.paint;

import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
/**
 * Created by devonwinkler on 9/15/15.
*/
public class PaletteView extends ViewGroup {
    public PaletteView(Context context){
        super(context);
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b){
        float childWidth = (float)getWidth() / ((float)getChildCount()*4.0f);
        float childHeight = (float)getHeight() / ((float)getChildCount()*4.0f);

        RectF ovalRect = new RectF();
        ovalRect.left = (float)getPaddingLeft() + childWidth * .5f;
        ovalRect.top = (float)getPaddingTop() + childHeight * .5f;
        ovalRect.right = (float)getWidth() - getPaddingRight()-childWidth *.5f;
        ovalRect.bottom = (float)getHeight() - getPaddingBottom() - childHeight*.5f;

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
            childRect.bottom = (int) (childCenter.y + childWidth * .5f);
            childView.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }
}