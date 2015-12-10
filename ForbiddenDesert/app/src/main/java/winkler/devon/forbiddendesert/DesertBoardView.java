package winkler.devon.forbiddendesert;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by devonwinkler on 11/16/15.
 */
public class DesertBoardView extends View {
    public DesertBoardView(Context context) {
        super(context);
    }

    public interface TileClickListener{
        public void onTileClick(int xPos, int yPos);
    }

    DesertTile [] _board;
    Player[] _players;
    TileClickListener _tileClickListener;

    public void setBoard(DesertTile[]board) {
        _board = board;
        invalidate();
    }

    public void setPlayers(Player[] players){
        _players = players;
        invalidate();
    }

    public void setTileClickListener(TileClickListener listener){
        _tileClickListener = listener;
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
        int squareWidth = getWidth()/5;
        int squareHeight = squareWidth;
        int newX = x/squareWidth;
        int newY = y/squareHeight;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (_tileClickListener != null) {
                _tileClickListener.onTileClick(newX, newY);
            }
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        Paint highlightPaint = new Paint();
        highlightPaint.setColor(0xAAFFFFFF);
        int squareWidth = getWidth() / 5;
        int squareHeight = getHeight() / 5;
        int squareMargin = 2;
        for (int i = 0; i < _board.length; i++) {
            DesertTile tile = _board[i];
            int color = 0xFFCC9900;
            int paddingTop = tile.yPos == 0 ? getPaddingTop() : 0;
            int paddingRight = tile.xPos == 4 ? getPaddingRight() : 0;
            int paddingBottom = tile.yPos == 4 ? getPaddingBottom() : 0;
            int paddingLeft = tile.xPos == 0 ? getPaddingLeft() : 0;
            Drawable drawable = getDrawableForTile(tile);
//            paint.setColor(color);
            Rect squareRect = new Rect();
            squareRect.top = paddingTop + tile.yPos * squareHeight + squareMargin;
            squareRect.left = paddingLeft + tile.xPos * squareWidth + squareMargin;
            squareRect.right = getWidth() - squareWidth * (5 - (tile.xPos + 1)) - squareMargin - paddingRight;
            squareRect.bottom = getHeight() - squareHeight * (5 - (tile.yPos + 1)) - squareMargin - paddingBottom;
            drawable.setBounds(squareRect);
            drawable.draw(canvas);
//            canvas.drawRect(squareRect, paint);
            if(tile.coveredBySolarShield()) {
                canvas.drawRect(squareRect, highlightPaint);
            }
            if(tile.numberOfSandTiles > 0){
                Drawable sandTile = getResources().getDrawable(R.drawable.sandtile, null);
                sandTile.setBounds(squareRect);
                sandTile.draw(canvas);
                float textX = (float)squareRect.left + ((float)squareRect.right - (float)squareRect.left)/2.0f;
                float textY = (float)squareRect.top + ((float)squareRect.bottom - (float)squareRect.top)/2.0f;
                Paint textPaint = new Paint();
                textPaint.setColor(Color.BLACK);
                textPaint.setTextSize(45.0f);
                canvas.drawText(tile.numberOfSandTiles + "",textX ,textY, textPaint);
            }
            if(tile.partContained != null){
                Drawable part = getDrawableForPart(tile.partContained);
                float partSquareWidth = squareRect.right - squareRect.left;
                float partSquareHeight = squareRect.bottom - squareRect.top;
                float partWidth = partSquareWidth/2.0f;
                float partHeight = partSquareHeight/2.0f;
                Rect partRect = new Rect();
                float xOffset = partWidth - squareMargin;
                float yOffset = 0 + squareMargin;
                partRect.top = (int)(squareRect.top+yOffset);
                partRect.left = (int)(squareRect.left+xOffset);
                partRect.right = (int)(partRect.left+partWidth);
                partRect.bottom = (int)(partRect.top+partHeight);
                if(part != null) {
                    part.setBounds(partRect);
                    part.draw(canvas);
                }
            }
        }

        drawPlayers(canvas, squareWidth, squareHeight, squareMargin);
    }

    private Drawable getDrawableForPart(Part partContained) {
        switch (partContained._type){
            case Propeller:
                return getResources().getDrawable(R.drawable.propeller_part_collected,null);
            case Engine:
                return getResources().getDrawable(R.drawable.engine_part_collected,null);
            case Navigation:
                return getResources().getDrawable(R.drawable.navigation_part_collected,null);
            case Crystal:
                return getResources().getDrawable(R.drawable.crystal_part_collected,null);
        }
        return null;
    }

    private void drawPlayers(Canvas canvas, int squareWidth, int squareHeight, int squareMargin) {
        for(int j = 0; j < _players.length; j++){
            Player player = _players[j];
            int playerColor = player._role.getColor();
            int id = player._id;
            float xOffset = 0;
            float yOffset = 0;
            int paddingTop = player.yPos == 0 ? getPaddingTop() : 0;
            int paddingRight = player.xPos == 4 ? getPaddingRight() : 0;
            int paddingBottom = player.yPos == 4 ? getPaddingBottom() : 0;
            int paddingLeft = player.xPos == 0 ? getPaddingLeft() : 0;
            Rect squareRect = new Rect();
            squareRect.top = paddingTop + player.yPos * squareHeight + squareMargin;
            squareRect.left = paddingLeft + player.xPos * squareWidth + squareMargin;
            squareRect.right = getWidth() - squareWidth * (5 - (player.xPos + 1)) - squareMargin - paddingRight;
            squareRect.bottom = getHeight() - squareHeight * (5 - (player.yPos + 1)) - squareMargin - paddingBottom;
            float playerSquareWidth = squareRect.right - squareRect.left;
            float playerSquareHeight = squareRect.bottom - squareRect.top;
            float playerWidth = playerSquareWidth/3.0f;
            float playerHeight = playerSquareHeight/3.0f;
            switch (id){
                case 0:
                    xOffset = 0 + squareMargin;
                    yOffset = 0 + squareMargin;
                    break;
                case 1:
                    xOffset = playerWidth * 2 - squareMargin;
                    yOffset = 0 + squareMargin;
                    break;
                case 2:
                    xOffset = 0 + squareMargin;
                    yOffset = playerHeight * 2 - squareMargin;
                    break;
                case 3:
                    xOffset = playerWidth * 2 - squareMargin;
                    yOffset = playerHeight * 2 - squareMargin;
                    break;
                case 4:
                    xOffset = playerWidth;
                    yOffset = playerHeight;
                    break;
            }
            RectF ovalRect = new RectF();
            ovalRect.top = squareRect.top+yOffset;
            ovalRect.left = squareRect.left+xOffset;
            ovalRect.right = ovalRect.left+playerWidth;
            ovalRect.bottom = ovalRect.top+playerHeight;
            Paint playerPaint = new Paint();
            playerPaint.setColor(playerColor);
            canvas.drawOval(ovalRect, playerPaint);
            if(player.isActive()){
                RectF highlightRect = new RectF();
                Paint highlightPaint = new Paint();
                highlightPaint.setColor(Color.WHITE);
                highlightRect.top = ovalRect.top + 5;
                highlightRect.left = ovalRect.left + 5;
                highlightRect.right = ovalRect.right - 5;
                highlightRect.bottom = ovalRect.bottom - 5;
                canvas.drawOval(highlightRect, highlightPaint);
            }
        }
    }

    private Drawable getDrawableForTile(DesertTile tile) {
        Drawable drawable = getResources().getDrawable(R.drawable.desert_tile_back, null);
        DesertTile.Type type = tile.type;
        DesertTile.Status status = tile.status;
        switch(status){
            case Unflipped:
                switch (type) {
                    case Oasis:
                    case Mirage:
//                            color = 0xFF0000FF;
                        drawable = getResources().getDrawable(R.drawable.water_tile_back, null);
                        break;
                    case Crash:
//                            color = 0xFFFF0000;
                        drawable = getResources().getDrawable(R.drawable.crash_tile_back, null);
                        break;
                    case Storm:
//                            color = 0xFF000000;
                        drawable = getResources().getDrawable(R.drawable.storm_tile, null);
                }
                break;
            case Flipped:
                switch(type){
                    case PieceColumn:
                        switch (tile.partHint){
                            case Propeller:
                                drawable = getResources().getDrawable(R.drawable.yellow_vertical, null);
                                break;
                            case Crystal:
                                drawable = getResources().getDrawable(R.drawable.orange_vertical, null);
                                break;
                            case Navigation:
                                drawable = getResources().getDrawable(R.drawable.red_vertical, null);
                                break;
                            case Engine:
                                drawable = getResources().getDrawable(R.drawable.silver_vertical, null);
                                break;
                        }
                        break;
                    case PieceRow:
                        switch (tile.partHint){
                            case Propeller:
                                drawable = getResources().getDrawable(R.drawable.yellow_horizontal, null);
                                break;
                            case Crystal:
                                drawable = getResources().getDrawable(R.drawable.orange_horizontal, null);
                                break;
                            case Navigation:
                                drawable = getResources().getDrawable(R.drawable.red_horizontal, null);
                                break;
                            case Engine:
                                drawable = getResources().getDrawable(R.drawable.silver_horizontal, null);
                                break;
                        }
                        break;
                    case Item:
                    case Crash:
                        drawable = getResources().getDrawable(R.drawable.item_tile, null);
                        break;
                    case Landing:
                        drawable = getResources().getDrawable(R.drawable.landing_tile, null);
                        break;
                    case Tunnel:
                        drawable = getResources().getDrawable(R.drawable.tunnel_tile, null);
                        break;
                    case Mirage:
                        drawable = getResources().getDrawable(R.drawable.mirage_tile, null);
                        break;
                    case Oasis:
                        drawable = getResources().getDrawable(R.drawable.oasis_tile, null);
                        break;
                }
                break;
        }
        return drawable;
    }
}
