package com.zfdang.mdoi.ui.edit;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.List;

public class DrawingOverlayView extends View {
    private Paint paint = new Paint();
    private List<Line> lines = new ArrayList<>();
    private PointF startPoint = new PointF();
    
    public static class Line {
        public float startX, startY, endX, endY;
        public Line(float sx, float sy, float ex, float ey) {
            startX = sx;
            startY = sy;
            endX = ex;
            endY = ey;
        }
    }

    public DrawingOverlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupPaint();
    }

    private void setupPaint() {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(8);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Line line : lines) {
            canvas.drawLine(line.startX, line.startY, line.endX, line.endY, paint);
        }
    }

    public interface OnDrawListener {
        void onLineDrawn();
    }

    private OnDrawListener drawListener;

    public void setOnDrawListener(OnDrawListener listener) {
        this.drawListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                lines.add(new Line(startPoint.x, startPoint.y, event.getX(), event.getY()));
                if(drawListener != null) {
                    drawListener.onLineDrawn();
                }
                invalidate();
                return true;
        }
        return false;
    }

    public void setLines(List<Line> newLines) {
        lines = new ArrayList<>(newLines);
        invalidate();
    }

    public List<Line> getLines() {
        return new ArrayList<>(lines);
    }
}