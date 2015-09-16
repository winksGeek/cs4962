package winkler.devon.paint;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by devonwinkler on 9/15/15.
 */
public class PaintAreaView extends View {
    private Path drawPath;
    private Paint drawPaint;
    private int paintColor = 0xff000000;
    List<PolyLine> lines;
    PolyLine line;

    public PaintAreaView(Context context){
        super(context);
        this.drawPath = new Path();
        this.drawPaint = new Paint();
        this.lines = new ArrayList<PolyLine>();
        drawPaint.setColor(this.paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10.0f);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas){
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight){
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        for(PolyLine pl:lines){
            List<PointF> points = pl.getPoints();
            PointF origin = points.get(0);
            drawPaint.setColor(pl.getColor());
            drawPath.moveTo(origin.x * getWidth(), origin.y * getHeight());
            for(int i = 1; i < points.size(); i++){
                PointF nextPoint = points.get(i);
                Log.d("TOUCH", "x: " + String.valueOf(nextPoint.x) + "y: " +  String.valueOf(nextPoint.y));
                drawPath.lineTo(nextPoint.x * getWidth(), nextPoint.y * getHeight());
            }
        }
//        drawPaint.setColor(this.paintColor);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                line = new PolyLine(this.paintColor);
                line.addPoint(x, y);
                drawPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                line.addPoint(x, y);
                drawPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                lines.add(line);
            default:
                return false;
        }
        invalidate();
        return true;
    }

    private class PolyLine{
        List<PointF> points;
        int color;
        public PolyLine(int color){
            this.points = new ArrayList<PointF>();
            this.color = color;
        }
        public void addPoint(float x, float y){
            PointF point = new PointF(x/(float)getWidth(), y/(float)getHeight());
            points.add(point);
        }

        public List<PointF> getPoints(){
            return points;
        }

        public int getColor(){
            return color;
        }

    }

}
