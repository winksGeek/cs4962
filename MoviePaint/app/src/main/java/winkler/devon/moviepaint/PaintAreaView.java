package winkler.devon.moviepaint;

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
public class PaintAreaView extends View implements PaletteView.OnPaintChangeListener{
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
    }

    @Override
    protected void onDraw(Canvas canvas){
        for(PolyLine pl : lines){
            Paint linePaint = new Paint();
            Path linePath = new Path();
            List<PointF> points = pl.getPoints();
            linePaint.setColor(pl.getColor());
            linePaint.setAntiAlias(true);
            linePaint.setStrokeWidth(10.0f);
            linePaint.setStyle(Paint.Style.STROKE);
            linePaint.setStrokeJoin(Paint.Join.ROUND);
            linePaint.setStrokeCap(Paint.Cap.ROUND);
            for(int i = 0; i < points.size(); i++){
                PointF point = points.get(i);
                if(i == 0){
                    linePath.moveTo(point.x*(float)getWidth(), point.y * (float)getHeight());
                }else{
                    linePath.lineTo(point.x*(float)getWidth(), point.y * (float)getHeight());
                }
            }
            canvas.drawPath(linePath, linePaint);
        }
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight){
        super.onSizeChanged(width, height, oldWidth, oldHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        float x = event.getX();
        float y = event.getY();
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                line = new PolyLine(this.paintColor);
                lines.add(line);
                line.addPoint(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                line.addPoint(x, y);
                break;
            case MotionEvent.ACTION_UP:
            default:
                return false;
        }
        invalidate();
        return true;
    }

    @Override
    public void onPaintChange(int color) {
        this.paintColor = color;
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
