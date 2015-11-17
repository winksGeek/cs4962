package winkler.devon.forbiddendesert;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class DesertBoardView extends View {
    public DesertBoardView(Context context) {
        super(context);
    }

    DesertTile [] _board;


    public void setBoard(DesertTile[]board) {
        _board = board;
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        int squareWidth = getWidth() / 5;
        int squareHeight = getHeight() / 5;
        int squareMargin = 2;
        for (int i = 0; i < _board.length; i++) {
            DesertTile tile = _board[i];
            DesertTile.Type type = tile.type;
            DesertTile.Status status = tile.status;
            boolean isBuried = tile.numberOfSandTiles > 0;
            boolean isImpassable = !tile.isPassable;
            int color = 0xFFFFFF00;
            int paddingTop = tile.yPos == 0 ? getPaddingTop() : 0;
            int paddingRight = tile.xPos == 4 ? getPaddingRight() : 0;
            int paddingBottom = tile.yPos == 4 ? getPaddingBottom() : 0;
            int paddingLeft = tile.xPos == 0 ? getPaddingLeft() : 0;
            switch(status){
                case Unflipped:
                    switch (type) {
                        case Oasis:
                        case Mirage:
                            color = 0xFF0000FF;
                            break;
                        case Crash:
                            color = 0xFFFF0000;
                            break;
                        case Storm:
                            color = 0xFF000000;
                    }
                    break;
                case Flipped:
                    break;
            }
            paint.setColor(color);
            Rect squareRect = new Rect();
            squareRect.top = paddingTop + tile.yPos * squareHeight + squareMargin;
            squareRect.left = paddingLeft + tile.xPos * squareWidth + squareMargin;
            squareRect.right = getWidth() - squareWidth * (5 - (tile.xPos + 1)) - squareMargin - paddingRight;
            squareRect.bottom = getHeight() - squareHeight * (5 - (tile.yPos + 1)) - squareMargin - paddingBottom;
            canvas.drawRect(squareRect, paint);
        }
    }
}
