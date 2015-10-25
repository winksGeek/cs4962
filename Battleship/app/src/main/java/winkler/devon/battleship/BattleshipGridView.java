package winkler.devon.battleship;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class BattleshipGridView extends View {

    public interface CellClickListener{
        public void onCellClick(int x, int y, BattleshipGridView view);
    }

    int[][]_board;
    boolean _showShips;
    CellClickListener _cellClickListener;
    public BattleshipGridView(Context context, boolean showShips) {
        super(context);
        _board = null;
        _showShips = showShips;
    }

    public void setBoard(int[][]board) {
        _board = board;
        invalidate();
    }

    public void setCellClickListener(CellClickListener cellClickListener){
        _cellClickListener = cellClickListener;
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
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        int squareWidth = getWidth()/10;
        int squareHeight = squareWidth;
        int newX = x/squareWidth;
        int newY = y/squareHeight;
        if(!_showShips) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (_cellClickListener != null) {
                    _cellClickListener.onCellClick(newX, newY, this);
                }
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(_board != null) {
            Paint paint = new Paint();
            int squareWidth = getWidth() / 10;
            int squareHeight = getHeight() / 10;
            int squareMargin = 2;
            for (int x = 0; x < _board.length; x++) {
                for (int y = 0; y < _board[x].length; y++) {
                    int content = _board[x][y];
                    int color = 0xFF0000FF;
                    int paddingTop = y == 0 ? getPaddingTop() : 0;
                    int paddingRight = x == _board.length-1 ? getPaddingRight() : 0;
                    int paddingBottom = y == _board[x].length-1 ? getPaddingBottom() : 0;
                    int paddingLeft = x == 0 ? getPaddingLeft() : 0;
                    switch (content) {
                        case 1:
                            if(_showShips) {
                                color = 0xFF444444;
                            }
                            break;
                        case 2://miss
                            color = 0xFFFFFFFF;
                            break;
                        case 3://hit
                            color = 0xFFFF0000;
                            break;
                    }
                    paint.setColor(color);
                    Rect squareRect = new Rect();
                    squareRect.top = paddingTop + y * squareHeight + squareMargin;
                    squareRect.left = paddingLeft + x * squareWidth + squareMargin;
                    squareRect.right = getWidth() - squareWidth * (10 - (x + 1)) - squareMargin - paddingRight;
                    squareRect.bottom = getHeight() - squareHeight * (10 - (y + 1)) - squareMargin - paddingBottom;
                    canvas.drawRect(squareRect, paint);
                }
            }
        }
    }
}
