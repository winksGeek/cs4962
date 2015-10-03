package winkler.devon.moviepaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
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
    private List<PolyLine> lines;
    private double percentage = 0.0;
    PolyLine line;
    boolean watchMode = false;

    public PaintAreaView(Context context){
        super(context);
        this.drawPath = new Path();
        this.drawPaint = new Paint();
        this.setLines(new ArrayList<PolyLine>());
    }

    @Override
    protected void onDraw(Canvas canvas){
        int totalNumberofPoints = 0;
        int i = 0;
        int lineIndex = 0;
        int pointIndex = 0;
        for(PolyLine pl : getLines()){
            totalNumberofPoints += pl.getNumberOfPoints();
        }
        Log.i("Drawing", "Number of points before percentage: " + totalNumberofPoints);
        if(watchMode) {
            totalNumberofPoints = (int) ((double) totalNumberofPoints * percentage);
        }
        Log.i("Drawing", "Number of points after percentage: " + totalNumberofPoints);
        Paint linePaint = new Paint();
        Path linePath = new Path();
        while(i <= totalNumberofPoints && lineIndex < this.lines.size()){
            PolyLine pl = this.lines.get(lineIndex);
            if(pointIndex == 0){
                linePaint = new Paint();
                linePath = new Path();
                linePaint.setColor(pl.getColor());
                linePaint.setAntiAlias(true);
                linePaint.setStrokeWidth(10.0f);
                linePaint.setStyle(Paint.Style.STROKE);
                linePaint.setStrokeJoin(Paint.Join.ROUND);
                linePaint.setStrokeCap(Paint.Cap.ROUND);
            }

            List<PointF> points = pl.getPoints();
            PointF point = points.get(pointIndex);
            if(pointIndex == 0){
                linePath.moveTo(point.x*(float)getWidth(), point.y * (float)getHeight());
            }else{
                linePath.lineTo(point.x*(float)getWidth(), point.y * (float)getHeight());
            }
            i++;
            pointIndex++;
            if(pointIndex >= pl.getNumberOfPoints() || i >= totalNumberofPoints){
                lineIndex++;
                pointIndex = 0;
                canvas.drawPath(linePath, linePaint);
            }
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
        if(!this.watchMode) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    line = new PolyLine(this.paintColor);
                    getLines().add(line);
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
        }
        return true;
    }

    @Override
    public void onPaintChange(int color) {
        this.paintColor = color;
    }

    public List<PolyLine> getLines() {
        List<PolyLine> list = new ArrayList<PolyLine>(lines);
        return lines;
    }

    public void setLines(List<PolyLine> lines) {
        this.lines = lines;
        invalidate();
    }

    public void clearLines(){
        this.lines = new ArrayList<PolyLine>();
        invalidate();
    }

    public void setWatchMode(boolean isWatchMode){
        this.watchMode = isWatchMode;
        invalidate();
    }

    public void setPercentage(int percentage) {
        this.percentage = (double)percentage / 100.0;
        invalidate();
    }

    public class PolyLine{
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

        public int getNumberOfPoints(){
            return points.size();
        }

        public List<PointF> getPoints(){
            return points;
        }

        public int getColor(){
            return color;
        }

    }

}
